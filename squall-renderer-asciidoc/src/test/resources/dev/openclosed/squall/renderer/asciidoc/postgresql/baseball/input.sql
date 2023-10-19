/**
 * A database of Major League Baseball (MLB).
 * @label mlb
 */
CREATE DATABASE mlb;

/**
 * MLB leagues.
 * @label League
 */
CREATE TABLE league(
  /**
   * Unique ID of the league.
   * - `NL` (the National League)
   * - `AL` (the American League)
   * @label ID
   */
  id char(2) PRIMARY KEY,
  /**
   * The name of the league.
   * @label Name
   */
  name varchar(128) NOT NULL
);

/**
 * Baseball teams in MLB.
 * @label Baseball team
 */
CREATE TABLE baseball_team(
  /**
   * Unique ID of the team.
   * @label ID
   */
  id integer PRIMARY KEY,
  /**
   * The name of the team.
   * @label Name
   */
  name varchar(128) NOT NULL,
  /**
   * The league to which this team belongs.
   * @label Affiliated league
   */
  affiliated_league char(2) NOT NULL REFERENCES league(id),
  /**
   * The year when this team was established.
   * @label Year established
   */
  year_established integer NOT NULL
);

/**
 * A sequence for generating baseball game IDs.
 * @label Game ID
 */
CREATE SEQUENCE game_id AS bigint;

/**
 * Ballpark.
 * @label Ballpark
 */
CREATE TABLE ballpark(
  /**
   * Unique ID of the ballpark.
   * @label ID
   */
  id integer PRIMARY KEY,
  /**
   * The name of the ballpark.
   * @label Name
   */
  name varchar(128) NOT NULL
);

/**
 * Baseball game.
 * @label Baseball game
 */
CREATE TABLE baseball_game(
  /**
   * Unique ID of the baseball game.
   * @label Baseball game ID
   */
  id bigint DEFAULT nextval('game_id') PRIMARY KEY,
  /**
   * The team batting first.
   * @label Batting first
   */
  batting_first integer NOT NULL REFERENCES baseball_team(id),
  /**
   * The team fielding first.
   * @label Fielding first
   */
  fielding_first integer NOT NULL REFERENCES baseball_team(id),
  /**
   * Ballpark where the game was held.
   * @label Ballpark.
   */
  ballpark integer NOT NULL REFERENCES ballpark(id)
);
