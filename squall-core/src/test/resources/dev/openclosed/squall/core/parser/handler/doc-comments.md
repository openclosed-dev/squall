# one line

```sql
/**Hello World */
```

```json
[
    {
        "type": "description",
        "value": "Hello World"
    }
]
```

# two lines

```sql
/**First line
   Second line */
```

```json
[
    {
        "type": "description",
        "value": "First line\nSecond line"
    }
]
```

# javadoc style

```sql
/**
 * This is a summary.
 * Second line
 * Third line
 */
```

```json
[
    {
        "type": "description",
        "value": "This is a summary.\nSecond line\nThird line"
    }
]
```

# markdown

```sql
/**
 * This _table_ has **three** columns:
 * 1. x
 * 2. y
 * 3. z
 */
```

```json
[
    {
        "type": "description",
        "value": "This _table_ has **three** columns:\n1. x\n2. y\n3. z"
    }
]
```

# paragraphs

```sql
/**
 * 1st paragraph
 *
 * 2nd paragraph
 */
```

```json
[
    {
        "type": "description",
        "value": "1st paragraph\n\n2nd paragraph"
    }
]
```

# list

```sql
/**
 * 1. 1st item
 * 2. 2nd item
 *   1. 1st child item
 *   2. 2nd child item
 * 3. 3rd item
 */
```

```json
[
    {
        "type": "description",
        "value": "1. 1st item\n2. 2nd item\n  1. 1st child item\n  2. 2nd child item\n3. 3rd item"
    }
]
```

# explicit description

```sql
/**
 * @description This is a description.
 */
```

```json
[
    {
        "type": "description",
        "value": "This is a description."
    }
]
```

# explicit description with multiple lines

```sql
/**
 * @description This is 1st line.
 * This is 2nd line.
 * This is 3rd line.
 */
```

```json
[
    {
        "type": "description",
        "value": "This is 1st line.\nThis is 2nd line.\nThis is 3rd line."
    }
]
```

# label

```sql
/**
 * @label name for display
 */
```

```json
[
    {
        "type": "label",
        "value": "name for display"
    }
]
```

# since

```sql
/**
 * @since v5.0
 */
```

```json
[
    {
        "type": "since",
        "value": "v5.0"
    }
]
```

# deprecated

```sql
/**
 * @deprecated Do not use this column
 */
```

```json
[
    {
        "type": "deprecated",
        "value": "Do not use this column"
    }
]
```

# deprecated without value

```sql
/**
 * @deprecated
 */
```

```json
[
    {
        "type": "deprecated",
        "value": ""
    }
]
```

# multiple annotations

```sql
/**
 * Order database.
 * @label order database
 */
```

```json
[
    {
        "type": "description",
        "value": "Order database."
    },
    {
        "type": "label",
        "value": "order database"
    }
]
```

# annotation without value

```sql
/**
 * @label
 */
```

```json
[
    {
        "type": "label",
        "value": ""
    }
]
```

# empty comment

```sql
/***/
```

```json
[]
```

# multiple lines without text

```sql
/**
 */
```

```json
[]
```

# comment block indented

```sql
  /**
   * The name of this customer.
   * @label customer name
   */
```

```json
[
    {
        "type": "description",
        "value": "The name of this customer."
    },
    {
        "type": "label",
        "value": "customer name"
    }
]
```

# all annotations

```sql
  /**
   * The name of the customer.
   * @label customer name
   * @since v5
   * @deprecated do not use
   */
```

```json
[
    {
        "type": "description",
        "value": "The name of the customer."
    },
    {
        "type": "label",
        "value": "customer name"
    },
    {
      "type": "since",
      "value": "v5"
    },
    {
      "type": "deprecated",
      "value": "do not use"
    }
]
```
