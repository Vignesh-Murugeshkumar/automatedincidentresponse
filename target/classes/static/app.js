// Main JavaScript for Automated Incident Response System

// Global variables
let currentTheme = localStorage.getItem('theme') || 'light';
let sortDirection = {};

// Initialize application
document.addEventListener('DOMContentLoaded', function() {
    initializeTheme();
    initializeTooltips();
    initializeCharts();
    initializeWebSocket();
    
    // Add fade-in animation to cards
    const cards = document.querySelectorAll('.card');
    cards.forEach((card, index) => {
        setTimeout(() => {
            card.classList.add('fade-in');
        }, index * 100);
    });
});

// Theme Management
function initializeTheme() {
    document.documentElement.setAttribute('data-theme', currentTheme);
    updateThemeIcon();
}

function toggleTheme() {
    currentTheme = currentTheme === 'light' ? 'dark' : 'light';
    document.documentElement.setAttribute('data-theme', currentTheme);
    localStorage.setItem('theme', currentTheme);
    updateThemeIcon();
    
    // Show theme change notification
    showNotification(`Switched to ${currentTheme} theme`, 'info');
}

function updateThemeIcon() {
    const themeIcon = document.getElementById('theme-icon');
    if (themeIcon) {
        themeIcon.className = currentTheme === 'light' ? 'fas fa-moon' : 'fas fa-sun';
    }
}

// Chart Initialization
function initializeCharts() {
    // Fetch incident data and create charts
    fetchIncidentData().then(data => {
        // Dashboard Charts
        if (document.getElementById('incidentsChart')) {
            createIncidentsChart(data);
        }
        if (document.getElementById('severityChart')) {
            createSeverityChart(data);
        }
        
        // Analytics Charts
        if (document.getElementById('trendChart')) {
            createTrendChart(data);
        }
        if (document.getElementById('typesChart')) {
            createTypesChart(data);
        }
        if (document.getElementById('severityAnalyticsChart')) {
            createSeverityAnalyticsChart(data);
        }
        if (document.getElementById('responseTimeChart')) {
            createResponseTimeChart(data);
        }
    });
}

// Fetch incident data from backend
async function fetchIncidentData() {
    try {
        const response = await fetch('/api/incidents/analytics');
        if (response.ok) {
            return await response.json();
        }
    } catch (error) {
        console.error('Failed to fetch incident data:', error);
    }
    
    // Return default empty data if fetch fails
    return {
        dailyIncidents: [0, 0, 0, 0, 0, 0, 0],
        severityCounts: { CRITICAL: 0, HIGH: 0, MEDIUM: 0, LOW: 0 },
        weeklyTrend: [0, 0, 0, 0],
        weeklyResolved: [0, 0, 0, 0],
        typeCounts: { 'Malware': 0, 'Phishing': 0, 'Suspicious Login': 0, 'Data Breach': 0 },
        responseTimeCounts: [0, 0, 0, 0]
    };
}

function createIncidentsChart(data) {
    const ctx = document.getElementById('incidentsChart').getContext('2d');
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
            datasets: [{
                label: 'Incidents',
                data: data.dailyIncidents || [0, 0, 0, 0, 0, 0, 0],
                backgroundColor: 'rgba(239, 72, 17, 0.8)',
                borderColor: 'rgba(18, 69, 224, 1)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    grid: {
                        color: 'rgba(0, 0, 0, 0.1)'
                    }
                },
                x: {
                    grid: {
                        display: false
                    }
                }
            }
        }
    });
}

function createSeverityChart(data) {
    const ctx = document.getElementById('severityChart').getContext('2d');
    const severityData = data.severityCounts || {};
    new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['Critical', 'High', 'Medium', 'Low'],
            datasets: [{
                data: [
                    severityData.CRITICAL || 0,
                    severityData.HIGH || 0,
                    severityData.MEDIUM || 0,
                    severityData.LOW || 0
                ],
                backgroundColor: [
                    '#e74a3b',
                    '#f6c23e',
                    '#36b9cc',
                    '#858796'
                ],
                borderWidth: 2,
                borderColor: '#fff'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'bottom'
                }
            }
        }
    });
}

function createTrendChart(data) {
    const ctx = document.getElementById('trendChart').getContext('2d');
    new Chart(ctx, {
        type: 'line',
        data: {
            labels: ['Week 1', 'Week 2', 'Week 3', 'Week 4'],
            datasets: [{
                label: 'Total Incidents',
                data: data.weeklyTrend || [0, 0, 0, 0],
                borderColor: 'rgba(78, 115, 223, 1)',
                backgroundColor: 'rgba(15, 36, 98, 0.73)',
                tension: 0.4,
                fill: true
            }, {
                label: 'Resolved',
                data: data.weeklyResolved || [0, 0, 0, 0],
                borderColor: 'rgba(28, 200, 138, 1)',
                backgroundColor: 'rgba(28, 200, 138, 0.1)',
                tension: 0.4,
                fill: true
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            interaction: {
                intersect: false,
                mode: 'index'
            },
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

function createTypesChart(data) {
    const ctx = document.getElementById('typesChart').getContext('2d');
    const typeData = data.typeCounts || {};
    new Chart(ctx, {
        type: 'pie',
        data: {
            labels: ['Malware', 'Phishing', 'Suspicious Login', 'Data Breach'],
            datasets: [{
                data: [
                    typeData['Malware'] || 0,
                    typeData['Phishing'] || 0,
                    typeData['Suspicious Login'] || 0,
                    typeData['Data Breach'] || 0
                ],
                backgroundColor: [
                    '#e74a3b',
                    '#f6c23e',
                    '#36b9cc',
                    '#858796'
                ]
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'bottom'
                }
            }
        }
    });
}

function createSeverityAnalyticsChart(data) {
    const ctx = document.getElementById('severityAnalyticsChart').getContext('2d');
    const severityData = data.severityCounts || {};
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ['Critical', 'High', 'Medium', 'Low'],
            datasets: [{
                label: 'Count',
                data: [
                    severityData.CRITICAL || 0,
                    severityData.HIGH || 0,
                    severityData.MEDIUM || 0,
                    severityData.LOW || 0
                ],
                backgroundColor: ['#e74a3b', '#f6c23e', '#36b9cc', '#858796']
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false
                }
            }
        }
    });
}

function createResponseTimeChart(data) {
    const ctx = document.getElementById('responseTimeChart').getContext('2d');
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ['< 1 min', '1-5 min', '5-15 min', '> 15 min'],
            datasets: [{
                label: 'Incidents',
                data: data.responseTimeCounts || [0, 0, 0, 0],
                backgroundColor: 'rgba(54, 185, 204, 0.8)'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false
                }
            }
        }
    });
}

// Incident Actions
function resolveIncident(incidentId) {
    Swal.fire({
        title: 'Resolve Incident?',
        text: `Are you sure you want to mark incident #${incidentId} as resolved?`,
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#1cc88a',
        cancelButtonColor: '#858796',
        confirmButtonText: 'Yes, resolve it!'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`/api/incidents/${incidentId}/resolve`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                }
            })
            .then(response => {
                if (response.ok) {
                    Swal.fire('Resolved!', 'The incident has been resolved.', 'success');
                    setTimeout(() => location.reload(), 1500);
                } else {
                    throw new Error('Failed to resolve incident');
                }
            })
            .catch(error => {
                Swal.fire('Error!', 'Failed to resolve incident.', 'error');
            });
        }
    });
}

function dismissIncident(incidentId) {
    Swal.fire({
        title: 'Dismiss Incident?',
        text: `Are you sure you want to dismiss incident #${incidentId}?`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#f6c23e',
        cancelButtonColor: '#858796',
        confirmButtonText: 'Yes, dismiss it!'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`/api/incidents/${incidentId}/dismiss`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                }
            })
            .then(response => {
                if (response.ok) {
                    Swal.fire('Dismissed!', 'The incident has been dismissed.', 'success');
                    setTimeout(() => location.reload(), 1500);
                } else {
                    throw new Error('Failed to dismiss incident');
                }
            })
            .catch(error => {
                Swal.fire('Error!', 'Failed to dismiss incident.', 'error');
            });
        }
    });
}

// Table Functions
function sortTable(columnIndex) {
    const table = document.getElementById('incidentsTable');
    if (!table) return;
    
    const tbody = table.querySelector('tbody');
    const rows = Array.from(tbody.querySelectorAll('tr')).filter(row => 
        !row.querySelector('td[colspan]')
    );
    
    const currentDirection = sortDirection[columnIndex] || 'asc';
    const newDirection = currentDirection === 'asc' ? 'desc' : 'asc';
    sortDirection = {}; // Reset all directions
    sortDirection[columnIndex] = newDirection;
    
    rows.sort((a, b) => {
        const aValue = a.cells[columnIndex].textContent.trim();
        const bValue = b.cells[columnIndex].textContent.trim();
        
        // Handle numeric values
        if (!isNaN(aValue) && !isNaN(bValue)) {
            return newDirection === 'asc' ? aValue - bValue : bValue - aValue;
        }
        
        // Handle text values
        const comparison = aValue.localeCompare(bValue);
        return newDirection === 'asc' ? comparison : -comparison;
    });
    
    // Clear tbody and append sorted rows
    tbody.innerHTML = '';
    rows.forEach(row => tbody.appendChild(row));
    
    // Update sort indicators
    updateSortIndicators(columnIndex, newDirection);
}

function updateSortIndicators(activeColumn, direction) {
    const headers = document.querySelectorAll('#incidentsTable th');
    headers.forEach((header, index) => {
        const icon = header.querySelector('i');
        if (icon) {
            if (index === activeColumn) {
                icon.className = direction === 'asc' ? 'fas fa-sort-up' : 'fas fa-sort-down';
            } else {
                icon.className = 'fas fa-sort';
            }
        }
    });
}

function filterIncidents() {
    const statusFilter = document.getElementById('statusFilter')?.value.toLowerCase() || '';
    const severityFilter = document.getElementById('severityFilter')?.value.toLowerCase() || '';
    const typeFilter = document.getElementById('typeFilter')?.value.toLowerCase() || '';
    
    const rows = document.querySelectorAll('#incidentsTable tbody tr.incident-row');
    let visibleCount = 0;
    
    rows.forEach(row => {
        const status = row.cells[4]?.textContent.toLowerCase() || '';
        const severity = row.cells[3]?.textContent.toLowerCase() || '';
        const type = row.cells[1]?.textContent.toLowerCase() || '';
        
        const statusMatch = !statusFilter || status.includes(statusFilter);
        const severityMatch = !severityFilter || severity.includes(severityFilter);
        const typeMatch = !typeFilter || type.includes(typeFilter);
        
        if (statusMatch && severityMatch && typeMatch) {
            row.style.display = '';
            visibleCount++;
        } else {
            row.style.display = 'none';
        }
    });
    
    updateIncidentCount(visibleCount);
}

function searchIncidents() {
    const searchTerm = document.getElementById('searchInput')?.value.toLowerCase() || '';
    const rows = document.querySelectorAll('#incidentsTable tbody tr.incident-row');
    let visibleCount = 0;
    
    rows.forEach(row => {
        const text = row.textContent.toLowerCase();
        if (text.includes(searchTerm)) {
            row.style.display = '';
            visibleCount++;
        } else {
            row.style.display = 'none';
        }
    });
    
    updateIncidentCount(visibleCount);
}

function updateIncidentCount(count) {
    const countElement = document.getElementById('incidentCount');
    if (countElement) {
        countElement.textContent = count;
    }
}

// Utility Functions
function refreshIncidents() {
    showNotification('Refreshing incidents...', 'info');
    setTimeout(() => {
        location.reload();
    }, 1000);
}

function refreshAnalytics() {
    showNotification('Refreshing analytics...', 'info');
    setTimeout(() => {
        location.reload();
    }, 1000);
}

function updateCharts() {
    const timeRange = document.getElementById('timeRange')?.value || '30';
    showNotification(`Updated charts for last ${timeRange} days`, 'success');
}

function exportIncident() {
    showNotification('Exporting incident report...', 'info');
}

function showSettings() {
    Swal.fire({
        title: 'System Settings',
        html: `
            <div class="text-start">
                <div class="mb-3">
                    <label class="form-label">Email Notifications</label>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" id="emailNotifications" checked>
                        <label class="form-check-label" for="emailNotifications">
                            Enable email alerts
                        </label>
                    </div>
                </div>
                <div class="mb-3">
                    <label class="form-label">SMS Notifications</label>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" id="smsNotifications" checked>
                        <label class="form-check-label" for="smsNotifications">
                            Enable SMS alerts
                        </label>
                    </div>
                </div>
            </div>
        `,
        showCancelButton: true,
        confirmButtonText: 'Save Settings',
        cancelButtonText: 'Cancel'
    }).then((result) => {
        if (result.isConfirmed) {
            showNotification('Settings saved successfully', 'success');
        }
    });
}

function showProfile() {
    Swal.fire({
        title: 'User Profile',
        html: `
            <div class="text-center mb-3">
                <i class="fas fa-user-circle fa-4x text-primary"></i>
            </div>
            <div class="text-start">
                <p><strong>Username:</strong> admin</p>
                <p><strong>Role:</strong> System Administrator</p>
                <p><strong>Last Login:</strong> ${new Date().toLocaleString()}</p>
            </div>
        `,
        confirmButtonText: 'Close'
    });
}

function showNotification(message, type = 'info') {
    const Toast = Swal.mixin({
        toast: true,
        position: 'top-end',
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.addEventListener('mouseenter', Swal.stopTimer);
            toast.addEventListener('mouseleave', Swal.resumeTimer);
        }
    });

    Toast.fire({
        icon: type,
        title: message
    });
}

function initializeTooltips() {
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

function initializeWebSocket() {
    if (typeof WebSocket !== 'undefined') {
        try {
            const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
            const wsUrl = `${protocol}//${window.location.host}/incident-feed`;
            const socket = new WebSocket(wsUrl);
            
            socket.onopen = function() {
                console.log('WebSocket connected');
            };
            
            socket.onmessage = function(event) {
                const data = JSON.parse(event.data);
                handleWebSocketMessage(data);
            };
            
            socket.onerror = function(error) {
                console.log('WebSocket error:', error);
            };
            
            socket.onclose = function() {
                console.log('WebSocket disconnected');
                setTimeout(initializeWebSocket, 5000);
            };
        } catch (error) {
            console.log('WebSocket not supported or failed to connect');
        }
    }
}

function handleWebSocketMessage(data) {
    if (data.type === 'new_incident') {
        showNotification(`New ${data.severity} incident detected: ${data.type}`, 'warning');
    }
}