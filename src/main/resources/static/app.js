var stompClient = null;
let id = "0";
let hand = [];
let playerHands = {
    "P1": [],
    "P2": [],
    "P3": [],
    "P4": []
};

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
    removeHotseatButton(id);
    stompClient.send("/app/changeHotseat", {}, JSON.stringify({ name: "" }));
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
        const hotseatLabel = document.getElementById("hotseatPlayerID")
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
    let player = {
        "P1": { hand: msg.p1Hand, discard: msg.p1Discard },
        "P2": { hand: msg.p2Hand, discard: msg.p2Discard },
        "P3": { hand: msg.p3Hand, discard: msg.p3Discard },
        "P4": { hand: msg.p4Hand, discard: msg.p4Discard }
    };

    if (id === msg.id) {
        updateHand(msg.id, player[msg.id].hand);
    }else if(id !== msg.id && id === "P1") {
        updateHand("P1", player["P1"].hand);
    }else if(id !== msg.id && id === "P2") {
        updateHand("P2", player["P2"].hand);
    }else if(id !== msg.id && id === "P3") {
        updateHand("P3", player["P3"].hand);
    }else if(id !== msg.id && id === "P4") {
        updateHand("P4", player["P4"].hand);
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
        }
        //Change Hotseat Player
        else if (msg.content === "changeHotseat") {
            removeEventCard();
            updateHotseatPlayerLabelAndDraw(msg.id);
        }
    });
}

//Subscription helper functions
function disableDraw(playerID) {
    if (id === playerID) {
        document.getElementById("draw").disabled = true;
    }
}
function showHotseatButton(playerID) {
    if (id === playerID) {
        console.log("HIT");
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
});