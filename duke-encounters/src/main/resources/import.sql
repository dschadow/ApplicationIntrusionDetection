-- ROLES
INSERT INTO roles(id, rolename, role) VALUES (1, 'user', 'ROLE_USER');
INSERT INTO roles(id, rolename, role) VALUES (2, 'admin', 'ROLE_ADMIN');

-- USERS
INSERT INTO users (id, firstname, lastname, email, username, password, registration_date, level, role_id, enabled) VALUES (1, 'Arthur', 'Dent', 'arthur@dent.com', 'arthur@dent.com', '$2a$10$UYOseFWgR87PwL2j8VmycO.IjuH2uGht3lGFGBn2ShUUS9B4zclIe', '2010-05-05 10:45:33', 'PRO', 1, TRUE);
INSERT INTO users (id, firstname, lastname, email, username, password, registration_date, level, role_id, enabled) VALUES (2, 'Ford', 'Prefect', 'ford@prefect.com', 'ford@prefect.com', '$2a$10$8yg/hBDXP0bpQuuKJneYbucl0/NsjfT1zQjMsEcnFrjt.6tVnrAEW', '2012-10-01 11:02:29', 'ROOKIE', 1, TRUE);
INSERT INTO users (id, firstname, lastname, email, username, password, registration_date, level, role_id, enabled) VALUES (3, 'Zaphod', 'Beeblebrox', 'zaphod@beeblebrox.com', 'zaphod@beeblebrox.com', '$2a$10$fBUuTN5MQafWRqWULmC8DuN30CTWOphJZlUOJnO/woK2uPM.5e4vC', '2015-09-23 08:52:11', 'NEWBIE', 1, TRUE);
INSERT INTO users (id, firstname, lastname, email, username, password, registration_date, level, role_id, enabled) VALUES (4, 'Marvin', null, 'marvin@marvin.com', 'marvin@marvin.com', '$2a$10$PL7R5GjZX2xJJDaGK9PSsuq4nUWM4v9XaBC0lsIRYhv8OpHFEMl8u', '2010-01-01 08:52:11', 'NEWBIE', 1, TRUE);
INSERT INTO users (id, firstname, lastname, email, username, password, registration_date, level, role_id, enabled) VALUES (5, 'Humma', 'Kavula', 'humma@kavula.com', 'humma@kavula.com', '$2a$10$wRQ2YUJI8TTK0jWLRidwbu0HJxAdOGWUdLV8gHjT9KkUtc/w7TdcW', '2011-02-02 08:52:11', 'PRO', 1, TRUE);
INSERT INTO users (id, firstname, lastname, email, username, password, registration_date, level, role_id, enabled) VALUES (6, 'Questular', 'Rontok', 'questular@rontok.com', 'questular@rontok.com', '$2a$10$OcYDMMBCzkOTLEn5AilcYupDNhHyZA2yowCjo9gXOtL1b0Fr0EGF2', '2012-03-03 08:52:11', 'NEWBIE', 1, TRUE);
INSERT INTO users (id, firstname, lastname, email, username, password, registration_date, level, role_id, enabled) VALUES (7, 'Deep', 'Thought', 'deep@thought.com', 'deep@thought.com', '$2a$10$8q2YjsU1axyOwC6Vjsr8VOvwpjqh84ZlAzTgUO/NV38BjsuuVoBTe', '2013-04-04 08:52:11', 'NEWBIE', 1, TRUE);
INSERT INTO users (id, firstname, lastname, email, username, password, registration_date, level, role_id, enabled) VALUES (8, 'Tricia', 'McMillan', 'tricia@mcmillan.com', 'tricia@mcmillan.com', '$2a$10$ubWNmc1TD8gDpBTR2Tfl9eE0lvYEZmB4dyvd47KLCLnTAnjNCA0e2', '2014-05-05 08:52:11', 'ROOKIE', 1, TRUE);
INSERT INTO users (id, firstname, lastname, email, username, password, registration_date, level, role_id, enabled) VALUES (9, 'Slartibartfast', null, 'slartibartfast@slartibartfast.com', 'slartibartfast@slartibartfast.com', '$2a$10$8lFXL0sPVQERcfJIG.UaIuPjoo9xeSRRdk8.Z20FdujLqohdvy8WO', '2015-06-06 08:52:11', 'NEWBIE', 1, TRUE);
INSERT INTO users (id, firstname, lastname, email, username, password, registration_date, level, role_id, enabled) VALUES (10, 'Jin', 'Jenz', 'jin@jenz.com', 'jin@jenz.com', '$2a$10$EaOdJoUZKay5RXLwR552h.QdTjYroGIU0WKT5BAWetOPSDBEA2BY2', '2010-07-07 08:52:11', 'NEWBIE', 1, TRUE);
INSERT INTO users (id, firstname, lastname, email, username, password, registration_date, level, role_id, enabled) VALUES (11, 'Gag', 'Halfrunt', 'gag@halfrunt.com', 'gag@halfrunt.com', '$2a$10$W7Ik5kkje5NGJQpKBV10TuKXGjZ2So2Oq8w9UMDuFj88ziwrONwQ6', '2011-08-08 08:52:11', 'NEWBIE', 1, TRUE);

-- ENCOUNTERS
INSERT INTO encounters (id, event, location, country, comment, date, user_id) VALUES (1, 'JavaOne 1996', 'San Francisco', 'USA', '', '1996-10-01', 5);
INSERT INTO encounters (id, event, location, country, comment, date, user_id) VALUES (2, 'JavaOne 1997', 'San Francisco', 'USA', '', '1997-10-01', 5);
INSERT INTO encounters (id, event, location, country, comment, date, user_id) VALUES (3, 'JavaOne 1998', 'San Francisco', 'USA', '', '1998-10-01', 5);
INSERT INTO encounters (id, event, location, country, comment, date, user_id) VALUES (4, 'JavaOne 1999', 'San Francisco', 'USA', '', '1999-10-01', 5);
INSERT INTO encounters (id, event, location, country, comment, date, user_id) VALUES (5, 'JavaOne 2000', 'San Francisco', 'USA', '', '2000-10-01', 5);
INSERT INTO encounters (id, event, location, country, comment, date, user_id) VALUES (6, 'JavaOne 2001', 'San Francisco', 'USA', '', '2001-10-01', 5);
INSERT INTO encounters (id, event, location, country, comment, date, user_id) VALUES (7, 'JavaOne 2002', 'San Francisco', 'USA', '', '2002-10-01', 5);
INSERT INTO encounters (id, event, location, country, comment, date, user_id) VALUES (8, 'JavaOne 2003', 'San Francisco', 'USA', '', '2003-10-01', 8);
INSERT INTO encounters (id, event, location, country, comment, date, user_id) VALUES (9, 'JavaOne 2004', 'San Francisco', 'USA', '', '2004-10-01', 8);
INSERT INTO encounters (id, event, location, country, comment, date, user_id) VALUES (10, 'JavaOne 2005', 'San Francisco', 'USA', '', '2005-10-01', 8);
INSERT INTO encounters (id, event, location, country, comment, date, user_id) VALUES (11, 'JavaOne 2006', 'San Francisco', 'USA', '', '2006-10-01', 5);
INSERT INTO encounters (id, event, location, country, comment, date, user_id) VALUES (12, 'JavaOne 2007', 'San Francisco', 'USA', '', '2007-10-01', 5);
INSERT INTO encounters (id, event, location, country, comment, date, user_id) VALUES (13, 'JavaOne 2008', 'San Francisco', 'USA', '', '2008-10-01', 5);
INSERT INTO encounters (id, event, location, country, comment, date, user_id) VALUES (14, 'JavaOne 2009', 'San Francisco', 'USA', '', '2009-10-01', 5);
INSERT INTO encounters (id, event, location, country, comment, date, user_id) VALUES (15, 'JavaOne 2010', 'San Francisco', 'USA', '', '2010-09-22', 5);
INSERT INTO encounters (id, event, location, country, comment, date, user_id) VALUES (16, 'JavaOne 2011', 'San Francisco', 'USA', '', '2011-10-01', 5);
INSERT INTO encounters (id, event, location, country, comment, date, user_id) VALUES (17, 'JavaOne 2012', 'San Francisco', 'USA', '', '2012-10-01', 1);
INSERT INTO encounters (id, event, location, country, comment, date, user_id) VALUES (18, 'JavaOne 2013', 'San Francisco', 'USA', '', '2013-09-22', 2);
INSERT INTO encounters (id, event, location, country, comment, date, user_id) VALUES (19, 'JavaOne 2014', 'San Francisco', 'USA', '', '2014-09-30', 1);

-- CONFIRMATIONS
INSERT INTO confirmations (user_id, encounter_id, date) VALUES (2, 19, '2014-09-30');
INSERT INTO confirmations (user_id, encounter_id, date) VALUES (3, 19, '2014-09-30');
INSERT INTO confirmations (user_id, encounter_id, date) VALUES (4, 19, '2014-10-01');
INSERT INTO confirmations (user_id, encounter_id, date) VALUES (7, 19, '2014-10-02');
INSERT INTO confirmations (user_id, encounter_id, date) VALUES (8, 19, '2014-10-30');
INSERT INTO confirmations (user_id, encounter_id, date) VALUES (2, 1, '1996-12-12');
INSERT INTO confirmations (user_id, encounter_id, date) VALUES (3, 2, '1998-02-15');
INSERT INTO confirmations (user_id, encounter_id, date) VALUES (2, 2, '1997-10-01');
INSERT INTO confirmations (user_id, encounter_id, date) VALUES (7, 1, '1996-10-02');
INSERT INTO confirmations (user_id, encounter_id, date) VALUES (11, 18, '2013-10-30');
INSERT INTO confirmations (user_id, encounter_id, date) VALUES (10, 18, '2013-10-30');
INSERT INTO confirmations (user_id, encounter_id, date) VALUES (1, 10, '2005-10-10');
INSERT INTO confirmations (user_id, encounter_id, date) VALUES (2, 9, '2004-12-12');
INSERT INTO confirmations (user_id, encounter_id, date) VALUES (3, 9, '2005-02-15');
INSERT INTO confirmations (user_id, encounter_id, date) VALUES (4, 9, '2004-10-01');
INSERT INTO confirmations (user_id, encounter_id, date) VALUES (4, 13, '2008-10-02');
INSERT INTO confirmations (user_id, encounter_id, date) VALUES (11, 11, '2006-10-30');
INSERT INTO confirmations (user_id, encounter_id, date) VALUES (10, 12, '2007-10-30');
INSERT INTO confirmations (user_id, encounter_id, date) VALUES (1, 17, '2012-10-10');