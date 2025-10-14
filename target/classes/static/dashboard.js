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

function scanDevice() {
    const btn = document.getElementById('scanDeviceBtn');
    const result = document.getElementById('scanResult');
    
    btn.disabled = true;
    btn.innerHTML = '🔄 Scanning...';
    result.innerHTML = '<div class="alert alert-info">Scanning device for threats...</div>';
    
    fetch('/api/scan/device')
        .then(response => response.text())
        .then(data => {
            btn.disabled = false;
            btn.innerHTML = '🔍 Scan Device for Threats';
            result.innerHTML = `<div class="alert alert-success">${data}</div>`;
            setTimeout(() => location.reload(), 2000);
        })
        .catch(error => {
            btn.disabled = false;
            btn.innerHTML = '🔍 Scan Device for Threats';
            result.innerHTML = `<div class="alert alert-danger">Scan failed: ${error.message}</div>`;
        });
}