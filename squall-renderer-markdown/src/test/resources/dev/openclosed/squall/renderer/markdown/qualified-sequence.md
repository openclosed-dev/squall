# schema qualified sequence

```sql
CREATE DATABASE db1;

CREATE SCHEMA schema1;

/**
 * A description of customer_id.
 * @label Customer Identifier
 */
CREATE SEQUENCE schema1.customer_id;
```

# Untitled

## 1. schema1 ![schema]

### 1.1. Customer Identifier `schema1.customer_id` ![sequence]

A description of customer_id.

| Type | Start | Increment | Minimum | Maximum |
| :-- | --: | --: | --: | --: |
| bigint | 1 | 1 | 1 | 9223372036854775807 |
