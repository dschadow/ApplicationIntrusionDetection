-- USERS
INSERT INTO user (id, username, firstname, lastname, email, password, registration_date, level) VALUES (1, 'dominik', 'Dominik', 'Schadow', 'dominikschadow@gmail.com', '', '2010-05-05 10:45:33', 'PRO');
INSERT INTO user (id, username, firstname, lastname, email, password, registration_date, level) VALUES (2, 'john', 'John', 'Doe', 'john@doe.com', '', '2012-10-01 11:02:29', 'ROOKIE');
INSERT INTO user (id, username, firstname, lastname, email, password, registration_date, level) VALUES (3, 'jane', 'Jane', 'Doe', 'jane@doe.com', '', '2015-09-23 08:52:11', 'NEWBIE');
INSERT INTO user (id, username, firstname, lastname, email, password, registration_date, level) VALUES (4, 'user1', 'User', '1', '1@user.com', '', '2010-01-01 08:52:11', 'NEWBIE');
INSERT INTO user (id, username, firstname, lastname, email, password, registration_date, level) VALUES (5, 'user2', 'User', '2', '2@user.com', '', '2011-02-02 08:52:11', 'PRO');
INSERT INTO user (id, username, firstname, lastname, email, password, registration_date, level) VALUES (6, 'user3', 'User', '3', '3@user.com', '', '2012-03-03 08:52:11', 'NEWBIE');
INSERT INTO user (id, username, firstname, lastname, email, password, registration_date, level) VALUES (7, 'user4', 'User', '4', '4@user.com', '', '2013-04-04 08:52:11', 'NEWBIE');
INSERT INTO user (id, username, firstname, lastname, email, password, registration_date, level) VALUES (8, 'user5', 'User', '5', '5@user.com', '', '2014-05-05 08:52:11', 'ROOKIE');
INSERT INTO user (id, username, firstname, lastname, email, password, registration_date, level) VALUES (9, 'user6', 'User', '6', '6@user.com', '', '2015-06-06 08:52:11', 'NEWBIE');
INSERT INTO user (id, username, firstname, lastname, email, password, registration_date, level) VALUES (10, 'user7', 'User', '7', '7@user.com', '', '2010-07-07 08:52:11', 'NEWBIE');
INSERT INTO user (id, username, firstname, lastname, email, password, registration_date, level) VALUES (11, 'user8', 'User', '8', '8@user.com', '', '2011-08-08 08:52:11', 'NEWBIE');
INSERT INTO user (id, username, firstname, lastname, email, password, registration_date, level) VALUES (12, 'user9', 'User', '9', '9@user.com', '', '2012-09-09 08:52:11', 'NEWBIE');

-- ENCOUNTERS
INSERT INTO encounter (event, location, country, comment, date, user_id) VALUES ('JavaOne 1996', 'San Francisco', 'USA', '', '1996-10-01', 5);
INSERT INTO encounter (event, location, country, comment, date, user_id) VALUES ('JavaOne 1997', 'San Francisco', 'USA', '', '1997-10-01', 5);
INSERT INTO encounter (event, location, country, comment, date, user_id) VALUES ('JavaOne 1998', 'San Francisco', 'USA', '', '1998-10-01', 5);
INSERT INTO encounter (event, location, country, comment, date, user_id) VALUES ('JavaOne 1999', 'San Francisco', 'USA', '', '1999-10-01', 5);
INSERT INTO encounter (event, location, country, comment, date, user_id) VALUES ('JavaOne 2000', 'San Francisco', 'USA', '', '2000-10-01', 5);
INSERT INTO encounter (event, location, country, comment, date, user_id) VALUES ('JavaOne 2001', 'San Francisco', 'USA', '', '2001-10-01', 5);
INSERT INTO encounter (event, location, country, comment, date, user_id) VALUES ('JavaOne 2002', 'San Francisco', 'USA', '', '2002-10-01', 5);
INSERT INTO encounter (event, location, country, comment, date, user_id) VALUES ('JavaOne 2003', 'San Francisco', 'USA', '', '2003-10-01', 8);
INSERT INTO encounter (event, location, country, comment, date, user_id) VALUES ('JavaOne 2004', 'San Francisco', 'USA', '', '2004-10-01', 8);
INSERT INTO encounter (event, location, country, comment, date, user_id) VALUES ('JavaOne 2005', 'San Francisco', 'USA', '', '2005-10-01', 8);
INSERT INTO encounter (event, location, country, comment, date, user_id) VALUES ('JavaOne 2006', 'San Francisco', 'USA', '', '2006-10-01', 5);
INSERT INTO encounter (event, location, country, comment, date, user_id) VALUES ('JavaOne 2007', 'San Francisco', 'USA', '', '2007-10-01', 5);
INSERT INTO encounter (event, location, country, comment, date, user_id) VALUES ('JavaOne 2008', 'San Francisco', 'USA', '', '2008-10-01', 5);
INSERT INTO encounter (event, location, country, comment, date, user_id) VALUES ('JavaOne 2009', 'San Francisco', 'USA', '', '2009-10-01', 5);
INSERT INTO encounter (event, location, country, comment, date, user_id) VALUES ('JavaOne 2010', 'San Francisco', 'USA', '', '2010-09-22', 5);
INSERT INTO encounter (event, location, country, comment, date, user_id) VALUES ('JavaOne 2011', 'San Francisco', 'USA', '', '2011-10-01', 5);
INSERT INTO encounter (event, location, country, comment, date, user_id) VALUES ('JavaOne 2012', 'San Francisco', 'USA', '', '2012-10-01', 1);
INSERT INTO encounter (event, location, country, comment, date, user_id) VALUES ('JavaOne 2013', 'San Francisco', 'USA', '', '2013-09-22', 2);
INSERT INTO encounter (event, location, country, comment, date, user_id) VALUES ('JavaOne 2014', 'San Francisco', 'USA', '', '2014-09-30', 1);