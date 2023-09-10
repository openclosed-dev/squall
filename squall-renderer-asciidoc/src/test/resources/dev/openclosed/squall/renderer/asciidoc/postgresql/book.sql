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
 * An author.
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
  birth_year integer NOT NULL,
  /**
   * The timestamp when the record was first created in the database.
   * @label Creation Time
   */
  created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
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
   * The title of this book.
   * @label Title
   */
  title varchar(256) NOT NULL,
  /**
   * Space-delimited tags for this book.
   * @label Tags
   */
  tags varchar(256),
  /**
   * The price of this book.
   * @label Price
   * @deprecated Do not use this column.
   */
  price numeric(8, 2),
  /**
   * The timestamp when the record was first created in the database.
   * @label Creation Time
   */
  created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

/**
 * Authors of the book.
 * @label Book Author
 */
CREATE TABLE book.book_author(
  /**
   * The identifier of the book.
   * @label Book ID
   */
  book_id bigint NOT NULL,
  /**
   * The identifier of the author.
   * @label Author ID
   */
  author_id bigint NOT NULL,
  /**
   * The ordinal number of the author in the book.
   * The number starts from `1`, that means the first author of the book.
   * @label Ordinal Number
   */
  ordinal_number integer NOT NULL DEFAULT 0,

  PRIMARY KEY(book_id, author_id),
  FOREIGN KEY(book_id) REFERENCES book.book(id),
  FOREIGN KEY(author_id) REFERENCES book.author(id)
);

/**
 * A category for books.
 * @label Category
 * @deprecated Use book tags for categorization purpose.
 */
CREATE TABLE book.category (
  /**
   * The identifier of this category.
   * @label Category ID
   */
  id bigserial PRIMARY KEY,
  /**
   * The name of this category.
   * @label Category Name
   */
  name varchar(256) NOT NULL
);
