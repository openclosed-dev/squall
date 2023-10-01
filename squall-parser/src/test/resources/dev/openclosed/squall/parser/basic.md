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
          "name" : "defaultschema",
          "parents": [""],
          "state": "undefined",
          "tables": [
            {
              "name": "example",
              "parents": ["", "defaultschema"],
              "columns": [
                {
                  "name": "a",
                  "parents": ["", "defaultschema", "example"],
                  "typeName": "integer"
                },
                {
                  "name": "b",
                  "parents": ["", "defaultschema", "example"],
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
          "name" : "defaultschema",
          "parents": [""],
          "state": "undefined",
          "tables": [
            {
              "name": "example",
              "parents": ["", "defaultschema"],
              "columns": [
                {
                  "name": "a",
                  "parents": ["", "defaultschema", "example"],
                  "typeName": "integer"
                },
                {
                  "name": "b",
                  "parents": ["", "defaultschema", "example"],
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
          "name" : "defaultschema",
          "parents": [""],
          "state": "undefined",
          "tables": [
            {
              "name": "table1",
              "parents": ["", "defaultschema"],
              "columns": [
                {
                  "name": "a",
                  "parents": ["", "defaultschema", "table1"],
                  "typeName": "integer"
                }
              ]
            },
            {
              "name": "table2",
              "parents": ["", "defaultschema"],
              "columns": [
                {
                  "name": "b",
                  "parents": ["", "defaultschema", "table2"],
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
