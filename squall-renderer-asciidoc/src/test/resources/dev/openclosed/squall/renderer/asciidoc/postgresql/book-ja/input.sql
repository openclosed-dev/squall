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
  birth_year integer NOT NULL,
  /**
   * このレコードをデータベースに登録した日時。
   * @label 登録日時
   */
  created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
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
   * この書籍のタイトル。
   * @label タイトル
   */
  title varchar(256) NOT NULL,
  /**
   * この本のタグ。スペース区切り。
   * @label タグ
   */
  tags varchar(256),
  /**
   * この本の価格。
   * @label 価格
   * @deprecated このカラムは使用しないこと。
   */
  price numeric(8, 2),
  /**
   * このレコードをデータベースに登録した日時。
   * @label 登録日時
   */
  created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

/**
 * 本の著者。
 * @label 本著者
 */
CREATE TABLE book.book_author(
  /**
   * 書籍のID。
   * @label 書籍ID
   */
  book_id bigint NOT NULL,
  /**
   * 著者のID。
   * @label 著者ID
   */
  author_id bigint NOT NULL,
  /**
   * この本における著者の序数。
   * 番号は`1`から開始し、これは第一執筆者を意味する。
   * @label 序数
   */
  ordinal_number integer NOT NULL DEFAULT 0,

  PRIMARY KEY(book_id, author_id),
  FOREIGN KEY(book_id) REFERENCES book.book(id),
  FOREIGN KEY(author_id) REFERENCES book.author(id)
);

/**
 * 本のカテゴリ。
 * @label カテゴリ
 * @deprecated 分類目的では本のタグを使用すること。
 */
CREATE TABLE book.category (
  /**
   * カテゴリのID。
   * @label Category ID
   */
  id bigserial PRIMARY KEY,
  /**
   * カテゴリの名前。
   * @label Name
   */
  name varchar(256) NOT NULL
);
