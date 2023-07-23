# empty object

```json
{}
```

# full configuration

```json
{
    "title": "untitled",
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
            "format": "html"
        }
    }
}
```

# redundant properties


```json
{
    "title": "untitled",
    "greeting": "hello"
}
```

```
WARNING: Unknown property "greeting" was found. [/greeting]
```
