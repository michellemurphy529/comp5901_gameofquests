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
    console.log(card);
    console.log(playerID);

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
    stompClient.send("/app/sponsorshipResponse", {}, JSON.stringify({ name: id + " no" }));
}
function sendStage() {
    console.log(id);

    hideQuitStageAndWeaponReqMessage(id);
    builtQuest[stage] = { cards: stageCards.slice(), value: calculateStageValue(stageCards) };

    stompClient.send(
        "/app/buildStage",
        {},
        JSON.stringify({ name: id + " " + stageCards.join(",") })
    );
}
function sponsorDone() {
    removeBeginQuestMessageAndButton(id);
    stompClient.send("/app/sponsorDone", {}, JSON.stringify({ name: id + "  sponsorDone" }));
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
        disableDraw(msg.id);
        showAskSponsorMessageAndButtons(msg.id);
    }
}
function executeSponsorshipResponse(msg) {
    if (id === msg.id && msg.sponsorFound === "yes") {
        if (msg.playerHand !== null) {
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
            displayEventCard(msg);
            carryOutPlague(msg);
        }else if (msg.content === "QueensFavor") {
            if(!document.getElementById("eventCard")) {
                displayEventCard(msg);
            }
            carryOutQueensFavor(msg);
        }else if (msg.content === "Prosperity") {
            if(!document.getElementById("eventCard")) {
                displayEventCard(msg);
            }
            carryOutProsperity(msg);
        }else if (msg.content.includes("Q") && msg.content.length === 2) {
            if(!document.getElementById("eventCard")) {
                displayEventCard(msg);
            }
            
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
            updateHotseatPlayerLabelAndDraw(msg.id);
        }
    });
}

//Subscription helper functions
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
            document.getElementById("stageBuildArea").appendChild(builtStagesElement);
        }
        builtStagesElement.innerHTML = "";
        for (let stage in builtQuest) {
            let stageElement = document.createElement("DIV");
            stageElement.innerHTML = `Stage ${stage}: ${builtQuest[stage].cards.join(", ")}`;
    
            let valueElement = document.createElement("DIV");
            valueElement.innerHTML = `Value ${stage}: ${builtQuest[stage].value}`;
            stageElement.appendChild(valueElement);
    
            builtStagesElement.appendChild(stageElement);
        }
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
            let beginQuestMessage = document.createElement("LABEL");
            beginQuestMessage.setAttribute("id", "sponsorDoneMessage");
            beginQuestMessage.innerHTML = "Press 'Begin Quest' to ask player's to participate!";
            document.getElementById("deck").appendChild(beginQuestMessage);
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
        sponsorID = msg.id;
    }
    if (id === msg.id) {
        //Stage building Message
        if (!document.getElementById("buildStage")) {
            //Set globals for all stages
            maxStages = parseInt(msg.content.slice(1), 10);
            console.log(maxStages);
    
            let buildingStageMessage = document.createElement("LABEL");
            buildingStageMessage.setAttribute("id", "buildStage");
            buildingStageMessage.style.visibility = "visible";
            document.getElementById("deck").appendChild(buildingStageMessage);
        }
        document.getElementById("buildStage").innerHTML = "Building Quest Stage: " + stage;

        if (!document.getElementById("foeReqMessage")) {
            let foeCardReqMessage = document.createElement("LABEL");
            foeCardReqMessage.setAttribute("id", "foeReqMessage");
            foeCardReqMessage.innerHTML = "Add 1 Foe Card to Stage now!";
            document.getElementById("deck").appendChild(foeCardReqMessage);
        }
        document.getElementById("foeReqMessage").style.visibility = "visible";

        if (!document.getElementById("weaponReqMessage")) {
            let weaponReqMessage = document.createElement("LABEL");
            weaponReqMessage.setAttribute("id", "weaponReqMessage");
            weaponReqMessage.innerHTML = "Add Non-Repeating Weapon Card(s) to Stage now! OR Press 'Quit' to finish building this stage!";
            document.getElementById("deck").appendChild(weaponReqMessage);
        }
        document.getElementById("weaponReqMessage").style.visibility = "hidden";
    }
}
function foeCardSelected(playerID, card) {
    console.log("foeCardSelected " + card)
    if (id === playerID) {
        stageCards.push(card);
        console.log(stageCards);
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
function weaponCardSelected(playerID, card) {
    stageCards.push(card);

    console.log(stageCards);

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
            document.getElementById("deck").appendChild(messageElement);
        }
        messageElement.innerHTML = `Stage value must be GREATER than ${previousStageValue}.`;
        messageElement.style.visibility = "visible";
    }
}
function removeStageValueMessage(playerID) {
    let messageElement = document.getElementById("stageValueMessage");
    if (messageElement) {
        messageElement.style.visibility = "hidden";
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
function displayStageCards(playerID) {
    if (id === playerID) {
        if (!document.getElementById("stageCards")) {
            let stageCardsElement = document.createElement("LABEL");
            stageCardsElement.setAttribute("id", "stageCards");
            stageCardsElement.innerHTML = "Stage " + stage + " Cards: " + stageCards.join(", ");
            document.getElementById("deck").appendChild(stageCardsElement);
        }
        document.getElementById("stageCards").innerHTML = "Stage " + stage + " Cards: " + stageCards.join(", ");
    }
}
function showNoSponsorFoundMessage(playerID) {
    if (id === playerID) {
        let noSponsorElement = document.createElement("LABEL");
        noSponsorElement.setAttribute("id", "noSponsorFound");
        noSponsorElement.innerHTML = "No Player's Sponsored Quest... Game Continues!";
        document.getElementById("deck").appendChild(noSponsorElement);
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
    playerHands[playerID].splice(0, playerHands[playerID].length);
    parseStartHand(newHand, playerID);
    removeCardsFromHand(playerID);
    addCardsToHand(playerID);
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
});