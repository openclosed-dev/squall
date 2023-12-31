# create a schema

```sql
CREATE SCHEMA myschema;
```

```json
{
  "databases" : [
    {
      "name": "",
      "state": "undefined",
      "schemas" : [
        {
          "name" : "myschema",
          "parents": [""],
          "state": "defined"
        }
      ]
    }
  ],
  "metadata": {
    "title": "Untitled"
  }
}
```

# create a schema if not exists

```sql
CREATE SCHEMA IF NOT EXISTS myschema;
```

```json
{
  "databases" : [
    {
      "name": "",
      "state": "undefined",
      "schemas": [
        {
          "name": "myschema",
          "parents": [""],
          "state": "defined"
        }
      ]
    }
  ],
  "metadata": {
    "title": "Untitled"
  }
}
```

# create a table in default schema

```sql
CREATE TABLE table1();
```

```json
{
  "databases": [
    {
      "name": "",
      "state": "undefined",
      "schemas": [
        {
          "name": "defaultschema",
          "parents": [""],
          "state": "undefined",
          "tables": [
            {
              "name": "table1",
              "parents": ["", "defaultschema"]
            }
          ]
        }
      ]
    }
  ],
  "metadata": {
    "title": "Untitled"
  }
}
```

# create a table in schema

```sql
CREATE SCHEMA schema1;
CREATE TABLE schema1.table1();
```

```json
{
  "databases": [
    {
      "name": "",
      "state": "undefined",
      "schemas": [
        {
          "name": "schema1",
          "parents": [""],
          "state": "defined",
          "tables": [
            {
              "name": "table1",
              "parents": ["", "schema1"]
            }
          ]
        }
      ]
    }
  ],
  "metadata": {
    "title": "Untitled"
  }
}
```
