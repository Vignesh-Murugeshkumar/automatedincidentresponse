export default function handler(req, res) {
  const incidents = [
    { id:1, type:'Suspicious Login', user:'user1@example.com', severity:'HIGH', score:85, status:'ACTIVE', createdAt:'2025-10-07T10:00:00' },
    { id:2, type:'Phishing Email', user:'victim@example.com', severity:'MEDIUM', score:60, status:'RESOLVED', createdAt:'2025-10-06T12:30:00' }
  ];
  res.setHeader('Content-Type', 'application/json');
  res.status(200).send(JSON.stringify(incidents));
}
