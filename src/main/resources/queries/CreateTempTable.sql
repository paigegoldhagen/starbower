CREATE TEMP TABLE Festival_TEMP (
	FK_Festival_Category INT NOT NULL,
	FestivalStart TIMESTAMP NOT NULL,
	FestivalEnd TIMESTAMP NOT NULL,

	FOREIGN KEY (FK_Festival_Category)
	REFERENCES Category(PK_CategoryID)
)
AS SELECT * FROM CSVREAD('${CurrentWorkingDirectory}/Festival.csv');