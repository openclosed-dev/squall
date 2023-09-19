# invalid type integer

```json
{
  "metadata": {
    "title": 42
  }
}
```

```
ERROR: Unexpected value type. Expected type is string, but actual type was integer. [/metadata/title]
```

# invalid type decimal

```json
{
  "metadata": {
    "title": 3.14
  }
}
```

```
ERROR: Unexpected value type. Expected type is string, but actual type was number. [/metadata/title]
```

# invalid type boolean

```json
{
  "metadata": {
    "title": false
  }
}
```

```
ERROR: Unexpected value type. Expected type is string, but actual type was boolean. [/metadata/title]
```

# invalid type array

```json
{
  "renderers": []
}
```

```
ERROR: Unexpected value type. Expected type is object, but actual type was array. [/renderers]
```

# illegal value for order

```json
{
  "renderers": {
    "default": {
      "order": "length"
    }
  }
}

```

```
ERROR: Illegal value. Allowed values are ["name", "definition"], but actual value was "length". [/renderers/default/order]
```

# illegal value for show

```json
{
  "renderers": {
    "default": {
      "show": ["database", "schema", "row"]
    }
  }
}

```

```
ERROR: Illegal value. Allowed values are ["database", "schema", "table", "column", "sequence"], but actual value was "row". [/renderers/default/show/2]
```

# illegal value for column attributes

```json
{
  "renderers": {
    "default": {
      "columnAttributes": ["no"]
    }
  }
}

```

```
ERROR: Illegal value. Allowed values are ["ordinal", "name", "label", "type", "type_name", "precision_length", "scale", "nullable", "required", "unique", "default_value", "foreign_key", "description"], but actual value was "no". [/renderers/default/columnAttributes/0]
```

# multiple problems

```json
{
  "metadata": {
    "title": 42
  },
  "sources": false,
  "renderers": {
    "a": {
      "format": 42
    },
    "b": {
      "format": false
    }
  }
}
```

```
ERROR: Unexpected value type. Expected type is string, but actual type was integer. [/metadata/title]
ERROR: Unexpected value type. Expected type is array, but actual type was boolean. [/sources]
ERROR: Unexpected value type. Expected type is string, but actual type was integer. [/renderers/a/format]
ERROR: Unexpected value type. Expected type is string, but actual type was boolean. [/renderers/b/format]
```
