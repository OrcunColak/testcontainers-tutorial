ALTER SESSION SET CONTAINER=FREEPDB1;

-- For an oracle-xe image, the default user and schema is "TEST".
CREATE TABLE all_char_types_table (
    char_column char(100),
    varchar2_column varchar2(100),
    varchar_column varchar(100),
    nchar_column nchar(100),
    nvarchar2_column nvarchar2(100)
);

INSERT INTO all_char_types_table (
    char_column,
    varchar2_column,
    varchar_column,
    nchar_column,
    nvarchar2_column
) VALUES (
    'abcde',                    -- char_column
    'varchar2_value',           -- varchar2_column
    'varchar_value',            -- varchar_column
    N'नचर',                     -- nchar_column (assuming a Unicode value)
    N'nvarchar2_value'          -- nvarchar2_column
);

