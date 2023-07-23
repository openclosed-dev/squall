# unexpected end of input

```sql
CREATE TABLE a
```

```
ERROR: Unexpected end of input. [line 1, column 15]
```

# incomplete token

```sql
CREATE TABLE "hello
```

```
ERROR: Unexpected end of input. [line 1, column 20]
```

# string literal

```sql
hello;
```

```
ERROR: Syntax error at or near "hello". [line 1, column 1]
hello;
^
```

# quoted string literal

```sql
"hello";
```

```
ERROR: Syntax error at or near ""hello"". [line 1, column 1]
"hello";
^
```

# numeric literal

```sql
42;
```

```
ERROR: Syntax error at or near "42". [line 1, column 1]
42;
^
```

# invalid scientific notation

```sql
CREATE TABLE example (
  a numeric default 1.234ea
);
```

```
ERROR: Invalid character 'a'. [line 2, column 27]
  a numeric default 1.234ea
                          ^
```

# missing schema object

```sql
CREATE;
```
```
ERROR: Syntax error at or near ";". [line 1, column 7]
CREATE;
      ^
```

# missing table name

```sql
CREATE TABLE;
```

```
ERROR: Syntax error at or near ";". [line 1, column 13]
CREATE TABLE;
            ^
```

# no table definition

```sql
CREATE TABLE foo;
```

```
ERROR: Syntax error at or near ";". [line 1, column 17]
CREATE TABLE foo;
                ^
```

# missing column name


```sql
CREATE TABLE foo(,);
```

```
ERROR: Syntax error at or near ",". [line 1, column 18]
CREATE TABLE foo(,);
                 ^
```

# invalid data type

```sql
CREATE TABLE table1(
    a enum
);
```

```
ERROR: Syntax error at or near "enum". [line 2, column 7]
    a enum
      ^
```

# missing table constraint

```sql
CREATE TABLE example (
  a integer,
  CONSTRAINT unique_identifier ALL
);
```

```
ERROR: Syntax error at or near "ALL". [line 3, column 32]
  CONSTRAINT unique_identifier ALL
                               ^
```

# create table if exists

```sql
CREATE TABLE IF EXISTS foo;
```

```
ERROR: Syntax error at or near "EXISTS". [line 1, column 17]
CREATE TABLE IF EXISTS foo;
                ^
```

# missing exists

```sql
CREATE TABLE IF NOT foo;
```

```
ERROR: Syntax error at or near "foo". [line 1, column 21]
CREATE TABLE IF NOT foo;
                    ^
```

# multiple errors


```sql
CREATE TABLE foo(,);
CREATE TABLE bar(,);
```

```
ERROR: Syntax error at or near ",". [line 1, column 18]
CREATE TABLE foo(,);
                 ^
ERROR: Syntax error at or near ",". [line 2, column 18]
CREATE TABLE bar(,);
                 ^
```
