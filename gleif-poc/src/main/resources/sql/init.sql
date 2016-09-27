
CREATE SEQUENCE HIBERNATE_SEQUENCE AS INT START WITH 0;

CREATE TABLE GLEIF_DATA_RAW(
	ID VARCHAR(50) not null,
	LEI VARCHAR(50) not null,
	LEGAL_NAME VARCHAR(2000) not null,
	LOU_LEI VARCHAR(50) not null,
	LAST_UPDATE_DATE TIMESTAMP
);