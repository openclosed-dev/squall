# empty object

```json
{}
```

# full configuration

```json
{
  "metadata": {
    "title": "untitled"
  },
  "sources": [
    "a.sql",
    "b.sql"
  ],
  "outDir": "output",
  "parser": {
    "dialect": "postgresql",
    "defaultSchema": "public"
  },
  "renderers": {
    "default": {
      "format": "html",
      "show": ["database", "schema", "table", "column", "sequence"]
    }
  }
}
```

# redundant properties

```json
{
  "metadata": {
    "title": "untitled"
  },
  "greeting": "hello"
}
```

```
WARNING: Unknown property "greeting" was found. [/greeting]
```

# duplicates in set

```json
{
  "renderers": {
    "default": {
      "show": ["database", "schema", "table", "column", "table", "sequence"]
    }
  }
}
```
