/*
Created: 24.05.2022
Modified: 04.07.2023
Model: Logical model
Database: Oracle 12c
*/


-- Create sequences section -------------------------------------------------

CREATE SEQUENCE ProwadzacySeq1
 INCREMENT BY 1
 START WITH 1
 NOMAXVALUE
 NOMINVALUE
 CACHE 20
/

CREATE SEQUENCE KonsultacjeSeq1
 INCREMENT BY 1
 START WITH 1
 NOMAXVALUE
 NOMINVALUE
 CACHE 20
/

CREATE SEQUENCE RealizacjeSeq1
 INCREMENT BY 1
 START WITH 1
 NOMAXVALUE
 NOMINVALUE
 CACHE 20
/

CREATE SEQUENCE StudenciSeq1
 INCREMENT BY 1
 START WITH 1
 NOMAXVALUE
 NOMINVALUE
 CACHE 20
/

CREATE SEQUENCE SpecjalizacjeSeq1
 INCREMENT BY 1
 START WITH 1
 NOMAXVALUE
 NOMINVALUE
 CACHE 20
/

CREATE SEQUENCE PrzedmiotySeq1
 INCREMENT BY 1
 START WITH 1
 NOMAXVALUE
 NOMINVALUE
 CACHE 20
/

CREATE SEQUENCE ForaSeq1
 INCREMENT BY 1
 START WITH 1
 NOMAXVALUE
 NOMINVALUE
 CACHE 20
/

-- Create tables section -------------------------------------------------

-- Table Prowadzacy

CREATE TABLE Prowadzacy(
  Nr_prowadzacego Integer NOT NULL,
  Imie Varchar2(30 ) NOT NULL,
  Nazwisko Varchar2(30 ) NOT NULL,
  Plec Char(1 ) NOT NULL
        CONSTRAINT CheckConstraintD1 CHECK (Plec IN ('M','K')),
  Tytul Char(1 ) NOT NULL
        CONSTRAINT CheckConstraintD2 CHECK (Tytul IN ('M','D','H','N,'Z')),
  Email Varchar2(30 ) NOT NULL,
  Nr_telefonu Varchar2(12 ),
  Haslo Char(6 ) NOT NULL,
  Zdjecie Blob
)
/

-- Add keys for table Prowadzacy

ALTER TABLE Prowadzacy ADD CONSTRAINT Prowadzacy_PK PRIMARY KEY (Nr_prowadzacego)
/

-- Table Studenci

CREATE TABLE Studenci(
  Nr_studenta Integer NOT NULL,
  Nr_indeksu Char(6 ) NOT NULL,
  Imie Varchar2(20 ) NOT NULL,
  Nazwisko Varchar2(30 ) NOT NULL,
  Plec Char(1 ) NOT NULL
        CONSTRAINT CheckConstraintD1 CHECK (Plec IN ('M','K')),
  Email Varchar2(30 ) NOT NULL,
  Nr_telefonu Varchar2(12 ),
  Zdjecie Blob
)
/

-- Add keys for table Studenci

ALTER TABLE Studenci ADD CONSTRAINT Student_PK PRIMARY KEY (Nr_studenta)
/

-- Table Konsultacje

CREATE TABLE Konsultacje(
  nr_konsultacji Integer NOT NULL,
  DataOd Date NOT NULL,
  DataDo Date NOT NULL,
  Nr_pokoju Integer,
  Czy_online Char(1 ) NOT NULL
        CONSTRAINT CheckConstraintD1 CHECK (Czy_online IN ('T','N')),
  Czy_publiczne Char(1 ) NOT NULL
        CONSTRAINT CheckConstraintD1 CHECK (Czy_publiczne IN ('T','N')),
  Pytanie Varchar2(400 ),
  Powiadomienie Varchar2(500 ),
  Nr_realizacji Integer,
  Nr_prowadzacego Integer NOT NULL
)
/

-- Create indexes for table Konsultacje

CREATE INDEX IX_Umozliwia ON Konsultacje (Nr_realizacji)
/

CREATE INDEX IX_Prowadzi_konsultacje ON Konsultacje (Nr_prowadzacego)
/

-- Add keys for table Konsultacje

ALTER TABLE Konsultacje ADD CONSTRAINT Konsultacja_PK PRIMARY KEY (nr_konsultacji)
/

-- Table Realizacje

CREATE TABLE Realizacje(
  Nr_realizacji Integer NOT NULL,
  Semestr Varchar2(3 ) NOT NULL,
  Nr_przedmiotu Integer NOT NULL
)
/

-- Create indexes for table Realizacje

CREATE INDEX IX_Przedmiot_ma_realizacje ON Realizacje (Nr_przedmiotu)
/

-- Add keys for table Realizacje

ALTER TABLE Realizacje ADD CONSTRAINT Przedmiot_PK PRIMARY KEY (Nr_realizacji)
/

-- Table Prowadzacy_Realizacja

CREATE TABLE Prowadzacy_Realizacja(
  Nr_prowadzacego Integer NOT NULL,
  Nr_realizacji Integer NOT NULL
)
/

-- Table Student_Realizacja

CREATE TABLE Student_Realizacja(
  Nr_studenta Integer NOT NULL,
  Nr_realizacji Integer NOT NULL
)
/

-- Table Student_Konsultacja

CREATE TABLE Student_Konsultacja(
  Nr_studenta Integer NOT NULL,
  nr_konsultacji Integer NOT NULL,
  Edycja Varchar2(400 )
)
/

-- Table Specjalizacje

CREATE TABLE Specjalizacje(
  Nr_specjalizacji Integer NOT NULL,
  Nazwa_specjalizacji Varchar2(30 ) NOT NULL,
  Opis Varchar2(400 )
)
/

-- Add keys for table Specjalizacje

ALTER TABLE Specjalizacje ADD CONSTRAINT PK_Specjalizacje PRIMARY KEY (Nr_specjalizacji)
/

ALTER TABLE Specjalizacje ADD CONSTRAINT Nazwa_specjalizacji UNIQUE (Nazwa_specjalizacji)
/

-- Table and Columns comments section

COMMENT ON COLUMN Specjalizacje.Nr_specjalizacji IS 'Unikatowy identyfikator specjalizacji'
/
COMMENT ON COLUMN Specjalizacje.Nazwa_specjalizacji IS 'Nazwa specjalizacji'
/
COMMENT ON COLUMN Specjalizacje.Opis IS 'Opis zdobytego doswiadczenia w ramach danej specjalizacji'
/

-- Table Prowadzacy_Specjalizacja

CREATE TABLE Prowadzacy_Specjalizacja(
  Nr_prowadzacego Integer NOT NULL,
  Nr_specjalizacji Integer NOT NULL
)
/

-- Add keys for table Prowadzacy_Specjalizacja

ALTER TABLE Prowadzacy_Specjalizacja ADD CONSTRAINT PK_Prowadzacy_Specjalizacja PRIMARY KEY (Nr_prowadzacego,Nr_specjalizacji)
/

-- Table Przedmioty

CREATE TABLE Przedmioty(
  Nr_przedmiotu Integer NOT NULL,
  Nazwa_przedmiotu NVarchar2(50) NOT NULL,
  Skrot_przedmiotu NVarchar2(5) NOT NULL,
  ECTS Integer NOT NULL
)
/

-- Add keys for table Przedmioty

ALTER TABLE Przedmioty ADD CONSTRAINT PK_Przedmioty PRIMARY KEY (Nr_przedmiotu)
/

-- Table and Columns comments section

COMMENT ON COLUMN Przedmioty.Nr_przedmiotu IS 'Unikatowy identyfikator przedmiotu'
/

-- Table Fora

CREATE TABLE Fora(
  Nr_forum Integer NOT NULL,
  Rodzaj Char(1 ) NOT NULL
        CONSTRAINT CheckConstraintD1 CHECK (Rodzaj IN ('S','P','W')),
  Nr_realizacji Integer,
  Nr_specjalizacji Integer
)
/

-- Create indexes for table Fora

CREATE INDEX IX_Dotyczy ON Fora (Nr_realizacji)
/

CREATE INDEX IX_Omawia ON Fora (Nr_specjalizacji)
/

-- Add keys for table Fora

ALTER TABLE Fora ADD CONSTRAINT PK_Fora PRIMARY KEY (Nr_forum)
/

-- Table Student_Forum

CREATE TABLE Student_Forum(
  Nr_forum Integer NOT NULL,
  Nr_studenta Integer NOT NULL
)
/

-- Add keys for table Student_Forum

ALTER TABLE Student_Forum ADD CONSTRAINT PK_Student_Forum PRIMARY KEY (Nr_forum,Nr_studenta)
/

-- Table Wiadomosci

CREATE TABLE Wiadomosci(
  Nr_wiadomosci Integer NOT NULL,
  Nadawca Varchar2(255 ) CONSTRAINT SYS_C008112 NOT NULL,
  Tekst Varchar2(4000 ) CONSTRAINT SYS_C008113 NOT NULL,
  Czas Timestamp(6) DEFAULT CURRENT_TIMESTAMP CONSTRAINT SYS_C008114 NOT NULL,
  Nr_forum Integer NOT NULL,
  Nr_prowadzacego Integer,
  Nr_studenta Integer,
  CONSTRAINT CK_WIADOMOSC_SENDER CHECK (
  (nr_studenta IS NULL AND nr_prowadzacego IS NOT NULL) OR
  (nr_studenta IS NOT NULL AND nr_prowadzacego IS NULL)
)
)
NO INMEMORY
/

-- Create indexes for table Wiadomosci

CREATE INDEX IX_Posiada ON Wiadomosci (Nr_forum)
/

CREATE INDEX IX_Relationship2 ON Wiadomosci (Nr_prowadzacego)
/

CREATE INDEX IX_Relationship3 ON Wiadomosci (Nr_studenta)
/

-- Add keys for table Wiadomosci

ALTER TABLE Wiadomosci ADD CONSTRAINT SYS_C008110 PRIMARY KEY (Nr_wiadomosci)
/

-- Create triggers for table SALBOWICZ_USER.Wiadomosci

CREATE TRIGGER TS_WIADOMOSCI_WIADOMOSCISEQ1
  BEFORE INSERT
  ON Wiadomosci
 REFERENCING  OLD AS OLD NEW AS NEW
 FOR EACH ROW
  BEGIN
  :new.Nr_wiadomosci := WiadomosciSeq1.nextval;
END;
/

-- Trigger for sequence ProwadzacySeq1 for column Nr_prowadzacego in table Prowadzacy ---------
CREATE OR REPLACE TRIGGER ts_Prowadzacy_ProwadzacySeq1 BEFORE INSERT
ON Prowadzacy FOR EACH ROW
BEGIN
  :new.Nr_prowadzacego := ProwadzacySeq1.nextval;
END;
/
CREATE OR REPLACE TRIGGER tsu_Prowadzacy_ProwadzacySeq1 AFTER UPDATE OF Nr_prowadzacego
ON Prowadzacy FOR EACH ROW
BEGIN
  RAISE_APPLICATION_ERROR(-20010,'Cannot update column Nr_prowadzacego in table Prowadzacy as it uses sequence.');
END;
/

-- Trigger for sequence StudenciSeq1 for column Nr_studenta in table Studenci ---------
CREATE OR REPLACE TRIGGER ts_Studenci_StudenciSeq1 BEFORE INSERT
ON Studenci FOR EACH ROW
BEGIN
  :new.Nr_studenta := StudenciSeq1.nextval;
END;
/
CREATE OR REPLACE TRIGGER tsu_Studenci_StudenciSeq1 AFTER UPDATE OF Nr_studenta
ON Studenci FOR EACH ROW
BEGIN
  RAISE_APPLICATION_ERROR(-20010,'Cannot update column Nr_studenta in table Studenci as it uses sequence.');
END;
/

-- Trigger for sequence KonsultacjeSeq1 for column nr_konsultacji in table Konsultacje ---------
CREATE OR REPLACE TRIGGER ts_Konsultacje_KonsultacjeSeq1 BEFORE INSERT
ON Konsultacje FOR EACH ROW
BEGIN
  :new.nr_konsultacji := KonsultacjeSeq1.nextval;
END;
/
CREATE OR REPLACE TRIGGER tsu_Konsultacje_KonsultacjeS_0 AFTER UPDATE OF nr_konsultacji
ON Konsultacje FOR EACH ROW
BEGIN
  RAISE_APPLICATION_ERROR(-20010,'Cannot update column nr_konsultacji in table Konsultacje as it uses sequence.');
END;
/

-- Trigger for sequence RealizacjeSeq1 for column Nr_realizacji in table Realizacje ---------
CREATE OR REPLACE TRIGGER ts_Realizacje_RealizacjeSeq1 BEFORE INSERT
ON Realizacje FOR EACH ROW
BEGIN
  :new.Nr_realizacji := RealizacjeSeq1.nextval;
END;
/
CREATE OR REPLACE TRIGGER tsu_Realizacje_RealizacjeSeq1 AFTER UPDATE OF Nr_realizacji
ON Realizacje FOR EACH ROW
BEGIN
  RAISE_APPLICATION_ERROR(-20010,'Cannot update column Nr_realizacji in table Realizacje as it uses sequence.');
END;
/

-- Trigger for sequence SpecjalizacjeSeq1 for column Nr_specjalizacji in table Specjalizacje ---------
CREATE OR REPLACE TRIGGER ts_Specjalizacje_Specjalizac_0 BEFORE INSERT
ON Specjalizacje FOR EACH ROW
BEGIN
  :new.Nr_specjalizacji := SpecjalizacjeSeq1.nextval;
END;
/
CREATE OR REPLACE TRIGGER tsu_Specjalizacje_Specjaliza_0 AFTER UPDATE OF Nr_specjalizacji
ON Specjalizacje FOR EACH ROW
BEGIN
  RAISE_APPLICATION_ERROR(-20010,'Cannot update column Nr_specjalizacji in table Specjalizacje as it uses sequence.');
END;
/

-- Trigger for sequence PrzedmiotySeq1 for column Nr_przedmiotu in table Przedmioty ---------
CREATE OR REPLACE TRIGGER ts_Przedmioty_PrzedmiotySeq1 BEFORE INSERT
ON Przedmioty FOR EACH ROW
BEGIN
  :new.Nr_przedmiotu := PrzedmiotySeq1.nextval;
END;
/
CREATE OR REPLACE TRIGGER tsu_Przedmioty_PrzedmiotySeq1 AFTER UPDATE OF Nr_przedmiotu
ON Przedmioty FOR EACH ROW
BEGIN
  RAISE_APPLICATION_ERROR(-20010,'Cannot update column Nr_przedmiotu in table Przedmioty as it uses sequence.');
END;
/

-- Trigger for sequence ForaSeq1 for column Nr_forum in table Fora ---------
CREATE OR REPLACE TRIGGER ts_Fora_ForaSeq1 BEFORE INSERT
ON Fora FOR EACH ROW
BEGIN
  :new.Nr_forum := ForaSeq1.nextval;
END;
/
CREATE OR REPLACE TRIGGER tsu_Fora_ForaSeq1 AFTER UPDATE OF Nr_forum
ON Fora FOR EACH ROW
BEGIN
  RAISE_APPLICATION_ERROR(-20010,'Cannot update column Nr_forum in table Fora as it uses sequence.');
END;
/


-- Create foreign keys (relationships) section ------------------------------------------------- 

ALTER TABLE Konsultacje ADD CONSTRAINT Umozliwia FOREIGN KEY (Nr_realizacji) REFERENCES Realizacje (Nr_realizacji)
/



ALTER TABLE Prowadzacy_Specjalizacja ADD CONSTRAINT Prowadzacy_ma_specjalizacje FOREIGN KEY (Nr_prowadzacego) REFERENCES Prowadzacy (Nr_prowadzacego)
/



ALTER TABLE Prowadzacy_Specjalizacja ADD CONSTRAINT Specjalizacja_nalezy FOREIGN KEY (Nr_specjalizacji) REFERENCES Specjalizacje (Nr_specjalizacji)
/



ALTER TABLE Realizacje ADD CONSTRAINT Przedmiot_ma_realizacje FOREIGN KEY (Nr_przedmiotu) REFERENCES Przedmioty (Nr_przedmiotu)
/



ALTER TABLE Konsultacje ADD CONSTRAINT Prowadzi_konsultacje FOREIGN KEY (Nr_prowadzacego) REFERENCES Prowadzacy (Nr_prowadzacego)
/



ALTER TABLE Student_Forum ADD CONSTRAINT Jest_wykorzystywane FOREIGN KEY (Nr_forum) REFERENCES Fora (Nr_forum)
/



ALTER TABLE Student_Forum ADD CONSTRAINT Uczestniczy FOREIGN KEY (Nr_studenta) REFERENCES Studenci (Nr_studenta)
/



ALTER TABLE Fora ADD CONSTRAINT Dotyczy FOREIGN KEY (Nr_realizacji) REFERENCES Realizacje (Nr_realizacji)
/



ALTER TABLE Fora ADD CONSTRAINT Omawia FOREIGN KEY (Nr_specjalizacji) REFERENCES Specjalizacje (Nr_specjalizacji)
/



ALTER TABLE Wiadomosci ADD CONSTRAINT Posiada FOREIGN KEY (Nr_forum) REFERENCES Fora (Nr_forum)
/



ALTER TABLE Wiadomosci ADD CONSTRAINT Prowadzacy_wysyla FOREIGN KEY (Nr_prowadzacego) REFERENCES Prowadzacy (Nr_prowadzacego)
/



ALTER TABLE Wiadomosci ADD CONSTRAINT Student_wysyla FOREIGN KEY (Nr_studenta) REFERENCES Studenci (Nr_studenta)
/




-- Grant permissions section -------------------------------------------------


