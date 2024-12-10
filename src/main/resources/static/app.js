var stompClient = null;
let id = "0";
let playerHands = {
    "P1": [],
    "P2": [],
    "P3": [],
    "P4": []
};
let builtQuest = {};
let maxStages = "0";
let stage = "0";
let stageCards = [];
let sponsorID = "0";
let attackCards = [];


//Conecting to server functions
window.onload = () => {
    connectToServer().then(() => {
        console.log("Connection to server established!");
    }).catch((error) => {
        console.error("Connection error: ", error);
    });
};
async function connectToServer() {
    try {
        await connect();
        document.getElementById("registerBtn").style.visibility = "visible";
        setupSubscriptions();
    } catch (error) {
        console.error("An error occurred during connection:", error);
    }
}
function connect() {
    return new Promise((resolve, reject) => {
        var socket = new SockJS("/gameOfQuests-websocket");
        stompClient = Stomp.over(socket);
        console.log("Attempting connection...");
        stompClient.connect({}, function (frame) {
            console.log("Connected: " + frame);
            resolve();
        }, function (error) {
            reject(error);
        });
    });
}

//Functions to send info to Game Controller
function registerPlayer() {
    document.getElementById("registerBtn").style.visibility = "hidden";
    stompClient.send("/app/connect", {}, JSON.stringify({ name: "" }));
}
function startGame() {
    document.getElementById("startBtn").style.visibility = "hidden";
    stompClient.send("/app/start", {}, JSON.stringify({ name: "" }));
}
function drawCard() {
    
    let eventCardElement = document.getElementById("eventCard");
    if (eventCardElement !== null) {
        let eventCards = document.querySelectorAll("#eventCard");
        eventCards.forEach(card => card.remove());
    }
    stompClient.send("/app/drawCard", {}, JSON.stringify({ name: id }));
}
function sendDiscard(playerID, card) {
    //Update the Discarded "pile" label
    updateDiscardLabel(playerID, card);
    //Remove card from hand
    let cardElement = document.getElementById(playerID + card);
    cardElement.remove();

    stompClient.send(
        "/app/discardCard",
        {},
        JSON.stringify({ name: playerID + " " + card })
    );
}
function changeHotseat() {
    let noSponsorElement = document.getElementById("noSponsorFound");
    if (noSponsorElement) {
        noSponsorElement.remove();
    }
    removeContinueGameMessage();
    removeHotseatButton(id);
    stompClient.send("/app/changeHotseat", {}, JSON.stringify({ name: "" }));
}
function changePlayerProsperity() {
    removeProsperityOk(id);
    stompClient.send("/app/prosperityChange", {}, JSON.stringify({ name: "" }));
}
function yesSponsorResponse() {
    removeAskSponsorMessageAndButtons(id);
    stompClient.send("/app/sponsorshipResponse", {}, JSON.stringify({ name: id + " yes" }));
}
function noSponsorResponse() {
    removeAskSponsorMessageAndButtons(id);
    displayDeclinedSponsorship(id);
    stompClient.send("/app/sponsorshipResponse", {}, JSON.stringify({ name: id + " no" }));
}
function sendStage() {
    hideQuitStageAndWeaponReqMessage(id);
    removeStageCardsFromHand(id);
    builtQuest[stage] = { cards: stageCards.slice(), value: calculateStageValue(stageCards) };

    stompClient.send(
        "/app/buildStage",
        {},
        JSON.stringify({ name: id + " " + stageCards.join(",") })
    );
}
function sponsorDone() {
    removeBeginQuestMessageAndButton(id);
    stompClient.send("/app/carryOutQuest", {}, JSON.stringify({ name: "" }));
}
function yesStage() {
    removeAskPlayerStage(id);
    removeStageLoserOrWinnerMessage(id);
    stompClient.send("/app/carryOutQuest", {}, JSON.stringify({ name: id + " yes" }));
}
function noStage() {
    removeAskPlayerStage(id);
    removeStageLoserOrWinnerMessage(id);
    stompClient.send("/app/carryOutQuest", {}, JSON.stringify({ name: id + " no"}));
}
function changePlayerQuestDiscard() {
    removeNextPlayerParticipate(id);
    stompClient.send("/app/carryOutQuest", {}, JSON.stringify({ name: id + " playerDiscardDone" }));
}
function sendAttack() {
    removeSelectAttackMessage(id);
    removeAttackCardsFromHand(id);

    let attackCardsString = attackCards.length === 0 ? "none" : attackCards.join(",");
    if (attackCardsString === "none") {
        displayAttackCards(id);
    }

    stompClient.send(
        "/app/buildAttack",
        {},
        JSON.stringify({ name: id + " " + attackCardsString })
    );
}

//Subscription functions
function setupPlayerID(msg) {
    if (id === "0") {
        id = msg.id;
        let playerIDLabel = document.createElement("LABEL");
        let playerIDText = document.createTextNode("Player ID: " + id);
        playerIDLabel.setAttribute("id", "playerID");
        playerIDLabel.setAttribute("style", "font-weight: bold;");
        playerIDLabel.appendChild(playerIDText);
        document.getElementById("header").appendChild(playerIDLabel);
    }
    if (msg.id === "P4" && id === "P1") {
        document.getElementById("startBtn").style.visibility = "visible";
    }
}
function setupStartGame(msg) {
    //Hide welcome text
    document.getElementById("welcomeTitle").style.visibility = "hidden";
    document.getElementById("gameTitle").style.visibility = "hidden";
    document.getElementById("nav").style.height = "10px";

    //Create shield leaderboard
    $("#shieldCountList").html("");
    let shieldList = document.createElement("LI");
    shieldList.innerHTML = "Shields";
    shieldList.setAttribute("style", "font-weight: bold; text-decoration: underline;");
    document.getElementById("shieldCountList").appendChild(shieldList);
    addShieldCount("P1", msg.shields1);
    addShieldCount("P2", msg.shields2);
    addShieldCount("P3", msg.shields3);
    addShieldCount("P4", msg.shields4);

    //Create player hands
    if (id === "P1") {
        document.getElementById(id + "label").style.visibility = "visible";
        parseStartHand(msg.p1Hand, id);
        addCardsToHand(id);
    } else if (id === "P2") {
        document.getElementById(id + "label").style.visibility = "visible";
        parseStartHand(msg.p2Hand, id);
        addCardsToHand(id);
    } else if (id === "P3") {
        document.getElementById(id + "label").style.visibility = "visible";
        parseStartHand(msg.p3Hand, id);
        addCardsToHand(id);
    } else if (id === "P4") {
        document.getElementById(id + "label").style.visibility = "visible";
        parseStartHand(msg.p4Hand, id);
        addCardsToHand(id);
    }

    //Disable hand and draw button if not in hotseat
    if (msg.currentPlayerInHotseat === id) {
        document.getElementById("draw").disabled = false;
    }
    document.getElementById("draw").style.visibility = "visible";

    //Create the event deck face down
    if (document.getElementById("eventDeck") !== null) {
        const eventDeck = document.getElementById("eventDeck")
        eventDeck.remove();
    }
    let eventDeckElement = document.createElement("IMG");
    eventDeckElement.setAttribute("src", "/imgs/eBack.jpg");
    eventDeckElement.setAttribute("id", "eventDeck");
    document.getElementById("deck").appendChild(eventDeckElement);

    //Create player in hotseat label
    if (document.getElementById("hotseatPlayerID") !== null) {
        const hotseatLabel = document.getElementById("hotseatPlayerID");
        hotseatLabel.remove();
    }
    let hotseatLabelElement = document.createElement("LABEL");
    let playerInHosteatText = document.createTextNode("Player in Hotseat: " + msg.currentPlayerInHotseat);
    hotseatLabelElement.setAttribute("id", "hotseatPlayerID");
    hotseatLabelElement.appendChild(playerInHosteatText);
    document.getElementById("header").appendChild(hotseatLabelElement);
}
function carryOutPlague(msg) {
    let shieldElement = document.getElementById("shields" + msg.id);
    let currentText = shieldElement.innerHTML;
    let playerNumber = currentText.split(":")[0];
    shieldElement.innerHTML = playerNumber + ": " + msg.shields;

    showHotseatButton(msg.id);
    disableDraw(msg.id);
}
function carryOutQueensFavor(msg) {

    if (id === msg.id) {
        // findCardDrawnAndDisplay(msg.id, msg.playerHand);
        updateHand(msg.id, msg.playerHand);
    }

    if (id === msg.id && msg.discardsLeft !== "0") {
        if(!document.getElementById("discardText")) {
            let discardText = document.createElement("LABEL");
            discardText.setAttribute("id", "discardText");
            discardText.innerHTML = msg.discardsLeft + " cards to discard!";
            document.getElementById("deck").appendChild(discardText);
        }
        document.getElementById("draw").disabled = true;
        enableHandCards(id);
        discardText.innerHTML = msg.discardsLeft + " cards to discard!";
    }

    if (id === msg.id && msg.discardsLeft === "0") {
        discardText.remove();
        disableHandCards(msg.id);

        //Ask Player to leave hotseat
        showHotseatButton(msg.id);
        disableDraw(msg.id);
    }
}
function carryOutProsperity(msg) {
    if (id === msg.id) {
        // findCardDrawnAndDisplay(msg.id, msg.playerHand);
        updateHand(msg.id, msg.playerHand);
    }

    if (id === msg.id && msg.discardsLeft !== "0") {
        if(!document.getElementById("discardText")) {
            let discardText = document.createElement("LABEL");
            discardText.setAttribute("id", "discardText");
            discardText.innerHTML = msg.discardsLeft + " cards to discard!";
            document.getElementById("deck").appendChild(discardText);
        }
        document.getElementById("draw").disabled = true;
        enableHandCards(id);
        discardText.innerHTML = msg.discardsLeft + " cards to discard!";
    }

    if (id === msg.id && msg.discardsLeft === "0") {
        if(document.getElementById("discardText")) {
            discardText.remove();
        }
        disableHandCards(msg.id);

        if (msg.isDone === "no") {
            showProsperityOk(msg.id);
            disableDraw(msg.id);
        }
        if (msg.isDone === "yes") {
            showHotseatButton(msg.id);
            disableDraw(msg.id);
        }
    }
}
function askForSponsorship(msg) {
    if (id === msg.id && msg.sponsorFound === "no") {
        stage = "0";
        disableDraw(msg.id);
        showAskSponsorMessageAndButtons(msg.id);
    }
}
function executeSponsorshipResponse(msg) {
    if (id === msg.id && msg.sponsorFound === "yes") {
        if (msg.playerHand !== null) {
            // findCardDrawnAndDisplay(msg.id, msg.playerHand);
            updateHand(msg.id, msg.playerHand);
        }
        
        if (msg.stageBeingBuilt !== null) {
            stage = msg.stageBeingBuilt;
        }
        stageCards.length = 0;
        //Create element for stage building
        displayStageBuildingMessage(msg);
        enableFoeCards(msg);
    }
}
function questBuildingComplete(msg) {
    if (id === msg.id) {
        if (msg.playerHand !== null) {
            // findCardDrawnAndDisplay(msg.id, msg.playerHand);
            updateHand(msg.id, msg.playerHand);
        }
        stageCards.length = 0;
        stage = "0";
        //Show stages built
        displayBuiltStages(msg.id);
        //Disable hand cards
        disableHandCards(msg.id);
        //Begin Quest button appears
        showBeginQuestButtonAndMessage(msg.id);
    }
}
function askPlayerJoinStage(playerID) {
    if (id === playerID) {
        // console.log("hit");
        document.getElementById("askStageLabel").innerHTML = `Do you want to participate in <strong>Stage ${stage}</strong>?`;
        document.getElementById("askStageLabel").style.visibility = "visible";
        document.getElementById("yesStage").disabled = false;
        document.getElementById("yesStage").style.visibility = "visible";
        document.getElementById("noStage").disabled = false;
        document.getElementById("noStage").style.visibility = "visible";
    }
}
function carryOutQuestDiscard(msg) {

    if (id === msg.id) {
        // findCardDrawnAndDisplay(msg.id, msg.playerHand);
        updateHand(msg.id, msg.playerHand);
    }

    if (id === msg.id && msg.discardsLeft !== "0") {
        if(!document.getElementById("discardText")) {
            let discardText = document.createElement("LABEL");
            discardText.setAttribute("id", "discardText");
            discardText.innerHTML = msg.discardsLeft + " cards to discard!";
            document.getElementById("deck").appendChild(discardText);
        }
        document.getElementById("draw").disabled = true;
        enableHandCards(id);
        discardText.innerHTML = msg.discardsLeft + " cards to discard!";
    }

    if (id === msg.id && msg.discardsLeft === "0") {
        if (document.getElementById("discardText")) {
            document.getElementById("discardText").remove();
        }
        disableHandCards(msg.id);

        //Ask Player to leave hotseat
        showAskNextPlayerParticipate(msg.id);
        disableDraw(msg.id);
    }
}
function getParticipantAttackCards(playerID) {
    if (id === playerID) {
        attackCards.length = 0;
        displaySelectAttackMessage(playerID);
        disableFoeCardsAndEnableWeaponCardsForAttack(playerID);
    }
}

//Set up subscriptions based on info sent from Game Controller
function setupSubscriptions() {
    stompClient.subscribe("/topic/message", function (greeting) {
        let msg = JSON.parse(greeting.body);
        console.log("message", msg);

        //Registering of PlayerID
        if (msg.content === "id") {
            setupPlayerID(msg);
        } 
        //Start game setup
        else if (msg.content === "start") {
            setupStartGame(msg)
        } 
        //Draw event cards
        else if (msg.content === "Plague") {
            stage = "0";
            displayEventCard(msg);
            carryOutPlague(msg);
        }else if (msg.content === "QueensFavor") {
            stage = "0";
            if(!document.getElementById("eventCard")) {
                displayEventCard(msg);
            }
            carryOutQueensFavor(msg);
        }else if (msg.content === "Prosperity") {
            stage = "0";
            if(!document.getElementById("eventCard")) {
                displayEventCard(msg);
            }
            updateHotseatPlayerLabelAndDraw(msg.id);
            carryOutProsperity(msg);
        }else if (msg.content.includes("Q") && msg.content.length === 2) {
            if(!document.getElementById("eventCard")) {
                displayEventCard(msg);
            }
            updateHotseatPlayerLabelAndDraw(msg.id);
            if (msg.sponsorFound === "no") {
                askForSponsorship(msg);
            }else if (msg.sponsorFound === "done") {
                disableDraw(msg.id);
                showNoSponsorFoundMessage(msg.id);
                showHotseatButton(msg.id);
            }else if (msg.sponsorFound === "yes" && msg.stageBeingBuilt !== "finishedBuilding") {
                executeSponsorshipResponse(msg);
            }else if (msg.sponsorFound === "yes" && msg.stageBeingBuilt === "finishedBuilding") {
                questBuildingComplete(msg);
            }
        }
        //Change Hotseat Player
        else if (msg.content === "changeHotseat") {
            removeEventCard();
            sponsorID = "0";
            updateHotseatPlayerLabelAndDraw(msg.id);
        }
        else if (msg.content === "askStageDiscard") {
            console.log("HITHITHIT");
            updateHotseatPlayerLabelAndDraw(msg.id);
            carryOutQuestDiscard(msg);
        }
        else if (msg.content === "askStage") {
            // console.log(msg.content);
            // console.log(msg.id);
            // console.log(msg.stage);

            if (stage === "0") {
                // console.log("changing stage glob to = " + msg.stage);
                console.log("hit stage 0");
                stage = msg.stage;
                // displaySponsorIDLabel(msg.id);
                removeDeclinedSponsorshipMessage(msg.id);
            }
            if (parseInt(stage) === parseInt(msg.stage) && msg.playerHand !== null &&
                msg.stageLosers === null && msg.stageWinners === null) {
                console.log("hit second if");
                if (id === msg.id) {
                    //findCardDrawnAndDisplay(msg.id, msg.playerHand);
                    updateHand(msg.id, msg.playerHand);
                }
                updateHotseatPlayerLabelAndDraw(msg.id);
                askPlayerJoinStage(msg.id);
            }else if (parseInt(stage) === parseInt(msg.stage) && msg.playerHand === null &&
                msg.stageLosers === null && msg.stageWinners === null) {
                console.log("hit first else if ");
                updateHotseatPlayerLabelAndDraw(msg.id);
                askPlayerJoinStage(msg.id);
            }else if (parseInt(stage) < parseInt(msg.stage) &&
                    msg.stageLosers !== null &&
                    msg.stageWinners !== null &&
                    msg.playerHand === null) {
                console.log("hit last block");
                updateHotseatPlayerLabelAndDraw(msg.id);
                removeAttackCards();
                showStageWinners(msg.stageWinners);
                showStageLosers(msg.stageLosers);
                stage = msg.stage;
                askPlayerJoinStage(msg.id);
            }
        }
        else if (msg.content === "setUpAttacks") {
            if (msg.playerHand !== null && id === msg.id) {
                // findCardDrawnAndDisplay(msg.id, msg.playerHand);
                updateHand(msg.id, msg.playerHand);
            }
            updateHotseatPlayerLabelAndDraw(msg.id);
            getParticipantAttackCards(msg.id);
        }
        else if (msg.content === "endQuestEarly") {
            // if (id === msg.id) {
            //     console.log(msg.playerHand);
            //     updateHand(sponsorID, msg.playerHand);
            // }
            if (id == sponsorID) {
                updateHand(sponsorID, msg.playerHand);
            }
            updateHotseatPlayerLabelAndDraw(sponsorID);
            removeAttackCards();
            showStageWinners(msg.stageWinners);
            showStageLosers(msg.stageLosers);
            removeBuiltStages(sponsorID);
            showEndingQuestEarlyMessage();
            if (id === sponsorID && msg.discardsLeft !== null) {
                carryOutQuestDiscard(msg);
            }
            stage = "0";
            
            
            //FRONT END 
            //Remove Attack Cards Message
            //Show all stage winners and losers on their screen
            //Get sponsorID hand updated and discards start
            //sponsorRedraw flag happens so when there is 0 discards left it triggers change hotseat of the player in current hotseat
            //Game continues
            //return new EndQuestMessage("endQuestEarly", discardsLeft, sponsorHand, stageLosers, stageWinners);
        }
        else if (msg.content === "endQuest") {
            if (id === msg.id) {
                updateHand(msg.id, msg.playerHand);
            }
            removeAttackCards();
            showStageWinnersAndShieldsEarnedForEndQuest(msg.stageWinners);
            showStageLosers(msg.stageLosers);
            removeBuiltStages(msg.id);
            showEndingQuest();
            updateShieldCounts("P1", msg.shields1);
            updateShieldCounts("P2", msg.shields2);
            updateShieldCounts("P3", msg.shields3);
            updateShieldCounts("P4", msg.shields4);
            stage = "0";
            updateHotseatPlayerLabelAndDraw(msg.id);
            if (id === msg.id && msg.discardsLeft !== null) {
                carryOutQuestDiscard(msg);
            }
        }
        else if (msg.content === "endQuestDiscard") {
            updateHotseatPlayerLabelAndDraw(msg.id);
            carryOutQuestDiscard(msg);
        }
        else if (msg.content === "continueGame") {
            updateHotseatPlayerLabelAndDraw(msg.id);
            if (id === msg.id) {
                showHotseatButton(msg.id);
                disableDraw(msg.id);
            }
            removeLastQuestWinnersMessages("P1");
            removeLastQuestWinnersMessages("P2");
            removeLastQuestWinnersMessages("P3");
            removeLastQuestWinnersMessages("P4");
            removeStageLoserOrWinnerMessage("P1");
            removeStageLoserOrWinnerMessage("P2");
            removeStageLoserOrWinnerMessage("P3");
            removeStageLoserOrWinnerMessage("P4");
            removeEndingQuest();
            showContinueGameMessage();
        }
        else if (msg.content === "winnersFound") {
            let gameWinners = msg.id.split(" ");
            displayWinnersMessage(gameWinners);
        }
            //Display Stage Losers and Winners
            //Remove Attack Cards Message
            //boolean to check for sponsor redraw 
            //+ questDone boolean to trigger winner situation when quest is over once discards are 0
            // return new EndQuestMessage("endQuest", gameData.getSponsorID(), discardsLeft, sponsorHand.toString(), stageLosers, stageWinners);
        
    });
}

//Subscription helper functions
function displayWinnersMessage(gameWinners) {
    if (!document.getElementById("finalQuestMessage")) {
        let messageElement = document.createElement("DIV");
        messageElement.setAttribute("id", "finalQuestMessage");
        messageElement.style.fontWeight = "bold";
        messageElement.style.backgroundColor = "red";
        messageElement.innerHTML = `A Game of Quests is Over!<br>Winners are ${gameWinners.join(" & ")}!`;
        document.getElementById("mainMessageArea").appendChild(messageElement);
    }
}
function updateShieldCounts(playerID, shields) {
    let shieldElement = document.getElementById("shields" + playerID);
    let currentText = shieldElement.innerHTML;
    let playerNumber = currentText.split(":")[0];
    shieldElement.innerHTML = playerNumber + ": " + shields;
}
function displayDeclinedSponsorship(playerID) {
    let declinedLabelID = playerID + "DeclinedSponsorshipLabel";
    let declinedLabel = document.getElementById(declinedLabelID);

    if (!declinedLabel) {
        declinedLabel = document.createElement("DIV");
        declinedLabel.setAttribute("id", declinedLabelID);
        declinedLabel.innerHTML = "You declined to Sponsor the Quest.";
        document.getElementById("mainMessageArea").appendChild(declinedLabel);
    }
}
function removeDeclinedSponsorshipMessage(playerID) {
    if (id === playerID) {
        let declinedLabelID = playerID + "DeclinedSponsorshipLabel";
        let declinedLabel = document.getElementById(declinedLabelID);

        if (declinedLabel) {
            declinedLabel.parentNode.removeChild(declinedLabel);
        }
    }
}
function updateDiscardLabel(playerID, card) {
    let discardLabelID = playerID + "DiscardLabel";
    let discardLabel = document.getElementById(discardLabelID);

    if (!discardLabel) {
        discardLabel = document.createElement("DIV");
        discardLabel.setAttribute("id", discardLabelID);
        document.getElementById("header").appendChild(discardLabel);
    }
    discardLabel.innerHTML = "Your Last Discard: <strong>" + card + "</strong>";
}
function showEndingQuest() {
    if (!document.getElementById("EndQuestMessage")) {
        let messageElement = document.createElement("DIV");
        messageElement.setAttribute("id", "EndQuestMessage");
        messageElement.innerHTML = "All Stages Completed & Quest is Over!";
        document.getElementById("mainMessageArea").appendChild(messageElement);
    }
}
function showContinueGameMessage() {
    if (!document.getElementById("finalQuestMessage")) {
        let messageElement = document.createElement("DIV");
        messageElement.setAttribute("id", "finalQuestMessage");
        messageElement.innerHTML = "There are no player's with 7 or more Shields<br>A Game of Quests Continues!";
        document.getElementById("mainMessageArea").appendChild(messageElement);
    }
}
function removeContinueGameMessage() {
    if (document.getElementById("finalQuestMessage")) {
        document.getElementById("finalQuestMessage").remove();
    }
}
function removeEndingQuest() {
    if (document.getElementById("EndQuestMessage")) {
        document.getElementById("EndQuestMessage").remove();
    }
    if (document.getElementById("endEarlyMessage")) {
        document.getElementById("endEarlyMessage").remove();
    }
}
function showEndingQuestEarlyMessage() {
    if (!document.getElementById("endEarlyMessage")) {
        let messageElement = document.createElement("DIV");
        messageElement.setAttribute("id", "endEarlyMessage");
        messageElement.innerHTML = "No more eligible players! <strong>Quest ended early!</strong><br>No one earns any shields!";
        document.getElementById("mainMessageArea").appendChild(messageElement);
    }
}
function showStageWinners(stageWinners) {
    let winners = stageWinners.split(" ");
    winners.forEach(winner => {
        if (id === winner) {
            let winnerElement = document.getElementById(winner + "EndStageMessage");
            if (!winnerElement) {
                winnerElement = document.createElement("DIV");
                winnerElement.setAttribute("id", winner + "EndStageMessage");
                winnerElement.innerHTML = `Congratulations you <strong>WON</strong> Stage ${stage}!<br>You are eligible to continue in the Quest!`;
                document.getElementById("mainMessageArea").appendChild(winnerElement);
            } else {
                winnerElement.style.visibility = "visible";
                winnerElement.innerHTML = `Congratulations you <strong>WON</strong> Stage ${stage}!<br>You are eligible to continue in the Quest!`;
            }
        }
    });
}
function showStageWinnersAndShieldsEarnedForEndQuest(stageWinners) {
    let winners = stageWinners.split(" ");
    winners.forEach(winner => {
        if (id === winner) {
            let winnerElement = document.getElementById(winner + "EndQuestStageMessage");
            if (!winnerElement) {
                winnerElement = document.createElement("DIV");
                winnerElement.setAttribute("id", winner + "EndQuestStageMessage");
                winnerElement.innerHTML = `Congratulations you <strong>WON</strong> the last stage of the Quest!<br>You have earned ${stage} shields!`;
                document.getElementById("mainMessageArea").appendChild(winnerElement);
            } else {
                winnerElement.style.visibility = "visible";
                winnerElement.innerHTML = `Congratulations you <strong>WON</strong> the last stage of the Quest!<br>You have earned ${stage} shields!`;
            }
        }
    });
}
function removeLastQuestWinnersMessages(playerID) {
    let winnerElement = document.getElementById(playerID + "EndQuestStageMessage");
    if (winnerElement) {
        winnerElement.parentNode.removeChild(winnerElement);
    }
}
function showStageLosers(stageLosers) {
    let losers = stageLosers.split(" ");
    losers.forEach(loser => {
        if (id === loser) {
            let loserElement = document.getElementById(loser + "EndStageMessage");
            if (!loserElement) {
                loserElement = document.createElement("DIV");
                loserElement.setAttribute("id", loser + "EndStageMessage");
                loserElement.innerHTML = `Sorry, you <strong>LOST</strong> Stage ${stage}!<br>You have been eliminated from participating in the Quest.`;
                document.getElementById("mainMessageArea").appendChild(loserElement);
            } else {
                loserElement.style.visibility = "visible";
                loserElement.innerHTML = `Sorry, you <strong>LOST</strong> Stage ${stage}!<br>You have been eliminated from participating in the Quest.`;
            }
        }
    });
}
function removeStageLoserOrWinnerMessage(playerID) {
    let loserElement = document.getElementById(playerID + "EndStageMessage");
    if (loserElement) {
        loserElement.parentNode.removeChild(loserElement);
    }
}
function displaySelectAttackMessage(playerID) {
    if (id === playerID) {
        let buildAttackElement = document.getElementById("buildAttackMessage");
        if (!buildAttackElement) {
            buildAttackElement = document.createElement("DIV");
            buildAttackElement.setAttribute("id", "buildAttackMessage");
            document.getElementById("mainMessageArea").appendChild(buildAttackElement);
        }
        buildAttackElement.innerHTML = `Build Your Attack for Stage: ${stage}<br>Add Non-Repeating Weapon Card(s) to your Attack now<br>OR<br>Select 'Quit' to Build an Empty Attack!`;
        document.getElementById("quitAttack").disabled = false;
        document.getElementById("quitAttack").style.visibility = "visible";
    }
}
function removeSelectAttackMessage(playerID) {
    if (id === playerID) {
        document.getElementById("buildAttackMessage").remove();
        document.getElementById("quitAttack").disabled = true;
        document.getElementById("quitAttack").style.visibility = "hidden";
    }
}
function showAskNextPlayerParticipate(playerID) {
    if (id === playerID) {
        document.getElementById("discardQuestLabel").style.visibility = "visible";
        document.getElementById("leaveQuestDiscard").style.visibility = "visible";
        document.getElementById("leaveQuestDiscard").disabled = false;
    }
}
function removeNextPlayerParticipate(playerID) {
    if (id === playerID) {
        document.getElementById("discardQuestLabel").style.visibility = "hidden";
        document.getElementById("leaveQuestDiscard").style.visibility = "hidden";
        document.getElementById("leaveQuestDiscard").disabled = true;
    }
}
function removeAskPlayerStage(playerID) {
    if (id === playerID) {
        document.getElementById("askStageLabel").innerHTML = "";
        document.getElementById("askStageLabel").style.visibility = "hidden";
        document.getElementById("yesStage").disabled = true;
        document.getElementById("yesStage").style.visibility = "hidden";
        document.getElementById("noStage").disabled = true;
        document.getElementById("noStage").style.visibility = "hidden";
    }
}
function calculateStageValue(cards) {
    return cards.reduce((sum, card) => {
        let value = parseInt(card.match(/\d+/)[0], 10);
        return sum + value;
    }, 0);
}
function displayBuiltStages(playerID) {
    if (id === playerID) {
        let builtStagesElement = document.getElementById("builtStages");
        if (!builtStagesElement) {
            builtStagesElement = document.createElement("DIV");
            builtStagesElement.setAttribute("id", "builtStages");
            document.getElementById("mainMessageArea").appendChild(builtStagesElement);
        }
        builtStagesElement.innerHTML = "";
        for (let stage in builtQuest) {
            let stageElement = document.createElement("DIV");
            stageElement.innerHTML = `<u>Stage ${stage}</u><br>Card(s): <strong>${builtQuest[stage].cards.join(", ")}</strong> & Value = <strong>${builtQuest[stage].value}</strong><br>`;
            builtStagesElement.appendChild(stageElement);
        }
    }
}
function removeBuiltStages(playerID) {
    if (id === playerID) {
        document.getElementById("builtStages").remove();
    }
}
function removeBeginQuestMessageAndButton(playerID) {
    if (id === playerID) {
        document.getElementById("sponsorDoneMessage").remove();
        document.getElementById("sponsorDone").disabled = true;
        document.getElementById("sponsorDone").style.visibility = "hidden";
    }
}
function showBeginQuestButtonAndMessage(playerID) {
    if (id === playerID) {
        document.getElementById("buildStage").remove();
        document.getElementById("foeReqMessage").remove();
        document.getElementById("weaponReqMessage").remove();

        if (!document.getElementById("sponsorDoneMessage")) {
            let beginQuestMessage = document.createElement("DIV");
            beginQuestMessage.setAttribute("id", "sponsorDoneMessage");
            beginQuestMessage.innerHTML = "<br><strong>Press 'Begin Quest' to ask player's to participate!</strong>";
            document.getElementById("mainMessageArea").appendChild(beginQuestMessage);
        }
        document.getElementById("sponsorDone").disabled = false;
        document.getElementById("sponsorDone").style.visibility = "visible";
    }
}
function hideQuitStageAndWeaponReqMessage(playerID) {
    if (id === playerID) {
        document.getElementById("weaponReqMessage").style.visibility = "hidden";
        document.getElementById("quitStage").disabled = true;
        document.getElementById("quitStage").style.visibility = "hidden";
        document.getElementById("stageCards").remove();
    }
}
function displayStageBuildingMessage(msg) {

    if (msg.stageBeingBuilt === "1") {
        //only updates for sponsor window
        sponsorID = msg.id;
    }
    if (id === msg.id) {
        //Stage building Message
        if (!document.getElementById("buildStage")) {
            //Set globals for all stages
            maxStages = parseInt(msg.content.slice(1), 10);
            // console.log(maxStages);
    
            let buildingStageMessage = document.createElement("DIV");
            buildingStageMessage.setAttribute("id", "buildStage");
            buildingStageMessage.style.visibility = "visible";
            document.getElementById("mainMessageArea").appendChild(buildingStageMessage);
        }
        document.getElementById("buildStage").innerHTML = "Building Quest Stage: " + stage;

        if (!document.getElementById("foeReqMessage")) {
            let foeCardReqMessage = document.createElement("DIV");
            foeCardReqMessage.setAttribute("id", "foeReqMessage");
            foeCardReqMessage.innerHTML = "Add 1 Foe Card to Stage now!";
            document.getElementById("mainMessageArea").appendChild(foeCardReqMessage);
        }
        document.getElementById("foeReqMessage").style.visibility = "visible";

        if (!document.getElementById("weaponReqMessage")) {
            let weaponReqMessage = document.createElement("DIV");
            weaponReqMessage.setAttribute("id", "weaponReqMessage");
            weaponReqMessage.innerHTML = "Add Non-Repeating Weapon Card(s) to Stage now!<br>OR<br>Press 'Quit' to finish building this stage!";
            document.getElementById("mainMessageArea").appendChild(weaponReqMessage);
        }
        document.getElementById("weaponReqMessage").style.visibility = "hidden";
    }
}
function foeCardSelected(playerID, card) {
    // console.log("foeCardSelected " + card)
    if (id === playerID) {
        stageCards.push(card);
        // console.log(stageCards);
        document.getElementById("foeReqMessage").style.visibility = "hidden";
        document.getElementById("weaponReqMessage").style.visibility = "visible";
        document.getElementById("quitStage").style.visibility = "visible";
        document.getElementById("quitStage").disabled = false;
    
        document.getElementById(playerID + card).remove();

        displayStageCards(playerID);
        disableFoeCardsAndEnableWeaponCards(playerID);
        updateQuitStageButtonForStageValue(playerID);
    }
}
function enableFoeCards(msg) {
    if (id === msg.id) {
        let handContainer = document.getElementById(msg.id + "hand");
        let cards = handContainer.getElementsByClassName("card");
        for (let card of cards) {
            if (card.id.startsWith(msg.id + "F")) {
                card.disabled = false;

                card.setAttribute("onclick", `foeCardSelected('${msg.id}', '${card.id.slice(2)}')`);
            } else {
                card.disabled = true;
            }
        }
    }
}
function disableFoeCardsAndEnableWeaponCards(playerID) {
    if (id === playerID) {
        let handContainer = document.getElementById(playerID + "hand");
        let cards = handContainer.getElementsByClassName("card");
        for (let card of cards) {
            if (card.id.startsWith(playerID + "F")) {
                card.disabled = true;
            } else {
                card.setAttribute("onclick", `weaponCardSelected('${playerID}', '${card.id.slice(2)}')`);
                card.disabled = false;
            }
        }
    }
}
function disableFoeCardsAndEnableWeaponCardsForAttack(playerID) {
    if (id === playerID) {
        let handContainer = document.getElementById(playerID + "hand");
        let cards = handContainer.getElementsByClassName("card");
        for (let card of cards) {
            if (card.id.startsWith(playerID + "F")) {
                card.disabled = true;
            } else {
                card.setAttribute("onclick", `weaponCardForAttack('${playerID}', '${card.id.slice(2)}')`);
                card.disabled = false;
            }
        }
    }
}
function weaponCardForAttack(playerID, card) {
    attackCards.push(card);

    // console.log(attackCards);

    document.getElementById(playerID + card).remove();
    displayAttackCards(playerID);
    disableRepeatingWeaponsAttack(playerID);
}
function weaponCardSelected(playerID, card) {
    stageCards.push(card);

    // console.log(stageCards);

    document.getElementById(playerID + card).remove();
    displayStageCards(playerID);
    disableRepeatingWeapons(playerID);
    updateQuitStageButtonForStageValue(playerID);
}
function updateQuitStageButtonForStageValue(playerID) {
    if (id === playerID && stage > 1) {
        let previousStageValue = builtQuest[stage - 1].value;
        let currentStageValue = calculateStageValue(stageCards);

        //Check that the current stage value is greater than the previous stage
        if (currentStageValue > previousStageValue) {
            document.getElementById("quitStage").disabled = false;
            removeStageValueMessage(playerID);
        } else {
            document.getElementById("quitStage").disabled = true;
            showStageValueMessage(previousStageValue, playerID);
        }
    }
}
function showStageValueMessage(previousStageValue, playerID) {
    if (id === playerID) {
        let messageElement = document.getElementById("stageValueMessage");
        if (!messageElement) {
            messageElement = document.createElement("DIV");
            messageElement.setAttribute("id", "stageValueMessage");
            messageElement.innerHTML = `Stage value must be GREATER than ${previousStageValue}!`;
            messageElement.style.visibility = "visible";
            document.getElementById("mainMessageArea").appendChild(messageElement);
        }else {
            messageElement.innerHTML = `Stage value must be GREATER than ${previousStageValue}!`;
            messageElement.style.visibility = "visible";
        }
    }    
}
function removeStageValueMessage(playerID) {
    if (id === playerID) {
        let messageElement = document.getElementById("stageValueMessage");
        if (messageElement) {
            messageElement.style.visibility = "hidden";
        }
    }
}
function disableRepeatingWeapons(playerID) {
    let handContainer = document.getElementById(playerID + "hand");
    let cards = handContainer.getElementsByClassName("card");
    for (let cardElement of cards) {
        if (stageCards.includes(cardElement.id.replace(playerID, ""))) {
            cardElement.disabled = true;
        }
    }
}
function disableRepeatingWeaponsAttack(playerID) {
    let handContainer = document.getElementById(playerID + "hand");
    let cards = handContainer.getElementsByClassName("card");
    for (let cardElement of cards) {
        if (attackCards.includes(cardElement.id.replace(playerID, ""))) {
            cardElement.disabled = true;
        }
    }
}
function displayStageCards(playerID) {
    if (id === playerID) {
        if (!document.getElementById("stageCards")) {
            let stageCardsElement = document.createElement("DIV");
            stageCardsElement.setAttribute("id", "stageCards");
            stageCardsElement.innerHTML = "<strong>Card(s) in Stage: " + stageCards.join(", ") + "</strong>";
            document.getElementById("mainMessageArea").appendChild(stageCardsElement);
        }
        document.getElementById("stageCards").innerHTML = "<strong>Card(s) in Stage: " + stageCards.join(", ") + "</strong>";
    }
}
function displayAttackCards(playerID) {
    if (id === playerID) {
        if (!document.getElementById(playerID + "attackCards")) {
            let attackCardsElement = document.createElement("DIV");
            attackCardsElement.setAttribute("id", playerID + "attackCards");
            attackCardsElement.innerHTML = "Card(s) in Attack: <strong>" + attackCards.join(", ") + "</strong>";
            document.getElementById("mainMessageArea").appendChild(attackCardsElement);
        }
        if (attackCards.length !== 0) {
            document.getElementById(playerID + "attackCards").innerHTML = "Card(s) in Attack: <strong>" + attackCards.join(", ") + "</strong> & Value = <strong>" + calculateStageValue(attackCards) + "</strong>";
        } else {
            document.getElementById(playerID + "attackCards").innerHTML = "You built an Empty Attack.";
        }
    }
}
function removeAttackCards() {
    for (let i = 1; i <= 4; i++) {
        let playerID = "P" + i;
        if (playerID !== sponsorID && id === playerID) {
            let attackCardsElement = document.getElementById(playerID + "attackCards");
            if (attackCardsElement) {
                attackCardsElement.remove();
            }
        }
    }
}
function showNoSponsorFoundMessage(playerID) {
    if (id === playerID) {
        let noSponsorElement = document.createElement("DIV");
        noSponsorElement.setAttribute("id", "noSponsorFound");
        noSponsorElement.innerHTML = "No Player's Sponsored Quest... Game Continues!";
        document.getElementById("mainMessageArea").appendChild(noSponsorElement);
    }
}
function showAskSponsorMessageAndButtons(playerID) {
    if (id === playerID) {
        document.getElementById("sponsorResponseLabel").style.visibility = "visible";
        document.getElementById("yesSponsor").style.visibility = "visible";
        document.getElementById("yesSponsor").disabled = false;
        document.getElementById("noSponsor").style.visibility = "visible";
        document.getElementById("noSponsor").disabled = false;
    }
}
function removeAskSponsorMessageAndButtons(playerID) {
    if (id === playerID) {
        document.getElementById("sponsorResponseLabel").style.visibility = "hidden";
        document.getElementById("yesSponsor").style.visibility = "hidden";
        document.getElementById("yesSponsor").disabled = true;
        document.getElementById("noSponsor").style.visibility = "hidden";
        document.getElementById("noSponsor").disabled = true;
    }
}
function disableDraw(playerID) {
    if (id === playerID) {
        document.getElementById("draw").disabled = true;
    }
}
function showProsperityOk(playerID) {
    if (id === playerID) {
        document.getElementById("prosperityOkLabel").style.visibility = "visible";
        document.getElementById("prosperityOk").style.visibility = "visible";
        document.getElementById("prosperityOk").disabled = false;
    }
}
function removeProsperityOk(playerID) {
    if (id === playerID) {
        document.getElementById("prosperityOkLabel").style.visibility = "hidden";
        document.getElementById("prosperityOk").style.visibility = "hidden";
        document.getElementById("prosperityOk").disabled = true;
    }
}
function showHotseatButton(playerID) {
    if (id === playerID) {
        document.getElementById("hotseatLabel").style.visibility = "visible";
        document.getElementById("leaveHotseat").style.visibility = "visible";
        document.getElementById("leaveHotseat").disabled = false;
    }
}
function removeHotseatButton(playerID) {
    if (id === playerID) {
        document.getElementById("hotseatLabel").style.visibility = "hidden";
        document.getElementById("leaveHotseat").style.visibility = "hidden";
        document.getElementById("leaveHotseat").disabled = true;
    }
}
function removeEventCard() {
    document.getElementById("eventCard").remove();
}
function updateHotseatPlayerLabelAndDraw(playerID) {
    if(id === playerID) {
        document.getElementById("draw").disabled = false;
    }
    document.getElementById("hotseatPlayerID").innerHTML = "Player in Hotseat: " + playerID;
}
function enableHandCards(playerID) {
    console.log("HIT IN ENABLE");
    let handContainer = document.getElementById(playerID + "hand");
    let cards = handContainer.getElementsByClassName("card");
    for (let card of cards) {
        card.disabled = false;
    }
}
function disableHandCards(playerID) {
    let handContainer = document.getElementById(playerID + "hand");
    let cards = handContainer.getElementsByClassName("card");
    for (let card of cards) {
        card.disabled = true;
    }
}
function displayEventCard(msg) {
    let eventCard = document.createElement("IMG");
    eventCard.setAttribute("src", "/imgs/cards/" + msg.content + ".svg");
    eventCard.setAttribute("id", "eventCard");
    document.getElementById("deck").appendChild(eventCard);
}
function removeEventCard() {
    let eventCards = document.querySelectorAll("#eventCard");
    eventCards.forEach(card => card.remove());
}
function addShieldCount(id, shields) {
    let shieldElement = document.createElement("LI");
    shieldElement.setAttribute("id", "shields" + id);
    shieldElement.innerHTML = id + ": " + shields;
    document.getElementById("shieldCountList").appendChild(shieldElement);
}
function parseStartHand(msg, playerID) {
    playerHands[playerID] = msg.split(" ");
}
function updateHand(playerID, newHand) {
    if (newHand !== "") {
        findCardDrawnAndDisplay(playerID, newHand);
        if (playerHands[playerID] !== undefined) {
            playerHands[playerID].splice(0, playerHands[playerID].length);
            removeCardsFromHand(playerID);
        }
        parseStartHand(newHand, playerID);
        addCardsToHand(playerID);
    }
}
function removeStageCardsFromHand(playerID) {
    stageCards.forEach(card => {
        let index = playerHands[playerID].indexOf(card);
        if (index !== -1) {
            playerHands[playerID].splice(index, 1);
        }
    });
}
function removeAttackCardsFromHand(playerID) {
    attackCards.forEach(card => {
        let index = playerHands[playerID].indexOf(card);
        if (index !== -1) {
            playerHands[playerID].splice(index, 1);
        }
    });
}
function findCardDrawnAndDisplay(playerID, newHand) {
    let addedCards = [...newHand.split(" ")];
    console.log(playerHands[playerID]);
    console.log(playerHands);
    console.log(addedCards);
    if (playerHands[playerID] !== undefined) {
        playerHands[playerID].forEach(card => {
            let index = addedCards.indexOf(card);
            if (index !== -1) {
                addedCards.splice(index, 1);
            }
        });
    }
    console.log(addedCards);
    
    
    // let addedCard = newHandCopy.length > 0 ? newHandCopy[0] : undefined;

    // if (addedCard !== undefined && id === playerID) {
    //     if (!document.getElementById(playerID + "DrawnCardLabel")) {
    //         drawnCardLabel = document.createElement("DIV");
    //         drawnCardLabel.setAttribute("id", playerID + "DrawnCardLabel");
    //         document.getElementById("header").appendChild(drawnCardLabel);
    //     }
    //     document.getElementById(playerID + "DrawnCardLabel").innerHTML = "Your Last Adventure Card Drawn: <strong>" + addedCard + "</strong>";
    // }

    let drawnCardLabelID = playerID + "DrawnCardLabel";
    let drawnCardLabel = document.getElementById(drawnCardLabelID);
    if (!drawnCardLabel) {
        drawnCardLabel = document.createElement("DIV");
        drawnCardLabel.setAttribute("id", drawnCardLabelID);
        document.getElementById("header").appendChild(drawnCardLabel);
    }

    // if (addedCards.length > 0 && addedCards[0] !== undefined) {
    //     if (addedCards.length === 1 && drawnCardLabel.style.visibility == "visible") {
    //         drawnCardLabel.innerHTML = "Your Last Adventure Card Drawn: <strong>" + addedCards[0] + "</strong>";
    //     } else if (addedCards.length === 1 && drawnCardLabel.style.visibility == "hidden") {
    //         drawnCardLabel.innerHTML = "Your Last Adventure Card Drawn: <strong>" + addedCards[0] + "</strong>";
    //         drawnCardLabel.style.visibility = "visible";
    //         document.getElementById(playerID + "DrawnCardMainMessage").remove();
    //     } else if (addedCards.length > 1 && addedCards.length < 5 && drawnCardLabel.style.visibility == "visible"){
    //         drawnCardLabel.innerHTML = "Your Last Adventure Cards Drawn: <strong>" + addedCards.join(", ") + "</strong> (" + addedCards.length + "cards)";
    //     } else if (addedCards.length > 1 && addedCards.length < 5 && drawnCardLabel.style.visibility == "hidden"){
    //         drawnCardLabel.innerHTML = "Your Last Adventure Cards Drawn: <strong>" + addedCards.join(", ") + "</strong> (" + addedCards.length + "cards)";
    //         drawnCardLabel.style.visibility = "visible";
    //         document.getElementById(playerID + "DrawnCardMainMessage").remove();
    //     } else if (addedCards.length >= 5) {
    //         drawnCardLabel.style.visibility = "hidden";
    //         if (!document.getElementById(playerID + "DrawnCardMainMessage")) {
    //             let mainMessageElement = document.createElement("DIV");
    //             mainMessageElement.setAttribute("id", playerID + "DrawnCardMainMessage");
    //             mainMessageElement.innerHTML = "Your Last Adventure Cards Drawn: <strong>" + addedCards.join(", ") + "</strong> (" + addedCards.length + "cards)";
    //             document.getElementById("mainMessageArea").appendChild(mainMessageElement);
    //         }
    //         document.getElementById(playerID + "DrawnCardMainMessage").innerHTML = "Your Last Adventure Cards Drawn: <strong>" + addedCards.join(", ") + "</strong> (" + addedCards.length + "cards)";
    //     }
    // }
    if (addedCards.length > 0 && addedCards[0] !== undefined) {
        if (addedCards.length === 1) {
            drawnCardLabel.innerHTML = "Your Last Adventure Card Drawn: <strong>" + addedCards[0] + "</strong>";
            drawnCardLabel.style.visibility = "visible";
            let mainMessageElement = document.getElementById(playerID + "DrawnCardMainMessage");
            if (mainMessageElement) {
                mainMessageElement.remove();
            }
        } else if (addedCards.length > 1 && addedCards.length < 5) {
            drawnCardLabel.innerHTML = "Your Last Adventure Cards Drawn: <strong>" + addedCards.join(", ") + "</strong> (" + addedCards.length + " cards)";
            drawnCardLabel.style.visibility = "visible";
            let mainMessageElement = document.getElementById(playerID + "DrawnCardMainMessage");
            if (mainMessageElement) {
                mainMessageElement.remove();
            }
        } else if (addedCards.length >= 5) {
            drawnCardLabel.style.visibility = "hidden";
            let mainMessageElement = document.getElementById(playerID + "DrawnCardMainMessage");
            if (!mainMessageElement) {
                mainMessageElement = document.createElement("DIV");
                mainMessageElement.setAttribute("id", playerID + "DrawnCardMainMessage");
                document.getElementById("mainMessageArea").appendChild(mainMessageElement);
            }
            mainMessageElement.innerHTML = "Your Last Adventure Cards Drawn: <strong>" + addedCards.join(", ") + "</strong> (" + addedCards.length + " cards)";
        }
    }
}
function removeCardsFromHand(playerID) {
    let handContainer = document.getElementById(playerID + "hand");
    while (handContainer.firstChild) {
        handContainer.removeChild(handContainer.firstChild);
    }
}
function addCardsToHand(playerID) {
    let hand = playerHands[playerID];
    let handContainer = document.getElementById(playerID + "hand");
    if (!handContainer) {
        handContainer = document.createElement("DIV");
        handContainer.setAttribute("id", playerID + "hand");
        handContainer.setAttribute("class", "hand-container");
        document.getElementById(playerID + "row").appendChild(handContainer);
    }
    for (let i = 0; i < hand.length; i++) {
        let card = document.createElement("INPUT");
        card.setAttribute("src", "/imgs/cards/" + hand[i] + ".svg");
        card.setAttribute("type", "image");
        card.setAttribute("id", playerID + hand[i]);
        card.setAttribute("class", "card");
        card.setAttribute("onclick", `sendDiscard('${playerID}', '${hand[i]}')`);
        card.setAttribute("disabled", "");
        handContainer.appendChild(card);
    }
}
//jQuery button handlers
$(function () {
    $("#registerBtn").click(function () {
        registerPlayer();
    });
    $("#startBtn").click(function () {
        startGame();
    });
    $("#draw").click(function () {
        drawCard();
    });
    $("#leaveHotseat").click(function () {
        changeHotseat();
    });
    $("#prosperityOk").click(function () {
        changePlayerProsperity();
    });
    $("#yesSponsor").click(function () {
        yesSponsorResponse();
    });
    $("#noSponsor").click(function () {
        noSponsorResponse();
    });
    $("#quitStage").click(function () {
        sendStage();
    });
    $("#sponsorDone").click(function () {
        sponsorDone();
    });
    $("#yesStage").click(function () {
        yesStage();
    });
    $("#noStage").click(function () {
        noStage();
    });
    $("#leaveQuestDiscard").click(function () {
        changePlayerQuestDiscard();
    });
    $("#quitAttack").click(function () {
        sendAttack();
    });
});