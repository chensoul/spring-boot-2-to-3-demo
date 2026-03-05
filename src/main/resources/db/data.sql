-- Used by spring.datasource.data (Boot 2) → spring.sql.init.data-locations (Boot 3)
INSERT INTO item (name) SELECT 'Sample' WHERE NOT EXISTS (SELECT 1 FROM item LIMIT 1);
