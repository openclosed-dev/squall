# nextval of unqualified sequence

```sql
nextval('serial')
```

```json
{
  "type": "sequence_function",
  "functionName": "nextval",
  "sequenceName": ["", "defaultschema", "serial"]
}
```

```text
nextval('defaultschema.serial')
```

# currval of unqualified sequence

```sql
currval('serial')
```

```json
{
  "type": "sequence_function",
  "functionName": "currval",
  "sequenceName": ["", "defaultschema", "serial"]
}
```

```text
currval('defaultschema.serial')
```

# nextval of qualified sequence

```sql
nextval('public.serial')
```

```json
{
  "type": "sequence_function",
  "functionName": "nextval",
  "sequenceName": ["", "public", "serial"]
}
```

```text
nextval('public.serial')
```

# currval of qualified sequence

```sql
currval('public.serial')
```

```json
{
  "type": "sequence_function",
  "functionName": "currval",
  "sequenceName": ["", "public", "serial"]
}
```

```text
currval('public.serial')
```
