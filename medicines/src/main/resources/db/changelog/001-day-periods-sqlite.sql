CREATE TABLE day_periods (
    id INTEGER PRIMARY KEY,
    description character varying,
    display_order integer,
    day_period_hour varchar(8)
);

INSERT INTO day_periods (description, display_order, day_period_hour) VALUES
                                                 ('BEFORE BREAKFAST', 1, '08:00'),
                                                 ('BREAKFAST', 2, '09:00'),
                                                 ('LUNCH', 3, '13:00'),
                                                 ('DINNER', 4, '20:00'),
                                                 ('NIGHT', 5, '22:00');
