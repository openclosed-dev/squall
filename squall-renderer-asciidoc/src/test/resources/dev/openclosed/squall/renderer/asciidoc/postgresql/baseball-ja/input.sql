/**
 * メジャーリークの野球データベース。
 * @label mlb
 */
CREATE DATABASE mlb;

/**
 * MLBのリーグ。
 * @label リーグ
 */
CREATE TABLE league(
  /**
   * リーグの一意なID。
   * - `NL` (ナショナルリーグ)
   * - `AL` (アメリカンリーグ)
   * @label ID
   */
  id char(2) PRIMARY KEY,
  /**
   * リーグの名前。
   * @label 名前
   */
  name varchar(128) NOT NULL,
);

/**
 * MLBの野球チーム。
 * @label 野球チーム
 */
CREATE TABLE baseball_team(
  /**
   * チームの一意なID。
   * @label ID
   */
  id integer PRIMARY KEY,
  /**
   * チームの名前。
   * @label 名前
   */
  name varchar(128) NOT NULL,
  /**
   * チームが所属するリーグ。
   * @label 所属リーグ
   */
  affiliated_league char(2) NOT NULL REFERENCES league(id),
  /**
   * チームが設立された年。
   * @label Year 設立年
   */
  year_established integer NOT NULL
);

/**
 * 試合IDを生成するためのシーケンス。
 * @label 試合ID
 */
CREATE SEQUENCE game_id AS bigint;

/**
 * 野球の試合を開催する球場。
 * @label 野球場
 */
CREATE TABLE ballpark(
  /**
   * 野球場の一意なID。
   * @label ID
   */
  id integer PRIMARY KEY,
  /**
   * 野球場の名前。
   * @label 球場名
   */
  name varchar(128) NOT NULL
);

/**
 * 野球の試合。
 * @label 試合
 */
CREATE TABLE baseball_game(
  /**
   * 試合の一意なID。
   * @label 試合ID
   */
  id bigint DEFAULT nextval('game_id') PRIMARY KEY,
  /**
   * 先攻のチーム。
   * @label 先攻チーム
   */
  batting_first integer NOT NULL REFERENCES baseball_team(id),
  /**
   * 後攻のチーム。
   * @label 後攻チーム
   */
  fielding_first integer NOT NULL REFERENCES baseball_team(id),
  /**
   * 試合を開催した野球場。
   * @label 野球場
   */
  ballpark integer NOT NULL REFERENCES ballpark(id)
);
