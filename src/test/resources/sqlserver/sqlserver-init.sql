CREATE TABLE datetime_types_table (
    date_column DATE,
    time_column TIME,
    datetime_column DATETIME,
    smalldatetime_column SMALLDATETIME,
    datetime2_column DATETIME2,
    datetimeoffset_column DATETIMEOFFSET(3),
    timestamp_column TIMESTAMP
);

INSERT INTO datetime_types_table (
    date_column,
    time_column,
    datetime_column,
    smalldatetime_column,
    datetime2_column,
    datetimeoffset_column
) VALUES (
    '2022-03-18',             -- date_column: Date value
    '12:34:56',               -- time_column: Time value
    '2022-03-18 12:34:56',    -- datetime_column: DateTime value
    '2022-03-18 12:34:00',    -- smalldatetime_column: SmallDateTime value
    '2022-03-18 12:34:56',    -- datetime2_column: DateTime2 value
    '2022-03-18 12:34:56+00:00' -- datetimeoffset_column: DateTimeOffset value
);
