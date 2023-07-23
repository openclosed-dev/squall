# empty document

```json
```

```
ERROR: Unexpected end of input.
```

# blank document

```json

```

```
ERROR: Unexpected end of input.
```

# boolean at root

```json
false
```

```
ERROR: Input JSON is ill-formed. [line 1, column 1]
false
^
```

# array at root

```json
[]
```

```
ERROR: Input JSON is ill-formed. [line 1, column 1]
[]
^
```

# incomplete

```json
{
```

```
ERROR: Unexpected end of input.
```

# incomplete with blank line

```json
{

```

```
ERROR: Unexpected end of input.
```

# ill-formed

```json
{
    "title": "db spec",
    "parser": {
        "dialect": "postgresql",
        defaultSchema: "public"
    }
}
```

```
ERROR: Input JSON is ill-formed. [line 5, column 9]
        defaultSchema: "public"
        ^
```
