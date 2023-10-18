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
  name varchar(128) NOT NULL,
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
