/* ENTITY USING LONG AS ID */
INSERT INTO entities_using_long(long_as_id, other_unique_item, non_unique_text)
VALUES      (nextval('long_integer_seq'), 1, 'FIRST RECORD'),
            (nextval('long_integer_seq'), 51, 'SECOND RECORD'),
            (nextval('long_integer_seq'), 101, 'THIRD RECORD'),
            (nextval('long_integer_seq'), 151, 'FOURTH RECORD');

/* ENTITY USING UUID AS ID */
-- This is annoying, I have to manually generate IDs to use for all entities (potentially many hundreds)
-- ALSO, H2 cannot use (causes)
-- Caused by: org.h2.jdbc.JdbcSQLException: Hexadecimal string contains non-hex character: "a6a7f9d4-90c9-4d8e-85d8-44a962e2ecc5"; SQL statement:
-- Commented out but can be used to create schema against PostgreSQL/CockroachDB
-- INSERT INTO entities_using_uuid(uuid_as_id, other_unique_item, non_unique_text)
-- VALUES      ('a6a7f9d4-90c9-4d8e-85d8-44a962e2ecc5', 1, 'FIRST RECORD'),
--             ('f882c4d0-45d3-41f8-ba18-d1437998e86f', 2, 'SECOND RECORD'),
--             ('c2dfa994-5e90-4f5e-94b1-178352da95ec', 3, 'THIRD RECORD'),
--             ('ddf7567d-bd3e-4e43-9bd1-8493f5ba6a4b', 4, 'FOURTH RECORD');

/* ENTITY USING UUID AS ID */
-- This is annoying, I have to manually generate IDs to use for all entities (potentially many hundreds)
-- This can be used for both H2 and PostgreSQL/CockroachDB. However, IDs are not easily referencable.
-- 5745a8efdfba43189a1ca7de656d5e70 -> 5745a8ef-dfba-4318-9a1c-a7de656d5e70 in JSON Output (all the dash placement could cause typos)
-- Creating FK could be a struggle
INSERT INTO entities_using_uuid(uuid_as_id, other_unique_item, non_unique_text)
VALUES      ('5745a8efdfba43189a1ca7de656d5e70', 1, 'FIRST RECORD'),
            ('0bd24fc45a0643b3a8ac66be10970b44', 2, 'SECOND RECORD'),
            ('a1b219834e224c39a51053a7d3f05ed3', 3, 'THIRD RECORD'),
            ('819841bdb0724101b0c1c188be81c250', 4, 'FOURTH RECORD');