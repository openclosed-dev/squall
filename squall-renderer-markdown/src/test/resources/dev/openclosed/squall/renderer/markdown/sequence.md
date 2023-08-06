# simple sequence

```sql
CREATE DATABASE customerdb;

CREATE SCHEMA customer;

/**
 * A description of customer_id.
 * @label Customer Identifier
 */
CREATE SEQUENCE customer.customer_id;
```

# Untitled
## 1. customerdb ![database]

### 1.1. customer ![schema]

#### 1.1.1. Customer Identifier `customer.customer_id` ![sequence]
A description of customer_id.

| Data Type | Start | Increment | Min Value | Max Value |
| :-- | --: | --: | --: | --: |
| bigint | 1 | 1 | 1 | 9223372036854775807 |
