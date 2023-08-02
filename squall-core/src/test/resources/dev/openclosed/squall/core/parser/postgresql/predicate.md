# isnull

```sql
1.5 ISNULL
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

# notnull

```sql
'null' NOTNULL
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

# null is true

```sql
NULL::boolean IS TRUE
```

```json
{
  "type" : "is",
  "subject" : {
    "type": "typecast",
    "typeName": "boolean",
    "source": {
      "type": "null"
    }
  },
  "predicate": "is_true"
}
```

```text
CAST(null AS boolean) IS TRUE
```

# null is not true

```sql
NULL::boolean IS NOT TRUE
```

```json
{
  "type" : "is",
  "subject" : {
    "type": "typecast",
    "typeName": "boolean",
    "source": {
      "type": "null"
    }
  },
  "predicate": "is_not_true"
}
```

```text
CAST(null AS boolean) IS NOT TRUE
```

# null is false

```sql
NULL::boolean IS FALSE
```

```json
{
  "type" : "is",
  "subject" : {
    "type": "typecast",
    "typeName": "boolean",
    "source": {
      "type": "null"
    }
  },
  "predicate": "is_false"
}
```

```text
CAST(null AS boolean) IS FALSE
```

# null is not false

```sql
NULL::boolean IS NOT FALSE
```

```json
{
  "type" : "is",
  "subject" : {
    "type": "typecast",
    "typeName": "boolean",
    "source": {
      "type": "null"
    }
  },
  "predicate": "is_not_false"
}
```

```text
CAST(null AS boolean) IS NOT FALSE
```

# null is unknown

```sql
NULL::boolean IS UNKNOWN
```

```json
{
  "type" : "is",
  "subject" : {
    "type": "typecast",
    "typeName": "boolean",
    "source": {
      "type": "null"
    }
  },
  "predicate": "is_unknown"
}
```

```text
CAST(null AS boolean) IS UNKNOWN
```

# null is not unknown

```sql
NULL::boolean IS NOT UNKNOWN
```

```json
{
  "type" : "is",
  "subject" : {
    "type": "typecast",
    "typeName": "boolean",
    "source": {
      "type": "null"
    }
  },
  "predicate": "is_not_unknown"
}
```

```text
CAST(null AS boolean) IS NOT UNKNOWN
```
