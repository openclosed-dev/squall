# is null

```sql
1.5 IS NULL
```

```json
{
  "type" : "is",
  "subject" : {
    "type": "number",
    "value": "1.5"
  },
  "predicate": "is_null"
}
```

```text
1.5 IS NULL
```

# is not null

```sql
'null' IS NOT NULL
```

```json
{
  "type" : "is",
  "subject" : {
    "type": "string",
    "value": "null"
  },
  "predicate": "is_not_null"
}
```

```text
'null' IS NOT NULL
```

# boolean is true

```sql
true IS TRUE
```

```json
{
  "type" : "is",
  "subject" : {
    "type": "boolean",
    "value": true
  },
  "predicate": "is_true"
}
```

```text
true IS TRUE
```

# boolean is not true

```sql
true IS NOT TRUE
```

```json
{
  "type" : "is",
  "subject" : {
    "type": "boolean",
    "value": true
  },
  "predicate": "is_not_true"
}
```

```text
true IS NOT TRUE
```

# boolean is false

```sql
true IS FALSE
```

```json
{
  "type" : "is",
  "subject" : {
    "type": "boolean",
    "value": true
  },
  "predicate": "is_false"
}
```

```text
true IS FALSE
```

# boolean is not false

```sql
true IS NOT FALSE
```

```json
{
  "type" : "is",
  "subject" : {
    "type": "boolean",
    "value": true
  },
  "predicate": "is_not_false"
}
```

```text
true IS NOT FALSE
```

# boolean is unknown

```sql
true IS UNKNOWN
```

```json
{
  "type" : "is",
  "subject" : {
    "type": "boolean",
    "value": true
  },
  "predicate": "is_unknown"
}
```

```text
true IS UNKNOWN
```

# boolean is not unknown

```sql
true IS NOT UNKNOWN
```

```json
{
  "type" : "is",
  "subject" : {
    "type": "boolean",
    "value": true
  },
  "predicate": "is_not_unknown"
}
```

```text
true IS NOT UNKNOWN
```

# multiple predicates

```sql
foo IS NOT NULL OR bar IS TRUE
```

```json
{
  "type": "binary_operator",
  "operator": "OR",
  "left": {
    "type": "is",
    "subject": {
      "type": "column_reference",
      "name": "foo"
    },
    "predicate": "is_not_null"
  },
  "right": {
    "type": "is",
    "subject": {
      "type": "column_reference",
      "name": "bar"
    },
    "predicate": "is_true"
  }
}
```

```text
(foo IS NOT NULL) OR (bar IS TRUE)
```
