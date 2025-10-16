-- Include your drop table DDL statements in this file.
-- Make sure to terminate each statement with a semicolon (;)

-- LEAVE this statement on. It is required to connect to your database.
CONNECT TO cs421;

-- Remember to put the drop table ddls for the tables with foreign key references
--    ONLY AFTER the parent tables has already been dropped (reverse of the creation order).

-- This is only an example of how you add drop table ddls to this file.
--   You may remove it.
DROP TABLE InformationSession;
DROP TABLE ISRegistrants;
DROP TABLE Midwife;
DROP TABLE BirthingInstitution;
DROP TABLE BirthingCentre;
DROP TABLE CommunityClinic;
DROP TABLE Pregnancy;
DROP TABLE Parents;
DROP TABLE Father;
DROP TABLE Patient;
DROP TABLE Baby;
DROP TABLE Mother;
DROP TABLE Appointment;
DROP TABLE AppointmentSchedules;
DROP TABLE Note;
DROP TABLE Test;
DROP TABLE TestCompletedFor;
DROP TABLE TestCompletedBy;
DROP TABLE Technician;
DROP TABLE BirthedAtBirthingCentre;