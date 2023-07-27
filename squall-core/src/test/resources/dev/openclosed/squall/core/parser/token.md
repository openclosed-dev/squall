# select statement

```sql
SELECT * FROM MY_TABLE;
```

```json
[
  "KEYWORD",
  "ASTERISK",
  "KEYWORD",
  "IDENTIFIER",
  "SEMICOLON"
]
```

# update statement

```sql
UPDATE MY_TABLE SET A = 5;
```

```json
[
  "KEYWORD",
  "IDENTIFIER",
  "KEYWORD",
  "KEYWORD",
  "EQUAL",
  "INTEGER",
  "SEMICOLON"
]
```

# insert statement

```sql
INSERT INTO MY_TABLE VALUES (3, 'hi there');
```

```json
[
  "KEYWORD",
  "KEYWORD",
  "IDENTIFIER",
  "KEYWORD",
  "OPEN_PAREN",
  "INTEGER",
  "COMMA",
  "STRING",
  "CLOSE_PAREN",
  "SEMICOLON"
]
```

# create statement

```sql
CREATE TABLE products (product_no integer, name text, price numeric);
```

```json
[
  "KEYWORD",
  "KEYWORD",
  "IDENTIFIER",
  "OPEN_PAREN",
  "IDENTIFIER",
  "KEYWORD",
  "COMMA",
  "KEYWORD",
  "KEYWORD",
  "COMMA",
  "IDENTIFIER",
  "KEYWORD",
  "CLOSE_PAREN",
  "SEMICOLON"
]
```

# drop statement

```sql
DROP TABLE products;
```

```json
[
  "KEYWORD",
  "KEYWORD",
  "IDENTIFIER",
  "SEMICOLON"
]
```

# line comment

```sql
-- This is a standard SQL comment
```

```json
[
  "LINE_COMMENT"
]
```

# line comment after statement

```sql
SELECT 1; -- this is a comment
```

```json
[
  "KEYWORD",
  "INTEGER",
  "SEMICOLON",
  "LINE_COMMENT"
]
```

# block comment

```sql
/* multiline comment
 * with nesting: /* nested block comment */
 */
```

```json
[
  "BLOCK_COMMENT"
]
```

# name separator

```sql
CREATE TABLE schema1.table1 (
  age integer
);
```

```json
[
  "KEYWORD",
  "KEYWORD",
  "IDENTIFIER",
  "PERIOD",
  "IDENTIFIER",
  "OPEN_PAREN",
  "IDENTIFIER",
  "KEYWORD",
  "CLOSE_PAREN",
  "SEMICOLON"
]
```
