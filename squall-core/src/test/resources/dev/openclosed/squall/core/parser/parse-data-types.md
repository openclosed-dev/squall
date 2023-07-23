# smallint

```sql
CREATE TABLE example (
  a smallint
);
```

```json
{
  "name" : "a",
  "dataType" : "smallint"
}
```

# integer

```sql
CREATE TABLE example (
  a integer
);
```

```json
{
  "name" : "a",
  "dataType" : "integer"
}
```

# bigint

```sql
CREATE TABLE example (
  a bigint
);
```

```json
{
  "name" : "a",
  "dataType" : "bigint"
}
```

# decimal

```sql
CREATE TABLE example (
  a decimal
);
```

```json
{
  "name" : "a",
  "dataType" : "decimal"
}
```

# decimal with precision

```sql
CREATE TABLE example (
  a decimal(3)
);
```

```json
{
  "name" : "a",
  "dataType" : "decimal",
  "precision" : 3
}
```

# decimal with scale

```sql
CREATE TABLE example (
  a decimal(3, 1)
);
```

```json
{
  "name" : "a",
  "dataType" : "decimal",
  "precision" : 3,
  "scale" : 1
}
```

# decimal with negative scale

```sql
CREATE TABLE example (
  a decimal(2, -3)
);
```

```json
{
  "name" : "a",
  "dataType" : "decimal",
  "precision" : 2,
  "scale" : -3
}
```

# decimal with larger scale

```sql
CREATE TABLE example (
  a decimal(3, 5)
);
```

```json
{
  "name" : "a",
  "dataType" : "decimal",
  "precision" : 3,
  "scale" : 5
}
```

# numeric

```sql
CREATE TABLE example (
  a numeric
);
```

```json
{
  "name" : "a",
  "dataType" : "numeric"
}
```

# numeric with precision

```sql
CREATE TABLE example (
  a numeric(3)
);
```

```json
{
  "name" : "a",
  "dataType" : "numeric",
  "precision" : 3
}
```

# numeric with scale

```sql
CREATE TABLE example (
  a numeric(3, 1)
);
```

```json
{
  "name" : "a",
  "dataType" : "numeric",
  "precision" : 3,
  "scale" : 1
}
```

# numeric with negative scale

```sql
CREATE TABLE example (
  a numeric(2, -3)
);
```

```json
{
  "name" : "a",
  "dataType" : "numeric",
  "precision" : 2,
  "scale" : -3
}
```

# numeric with larger scale

```sql
CREATE TABLE example (
  a numeric(3, 5)
);
```

```json
{
  "name" : "a",
  "dataType" : "numeric",
  "precision" : 3,
  "scale" : 5
}
```

# real

```sql
CREATE TABLE example (
  a real
);
```

```json
{
  "name" : "a",
  "dataType" : "real"
}
```

# float

```sql
CREATE TABLE example (
  a float
);
```

```json
{
  "name" : "a",
  "dataType" : "float"
}
```

# float with precision

```sql
CREATE TABLE example (
  a float(53)
);
```

```json
{
  "name" : "a",
  "dataType" : "float",
  "precision" : 53
}
```

# double precision

```sql
CREATE TABLE example (
  a double precision
);
```

```json
{
  "name" : "a",
  "dataType" : "double precision"
}
```

# character varying

```sql
CREATE TABLE example (
  a character varying(42)
);
```

```json
{
  "name" : "a",
  "dataType" : "character varying",
  "length" : 42
}
```

# character varying without length 

```sql
CREATE TABLE example (
  a character varying
);
```

```json
{
  "name" : "a",
  "dataType" : "character varying"
}
```
# varchar

```sql
CREATE TABLE example (
  a varchar(42)
);
```

```json
{
  "name" : "a",
  "dataType" : "varchar",
  "length" : 42
}
```

# varchar without length

```sql
CREATE TABLE example (
  a varchar
);
```

```json
{
  "name" : "a",
  "dataType" : "varchar"
}
```

# text

```sql
CREATE TABLE example (
  a text
);
```

```json
{
  "name" : "a",
  "dataType" : "text"
}
```

# character

```sql
CREATE TABLE example (
  a character(42)
);
```

```json
{
  "name" : "a",
  "dataType" : "character",
  "length" : 42
}
```

# char

```sql
CREATE TABLE example (
  a char(42)
);
```

```json
{
  "name" : "a",
  "dataType" : "char",
  "length" : 42
}
```


# date

```sql
CREATE TABLE example (
  a date
);
```

```json
{
  "name" : "a",
  "dataType" : "date"
}
```

# timestamp

```sql
CREATE TABLE example (
  a timestamp
);
```

```json
{
  "name" : "a",
  "dataType" : "timestamp"
}
```

# timestamp with time zone

```sql
CREATE TABLE example (
  a timestamp with time zone
);
```

```json
{
  "name" : "a",
  "dataType" : "timestamp with time zone"
}
```

# timestamp without time zone

```sql
CREATE TABLE example (
  a timestamp without time zone
);
```

```json
{
  "name" : "a",
  "dataType" : "timestamp"
}
```

# timestamp with precision

```sql
CREATE TABLE example (
  a timestamp(4)
);
```

```json
{
  "name" : "a",
  "dataType" : "timestamp",
  "precision" : 4
}
```

# timestamp with time zone with precision

```sql
CREATE TABLE example (
  a timestamp(4) with time zone
);
```

```json
{
  "name" : "a",
  "dataType" : "timestamp with time zone",
  "precision" : 4
}
```

# timestamp without time zone with precision

```sql
CREATE TABLE example (
  a timestamp(4) without time zone
);
```

```json
{
  "name" : "a",
  "dataType" : "timestamp",
  "precision" : 4
}
```

# time

```sql
CREATE TABLE example (
  a time
);
```

```json
{
  "name" : "a",
  "dataType" : "time"
}
```

# time with time zone

```sql
CREATE TABLE example (
  a time with time zone
);
```

```json
{
  "name" : "a",
  "dataType" : "time with time zone"
}
```

# time without time zone

```sql
CREATE TABLE example (
  a time without time zone
);
```

```json
{
  "name" : "a",
  "dataType" : "time"
}
```

# time with precision

```sql
CREATE TABLE example (
  a time(4)
);
```

```json
{
  "name" : "a",
  "dataType" : "time",
  "precision" : 4
}
```

# time with time zone with precision

```sql
CREATE TABLE example (
  a time(4) with time zone
);
```

```json
{
  "name" : "a",
  "dataType" : "time with time zone",
  "precision" : 4
}
```

# time without time zone with precision

```sql
CREATE TABLE example (
  a time(4) without time zone
);
```

```json
{
  "name" : "a",
  "dataType" : "time",
  "precision" : 4
}
```

# interval

```sql
CREATE TABLE example (
  a interval
);
```

```json
{
  "name" : "a",
  "dataType" : "interval"
}
```

# interval year

```sql
CREATE TABLE example (
  a interval year
);
```

```json
{
  "name" : "a",
  "dataType" : "interval year"
}
```

# interval month

```sql
CREATE TABLE example (
  a interval month
);
```

```json
{
  "name" : "a",
  "dataType" : "interval month"
}
```

# interval day

```sql
CREATE TABLE example (
  a interval day
);
```

```json
{
  "name" : "a",
  "dataType" : "interval day"
}
```

# interval hour

```sql
CREATE TABLE example (
  a interval hour
);
```

```json
{
  "name" : "a",
  "dataType" : "interval hour"
}
```

# interval minute

```sql
CREATE TABLE example (
  a interval minute
);
```

```json
{
  "name" : "a",
  "dataType" : "interval minute"
}
```

# interval second

```sql
CREATE TABLE example (
  a interval second
);
```

```json
{
  "name" : "a",
  "dataType" : "interval second"
}
```

# interval year to month

```sql
CREATE TABLE example (
  a interval year to month
);
```

```json
{
  "name" : "a",
  "dataType" : "interval year to month"
}
```

# interval day to hour

```sql
CREATE TABLE example (
  a interval day to hour
);
```

```json
{
  "name" : "a",
  "dataType" : "interval day to hour"
}
```

# interval day to minute

```sql
CREATE TABLE example (
  a interval day to minute
);
```

```json
{
  "name" : "a",
  "dataType" : "interval day to minute"
}
```

# interval day to second

```sql
CREATE TABLE example (
  a interval day to second
);
```

```json
{
  "name" : "a",
  "dataType" : "interval day to second"
}
```

# interval minute to second

```sql
CREATE TABLE example (
  a interval minute to second
);
```

```json
{
  "name" : "a",
  "dataType" : "interval minute to second"
}
```

# interval with precision

```sql
CREATE TABLE example (
  a interval(6)
);
```

```json
{
  "name" : "a",
  "dataType" : "interval",
  "precision" : 6
}
```

# interval year with precision

```sql
CREATE TABLE example (
  a interval year(6)
);
```

```json
{
  "name" : "a",
  "dataType" : "interval year",
  "precision" : 6
}
```

# interval month with precision

```sql
CREATE TABLE example (
  a interval month(6)
);
```

```json
{
  "name" : "a",
  "dataType" : "interval month",
  "precision" : 6
}
```

# interval day with precision

```sql
CREATE TABLE example (
  a interval day(6)
);
```

```json
{
  "name" : "a",
  "dataType" : "interval day",
  "precision" : 6
}
```

# interval hour with precision

```sql
CREATE TABLE example (
  a interval hour(6)
);
```

```json
{
  "name" : "a",
  "dataType" : "interval hour",
  "precision" : 6
}
```

# interval minute with precision

```sql
CREATE TABLE example (
  a interval minute(6)
);
```

```json
{
  "name" : "a",
  "dataType" : "interval minute",
  "precision" : 6
}
```

# interval second with precision

```sql
CREATE TABLE example (
  a interval second(6)
);
```

```json
{
  "name" : "a",
  "dataType" : "interval second",
  "precision" : 6
}
```

# interval year to month with precision

```sql
CREATE TABLE example (
  a interval year to month(6)
);
```

```json
{
  "name" : "a",
  "dataType" : "interval year to month",
  "precision" : 6
}
```

# interval day to hour with precision

```sql
CREATE TABLE example (
  a interval day to hour(6)
);
```

```json
{
  "name" : "a",
  "dataType" : "interval day to hour",
  "precision" : 6
}
```

# interval day to minute with precision

```sql
CREATE TABLE example (
  a interval day to minute(6)
);
```

```json
{
  "name" : "a",
  "dataType" : "interval day to minute",
  "precision" : 6
}
```

# interval day to second with precision

```sql
CREATE TABLE example (
  a interval day to second(6)
);
```

```json
{
  "name" : "a",
  "dataType" : "interval day to second",
  "precision" : 6
}
```

# interval minute to second with precision

```sql
CREATE TABLE example (
  a interval minute to second(6)
);
```

```json
{
  "name" : "a",
  "dataType" : "interval minute to second",
  "precision" : 6
}
```

# boolean

```sql
CREATE TABLE example (
  a boolean
);
```

```json
{
  "name" : "a",
  "dataType" : "boolean"
}
```

# bit

```sql
CREATE TABLE example (
  a bit
);
```

```json
{
  "name" : "a",
  "dataType" : "bit"
}
```

# bit with length

```sql
CREATE TABLE example (
  a bit(42)
);
```

```json
{
  "name" : "a",
  "dataType" : "bit",
  "length" : 42
}
```

# bit varying

```sql
CREATE TABLE example (
  a bit varying
);
```

```json
{
  "name" : "a",
  "dataType" : "bit varying"
}
```

# bit varying with length

```sql
CREATE TABLE example (
  a bit varying(42)
);
```

```json
{
  "name" : "a",
  "dataType" : "bit varying",
  "length" : 42
}
```

