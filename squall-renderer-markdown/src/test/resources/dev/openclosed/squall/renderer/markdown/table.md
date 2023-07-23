# table in declared database and schema

```sql
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
```

# Untitled
## 1. database1 `database1` ![database]

### 1.1. schema1 `schema1` ![schema]

#### 1.1.1. table 1 `table1` ![table]
A description of table 1.

| No. | Name | Display Name | Data Type | Precision / Length | Scale | Nullable | Unique | Default Value | Description |
| --: | :-- | :-- | :-- | --: | --: | :-: | :-: | :-- | :-- |
| 1 | `a` :key: | column A | varchar | 64 | - | - | &#x2713; | - | A description of column A. |
| 2 | `b` | column B | numeric | 9 | 4 | - | - | - | A description of column B. |
| 3 | `c` | column C | integer | - | - | &#x2713; | - | - | A description of column C. |
