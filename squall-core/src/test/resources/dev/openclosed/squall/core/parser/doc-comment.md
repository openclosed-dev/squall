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
          "name" : "public",
          "state": "undefined",
          "tables": [
            {
              "name": "customer",
              "columns": [
                {
                  "name": "id",
                  "typeName": "integer",
                  "annotations": [
                    {
                      "type": "description",
                      "value": "The identifier of the customer."
                    },
                    {
                      "type": "label",
                      "value": "customer id"
                    }
                  ]
                }
              ],
              "annotations": [
                {
                  "type": "description",
                  "value": "The customers in this system."
                },
                {
                  "type": "label",
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
          "name" : "public",
          "state": "undefined",
          "sequences": [
            {
              "name": "customer_number",
              "typeName": "bigint",
              "start": 1,
              "increment": 1,
              "minValue": 1,
              "maxValue": 9223372036854775807,
              "annotations": [
                {
                  "type": "description",
                  "value": "Customer number generator."
                },
                {
                  "type": "label",
                  "value": "customer number"
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
