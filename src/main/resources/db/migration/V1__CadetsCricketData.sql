CREATE TABLE player (
    player_id BIGINT PRIMARY KEY,
    first_name TEXT NOT NULL,
    gender TEXT,
    surname TEXT,
    initial TEXT NOT NULL
);

CREATE TABLE team (
    team_id BIGINT PRIMARY KEY,
    team_name TEXT,
    club TEXT NOT NULL
);

CREATE TABLE player_team (
    player_id BIGINT NOT NULL,
    team_id BIGINT NOT NULL,
    season_id BIGINT NOT NULL,

    Primary Key (player_id, team_id, season_id),
    Foreign Key (player_id) REFERENCES Player(player_id),
    Foreign Key (team_id) REFERENCES Team(team_id),
    Foreign Key (season_id) REFERENCES Season(season_id)
);

CREATE TABLE match (
    match_id BIGINT PRIMARY KEY,
    start_date TEXT NOT NULL,
    end_date TEXT NOT NULL,
    team_id BIGINT NOT NULL,
    opponent_team_id BIGINT NOT NULL,
    match_format TEXT NOT NULL,
    match_status TEXT,
    competition_id BIGINT NOT NULL,
    season_id BIGINT NOT NULL,
    result TEXT,
    notes TEXT,

    Foreign Key (team_id) REFERENCES Team(team_id),
    Foreign Key (opponent_team_id) REFERENCES Team(team_id),
    Foreign Key (competition_id) REFERENCES Competition(competition_id),
    Foreign Key (season_id) REFERENCES Season(season_id)
);

CREATE TABLE innings (
    innings_id BIGINT PRIMARY KEY,

    match_id BIGINT NOT NULL,
    team_id BIGINT NOT NULL,

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


CREATE TABLE season (
    season_id BIGINT PRIMARY KEY,
    start_year INTEGER NOT NULL,
    end_year INTEGER NOT NULL,

    CHECK (end_year >= start_year)
);

CREATE TABLE competition (
    competition_id BIGINT PRIMARY KEY,
    competition_name TEXT NOT NULL
);

CREATE TABLE batting (
    battingid BIGINT PRIMARY KEY,

    innings_id BIGINT NOT NULL,
    player_id BIGINT NOT NULL,

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


CREATE TABLE bowling (
    bowling_id BIGINT PRIMARY KEY,
    player_id BIGINT NOT NULL,
    innings_id BIGINT NOT NULL,
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

CREATE TABLE fielding (
    fielding_id BIGINT PRIMARY KEY,
    player_id BIGINT NOT NULL,
    innings_id BIGINT NOT NULL,
    catches INTEGER,
    stumpings INTEGER,
    run_outs INTEGER,
    run_out_assists INTEGER,

    Foreign Key (player_id) REFERENCES Player(player_id),
    Foreign Key (innings_id) REFERENCES Innings(innings_id),

    UNIQUE (innings_id, player_id)
);
