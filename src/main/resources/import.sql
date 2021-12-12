/* ENTITY USING LONG AS ID */
INSERT INTO entities_using_long(long_as_id, other_unique_item, non_unique_text)
VALUES      (nextval('long_integer_seq'), 1, 'FIRST RECORD'),
            (nextval('long_integer_seq'), 51, 'SECOND RECORD'),
            (nextval('long_integer_seq'), 101, 'THIRD RECORD'),
            (nextval('long_integer_seq'), 151, 'FOURTH RECORD');