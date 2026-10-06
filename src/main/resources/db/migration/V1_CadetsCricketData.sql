CREATE TABLE Player (
    player_id INTEGER PRIMARY KEY AUTOINCREMENT,
    first_name TEXT NOT NULL,
    gender TEXT,
    surname TEXT,
    initial TEXT NOT NULL
);

CREATE TABLE Team (
    team_id INTEGER PRIMARY KEY AUTOINCREMENT,
    team_name TEXT,
    club TEXT NOT NULL
);

CREATE TABLE Player_Team (
    player_id INTEGER NOT NULL,
    team_id INTEGER NOT NULL,
    season_id INTEGER NOT NULL,

    Primary Key (player_id, team_id, season_id),
    Foreign Key (player_id) REFERENCES Player(player_id)
    Foreign Key (team_id) REFERENCES Team(team_id)
    Foreign Key (season_id) REFERENCES Season(season_id)
);

CREATE TABLE Match (
    match_id INTEGER PRIMARY KEY AUTOINCREMENT,
    start_date TEXT NOT NULL,
    end_date TEXT NOT NULL,
    team_id INTEGER NOT NULL,
    opponent_team_id INTEGER NOT NULL,
    match_format TEXT NOT NULL,
    match_status TEXT,
    competition_id INTEGER NOT NULL,
    season_id INTEGER NOT NULL,
    result TEXT,
    notes TEXT

    Foreign Key (team_id) REFERENCES Team(team_id)
    Foreign Key (opponent_team_id) REFERENCES Team(team_id)
    Foreign Key (competition_id) REFERENCES Competition(competition_id)
    Foreign Key (season_id) REFERENCES Season(season_id)
);

CREATE TABLE innings (
    innings_id INTEGER PRIMARY KEY AUTOINCREMENT,

    match_id INTEGER NOT NULL,
    team_id INTEGER NOT NULL,

    innings_number INTEGER NOT NULL,

    runs INTEGER NOT NULL DEFAULT 0,
    wickets INTEGER NOT NULL DEFAULT 0,

    overs REAL,

    declared INTEGER NOT NULL DEFAULT 0,
    completed INTEGER NOT NULL DEFAULT 0,

    FOREIGN KEY (match_id)
        REFERENCES matches(match_id),

    FOREIGN KEY (team_id)
        REFERENCES team(team_id),

    UNIQUE (match_id, innings_number)
);


CREATE TABLE Season (
    season_id INTEGER PRIMARY KEY AUTOINCREMENT,
    start_year INTEGER NOT NULL,
    end_year INTEGER NOT NULL,

    CHECK (end_year >= start_year)
);

CREATE TABLE Competition (
    competition_id INTEGER PRIMARY KEY AUTOINCREMENT,
    competition_name TEXT NOT NULL
);

CREATE TABLE Batting (
    battingid INTEGER PRIMARY KEY AUTOINCREMENT,

    innings_id INTEGER NOT NULL,
    player_id INTEGER NOT NULL,

    batting_position INTEGER,
    runs INTEGER NOT NULL DEFAULT 0,
    balls_faced INTEGER,

    fours INTEGER NOT NULL DEFAULT 0,
    sixes INTEGER  NOT NULL DEFAULT 0,

    not_out INTEGER NOT NULL DEFAULT 0,

    dismissal_type TEXT,

    FOREIGN KEY (innings_id)
        REFERENCES innings(innings_id),

    FOREIGN KEY (player_id)
        REFERENCES player(player_id),

    UNIQUE (innings_id, player_id)
);


CREATE TABLE Bowling (
    bowling_id INTEGER PRIMARY KEY AUTOINCREMENT,
    player_id INTEGER NOT NULL,
    innings_id INTEGER NOT NULL,
    overs REAL,
    maidens INTEGER,
    runs_conceded INTEGER,
    wickets INTEGER,
    dismissal_bowled INTEGER,
    dismissal_caught INTEGER,
    dismissal_lb INTEGER,
    dismissal_stumped INTEGER,
    dismissal_caught_and_bowled INTEGER,
    wides INTEGER,
    no_balls INTEGER,

    Foreign Key (player_id) REFERENCES Player(player_id),
    Foreign Key (innings_id) REFERENCES Innings(innings_id),
    UNIQUE (innings_id, player_id)
);

CREATE TABLE Fielding (
    fielding_id INTEGER PRIMARY KEY AUTOINCREMENT,
    player_id INTEGER NOT NULL,
    innings_id INTEGER NOT NULL,
    catches INTEGER,
    stumpings INTEGER,
    run_outs INTEGER,
    run_out_assists INTEGER,

    Foreign Key (player_id) REFERENCES Player(player_id),
    Foreign Key (innings_id) REFERENCES Innings(innings_id),

    UNIQUE (innings_id, player_id)
);

