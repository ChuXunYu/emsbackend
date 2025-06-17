-- This file is executed on startup by Spring Boot to populate the database.

-- Clear existing data to ensure a clean slate on each restart.
-- Use with caution in production. In a real app, this would be handled by migrations (Flyway, Liquibase).
-- DELETE FROM task_history;
-- DELETE FROM task_submission;
-- DELETE FROM task;
-- DELETE FROM feedback;
-- DELETE FROM user_account;
-- DELETE FROM grid;


-- Insert initial user accounts
-- Passwords are encrypted using a BCrypt online tool.
-- Raw passwords: Admin@123456, User@123456
INSERT INTO user_account (id, name, phone, email, password, role, status, region, level, skills, grid_x, grid_y) VALUES
(1, '张建华', '18800000001', 'zhang.jianguo@example.com', '$2a$10$w9eS.8fH4vjR5sL7tW8e.eMa7/m2tYAE0aJvUoXHcR/z.m.Y6G1tS', 'ADMIN', 'ACTIVE', '河北省', 'PROVINCE', NULL, NULL, NULL),
(2, '李志强', '18800000002', 'li.zhiqiang@example.com', '$2a$10$w9eS.8fH4vjR5sL7tW8e.eMa7/m2tYAE0aJvUoXHcR/z.m.Y6G1tS', 'ADMIN', 'ACTIVE', '河北省-石家庄市', 'CITY', NULL, NULL, NULL),
(3, '王伟', '18800000003', 'wang.wei@example.com', '$2a$10$5Zz.p.yP7iO4J1J1k.5/iO1r1K.1gG.5G.5c1A.5e.5a.5e', 'GRID_WORKER', 'ACTIVE', '河北省-石家庄市-长安区', 'CITY', '["大气异味", "工业排放"]', 55, 55),
(4, '刘丽', '18800000004', 'li.li@example.com', '$2a$10$5Zz.p.yP7iO4J1J1k.5/iO1r1K.1gG.5G.5c1A.5e.5a.5e', 'SUPERVISOR', 'ACTIVE', '河北省-石家庄市-裕华区', 'CITY', NULL, NULL, NULL),
(5, '陈思远', '18800000005', 'chen.siyuan@example.com', '$2a$10$5Zz.p.yP7iO4J1J1k.5/iO1r1K.1gG.5G.5c1A.5e.5a.5e', 'DECISION_MAKER', 'ACTIVE', '全国', 'PROVINCE', NULL, NULL, NULL),
(6, '赵敏', '18800000006', 'zhao.min@example.com', '$2a$10$5Zz.p.yP7iO4J1J1k.5/iO1r1K.1gG.5G.5c1A.5e.5a.5e', 'PUBLIC_SUPERVISOR', 'ACTIVE', '河北省-石家庄市-桥西区', 'CITY', NULL, NULL, NULL)
ON DUPLICATE KEY UPDATE name=VALUES(name), phone=VALUES(phone), email=VALUES(email), password=VALUES(password), role=VALUES(role), status=VALUES(status), region=VALUES(region), level=VALUES(level), skills=VALUES(skills), grid_x=VALUES(grid_x), grid_y=VALUES(grid_y);

-- Insert grid data (a simple 10x10 area for example)
-- Let's make a few cells obstacles
INSERT INTO grid (id, grid_x, grid_y, city_name, district_name, is_obstacle) VALUES
(1, 52, 55, '石家庄市', '长安区', true),
(2, 53, 55, '石家庄市', '长安区', true),
(3, 54, 55, '石家庄市', '长安区', false),
(4, 55, 55, '石家庄市', '长安区', false),
(5, 56, 55, '石家庄市', '长安区', false)
ON DUPLICATE KEY UPDATE grid_x=VALUES(grid_x), grid_y=VALUES(grid_y), city_name=VALUES(city_name), district_name=VALUES(district_name), is_obstacle=VALUES(is_obstacle); 