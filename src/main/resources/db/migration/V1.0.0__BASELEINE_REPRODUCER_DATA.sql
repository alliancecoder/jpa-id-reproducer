/* ENTITY USING LONG AS ID */

-- SEQUENCE
CREATE SEQUENCE long_integer_seq
    INCREMENT 1
    START 54
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE entities_using_long
(
    long_as_id bigint NOT NULL DEFAULT nextval('long_integer_seq'),
    non_unique_text character varying(255) NOT NULL,
    other_unique_item bigint,
    CONSTRAINT entities_using_long_pkey PRIMARY KEY (long_as_id),
    CONSTRAINT uk_unique_other_by_long UNIQUE (other_unique_item)
);

-- DATA
INSERT INTO entities_using_long(long_as_id, other_unique_item, non_unique_text)
VALUES      (1, 1, 'FIRST RECORD'),
            (2, 2, 'SECOND RECORD'),
            (3, 3, 'THIRD RECORD'),
            (4, 4, 'FOURTH RECORD');

/**********************************************************************/

/* ENTITY USING UUID AS ID */
CREATE TABLE entities_using_uuid
(
    uuid_as_id uuid NOT NULL,
    non_unique_text character varying(255) NOT NULL,
    other_unique_item bigint,
    CONSTRAINT entities_using_uuid_pkey PRIMARY KEY (uuid_as_id),
    CONSTRAINT uk_unique_other_by_uuid UNIQUE (other_unique_item)
);

-- DATA
INSERT INTO entities_using_uuid(uuid_as_id, other_unique_item, non_unique_text)
VALUES      ('5745a8efdfba43189a1ca7de656d5e70', 1, 'FIRST RECORD'),
            ('0bd24fc45a0643b3a8ac66be10970b44', 2, 'SECOND RECORD'),
            ('a1b219834e224c39a51053a7d3f05ed3', 3, 'THIRD RECORD'),
            ('819841bdb0724101b0c1c188be81c250', 4, 'FOURTH RECORD');