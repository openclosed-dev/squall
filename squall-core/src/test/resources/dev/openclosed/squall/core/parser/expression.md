# string literal

```sql
'Hello World'
```

```json
{
  "type" : "string",
  "value" : "Hello World"
}
```

```text
'Hello World'
```

# empty string literal

```sql
''
```

```json
{
  "type" : "string",
  "value" : ""
}
```

```text
''
```

# numeric literal

```sql
42
```

```json
{
  "type" : "number",
  "value" : "42"
}
```

```text
42
```

# boolean true literal

```sql
true
```

```json
{
  "type" : "boolean",
  "value" : "true"
}
```

```text
true
```

# boolean false literal

```sql
false
```

```json
{
  "type" : "boolean",
  "value" : "false"
}
```

```text
false
```

# number with plus sign

```sql
+1.23
```

```json
{
  "type" : "number",
  "value" : "1.23"
}
```

```text
1.23
```

# number with minus sign

```sql
-1.23
```

```json
{
  "type" : "number",
  "value" : "-1.23"
}
```

```text
-1.23
```

# addition

```sql
1 + 2
```

```json
{
  "type" : "binary_operator",
  "operator" : "+",
  "left": {
    "type": "number",
    "value": "1"
  },
  "right": {
    "type": "number",
    "value": "2"
  }
}
```

```text
(1 + 2)
```

# subtraction

```sql
1 - 2
```

```json
{
  "type" : "binary_operator",
  "operator" : "-",
  "left": {
    "type": "number",
    "value": "1"
  },
  "right": {
    "type": "number",
    "value": "2"
  }
}
```

```text
(1 - 2)
```

# multiplication

```sql
1 * 2
```

```json
{
  "type" : "binary_operator",
  "operator" : "*",
  "left": {
    "type": "number",
    "value": "1"
  },
  "right": {
    "type": "number",
    "value": "2"
  }
}
```

```text
(1 * 2)
```

# division

```sql
1 / 2
```

```json
{
  "type" : "binary_operator",
  "operator" : "/",
  "left": {
    "type": "number",
    "value": "1"
  },
  "right": {
    "type": "number",
    "value": "2"
  }
}
```

```text
(1 / 2)
```

# modulo

```sql
5 % 2
```

```json
{
  "type" : "binary_operator",
  "operator" : "%",
  "left": {
    "type": "number",
    "value": "5"
  },
  "right": {
    "type": "number",
    "value": "2"
  }
}
```

```text
(5 % 2)
```

# exponentiation

```sql
2 ^ 3
```

```json
{
  "type" : "binary_operator",
  "operator" : "^",
  "left": {
    "type": "number",
    "value": "2"
  },
  "right": {
    "type": "number",
    "value": "3"
  }
}
```

```text
(2 ^ 3)
```

# multiple additions

```sql
1 + 2 + 3
```

```json
{
  "type" : "binary_operator",
  "operator" : "+",
  "left": {
    "type": "binary_operator",
    "operator": "+",
    "left": {
      "type": "number",
      "value": "1"
    },
    "right": {
      "type": "number",
      "value": "2"
    }
  },
  "right": {
    "type": "number",
    "value": "3"
  }
}
```

```text
((1 + 2) + 3)
```

# multiple exponentiation

```sql
1 ^ 2 ^ 3
```

```json
{
  "type" : "binary_operator",
  "operator" : "^",
  "left": {
    "type": "binary_operator",
    "operator": "^",
    "left": {
      "type": "number",
      "value": "1"
    },
    "right": {
      "type": "number",
      "value": "2"
    }
  },
  "right": {
    "type": "number",
    "value": "3"
  }
}
```

```text
((1 ^ 2) ^ 3)
```

# addition and subtraction

```sql
1 + 2 - 3
```

```json
{
  "type" : "binary_operator",
  "operator" : "-",
  "left": {
    "type": "binary_operator",
    "operator": "+",
    "left": {
      "type": "number",
      "value": "1"
    },
    "right": {
      "type": "number",
      "value": "2"
    }
  },
  "right": {
    "type": "number",
    "value": "3"
  }
}
```

```text
((1 + 2) - 3)
```
# addition and multiplication

```sql
1 + 2 * 3
```

```json
{
  "type" : "binary_operator",
  "operator" : "+",
  "left": {
    "type": "number",
    "value": "1"
  },
  "right": {
    "type": "binary_operator",
    "operator": "*",
    "left": {
      "type": "number",
      "value": "2"
    },
    "right": {
      "type": "number",
      "value": "3"
    }
  }
}
```

```text
(1 + (2 * 3))
```

# group to change precedence

```sql
(1 + 2) * 3
```

```json
{
  "type" : "binary_operator",
  "operator" : "*",
  "left": {
    "type": "binary_operator",
    "operator": "+",
    "left": {
      "type": "number",
      "value": "1"
    },
    "right": {
      "type": "number",
      "value": "2"
    }
  },
  "right": {
    "type": "number",
    "value": "3"
  }
}
```

```text
((1 + 2) * 3)
```

# unary plus

```sql
+(1 + 2)
```

```json
{
  "type" : "unary_operator",
  "operator" : "+",
  "operand": {
    "type": "binary_operator",
    "operator": "+",
    "left": {
      "type": "number",
      "value": "1"
    },
    "right": {
      "type": "number",
      "value": "2"
    }
  }
}
```

```text
(+ (1 + 2))
```

# unary minus

```sql
-(1 + 2)
```

```json
{
  "type" : "unary_operator",
  "operator" : "-",
  "operand": {
    "type": "binary_operator",
    "operator": "+",
    "left": {
      "type": "number",
      "value": "1"
    },
    "right": {
      "type": "number",
      "value": "2"
    }
  }
}
```

```text
(- (1 + 2))
```

# current_time

```sql
CURRENT_TIME
```

```json
{
  "type" : "function",
  "name" : "CURRENT_TIME"
}
```

```text
CURRENT_TIME
```

# current_date

```sql
CURRENT_DATE
```

```json
{
  "type" : "function",
  "name" : "CURRENT_DATE"
}
```

```text
CURRENT_DATE
```

# current_timestamp

```sql
CURRENT_TIMESTAMP
```

```json
{
  "type" : "function",
  "name" : "CURRENT_TIMESTAMP"
}
```

```text
CURRENT_TIMESTAMP
```

# current_timestamp with precision

```sql
CURRENT_TIMESTAMP(2)
```

```json
{
  "type" : "function",
  "name" : "CURRENT_TIMESTAMP",
  "arguments": [
    {
      "type": "number",
      "value": "2"
    }
  ]
}
```

```text
CURRENT_TIMESTAMP(2)
```

# localtimestamp

```sql
LOCALTIMESTAMP
```

```json
{
  "type" : "function",
  "name" : "LOCALTIMESTAMP"
}
```

```text
LOCALTIMESTAMP
```

# equal comparison operator

```sql
1 = 2
```

```json
{
  "type" : "binary_operator",
  "operator" : "=",
  "left": {
    "type": "number",
    "value": "1"
  },
  "right": {
    "type": "number",
    "value": "2"
  }
}
```

```text
(1 = 2)
```

# less-than comparison operator

```sql
1 < 2
```

```json
{
  "type" : "binary_operator",
  "operator" : "<",
  "left": {
    "type": "number",
    "value": "1"
  },
  "right": {
    "type": "number",
    "value": "2"
  }
}
```

```text
(1 < 2)
```

# greater-than comparison operator

```sql
1 > 2
```

```json
{
  "type" : "binary_operator",
  "operator" : ">",
  "left": {
    "type": "number",
    "value": "1"
  },
  "right": {
    "type": "number",
    "value": "2"
  }
}
```

```text
(1 > 2)
```

# not-equal comparison operator

```sql
1 <> 2
```

```json
{
  "type" : "binary_operator",
  "operator" : "<>",
  "left": {
    "type": "number",
    "value": "1"
  },
  "right": {
    "type": "number",
    "value": "2"
  }
}
```

```text
(1 <> 2)
```

# not-equal alias comparison operator

```sql
1 != 2
```

```json
{
  "type" : "binary_operator",
  "operator" : "<>",
  "left": {
    "type": "number",
    "value": "1"
  },
  "right": {
    "type": "number",
    "value": "2"
  }
}
```

```text
(1 <> 2)
```

# less-than-or-equal-to comparison operator

```sql
1 <= 2
```

```json
{
  "type" : "binary_operator",
  "operator" : "<=",
  "left": {
    "type": "number",
    "value": "1"
  },
  "right": {
    "type": "number",
    "value": "2"
  }
}
```

```text
(1 <= 2)
```

# greater-than-or-equal-to comparison operator

```sql
1 >= 2
```

```json
{
  "type" : "binary_operator",
  "operator" : ">=",
  "left": {
    "type": "number",
    "value": "1"
  },
  "right": {
    "type": "number",
    "value": "2"
  }
}
```

```text
(1 >= 2)
```

# AND logical operator

```sql
true AND false
```

```json
{
  "type" : "binary_operator",
  "operator" : "AND",
  "left": {
    "type": "boolean",
    "value": "true"
  },
  "right": {
    "type": "boolean",
    "value": "false"
  }
}
```

```text
(true AND false)
```

# OR logical operator

```sql
true OR false
```

```json
{
  "type" : "binary_operator",
  "operator" : "OR",
  "left": {
    "type": "boolean",
    "value": "true"
  },
  "right": {
    "type": "boolean",
    "value": "false"
  }
}
```

```text
(true OR false)
```

# NOT logical operator

```sql
NOT true
```

```json
{
  "type" : "unary_operator",
  "operator" : "NOT",
  "operand": {
    "type": "boolean",
    "value": "true"
  }
}
```

```text
(NOT true)
```

# column reference

```sql
a + b
```

```json
{
  "type" : "binary_operator",
  "operator" : "+",
  "left": {
    "type": "column_reference",
    "name": "a"
  },
  "right": {
    "type": "column_reference",
    "name": "b"
  }
}
```

```text
(a + b)
```
# typecasting

```sql
'1.23'::real
```

```json
{
  "type" : "typecast",
  "source": {
    "type": "string",
    "value": "1.23"
  },
  "typeName": "real"
}
```

```text
CAST('1.23' AS real)
```

# typecast followed by binary operator

```sql
'1'::numeric + 2
```

```json
{
  "type": "binary_operator",
  "operator": "+",
  "left": {
    "type" : "typecast",
    "source": {
      "type": "string",
      "value": "1"
    },
    "typeName": "numeric"
  },
  "right": {
    "type": "number",
    "value": "2"
  }
}
```

```text
(CAST('1' AS numeric) + 2)
```

# in array comparison

```sql
'a' IN ('foo', 'bar', 'baz')
```

```json
{
  "type" : "in",
  "left" : {
    "type": "string",
    "value": "a"
  },
  "right": [
    {
      "type": "string",
      "value": "foo"
    },
    {
      "type": "string",
      "value": "bar"
    },
    {
      "type": "string",
      "value": "baz"
    }
  ]
}
```

```text
'a' IN ('foo', 'bar', 'baz')
```

# not in array comparison

```sql
7 NOT IN (2, 3, 5)
```

```json
{
  "type" : "not_in",
  "left" : {
    "type": "number",
    "value": "7"
  },
  "right": [
    {
      "type": "number",
      "value": "2"
    },
    {
      "type": "number",
      "value": "3"
    },
    {
      "type": "number",
      "value": "5"
    }
  ]
}
```

```text
7 NOT IN (2, 3, 5)
```

# case expression

```sql
CASE WHEN a=1 THEN 'one'
     WHEN a=2 THEN 'two'
     ELSE 'other'
END
```

```json
{
  "type": "case",
  "when": [
    {
      "condition": {
        "type": "binary_operator",
        "operator": "=",
        "left": {
          "type": "column_reference",
          "name": "a"
        },
        "right": {
          "type": "number",
          "value": "1"
        }
      },
      "result": {
        "type": "string",
        "value": "one"
      }
    },
    {
      "condition": {
        "type": "binary_operator",
        "operator": "=",
        "left": {
          "type": "column_reference",
          "name": "a"
        },
        "right": {
          "type": "number",
          "value": "2"
        }
      },
      "result": {
        "type": "string",
        "value": "two"
      }
    }
  ],
  "elseResult": {
    "type": "string",
    "value": "other"
  }
}
```

```text
CASE WHEN (a = 1) THEN 'one' WHEN (a = 2) THEN 'two' ELSE 'other' END
```

# case with value

```sql
CASE a WHEN 1 THEN 'one'
       WHEN 2 THEN 'two'
       ELSE 'other'
END
```

```json
{
  "type": "case",
  "expression": {
    "type": "column_reference",
    "name": "a"
  },
  "when": [
    {
      "condition": {
        "type": "number",
        "value": "1"
      },
      "result": {
        "type": "string",
        "value": "one"
      }
    },
    {
      "condition": {
        "type": "number",
        "value": "2"
      },
      "result": {
        "type": "string",
        "value": "two"
      }
    }
  ],
  "elseResult": {
    "type": "string",
    "value": "other"
  }
}
```

```text
CASE a WHEN 1 THEN 'one' WHEN 2 THEN 'two' ELSE 'other' END
```
