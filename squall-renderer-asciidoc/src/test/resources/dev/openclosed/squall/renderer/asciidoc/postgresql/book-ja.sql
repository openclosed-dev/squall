/**
 * 書籍のデータベース。
 * @label 書籍データベース
 */
CREATE DATABASE bookdb;

/**
 * 書籍のスキーマ。
 * @label 書籍スキーマ
 */
CREATE SCHEMA book;

/**
 * 著者IDを生成するシーケンス。
 * @label 著者ID
 */
CREATE SEQUENCE book.author_id;

/**
 * 書籍IDを生成するシーケンス。
 * @label 書籍ID
 */
CREATE SEQUENCE book.book_id;

/**
 * 書籍の著者。
 * @label 著者
 */
CREATE TABLE book.author (
  /**
   * 著者のID。
   * @label 著者ID
   */
  id bigserial PRIMARY KEY,
  /**
   * 著者の名前。
   * @label 著者名
   */
  name varchar(128) NOT NULL,
  /**
   * 著者が生まれた年。
   * @label 生年
   */
  birth_year integer NOT NULL
);

/**
 * 書籍。
 * @label 書籍
 */
CREATE TABLE book.book (
  /**
   * 書籍のID。
   * @label 書籍ID
   */
  id bigserial PRIMARY KEY,
  /**
   * この書籍の著者。
   * @label 著者ID
   */
  author_id bigint NOT NULL,
  /**
   * この書籍のタイトル。
   * @label タイトル
   */
  title varchar(256) NOT NULL,

  FOREIGN KEY(author_id) REFERENCES book.author(id)
);
