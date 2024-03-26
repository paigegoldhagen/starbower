CREATE TABLE IF NOT EXISTS NotifyState (
	PK_NotifyStateID INT PRIMARY KEY,
	NotifyStateEnabled BIT
)
AS SELECT * FROM CSVREAD('${CurrentWorkingDirectory}/NotifyState.csv');

CREATE TABLE IF NOT EXISTS Expansion (
	PK_ExpansionID INT PRIMARY KEY,
	ExpansionName NVARCHAR(50) NOT NULL
)
AS SELECT * FROM CSVREAD('${CurrentWorkingDirectory}/Expansion.csv');

CREATE TABLE IF NOT EXISTS Category (
	PK_CategoryID INT PRIMARY KEY,
	CategoryName NVARCHAR(50) NOT NULL,
	FK_Category_NotifyState INT,
	FK_Category_Expansion INT NOT NULL,

	FOREIGN KEY (FK_Category_NotifyState)
	REFERENCES NotifyState(PK_NotifyStateID),

	FOREIGN KEY (FK_Category_Expansion)
	REFERENCES Expansion(PK_ExpansionID)
)
AS SELECT * FROM CSVREAD('${CurrentWorkingDirectory}/Category.csv');

CREATE TABLE IF NOT EXISTS Kind (
	PK_KindID INT PRIMARY KEY,
	KindName NVARCHAR(50) NOT NULL
)
AS SELECT * FROM CSVREAD('${CurrentWorkingDirectory}/Kind.csv');

CREATE TABLE IF NOT EXISTS Map (
	PK_MapID INT PRIMARY KEY,
	MapName NVARCHAR(50) NOT NULL
)
AS SELECT * FROM CSVREAD('${CurrentWorkingDirectory}/Map.csv');

CREATE TABLE IF NOT EXISTS Waypoint (
	PK_WaypointID INT PRIMARY KEY,
	WaypointName NVARCHAR(50) NOT NULL,
	FK_Waypoint_Map INT NOT NULL,

	FOREIGN KEY (FK_Waypoint_Map)
	REFERENCES Map(PK_MapID)
)
AS SELECT * FROM CSVREAD('${CurrentWorkingDirectory}/Waypoint.csv');

CREATE TABLE IF NOT EXISTS DynamicEvent (
	PK_DynamicEventID INT PRIMARY KEY,
	DynamicEventName NVARCHAR(50) NOT NULL,
	FK_DynamicEvent_NotifyState INT NOT NULL,
	FK_DynamicEvent_Category INT NOT NULL,
	FK_DynamicEvent_Kind INT NOT NULL,
	FK_DynamicEvent_Waypoint INT NOT NULL,

	FOREIGN KEY (FK_DynamicEvent_NotifyState)
	REFERENCES NotifyState(PK_NotifyStateID),

	FOREIGN KEY (FK_DynamicEvent_Category)
	REFERENCES Category(PK_CategoryID),

	FOREIGN KEY (FK_DynamicEvent_Kind)
	REFERENCES Kind(PK_KindID),

	FOREIGN KEY (FK_DynamicEvent_Waypoint)
	REFERENCES Waypoint(PK_WaypointID)
)
AS SELECT * FROM CSVREAD('${CurrentWorkingDirectory}/DynamicEvent.csv');

CREATE TABLE IF NOT EXISTS Schedule (
	PK_ScheduleID INT PRIMARY KEY,
	ScheduleTime TIME,
	ScheduleFrequency INT,
	FK_Schedule_DynamicEvent INT NOT NULL,

	FOREIGN KEY (FK_Schedule_DynamicEvent)
	REFERENCES DynamicEvent(PK_DynamicEventID)
)
AS SELECT * FROM CSVREAD('${CurrentWorkingDirectory}/Schedule.csv');

CREATE TABLE IF NOT EXISTS Festival (
	FK_Festival_Category INT NOT NULL,
	FestivalStart TIMESTAMP NOT NULL,
	FestivalEnd TIMESTAMP NOT NULL,

	FOREIGN KEY (FK_Festival_Category)
	REFERENCES Category(PK_CategoryID)
)
AS SELECT * FROM CSVREAD('${CurrentWorkingDirectory}/Festival.csv');