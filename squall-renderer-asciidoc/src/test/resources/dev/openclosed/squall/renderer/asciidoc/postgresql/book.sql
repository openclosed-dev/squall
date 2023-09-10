/**
 * A database for books.
 * @label Book Database
 */
CREATE DATABASE bookdb;

/**
 * A schema for books.
 * @label Book Schema
 */
CREATE SCHEMA book;

/**
 * A sequence for generating author identifiers.
 * @label Author ID
 */
CREATE SEQUENCE book.author_id;

/**
 * A sequence for generating book identifiers.
 * @label Book ID
 */
CREATE SEQUENCE book.book_id;

/**
 * Author of the books.
 * @label Author
 */
CREATE TABLE book.author (
  /**
   * The identifier of this author.
   * @label Author ID
   */
  id bigserial PRIMARY KEY,
  /**
   * The name of this author.
   * @label Author Name
   */
  name varchar(128) NOT NULL,
  /**
   * The birth year of this author.
   * @label Birth Year
   */
  birth_year integer NOT NULL
);

/**
 * A book.
 * @label Book
 */
CREATE TABLE book.book (
  /**
   * The identifier of this book.
   * @label Book ID
   */
  id bigserial PRIMARY KEY,
  /**
   * The author of this book.
   * @label Author ID
   */
  author_id bigint NOT NULL,
  /**
   * The title of this book.
   * @label Title
   */
  title varchar(256) NOT NULL,

  FOREIGN KEY(author_id) REFERENCES book.author(id)
);
