// Real-time dashboard updates
let stompClient = null;

function connect() {
    const socket = new SockJS('/incident-feed');
    stompClient = Stomp.over(socket);
    
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        
        stompClient.subscribe('/topic/incidents', function (incident) {
            updateDashboard();
            showNotification('New incident detected!', 'warning');
        });
    });
}

function updateDashboard() {
    fetch('/api/dashboard/metrics')
        .then(response => response.json())
        .then(data => {
            document.querySelector('[data-metric="totalIncidents"]').textContent = data.totalIncidents;
            document.querySelector('[data-metric="activeCount"]').textContent = data.activeCount;
            document.querySelector('[data-metric="criticalCount"]').textContent = data.criticalCount;
            document.querySelector('[data-metric="trojanCount"]').textContent = data.trojanCount;
            document.querySelector('[data-metric="last24Hours"]').textContent = data.last24Hours;
        })
        .catch(error => console.error('Error updating dashboard:', error));
}

function showNotification(message, type) {
    const notification = document.createElement('div');
    notification.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
    notification.style.top = '20px';
    notification.style.right = '20px';
    notification.style.zIndex = '9999';
    notification.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    document.body.appendChild(notification);
    
    setTimeout(() => notification.remove(), 5000);
}

// Auto-refresh every 30 seconds
setInterval(updateDashboard, 30000);

// Connect on page load
document.addEventListener('DOMContentLoaded', connect);