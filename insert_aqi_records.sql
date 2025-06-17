-- 插入测试数据到 aqi_records 表
USE ems;

-- 清空表
DELETE FROM aqi_records;

-- 插入不同 AQI 级别的记录
-- Good (0-50)
INSERT INTO aqi_records (city_name, aqi_value, record_time, pm25, pm10, so2, no2, co, o3) 
VALUES ('北京', 40, NOW() - INTERVAL 1 DAY, 10.5, 20.3, 5.1, 15.2, 0.8, 30.1);

INSERT INTO aqi_records (city_name, aqi_value, record_time, pm25, pm10, so2, no2, co, o3) 
VALUES ('上海', 35, NOW() - INTERVAL 2 DAY, 9.2, 18.5, 4.8, 14.0, 0.7, 28.5);

-- Moderate (51-100)
INSERT INTO aqi_records (city_name, aqi_value, record_time, pm25, pm10, so2, no2, co, o3) 
VALUES ('广州', 75, NOW() - INTERVAL 3 DAY, 25.3, 45.7, 10.2, 30.5, 1.5, 60.2);

INSERT INTO aqi_records (city_name, aqi_value, record_time, pm25, pm10, so2, no2, co, o3) 
VALUES ('深圳', 85, NOW() - INTERVAL 4 DAY, 28.6, 52.1, 12.3, 35.8, 1.8, 68.5);

-- Unhealthy for Sensitive Groups (101-150)
INSERT INTO aqi_records (city_name, aqi_value, record_time, pm25, pm10, so2, no2, co, o3) 
VALUES ('成都', 130, NOW() - INTERVAL 5 DAY, 45.2, 85.3, 20.5, 55.8, 3.2, 90.5);

-- Unhealthy (151-200)
INSERT INTO aqi_records (city_name, aqi_value, record_time, pm25, pm10, so2, no2, co, o3) 
VALUES ('武汉', 175, NOW() - INTERVAL 6 DAY, 60.5, 110.8, 30.2, 70.5, 4.5, 120.3);

-- Very Unhealthy (201-300)
INSERT INTO aqi_records (city_name, aqi_value, record_time, pm25, pm10, so2, no2, co, o3) 
VALUES ('西安', 250, NOW() - INTERVAL 7 DAY, 90.3, 160.5, 45.8, 95.2, 6.8, 150.5);

-- Hazardous (301+)
INSERT INTO aqi_records (city_name, aqi_value, record_time, pm25, pm10, so2, no2, co, o3) 
VALUES ('兰州', 320, NOW() - INTERVAL 8 DAY, 120.5, 200.8, 60.2, 120.5, 8.5, 180.3);

-- 查询插入后的记录数
SELECT COUNT(*) AS '记录数' FROM aqi_records;

-- 按 AQI 级别分组查询
SELECT 
    CASE 
        WHEN aqi_value <= 50 THEN 'Good'
        WHEN aqi_value <= 100 THEN 'Moderate'
        WHEN aqi_value <= 150 THEN 'Unhealthy for Sensitive Groups'
        WHEN aqi_value <= 200 THEN 'Unhealthy'
        WHEN aqi_value <= 300 THEN 'Very Unhealthy'
        ELSE 'Hazardous'
    END AS level,
    COUNT(*) AS count
FROM aqi_records
GROUP BY level; 