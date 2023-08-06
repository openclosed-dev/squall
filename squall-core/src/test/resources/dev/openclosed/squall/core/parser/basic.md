# empty SQL

```sql
```

```json
{
}
```

# line comment

```sql
CREATE TABLE example(
    a integer, -- This is a comment
    b integer
);
```

```json
{
  "databases" : [
    {
      "name": "",
      "state": "undefined",
      "schemas" : [
        {
          "name" : "",
          "state": "undefined",
          "tables": [
            {
              "name": "example",
              "qualifiedName": "example",
              "columns": [
                {
                  "name": "a",
                  "typeName": "integer"
                },
                {
                  "name": "b",
                  "typeName": "integer"
                }
              ]
            }
          ]
        }
      ]
    }
  ]
}
```

# block comment

```sql
CREATE TABLE example(
    a integer,
    /* first line
     * second line
     */
    b integer
);
```

```json
{
  "databases" : [
    {
      "name": "",
      "state": "undefined",
      "schemas" : [
        {
          "name" : "",
          "state": "undefined",
          "tables": [
            {
              "name": "example",
              "qualifiedName": "example",
              "columns": [
                {
                  "name": "a",
                  "typeName": "integer"
                },
                {
                  "name": "b",
                  "typeName": "integer"
                }
              ]
            }
          ]
        }
      ]
    }
  ]
}
```

# multiple SQL files

```sql
CREATE TABLE table1(
    a integer
);
```

```sql
CREATE TABLE table2(
    b integer
);
```

```json
{
  "databases" : [
    {
      "name": "",
      "state": "undefined",
      "schemas" : [
        {
          "name" : "",
          "state": "undefined",
          "tables": [
            {
              "name": "table1",
              "qualifiedName": "table1",
              "columns": [
                {
                  "name": "a",
                  "typeName": "integer"
                }
              ]
            },
            {
              "name": "table2",
              "qualifiedName": "table2",
              "columns": [
                {
                  "name": "b",
                  "typeName": "integer"
                }
              ]
            }
          ]
        }
      ]
    }
  ]
}
```
