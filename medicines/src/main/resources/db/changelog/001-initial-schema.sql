CREATE TABLE medicines (
	barcode integer NOT NULL,
	"name" varchar NULL,
	dosage varchar NULL,
	generic bool NULL,
	international_common_name varchar NULL,
	form varchar NULL,
	packaging_size varchar NULL
);

CREATE TABLE stock (
    uuid uuid NOT NULL,
    medicine_barcode integer NOT NULL,
    validity_end_date timestamp without time zone,
    created timestamp without time zone,
    person_uuid uuid NOT NULL
);

CREATE TABLE stock_notifications (
    uuid uuid NOT NULL,
    stock_uuid uuid,
    title character varying,
    body character varying,
    created timestamp without time zone
);

CREATE TABLE day_periods (
    id integer NOT NULL,
    description character varying,
    display_order integer,
    day_period_hour time without time zone
);


CREATE SEQUENCE day_periods_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE medicine_image (
    medicine_barcode integer,
    image text,
    created timestamp without time zone,
    medicine_image_uuid uuid
);

CREATE TABLE persons (
    person_uuid uuid,
    name character varying
);

CREATE TABLE regular_medication (
    uuid uuid NOT NULL,
    medicine_barcode integer,
    day_period_id integer,
    created timestamp without time zone,
    observations text,
    person_uuid uuid NOT NULL
);

CREATE TABLE temporary_medication (
    uuid uuid,
    medication_uuid uuid,
    days integer,
    start_date timestamp without time zone
);

ALTER TABLE ONLY day_periods ALTER COLUMN id SET DEFAULT nextval('day_periods_id_seq'::regclass);
ALTER TABLE ONLY stock_notifications ADD CONSTRAINT stock_notifications_pk PRIMARY KEY (uuid);
ALTER TABLE ONLY medicines ADD CONSTRAINT medicamentos_pk PRIMARY KEY (barcode);
ALTER TABLE ONLY medicine_image ADD CONSTRAINT medicine_image_unique UNIQUE (medicine_image_uuid);
ALTER TABLE ONLY day_periods ADD CONSTRAINT newtable_pk PRIMARY KEY (id);
ALTER TABLE ONLY persons ADD CONSTRAINT persons_unique UNIQUE (person_uuid);
ALTER TABLE ONLY regular_medication ADD CONSTRAINT regular_medication_pk PRIMARY KEY (uuid);
ALTER TABLE ONLY stock ADD CONSTRAINT stock_pk PRIMARY KEY (uuid);
ALTER TABLE ONLY temporary_medication ADD CONSTRAINT temporary_medication_unique UNIQUE (uuid);
ALTER TABLE ONLY stock_notifications ADD CONSTRAINT stock_notifications_fk FOREIGN KEY (stock_uuid) REFERENCES stock(uuid);
ALTER TABLE ONLY medicine_image ADD CONSTRAINT medicine_image_medicines_fk FOREIGN KEY (medicine_barcode) REFERENCES medicines(barcode);
ALTER TABLE ONLY regular_medication ADD CONSTRAINT regular_medication_fk FOREIGN KEY (day_period_id) REFERENCES day_periods(id);
ALTER TABLE ONLY regular_medication ADD CONSTRAINT regular_medication_persons_fk FOREIGN KEY (person_uuid) REFERENCES persons(person_uuid);
ALTER TABLE ONLY stock ADD CONSTRAINT stock_fk FOREIGN KEY (medicine_barcode) REFERENCES medicines(barcode);
ALTER TABLE ONLY stock ADD CONSTRAINT stock_persons_fk FOREIGN KEY (person_uuid) REFERENCES persons(person_uuid);
ALTER TABLE ONLY temporary_medication ADD CONSTRAINT temporary_medication_regular_medication_fk FOREIGN KEY (medication_uuid) REFERENCES regular_medication(uuid);
