CREATE TABLE medicines (
	barcode integer NOT NULL PRIMARY KEY,
	"name" varchar NULL,
	dosage varchar NULL,
	generic bool NULL,
	international_common_name varchar NULL,
	form varchar NULL,
	packaging_size varchar NULL
);

CREATE TABLE persons (
    person_uuid uuid PRIMARY KEY,
    name character varying
);

CREATE TABLE stock (
    uuid uuid NOT NULL PRIMARY KEY,
    medicine_barcode integer NOT NULL,
    validity_end_date timestamp without time zone,
    created timestamp without time zone,
    person_uuid uuid NOT NULL,
    FOREIGN KEY (medicine_barcode) REFERENCES medicines(barcode),
    FOREIGN KEY (person_uuid) REFERENCES persons(person_uuid)
);

CREATE TABLE stock_notifications (
    uuid uuid NOT NULL PRIMARY KEY,
    stock_uuid uuid,
    title character varying,
    body character varying,
    created timestamp without time zone,
    FOREIGN KEY (stock_uuid) REFERENCES stock(uuid)
);

CREATE TABLE medicine_image (
    medicine_image_uuid uuid PRIMARY KEY,
    medicine_barcode integer,
    image text,
    created timestamp without time zone,
    FOREIGN KEY (medicine_barcode) REFERENCES medicines(barcode)
);

CREATE TABLE regular_medication (
    uuid uuid NOT NULL PRIMARY KEY,
    medicine_barcode integer,
    day_period_id integer,
    created timestamp without time zone,
    observations text,
    person_uuid uuid NOT NULL,
    FOREIGN KEY (day_period_id) REFERENCES day_periods(id),
    FOREIGN KEY (person_uuid) REFERENCES persons(person_uuid)
);

CREATE TABLE temporary_medication (
    uuid uuid PRIMARY KEY,
    medication_uuid uuid,
    days integer,
    start_date timestamp without time zone,
    FOREIGN KEY (medication_uuid) REFERENCES regular_medication(uuid)
);

