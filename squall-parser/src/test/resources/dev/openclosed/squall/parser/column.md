# default value of numeric

```sql
CREATE TABLE example (
  a numeric default 9.99
);
```

```json
{
  "name" : "a",
  "parents": ["", "defaultschema", "example"],
  "typeName" : "numeric",
  "defaultValue" : {
    "type": "number",
    "value": "9.99"
  }
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
  "parents": ["", "defaultschema", "example"],
  "typeName" : "text",
  "defaultValue" : {
    "type": "string",
    "value": "hello world"
  }
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
  "parents": ["", "defaultschema", "example"],
  "typeName" : "text",
  "defaultValue" : {
    "type": "string",
    "value": ""
  }
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
  "parents": ["", "defaultschema", "example"],
  "typeName": "bit varying",
  "defaultValue": {
    "type": "bit_string",
    "value":"1001"
  }
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
  "parents": ["", "defaultschema", "example"],
  "typeName" : "boolean",
  "defaultValue" : {
    "type": "boolean",
    "value": true
  }
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
  "parents": ["", "defaultschema", "example"],
  "typeName" : "boolean",
  "defaultValue" : {
    "type": "boolean",
    "value": false
  }
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
  "parents": ["", "defaultschema", "example"],
  "typeName" : "timestamp",
  "defaultValue" : {
    "type": "function",
    "name": "CURRENT_TIMESTAMP"
  }
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
  "parents": ["", "defaultschema", "example"],
  "typeName" : "character varying",
  "defaultValue" : {
    "type": "null"
  }
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
  "parents": ["", "defaultschema", "example"],
  "typeName" : "integer",
  "isRequired": true
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
  "parents": ["", "defaultschema", "example"],
  "typeName" : "text"
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
  "parents": ["", "defaultschema", "example"],
  "typeName" : "numeric",
  "defaultValue" : {
    "type": "number",
    "value": "9.99"
  },
  "isRequired": true
}
```
