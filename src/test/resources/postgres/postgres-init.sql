CREATE TABLE all_char_types_table (
    char_column1 BPCHAR(100),                  -- Fixed-length character string
    char_column2 CHAR(100),                    -- Fixed-length character string
    varchar_column VARCHAR(255),               -- Variable-length character string with a maximum length of 255 characters
    text_column TEXT,                          -- Variable-length character string with no specified maximum length
    char_varying_column CHARACTER VARYING(100) -- Variable-length character string with a maximum length of 20 characters
);

INSERT INTO all_char_types_table (
    char_column1,
    char_column2,
    varchar_column,
    text_column,
    char_varying_column
) VALUES (
    'FixedChar1',        -- char_column1 BPCHAR(10)
    'FixedChar2',        -- char_column2 CHAR(10)
    'VariableString',    -- varchar_column VARCHAR(255)
    'VariableText',      -- text_column TEXT
    'VarCharString'      -- char_varying_column CHARACTER VARYING(20)
);




