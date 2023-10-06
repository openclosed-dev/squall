CREATE DATABASE database1;

CREATE SCHEMA schema1;

/**
 * A description of table 1.
 * @label table 1
 */
CREATE TABLE schema1.table1 (
  /**
   * A description of column A.
   * @label column A
   */
  a varchar(64) PRIMARY KEY,
  /**
   * A description of column B.
   * @label column B
   */
  b numeric(9, 4) NOT NULL,
  /**
   * A description of column C.
   * @label column C
   */
  c integer
);
