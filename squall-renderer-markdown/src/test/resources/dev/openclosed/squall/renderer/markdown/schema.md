# schema without comments

```json
{
  "numbering": true
}
```

```sql
CREATE DATABASE database1;

CREATE SCHEMA schema1;
```

# Untitled
## 1. database1 ![database]

### 1.1. schema1 ![schema]

---
# schema with comments

```json
{
  "numbering": true
}
```

```sql
/**
 * A description of the database1.
 * @label Database 1
 */
CREATE DATABASE database1;

/**
 * A description of the schema1.
 * @label Schema 1
 */
CREATE SCHEMA schema1;
```

# Untitled
## 1. Database 1 `database1` ![database]
A description of the database1.
### 1.1. Schema 1 `schema1` ![schema]
A description of the schema1.
