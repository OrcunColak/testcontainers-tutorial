-- This script is run by sys user
-- Create a new user and grant all privileges
-- This is just an example to test if sys can create new user or nor
CREATE USER test1 IDENTIFIED BY "password";
GRANT ALL PRIVILEGES TO test1;