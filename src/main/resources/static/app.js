var stompClient = null;

//Establishing connection
async function connect() {
    return new Promise((resolve, reject) => {
        var socket = new SockJS("/gameOfQuests-websocket");
        stompClient = Stomp.over(socket);
        console.log("Attempting connection...");

        stompClient.connect({}, frame => {
            console.log("Connected: " + frame);
            resolve();
        }, error => {
            console.error("Connection failed: ", error);
            reject(error);
        });
    });
}

//Add event listener for when the DOM is loaded to then initalize connection
document.addEventListener("DOMContentLoaded", () => {
    initializeConnection()
        .then(() => {
        console.log("Connection to server established!");
    })
        .catch(error => {
        console.error("Connection error: ", error);
    });
});

async function initializeConnection() {
    try {
        await connect();
        document.getElementById("startBtn").style.visibility = "visible";
        setupSubscriptions();
    } catch (error) {
        console.error("An error occurred during connection:", error);
    }
}

function startGame() {
    document.getElementById("startBtn").style.visibility = "hidden";
    stompClient.send("/app/start", {}, JSON.stringify({ name: "" }));
}

function registerPlayer() {
    document.getElementById("registerBtn").style.visibility = "hidden";
    stompClient.send("/app/connect", {}, JSON.stringify({ name: "" }));
}

//Set up WebSocket subscriptions
function setupSubscriptions() {
    stompClient.subscribe("/topic/message", function (message) {
        console.log("Received game message: ", message.body);
    });
}

//jQuery button handlers
$(function () {
    $("#startBtn").click(function () {
        startGame();
    });
    $("#registerBtn").click(function () {
        registerPlayer();
    });
});