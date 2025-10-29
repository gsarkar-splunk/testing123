-- Check if the 'data' table exists in the database
SELECT name FROM sqlite_master
WHERE type = 'table' AND name = 'data';
