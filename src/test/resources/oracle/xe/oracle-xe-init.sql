-- This script is run by sys user so we need to change user first
ALTER SESSION SET CONTAINER=XEPDB1;

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

