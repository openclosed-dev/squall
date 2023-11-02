# documented table

```sql
/**
 * The customers in this system.
 * @label customer
 */
CREATE TABLE customer(
  /**
   * The identifier of the customer.
   * @label customer id
   */
  id integer
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
              "name": "customer",
              "parents": ["", ""],
              "columns": [
                {
                  "name": "id",
                  "parents": ["", "", "customer"],
                  "typeName": "integer",
                  "annotations": [
                    {
                      "name": "description",
                      "value": "The identifier of the customer."
                    },
                    {
                      "name": "label",
                      "value": "customer id"
                    }
                  ]
                }
              ],
              "annotations": [
                {
                  "name": "description",
                  "value": "The customers in this system."
                },
                {
                  "name": "label",
                  "value": "customer"
                }
              ]
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

# documented sequence

```sql
/**
 * Customer number generator.
 * @label customer number
 */
CREATE SEQUENCE customer_number;
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
          "sequences": [
            {
              "name": "customer_number",
              "parents": ["", ""],
              "typeName": "bigint",
              "start": 1,
              "increment": 1,
              "minValue": 1,
              "maxValue": 9223372036854775807,
              "annotations": [
                {
                  "name": "description",
                  "value": "Customer number generator."
                },
                {
                  "name": "label",
                  "value": "customer number"
                }
              ]
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

# ignore comments for previous statement

```sql
/** table t1. */
DROP TABLE t1;

/** table t2. */
CREATE TABLE t2();
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
              "name": "t2",
              "parents": ["", ""],
              "annotations": [
                {
                  "name": "description",
                  "value": "table t2."
                }
              ]
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

# ignore comments but preceding one

```sql
/** table t1. */
DROP TABLE t1;

CREATE TABLE t2();
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
              "name": "t2",
              "parents": ["", ""]
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
