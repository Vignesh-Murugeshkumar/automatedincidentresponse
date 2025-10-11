async function loadIncidents() {
  try {
    const res = await fetch('/api/incidents');
    const incidents = await res.json();

    document.getElementById('totalIncidents').textContent = incidents.length;
    document.getElementById('highPriorityCount').textContent = incidents.filter(i=>i.severity==='HIGH').length;
    document.getElementById('activeCount').textContent = incidents.filter(i=>i.status==='ACTIVE').length;
    document.getElementById('resolvedCount').textContent = incidents.filter(i=>i.status==='RESOLVED').length;

    const tbody = document.querySelector('#incidents-table tbody');
    tbody.innerHTML = '';
    incidents.forEach(i=>{
      const tr = document.createElement('tr');
      tr.innerHTML = `<td>${i.id}</td><td>${i.type}</td><td>${i.user}</td><td>${i.severity}</td><td>${i.score}</td><td>${i.status}</td><td>${i.createdAt}</td>`;
      tbody.appendChild(tr);
    });
  } catch(e){
    console.error(e);
  }
}

window.addEventListener('DOMContentLoaded', loadIncidents);
