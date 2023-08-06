# create simple sequence

```sql
CREATE SEQUENCE seq1;
```

```json
{
  "name": "seq1",
  "qualifiedName": "seq1",
  "typeName": "bigint",
  "start": 1,
  "increment": 1,
  "minValue": 1,
  "maxValue": 9223372036854775807
}
```

# create sequence in schema

```sql
CREATE SEQUENCE schema1.seq1;
```

```json
{
  "name": "seq1",
  "qualifiedName": "schema1.seq1",
  "typeName": "bigint",
  "start": 1,
  "increment": 1,
  "minValue": 1,
  "maxValue": 9223372036854775807
}
```

# create sequence if not exists

```sql
CREATE SEQUENCE IF NOT EXISTS seq1;
```

```json
{
  "name": "seq1",
  "qualifiedName": "seq1",
  "typeName": "bigint",
  "start": 1,
  "increment": 1,
  "minValue": 1,
  "maxValue": 9223372036854775807
}
```

# create sequence of bigint

```sql
CREATE SEQUENCE seq1 AS bigint;
```

```json
{
  "name": "seq1",
  "qualifiedName": "seq1",
  "typeName": "bigint",
  "start": 1,
  "increment": 1,
  "minValue": 1,
  "maxValue": 9223372036854775807
}
```

# create sequence of integer

```sql
CREATE SEQUENCE seq1 AS integer;
```

```json
{
  "name": "seq1",
  "qualifiedName": "seq1",
  "typeName": "integer",
  "start": 1,
  "increment": 1,
  "minValue": 1,
  "maxValue": 2147483647
}
```

# create sequence of smallint

```sql
CREATE SEQUENCE seq1 AS smallint;
```

```json
{
  "name": "seq1",
  "qualifiedName": "seq1",
  "typeName": "smallint",
  "start": 1,
  "increment": 1,
  "minValue": 1,
  "maxValue": 32767
}
```

# multiple options

```sql
CREATE SEQUENCE seq1 AS bigint START WITH 100 INCREMENT BY 3 MINVALUE 42 MAXVALUE 999;
```

```json
{
  "name": "seq1",
  "qualifiedName": "seq1",
  "typeName": "bigint",
  "start": 100,
  "increment": 3,
  "minValue": 42,
  "maxValue": 999
}
```

# descending sequence of bigint

```sql
CREATE SEQUENCE seq1 AS bigint INCREMENT BY -1;
```

```json
{
  "name": "seq1",
  "qualifiedName": "seq1",
  "typeName": "bigint",
  "start": -1,
  "increment": -1,
  "minValue": -9223372036854775808,
  "maxValue": -1
}
```

# descending sequence of integer

```sql
CREATE SEQUENCE seq1 AS integer INCREMENT BY -1;
```

```json
{
  "name": "seq1",
  "qualifiedName": "seq1",
  "typeName": "integer",
  "start": -1,
  "increment": -1,
  "minValue": -2147483648,
  "maxValue": -1
}
```

# descending sequence of smallint

```sql
CREATE SEQUENCE seq1 AS smallint INCREMENT BY -1;
```

```json
{
  "name": "seq1",
  "qualifiedName": "seq1",
  "typeName": "smallint",
  "start": -1,
  "increment": -1,
  "minValue": -32768,
  "maxValue": -1
}
```

# no minvalue/no maxvalue

```sql
CREATE SEQUENCE seq1 NO MINVALUE NO MAXVALUE;
```

```json
{
  "name": "seq1",
  "qualifiedName": "seq1",
  "typeName": "bigint",
  "start": 1,
  "increment": 1,
  "minValue": 1,
  "maxValue": 9223372036854775807
}
```
