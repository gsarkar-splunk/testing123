-- Verify data by counting records per category
SELECT
    category,
    COUNT(*) AS count
FROM data
GROUP BY category
ORDER BY category;
