-- Write MySQL query to find IPs that mode more than a certain number of requests for a given time period.
SELECT ip
FROM "log-parser".logs
WHERE request_date_time BETWEEN '2017-01-01 00:00:000' AND '2017-01-02 00:00:00'
GROUP BY ip
HAVING count(ip) > 500
ORDER BY count(ip) DESC;

-- Write MySQL query to find requests made by a given IP
SELECT request
FROM "log-parser".logs
WHERE ip = '192.168.234.82'