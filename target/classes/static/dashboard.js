const socket = new SockJS('/incident-feed');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/incidents', function (message) {
        const incident = JSON.parse(message.body);
        updateDashboard(incident);
    });
});

function updateDashboard(incident) {
    // Add logic to update HTML table or alert box
    alert("New incident: " + incident.type + " | Severity: " + incident.severity);
}