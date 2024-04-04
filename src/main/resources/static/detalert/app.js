var currentUser = null;
var isConnected = false;

var stompClient = new StompJs.Client({
    brokerURL: 'ws://katpoc.duckdns.org/prioritymsg-websocket'
    //brokerURL: 'ws://localhost:8080/prioritymsg-websocket'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ', frame);
    currentUser = frame.headers["user-name"];
    $("#username").text(currentUser);
    stompClient.subscribe('/topic/messages', (response) => {
        $("#name").val("");
        console.log("Response: ", response);
        var message = JSON.parse(response.body)
        addMessageEntry(message);
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    isConnected = connected;
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
}

function toggleConnection() {
    isConnected ? disconnect() : connect();
}

function connect() {
    stompClient.activate();
    setConnected(true);
    $("#connect").removeClass("is-dark");
    $("#connect").addClass("is-primary");
    $("#connect").addClass("is-active");
    $("#connectedIcon").text('comment');
    $("#connectedIcon").addClass('connected_icon');
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");

    $("#connect").removeClass("is-primary");
    $("#connect").removeClass("is-active");
    $("#connectedIcon").removeClass('connected_icon');
    $("#connect").addClass("is-dark");
    $("#connectedIcon").text('comments_disabled');
}

function sendMessage(isPriority) {
    stompClient.publish({
        destination: "/app/msg/messages",
        body: JSON.stringify({'message': $("#name").val(), 'priority': isPriority})
    });
}

function addMessageEntry(message) {
    var username = message.username;
    var messageText = message.message;
    var time = message.time;
    var isPriority = message.priority;
    var isCurrentUser = currentUser === username;

    var messageEntry = '<div class="columns is-mobile">';
    var emptyColumn = '<div class="column is-one-fifth"></div>';
    var messageColumn = '<div class="column"><div class="notification pt-1 pb-2' + (isCurrentUser ? ' is-link' : (isPriority ? ' priority-notification' : '')) + '"><article class="media"><div class="media-content"><span class="is-size-7 has-text-grey' + (isCurrentUser ? 'dark' : 'light') + '">' + username + '</span><p>' + messageText + '</p></div><figure class="media-right">' + (isPriority ? '<div class="circle"></div>' : '') + '</figure></article></div></div>'

    messageEntry += (isCurrentUser ? emptyColumn + messageColumn : messageColumn + emptyColumn) + "</div>";

    // TODO: Add time as tooltip
    //messageEntry += "<td>" + time + "</td></tr>";
    console.log(messageEntry);

    $("#conversation").append(messageEntry);
    $("html").animate({ scrollTop: $("html").prop("scrollHeight")}, 1000);
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => toggleConnection());
    $( "#conversation" ).hide();
    $( "#send" ).click(() => sendMessage(false));
    $( "#sendMobile" ).click(() => sendMessage(false));
    $( "#sendPriority" ).click(() => sendMessage(true));
});