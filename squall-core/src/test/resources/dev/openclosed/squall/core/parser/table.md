# create a table

```sql
CREATE TABLE products (
  product_no integer,
  name text,
  price numeric
);
```

```json
{
  "name": "products",
  "qualifiedName": "products",
  "columns": [
    {
      "name": "product_no",
      "typeName": "integer"
    },
    {
      "name": "name",
      "typeName": "text"
    },
    {
      "name": "price",
      "typeName": "numeric"
    }
  ]
}
```

# check constraints for column

```sql
CREATE TABLE products (
  price numeric CHECK (price > 0)
);
```

```json
{
  "name": "products",
  "qualifiedName": "products",
  "columns": [
    {
      "name": "price",
      "typeName": "numeric"
    }
  ]
}
```

# unique constraint for column

```sql
CREATE TABLE products (
  product_no integer UNIQUE
);
```

```json
{
  "name": "products",
  "qualifiedName": "products",
  "columns": [
    {
      "name": "product_no",
      "typeName": "integer",
      "isUnique": true
    }
  ],
  "unique": [
    {
      "columns": [ "product_no" ]
    }
  ]
}
```

# unique constraint with NULLS NOT DISTINCT for column

```sql
CREATE TABLE products (
  product_no integer UNIQUE NULLS NOT DISTINCT
);
```

```json
{
  "name": "products",
  "qualifiedName": "products",
  "columns": [
    {
      "name": "product_no",
      "typeName": "integer",
      "isUnique": true
    }
  ],
  "unique": [
    {
      "columns": [ "product_no" ]
    }
  ]
}
```

# primary key constraint for column

```sql
CREATE TABLE products (
  product_no integer PRIMARY KEY
);
```

```json
{
  "name": "products",
  "qualifiedName": "products",
  "columns": [
    {
      "name": "product_no",
      "typeName": "integer",
      "isRequired": true,
      "isPrimaryKey": true,
      "isUnique": true
    }
  ],
  "primaryKey": {
    "columns": [ "product_no" ]
  }
}
```

# primary key constraint for table

```sql
CREATE TABLE example (
  a integer,
  b integer,
  c integer,
  PRIMARY KEY (a, c)
);
```

```json
{
  "name": "example",
  "qualifiedName": "example",
  "columns": [
    {
      "name": "a",
      "typeName": "integer",
      "isPrimaryKey": true,
      "isRequired": true
    },
    {
      "name": "b",
      "typeName": "integer"
    },
    {
      "name": "c",
      "typeName": "integer",
      "isPrimaryKey": true,
      "isRequired": true
    }
  ],
  "primaryKey": {
    "columns": [ "a", "c" ]
  }
}
```

# named primary key constraint for table

```sql
CREATE TABLE example (
  a integer,
  b integer,
  c integer,
  CONSTRAINT unique_identifier PRIMARY KEY (a, c)
);
```

```json
{
  "name": "example",
  "qualifiedName": "example",
  "columns": [
    {
      "name": "a",
      "typeName": "integer",
      "isRequired": true,
      "isPrimaryKey": true
    },
    {
      "name": "b",
      "typeName": "integer"
    },
    {
      "name": "c",
      "typeName": "integer",
      "isRequired": true,
      "isPrimaryKey": true
    }
  ],
  "primaryKey": {
    "constraintName": "unique_identifier",
    "columns": [ "a", "c" ]
  }
}
```

# unique constraint for a column in table

```sql
CREATE TABLE products (
  product_no integer,
  name text,
  price numeric,
  UNIQUE (product_no)
);
```

```json
{
  "name": "products",
  "qualifiedName": "products",
  "columns": [
    {
      "name": "product_no",
      "typeName": "integer",
      "isUnique": true
    },
    {
      "name": "name",
      "typeName": "text"
    },
    {
      "name": "price",
      "typeName": "numeric"
    }
  ],
  "unique" : [
    {
      "columns": [ "product_no" ]
    }
  ]
}
```

# unique constraint for a group of columns in table

```sql
CREATE TABLE products (
  a integer,
  b integer,
  c integer,
  UNIQUE (a, c)
);
```

```json
{
  "name": "products",
  "qualifiedName": "products",
  "columns": [
    {
      "name": "a",
      "typeName": "integer"
    },
    {
      "name": "b",
      "typeName": "integer"
    },
    {
      "name": "c",
      "typeName": "integer"
    }
  ],
  "unique": [
    {
      "columns": [ "a", "c" ]
    }
  ]
}
```

# named unique constraint for a column in table

```sql
CREATE TABLE products (
  product_no integer,
  name text,
  price numeric,
  CONSTRAINT must_be_different UNIQUE (product_no)
);
```

```json
{
  "name": "products",
  "qualifiedName": "products",
  "columns": [
    {
      "name": "product_no",
      "typeName": "integer",
      "isUnique": true
    },
    {
      "name": "name",
      "typeName": "text"
    },
    {
      "name": "price",
      "typeName": "numeric"
    }
  ],
  "unique": [
    {
      "constraintName": "must_be_different",
      "columns": [ "product_no" ]
    }
  ]
}
```

# unique constraint with NULLS NOT DISTINCT for a column in table

```sql
CREATE TABLE products (
  product_no integer,
  name text,
  price numeric,
  UNIQUE NULLS NOT DISTINCT (product_no)
);
```

```json
{
  "name": "products",
  "qualifiedName": "products",
  "columns": [
    {
      "name": "product_no",
      "typeName": "integer",
      "isUnique": true
    },
    {
      "name": "name",
      "typeName": "text"
    },
    {
      "name": "price",
      "typeName": "numeric"
    }
  ],
  "unique": [
    {
      "columns": [ "product_no" ]
    }
  ]
}
```

# check constraint for table

```sql
CREATE TABLE products (
  price numeric CHECK (price > 0),
  discounted_price numeric CHECK (discounted_price > 0),
  CHECK (price > discounted_price)
);

```

```json
{
  "name": "products",
  "qualifiedName": "products",
  "columns": [
    {
      "name": "price",
      "typeName": "numeric"
    },
    {
      "name": "discounted_price",
      "typeName": "numeric"
    }
  ]
}
```

# named check constraint for table

```sql
CREATE TABLE products (
  price numeric CHECK (price > 0),
  discounted_price numeric CHECK (discounted_price > 0),
  CONSTRAINT valid_discount CHECK (price > discounted_price)
);
```

```json
{
  "name": "products",
  "qualifiedName": "products",
  "columns": [
    {
      "name": "price",
      "typeName": "numeric"
    },
    {
      "name": "discounted_price",
      "typeName": "numeric"
    }
  ]
}
```

# foreign key constraint for column

```sql
CREATE TABLE ta (
  a integer
);

CREATE TABLE tb (
  b integer REFERENCES ta(a)
);
```

```json
[
  {
    "name": "ta",
    "qualifiedName": "ta",
    "columns": [
      {
        "name": "a",
        "typeName": "integer"
      }
    ]
  },
  {
    "name": "tb",
    "qualifiedName": "tb",
    "columns": [
      {
        "name": "b",
        "typeName": "integer"
      }
    ],
    "foreignKeys": [
      {
        "schemaName": "",
        "tableName": "ta",
        "columnMapping": {
          "b": "a"
        }
      }
    ]
  }
]
```

# foreign key constraint for table

```sql
CREATE TABLE ta (
  a1 integer,
  a2 integer
);

CREATE TABLE tb (
  b1 integer,
  b2 integer,
  FOREIGN KEY(b1, b2) REFERENCES ta(a1, a2)
);
```

```json
[
  {
    "name": "ta",
    "qualifiedName": "ta",
    "columns": [
      {
        "name": "a1",
        "typeName": "integer"
      },
      {
        "name": "a2",
        "typeName": "integer"
      }
    ]
  },
  {
    "name": "tb",
    "qualifiedName": "tb",
    "columns": [
      {
        "name": "b1",
        "typeName": "integer"
      },
      {
        "name": "b2",
        "typeName": "integer"
      }
    ],
    "foreignKeys": [
      {
        "schemaName": "",
        "tableName": "ta",
        "columnMapping": {
          "b1": "a1",
          "b2": "a2"
        }
      }
    ]
  }
]
```

# foreign key with ON DELETE CASCADE

```sql
CREATE TABLE ta (
  a integer
);

CREATE TABLE tb (
  b integer,
  FOREIGN KEY(b) REFERENCES ta(a) ON DELETE CASCADE
);
```

```json
[
  {
    "name": "ta",
    "qualifiedName": "ta",
    "columns": [
      {
        "name": "a",
        "typeName": "integer"
      }
    ]
  },
  {
    "name": "tb",
    "qualifiedName": "tb",
    "columns": [
      {
        "name": "b",
        "typeName": "integer"
      }
    ],
    "foreignKeys": [
      {
        "schemaName": "",
        "tableName": "ta",
        "columnMapping": {
          "b": "a"
        }
      }
    ]
  }
]
```

# foreign key with ON DELETE SET NULL

```sql
CREATE TABLE ta (
  a integer
);

CREATE TABLE tb (
  b integer,
  FOREIGN KEY(b) REFERENCES ta(a) ON DELETE SET NULL
);
```

```json
[
  {
    "name": "ta",
    "qualifiedName": "ta",
    "columns": [
      {
        "name": "a",
        "typeName": "integer"
      }
    ]
  },
  {
    "name": "tb",
    "qualifiedName": "tb",
    "columns": [
      {
        "name": "b",
        "typeName": "integer"
      }
    ],
    "foreignKeys": [
      {
        "schemaName": "",
        "tableName": "ta",
        "columnMapping": {
          "b": "a"
        }
      }
    ]
  }
]
```
