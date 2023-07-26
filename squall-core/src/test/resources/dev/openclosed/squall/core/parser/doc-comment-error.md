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
          "name" : "public",
          "state": "undefined",
          "tables": [
            {
              "name": "customer",
              "columns": [
                {
                  "name": "id",
                  "dataType": "integer",
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
                  "value": "The customers registered with the service."
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

```
ERROR: Unknown annotation "unknown". [line 3, column 4]
 * @unknown customer
   ^
```
