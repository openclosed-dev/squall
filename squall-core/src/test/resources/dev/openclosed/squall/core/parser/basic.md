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
          "name" : "public",
          "state": "undefined",
          "tables": [
            {
              "name": "example",
              "columns": [
                {
                  "name": "a",
                  "dataType": "integer"
                },
                {
                  "name": "b",
                  "dataType": "integer"
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
          "name" : "public",
          "state": "undefined",
          "tables": [
            {
              "name": "example",
              "columns": [
                {
                  "name": "a",
                  "dataType": "integer"
                },
                {
                  "name": "b",
                  "dataType": "integer"
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
          "name" : "public",
          "state": "undefined",
          "tables": [
            {
              "name": "table1",
              "columns": [
                {
                  "name": "a",
                  "dataType": "integer"
                }
              ]
            },
            {
              "name": "table2",
              "columns": [
                {
                  "name": "b",
                  "dataType": "integer"
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
