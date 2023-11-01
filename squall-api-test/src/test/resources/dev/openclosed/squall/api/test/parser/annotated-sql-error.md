# unknown annotation

```sql
/**
 * The customers registered with the service.
 * @unknown customer
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
                  "value": "The customers registered with the service."
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
  ]
}
```

```
ERROR: Unknown annotation "unknown". [line 3, column 4]
 * @unknown customer
   ^
```

# missing value for label

```sql
/**
 * The customers registered with the service.
 * @label
 * @since v1.2
 */
CREATE TABLE customer(
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
                  "typeName": "integer"
                }
              ],
              "annotations": [
                {
                  "name": "description",
                  "value": "The customers registered with the service."
                },
                {
                  "name": "since",
                  "value": "v1.2"
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

```
ERROR: Invalid value for the annotation "label". [line 3, column 10]
 * @label
         ^
```
