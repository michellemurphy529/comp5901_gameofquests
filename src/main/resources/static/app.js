var stompClient = null;
let id = "0";
let hand = [];

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
    stompClient.send("/app/drawCard", {}, JSON.stringify({ name: id }));
}
function sendCard(card) {
    console.log(card);
    stompClient.send(
        "/app/playCard",
        {},
        JSON.stringify({ name: id + " " + card })
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
        parseStartHand(msg.p1Hand);
    } else if (id === "P2") {
        parseStartHand(msg.p2Hand);
    } else if (id === "P3") {
        parseStartHand(msg.p3Hand);
    } else if (id === "P4") {
        parseStartHand(msg.p4Hand);
    }

    //Show player hand
    document.getElementById("handLabel").style.visibility = "visible";
    addCardsToHand();

    //Disable hand and draw button if not in hotseat
    if (msg.currentPlayerInHotseat === id) {
        // $("#hand :input").attr("disabled", false);
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

//Set up subscriptions based on info sent from Game Controller
function setupSubscriptions() {
    stompClient.subscribe("/topic/message", function (greeting) {
        let msg = JSON.parse(greeting.body);
        console.log("message", msg)
        //Registering of PlayerID
        if (msg.content === "id") {
            setupPlayerID(msg);
        } else if (msg.content === "start") {
            setupStartGame(msg)
        } else if (msg.content === "draw") {
            if (msg.id === id) {
                addCard(msg.card);
            }
        }
    });
}

//Subscription helper functions
function addShieldCount(id, shields) {
    let shieldElement = document.createElement("LI");
    shieldElement.setAttribute("id", id);
    shieldElement.innerHTML = id + ": " + shields;
    document.getElementById("shieldCountList").appendChild(shieldElement);
}
function addCard(c) {
    let card = document.createElement("INPUT");
    card.setAttribute("src", "/imgs/cards/" + c + ".svg");
    card.setAttribute("type", "image");
    card.setAttribute("id", c);
    card.setAttribute("class", "card");
    card.setAttribute("onclick", "sendCard('" + c + "')");
    document.getElementById("hand").appendChild(card);
}
function addCardsToHand() {
    for (let i = 0; i < hand.length; i++) {
        let card = document.createElement("INPUT");
        card.setAttribute("src", "/imgs/cards/" + hand[i] + ".svg");
        card.setAttribute("type", "image");
        card.setAttribute("id", hand[i]);
        card.setAttribute("class", "card");
        card.setAttribute("onclick", "sendCard('" + hand[i] + "')");
        card.setAttribute("disabled", "");
        document.getElementById("hand").appendChild(card);
    }
}
function parseStartHand(msg) {
    hand = msg.split(" ");
}
function parseMessage(message) {
    let m = message.split(" ");
    console.log(m);
    return m;
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
});