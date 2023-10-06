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
   * @deprecated Do not use this column.
   */
  b numeric(9, 4) NOT NULL
);
