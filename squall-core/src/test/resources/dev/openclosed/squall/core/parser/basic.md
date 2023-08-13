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
          "parents": [""],
          "state": "undefined",
          "tables": [
            {
              "name": "example",
              "parents": ["", ""],
              "columns": [
                {
                  "name": "a",
                  "parents": ["", "", "example"],
                  "typeName": "integer"
                },
                {
                  "name": "b",
                  "parents": ["", "", "example"],
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
          "parents": [""],
          "state": "undefined",
          "tables": [
            {
              "name": "example",
              "parents": ["", ""],
              "columns": [
                {
                  "name": "a",
                  "parents": ["", "", "example"],
                  "typeName": "integer"
                },
                {
                  "name": "b",
                  "parents": ["", "", "example"],
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
          "parents": [""],
          "state": "undefined",
          "tables": [
            {
              "name": "table1",
              "parents": ["", ""],
              "columns": [
                {
                  "name": "a",
                  "parents": ["", "", "table1"],
                  "typeName": "integer"
                }
              ]
            },
            {
              "name": "table2",
              "parents": ["", ""],
              "columns": [
                {
                  "name": "b",
                  "parents": ["", "", "table2"],
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
