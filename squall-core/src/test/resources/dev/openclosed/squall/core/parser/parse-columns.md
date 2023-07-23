# default value of numeric

```sql
CREATE TABLE example (
  a numeric default 9.99
);
```

```json
{
  "name" : "a",
  "dataType" : "numeric",
  "defaultValue" : {
    "type": "number",
    "value": "9.99"
  },
  "nullable": true,
  "unique": false
}
```

# default value of string

```sql
CREATE TABLE example (
  a text default 'hello world'
);
```

```json
{
  "name" : "a",
  "dataType" : "text",
  "defaultValue" : {
    "type": "string",
    "value": "hello world"
  },
  "nullable": true,
  "unique": false
}
```

# default value of empty string

```sql
CREATE TABLE example (
  a text default ''
);
```

```json
{
  "name" : "a",
  "dataType" : "text",
  "defaultValue" : {
    "type": "string",
    "value": ""
  },
  "nullable": true,
  "unique": false
}
```

# default value of bit string

```sql
CREATE TABLE example (
  a bit varying default b'1001'
);
```

```json
{
  "name": "a",
  "dataType": "bit varying",
  "defaultValue": {
    "type": "bit_string",
    "value":"1001"
  },
  "nullable": true,
  "unique": false
}
```

# default value with true

```sql
CREATE TABLE example (
  a boolean default true
);
```

```json
{
  "name" : "a",
  "dataType" : "boolean",
  "defaultValue" : {
    "type": "boolean",
    "value": "true"
  },
  "nullable": true,
  "unique": false
}
```

# default value with false

```sql
CREATE TABLE example (
  a boolean default false
);
```

```json
{
  "name" : "a",
  "dataType" : "boolean",
  "defaultValue" : {
    "type": "boolean",
    "value": "false"
  },
  "nullable": true,
  "unique": false
}
```

# default value with current_timestamp

```sql
CREATE TABLE example (
  a timestamp default CURRENT_TIMESTAMP
);
```

```json
{
  "name" : "a",
  "dataType" : "timestamp",
  "defaultValue" : {
    "type": "function",
    "name": "CURRENT_TIMESTAMP"
  },
  "nullable": true,
  "unique": false
}
```

# default value null

```sql
CREATE TABLE example (
  a character varying default NULL
);
```

```json
{
  "name" : "a",
  "dataType" : "character varying",
  "defaultValue" : {
    "type": "null"
  },
  "nullable": true,
  "unique": false
}
```

# not-null constraint

```sql
CREATE TABLE example (
  a integer NOT NULL
);
```

```json
{
  "name" : "a",
  "dataType" : "integer",
  "nullable" : false,
  "unique": false
}
```

# null constraint

```sql
CREATE TABLE example (
  a text NULL
);
```

```json
{
  "name" : "a",
  "dataType" : "text",
  "nullable" : true,
  "unique": false
}
```

# default value followed by not null

```sql
CREATE TABLE example (
  a numeric DEFAULT 9.99 NOT NULL
);
```

```json
{
  "name" : "a",
  "dataType" : "numeric",
  "defaultValue" : {
    "type": "number",
    "value": "9.99"
  },
  "nullable": false,
  "unique": false
}
```
