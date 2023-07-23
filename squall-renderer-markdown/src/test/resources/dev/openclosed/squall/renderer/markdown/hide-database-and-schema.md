# hide database and schema

```json
{
  "numbering": true,
  "hide": ["database", "schema"]
}

```

```sql
/**
 * A description of table 1.
 * @label table 1
 */
CREATE TABLE table1 (
  /**
   * A description of column A.
   * @label column A
   */
  a integer PRIMARY KEY
);

/**
 * A description of table 2.
 * @label table 2
 */
CREATE TABLE table2 (
  /**
   * A description of column B.
   * @label column B
   */
  b integer PRIMARY KEY
);
```

# Untitled
## 1. table 1 `table1` ![table]
A description of table 1.

| No. | Name | Display Name | Data Type | Precision / Length | Scale | Nullable | Unique | Default Value | Description |
| --: | :-- | :-- | :-- | --: | --: | :-: | :-: | :-- | :-- |
| 1 | `a` :key: | column A | integer | - | - | - | &#x2713; | - | A description of column A. |

## 2. table 2 `table2` ![table]
A description of table 2.

| No. | Name | Display Name | Data Type | Precision / Length | Scale | Nullable | Unique | Default Value | Description |
| --: | :-- | :-- | :-- | --: | --: | :-: | :-: | :-- | :-- |
| 1 | `b` :key: | column B | integer | - | - | - | &#x2713; | - | A description of column B. |
