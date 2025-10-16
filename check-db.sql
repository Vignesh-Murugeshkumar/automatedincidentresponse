-- Show all tables
\dt

-- Check incidents table structure
\d incidents;

-- Count all incidents in incidents table
SELECT COUNT(*) FROM incidents;

-- Show ALL incidents from incidents table
SELECT id, type, username, severity, status, created_at FROM incidents ORDER BY created_at DESC;

-- Check if incident table exists (singular)
\d incident;

-- Count all incidents in incident table (if exists)
SELECT COUNT(*) FROM incident;

-- Show ALL incidents from incident table (if exists)
SELECT * FROM incident ORDER BY created_at DESC;