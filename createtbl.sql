-- Include your create table DDL statements in this file.
-- Make sure to terminate each statement with a semicolon (;)

-- LEAVE this statement on. It is required to connect to your database.
CONNECT TO cs421;

-- Remember to put the create table ddls for the tables with foreign key references
--    ONLY AFTER the parent tables has already been created.

-- This is only an example of how you add create table ddls to this file.
--   You may remove it.
CREATE TABLE BirthingInstitution
(
    Email VARCHAR(50) NOT NULL
    ,Name VARCHAR(50) NOT NULL
    ,Address VARCHAR(50) NOT NULL
    ,Website VARCHAR(50)
    ,PhoneNumber VARCHAR(50) NOT NULL
    ,PRIMARY KEY(Email)
);

CREATE TABLE BirthingCentre
(
    InstitutionEmail VARCHAR(50) NOT NULL
    ,PRIMARY KEY(InstitutionEmail)
    ,FOREIGN KEY(InstitutionEmail) REFERENCES BirthingInstitution(email)
);

CREATE TABLE CommunityClinic
(
    InstitutionEmail VARCHAR(50) NOT NULL
    ,PRIMARY KEY(InstitutionEmail)
    ,FOREIGN KEY(InstitutionEmail) REFERENCES BirthingInstitution(email)
);

CREATE TABLE Midwife
(
    PractitionerID INT NOT NULL
    ,Name VARCHAR(50) NOT NULL
    ,Email VARCHAR(50) UNIQUE NOT NULL
    ,PhoneNumber VARCHAR(50) NOT NULL
    ,InstitutionEmail VARCHAR(50) NOT NULL
    ,PRIMARY KEY(PractitionerID)
    ,FOREIGN KEY(InstitutionEmail) REFERENCES BirthingInstitution(Email)
);

CREATE TABLE InformationSession
(
  SessionID INT NOT NULL
 ,PractitionerID INT NOT NULL
 ,Language VARCHAR(50) NOT NULL
 ,Time TIME NOT NULL
 ,Date DATE NOT NULL
 ,PRIMARY KEY(SessionID)
 ,FOREIGN KEY(PractitionerID) REFERENCES Midwife(PractitionerID)
);

CREATE TABLE Patient
(
    PatientID INT NOT NULL
    ,Blood_Type VARCHAR(3)
    ,PRIMARY KEY(PatientID)
);

CREATE TABLE Mother
(
    PatientID INT NOT NULL
    ,Name VARCHAR(50) NOT NULL
    ,Address VARCHAR(50) NOT NULL
    ,PhoneNumber VARCHAR(50) NOT NULL
    ,HealthCardNum VARCHAR(15) UNIQUE NOT NULL
    ,Email VARCHAR(50) NOT NULL
    ,DOB DATE NOT NULL
    ,PRIMARY KEY(PatientID)
    ,FOREIGN KEY(PatientID) REFERENCES Patient(PatientID)
);

CREATE TABLE Father
(
    FatherID INT NOT NULL
    ,Name VARCHAR(50) NOT NULL
    ,BloodType VARCHAR(50)
    ,Address VARCHAR(50)
    ,Email VARCHAR(50)
    ,DOB DATE NOT NULL
    ,HealthCardNum VARCHAR(15) UNIQUE NOT NULL
    ,PhoneNumber VARCHAR(50) NOT NULL
    ,Profession VARCHAR(50) NOT NULL
    ,PRIMARY KEY(FatherID)
);

CREATE TABLE Parents
(
    ParentsID INT NOT NULL
    ,MotherID INT NOT NULL
    ,FatherID INT
    ,PRIMARY KEY(ParentsID)
    ,FOREIGN KEY(MotherID) REFERENCES Patient(PatientID)
    ,FOREIGN KEY(FatherID) REFERENCES Father(FatherID)
);

CREATE TABLE ISRegistrants
(
    ParentsID INT NOT NULL
    ,SessionID INT NOT NULL
    ,Attended VARCHAR(3)
    ,PRIMARY KEY(ParentsID,SessionID)
    ,FOREIGN KEY(ParentsID) REFERENCES Parents(ParentsID)
    ,FOREIGN KEY(SessionID) REFERENCES InformationSession(SessionID)
);

CREATE TABLE Pregnancy
(
    PregnancyID INT NOT NULL
    ,PregnancyNumber INT NOT NULL
    ,MenstrualDD DATE
    ,DatingUltraDD DATE
    ,ExpectedBirth DATE NOT NULL
    ,FinalDue DATE
    ,PrimaryMidwifeID INT NOT NULL
    ,BackupMidwifeID INT NOT NULL
    ,ParentsID INT NOT NULL
    ,InstitutionEmail VARCHAR(50) NOT NULL
    ,PRIMARY KEY(PregnancyID)
    ,FOREIGN KEY(PrimaryMidwifeID) REFERENCES Midwife(PractitionerID)
    ,FOREIGN KEY(BackupMidwifeID) REFERENCES Midwife(PractitionerID)
    ,FOREIGN KEY(ParentsID) REFERENCES Parents(ParentsID)
    ,FOREIGN KEY(InstitutionEmail) REFERENCES BirthingInstitution(email)
    ,CONSTRAINT checkExpectedDay CHECK(EXTRACT(day from ExpectedBirth) = 01)
    ,CONSTRAINT checkmidwives CHECK(PrimaryMidwifeID <> Pregnancy.BackupMidwifeID)
    ,CONSTRAINT checkfinaldate CHECK(FinalDue = MenstrualDD OR FinalDue = DatingUltraDD)
);

CREATE TABLE Baby
(
    PatientID INT NOT NULL
    ,PregnancyID INT NOT NULL
    ,Name VARCHAR(50)
    ,Gender VARCHAR(10)
    ,DOB DATE
    ,TOB TIME
    ,PRIMARY KEY(PatientID)
    ,FOREIGN KEY(PatientID) REFERENCES Patient(PatientID)
    ,FOREIGN KEY(PregnancyID) REFERENCES Pregnancy(PregnancyID)
);

CREATE TABLE BirthedAtBirthingCentre
(
    PregnancyID INT NOT NULL
    ,BirthingCentreEmail VARCHAR(50) NOT NULL
    ,PRIMARY KEY(PregnancyID, BirthingCentreEmail)
    ,FOREIGN KEY(BirthingCentreEmail) REFERENCES BirthingCentre(InstitutionEmail)
);

CREATE TABLE Appointment
(
    AppointmentID INT NOT NULL
    ,Date DATE NOT NULL
    ,Time TIME NOT NULL
    ,PregnancyID INT NOT NULL
    ,PRIMARY KEY(AppointmentID)
    ,FOREIGN KEY(PregnancyID) REFERENCES Pregnancy(PregnancyID)
);

CREATE TABLE AppointmentSchedules
(
    PractitionerID INT NOT NULL
    ,AppointmentID INT NOT NULL
    ,PRIMARY KEY (PractitionerID,AppointmentID)
    ,FOREIGN KEY (PractitionerID) REFERENCES Midwife(PractitionerID)
    ,FOREIGN KEY (AppointmentID) REFERENCES Appointment(AppointmentID)
);

CREATE TABLE Note
(
    NoteID INT NOT NULL
    ,AppointmentID INT NOT NULL
    ,Date DATE NOT NULL
    ,Time TIME NOT NULL
    ,PRIMARY KEY(NoteID, AppointmentID)
    ,FOREIGN KEY(AppointmentID) REFERENCES Appointment(AppointmentID)
);

CREATE TABLE Test
(
    TestID INT NOT NULL
    ,Type VARCHAR(50) NOT NULL
    ,Prescribed DATE NOT NULL
    ,SampleTaken DATE
    ,LabDone DATE
    ,Result VARCHAR(50)
    ,PractitionerID INT NOT NULL
    ,PRIMARY KEY(TestID)
    ,FOREIGN KEY(PractitionerID) REFERENCES Midwife(PractitionerID)
);

CREATE TABLE TestCompletedFor
(
    TestID INT NOT NULL
    ,PatientID INT NOT NULL
    ,PRIMARY KEY(TestID, PatientID)
    ,FOREIGN KEY(TestID) REFERENCES Test(TestID)
    ,FOREIGN KEY(PatientID) REFERENCES Patient(PatientID)
);

CREATE TABLE Technician
(
    TechnicianID INT NOT NULL
    ,Name VARCHAR(50) NOT NULL
    ,PhoneNumber VARCHAR(50) NOT NULL
    ,PRIMARY KEY(TechnicianID)
);

CREATE TABLE TestCompletedBy
(
    TestID INT NOT NULL
    ,TechnicianID INT NOT NULL
    ,PRIMARY KEY(TestID, TechnicianID)
    ,FOREIGN KEY(TestID) REFERENCES Test(TestID)
    ,FOREIGN KEY(TechnicianID) REFERENCES Technician(TechnicianID)
);






