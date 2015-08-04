-- USERS
INSERT INTO user (id, firstname, lastname, email, password, registration_date, level) VALUES (1, 'Dominik', 'Schadow', 'dominikschadow@gmail.com', '', '2010-05-05 10:45:33', 'PRO');
INSERT INTO user (id, firstname, lastname, email, password, registration_date, level) VALUES (2, 'John', 'Doe', 'john@doe.com', '', '2012-10-01 11:02:29', 'ROOKIE');
INSERT INTO user (id, firstname, lastname, email, password, registration_date, level) VALUES (3, 'Jane', 'Doe', 'jane@doe.com', '', '2015-09-23 08:52:11', 'NEWBIE');
INSERT INTO user (id, firstname, lastname, email, password, registration_date, level) VALUES (4, 'User', '1', '1@user.com', '', '2010-01-01 08:52:11', 'NEWBIE');
INSERT INTO user (id, firstname, lastname, email, password, registration_date, level) VALUES (5, 'User', '2', '2@user.com', '', '2011-02-02 08:52:11', 'PRO');
INSERT INTO user (id, firstname, lastname, email, password, registration_date, level) VALUES (6, 'User', '3', '3@user.com', '', '2012-03-03 08:52:11', 'NEWBIE');
INSERT INTO user (id, firstname, lastname, email, password, registration_date, level) VALUES (7, 'User', '4', '4@user.com', '', '2013-04-04 08:52:11', 'NEWBIE');
INSERT INTO user (id, firstname, lastname, email, password, registration_date, level) VALUES (8, 'User', '5', '5@user.com', '', '2014-05-05 08:52:11', 'ROOKIE');
INSERT INTO user (id, firstname, lastname, email, password, registration_date, level) VALUES (9, 'User', '6', '6@user.com', '', '2015-06-06 08:52:11', 'NEWBIE');
INSERT INTO user (id, firstname, lastname, email, password, registration_date, level) VALUES (10, 'User', '7', '7@user.com', '', '2010-07-07 08:52:11', 'NEWBIE');
INSERT INTO user (id, firstname, lastname, email, password, registration_date, level) VALUES (11, 'User', '8', '8@user.com', '', '2011-08-08 08:52:11', 'NEWBIE');
INSERT INTO user (id, firstname, lastname, email, password, registration_date, level) VALUES (12, 'User', '9', '9@user.com', '', '2012-09-09 08:52:11', 'NEWBIE');

-- ENCOUNTERS
INSERT INTO encounter (id, event, location, country, comment, date, user_id) VALUES (1, 'JavaOne 1996', 'San Francisco', 'USA', '', '1996-10-01', 5);
INSERT INTO encounter (id, event, location, country, comment, date, user_id) VALUES (2, 'JavaOne 1997', 'San Francisco', 'USA', '', '1997-10-01', 5);
INSERT INTO encounter (id, event, location, country, comment, date, user_id) VALUES (3, 'JavaOne 1998', 'San Francisco', 'USA', '', '1998-10-01', 5);
INSERT INTO encounter (id, event, location, country, comment, date, user_id) VALUES (4, 'JavaOne 1999', 'San Francisco', 'USA', '', '1999-10-01', 5);
INSERT INTO encounter (id, event, location, country, comment, date, user_id) VALUES (5, 'JavaOne 2000', 'San Francisco', 'USA', '', '2000-10-01', 5);
INSERT INTO encounter (id, event, location, country, comment, date, user_id) VALUES (6, 'JavaOne 2001', 'San Francisco', 'USA', '', '2001-10-01', 5);
INSERT INTO encounter (id, event, location, country, comment, date, user_id) VALUES (7, 'JavaOne 2002', 'San Francisco', 'USA', '', '2002-10-01', 5);
INSERT INTO encounter (id, event, location, country, comment, date, user_id) VALUES (8, 'JavaOne 2003', 'San Francisco', 'USA', '', '2003-10-01', 8);
INSERT INTO encounter (id, event, location, country, comment, date, user_id) VALUES (9, 'JavaOne 2004', 'San Francisco', 'USA', '', '2004-10-01', 8);
INSERT INTO encounter (id, event, location, country, comment, date, user_id) VALUES (10, 'JavaOne 2005', 'San Francisco', 'USA', '', '2005-10-01', 8);
INSERT INTO encounter (id, event, location, country, comment, date, user_id) VALUES (11, 'JavaOne 2006', 'San Francisco', 'USA', '', '2006-10-01', 5);
INSERT INTO encounter (id, event, location, country, comment, date, user_id) VALUES (12, 'JavaOne 2007', 'San Francisco', 'USA', '', '2007-10-01', 5);
INSERT INTO encounter (id, event, location, country, comment, date, user_id) VALUES (13, 'JavaOne 2008', 'San Francisco', 'USA', '', '2008-10-01', 5);
INSERT INTO encounter (id, event, location, country, comment, date, user_id) VALUES (14, 'JavaOne 2009', 'San Francisco', 'USA', '', '2009-10-01', 5);
INSERT INTO encounter (id, event, location, country, comment, date, user_id) VALUES (15, 'JavaOne 2010', 'San Francisco', 'USA', '', '2010-09-22', 5);
INSERT INTO encounter (id, event, location, country, comment, date, user_id) VALUES (16, 'JavaOne 2011', 'San Francisco', 'USA', '', '2011-10-01', 5);
INSERT INTO encounter (id, event, location, country, comment, date, user_id) VALUES (17, 'JavaOne 2012', 'San Francisco', 'USA', '', '2012-10-01', 1);
INSERT INTO encounter (id, event, location, country, comment, date, user_id) VALUES (18, 'JavaOne 2013', 'San Francisco', 'USA', '', '2013-09-22', 2);
INSERT INTO encounter (id, event, location, country, comment, date, user_id) VALUES (19, 'JavaOne 2014', 'San Francisco', 'USA', '', '2014-09-30', 1);

-- CONFIRMATIONS
INSERT INTO confirmation (user_id, encounter_id, date) VALUES (2, 19, '2014-09-30');
INSERT INTO confirmation (user_id, encounter_id, date) VALUES (3, 19, '2014-09-30');
INSERT INTO confirmation (user_id, encounter_id, date) VALUES (4, 19, '2014-10-01');
INSERT INTO confirmation (user_id, encounter_id, date) VALUES (7, 19, '2014-10-02');
INSERT INTO confirmation (user_id, encounter_id, date) VALUES (8, 19, '2014-10-30');
INSERT INTO confirmation (user_id, encounter_id, date) VALUES (2, 1, '1996-12-12');
INSERT INTO confirmation (user_id, encounter_id, date) VALUES (3, 2, '1998-02-15');
INSERT INTO confirmation (user_id, encounter_id, date) VALUES (2, 2, '1997-10-01');
INSERT INTO confirmation (user_id, encounter_id, date) VALUES (12, 1, '1996-10-02');
INSERT INTO confirmation (user_id, encounter_id, date) VALUES (11, 18, '2013-10-30');
INSERT INTO confirmation (user_id, encounter_id, date) VALUES (10, 18, '2013-10-30');
INSERT INTO confirmation (user_id, encounter_id, date) VALUES (1, 10, '2005-10-10');
INSERT INTO confirmation (user_id, encounter_id, date) VALUES (2, 9, '2004-12-12');
INSERT INTO confirmation (user_id, encounter_id, date) VALUES (3, 9, '2005-02-15');
INSERT INTO confirmation (user_id, encounter_id, date) VALUES (4, 9, '2004-10-01');
INSERT INTO confirmation (user_id, encounter_id, date) VALUES (12, 13, '2008-10-02');
INSERT INTO confirmation (user_id, encounter_id, date) VALUES (11, 11, '2006-10-30');
INSERT INTO confirmation (user_id, encounter_id, date) VALUES (10, 12, '2007-10-30');
INSERT INTO confirmation (user_id, encounter_id, date) VALUES (1, 17, '2012-10-10');