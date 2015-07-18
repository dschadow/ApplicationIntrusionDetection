-- USERS
INSERT INTO user (id, username, firstname, lastname, email, password, registration_date, level) VALUES (1, 'dschadow', 'Dominik', 'Schadow', 'dominikschadow@gmail.com', '', '2010-05-05 10:45:33', 'PRO');

-- ENCOUNTERS
INSERT INTO encounter (event, location, country, comment, date, user_id, confirmations) VALUES ('JavaOne 2012', 'San Francisco', 'USA', '', '2012-10-01', 1, 5);
INSERT INTO encounter (event, location, country, comment, date, user_id, confirmations) VALUES ('JavaOne 2013', 'San Francisco', 'USA', '', '2012-09-22', 1, 0);