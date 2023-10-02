# nextval of unqualified sequence

```sql
nextval('serial')
```

```json
{
  "type": "sequence_function",
  "name": "nextval",
  "arguments": [
    {
      "type": "string",
      "value": "serial"
    }
  ],
  "sequenceName": ["", "defaultschema", "serial"]
}
```

```text
nextval('serial')
```

# currval of unqualified sequence

```sql
currval('serial')
```

```json
{
  "type": "sequence_function",
  "name": "currval",
  "arguments": [
    {
      "type": "string",
      "value": "serial"
    }
  ],
  "sequenceName": ["", "defaultschema", "serial"]
}
```

```text
currval('serial')
```

# nextval of qualified sequence

```sql
nextval('public.serial')
```

```json
{
  "type": "sequence_function",
  "name": "nextval",
  "arguments": [
    {
      "type": "string",
      "value": "public.serial"
    }
  ],
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
  "name": "currval",
  "arguments": [
    {
      "type": "string",
      "value": "public.serial"
    }
  ],
  "sequenceName": ["", "public", "serial"]
}
```

```text
currval('public.serial')
```
