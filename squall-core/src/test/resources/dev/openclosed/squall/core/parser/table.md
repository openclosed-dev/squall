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
  "columns": [
    {
      "name": "product_no",
      "dataType": "integer",
      "nullable": true,
      "unique": false

    },
    {
      "name": "name",
      "dataType": "text",
      "nullable": true,
      "unique": false
    },
    {
      "name": "price",
      "dataType": "numeric",
      "nullable": true,
      "unique": false
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
  "columns": [
    {
      "name": "price",
      "dataType": "numeric",
      "nullable": true,
      "unique": false
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
  "columns": [
    {
      "name": "product_no",
      "dataType": "integer",
      "nullable": true,
      "unique": true
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
  "columns": [
    {
      "name": "product_no",
      "dataType": "integer",
      "nullable": true,
      "unique": true
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
  "columns": [
    {
      "name": "product_no",
      "dataType": "integer",
      "nullable": false,
      "unique": true
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
  "columns": [
    {
      "name": "a",
      "dataType": "integer",
      "nullable": false,
      "unique": false
    },
    {
      "name": "b",
      "dataType": "integer",
      "nullable": true,
      "unique": false
    },
    {
      "name": "c",
      "dataType": "integer",
      "nullable": false,
      "unique": false
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
  "columns": [
    {
      "name": "a",
      "dataType": "integer",
      "nullable": false,
      "unique": false
    },
    {
      "name": "b",
      "dataType": "integer",
      "nullable": true,
      "unique": false
    },
    {
      "name": "c",
      "dataType": "integer",
      "nullable": false,
      "unique": false
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
  "columns": [
    {
      "name": "product_no",
      "dataType": "integer",
      "nullable": true,
      "unique": true
    },
    {
      "name": "name",
      "dataType": "text",
      "nullable": true,
      "unique": false
    },
    {
      "name": "price",
      "dataType": "numeric",
      "nullable": true,
      "unique": false
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
  "columns": [
    {
      "name": "a",
      "dataType": "integer",
      "nullable": true,
      "unique": false
    },
    {
      "name": "b",
      "dataType": "integer",
      "nullable": true,
      "unique": false
    },
    {
      "name": "c",
      "dataType": "integer",
      "nullable": true,
      "unique": false
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
  "columns": [
    {
      "name": "product_no",
      "dataType": "integer",
      "nullable": true,
      "unique": true
    },
    {
      "name": "name",
      "dataType": "text",
      "nullable": true,
      "unique": false
    },
    {
      "name": "price",
      "dataType": "numeric",
      "nullable": true,
      "unique": false
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
  "columns": [
    {
      "name": "product_no",
      "dataType": "integer",
      "nullable": true,
      "unique": true
    },
    {
      "name": "name",
      "dataType": "text",
      "nullable": true,
      "unique": false
    },
    {
      "name": "price",
      "dataType": "numeric",
      "nullable": true,
      "unique": false
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
  "columns": [
    {
      "name": "price",
      "dataType": "numeric",
      "nullable": true,
      "unique": false
    },
    {
      "name": "discounted_price",
      "dataType": "numeric",
      "nullable": true,
      "unique": false
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
  "columns": [
    {
      "name": "price",
      "dataType": "numeric",
      "nullable": true,
      "unique": false
    },
    {
      "name": "discounted_price",
      "dataType": "numeric",
      "nullable": true,
      "unique": false
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
    "columns": [
      {
        "name": "a",
        "dataType": "integer",
        "nullable": true,
        "unique": false
      }
    ]
  },
  {
    "name": "tb",
    "columns": [
      {
        "name": "b",
        "dataType": "integer",
        "nullable": true,
        "unique": false
      }
    ],
    "foreignKeys": [
      {
        "schemaName": "public",
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
    "columns": [
      {
        "name": "a1",
        "dataType": "integer",
        "nullable": true,
        "unique": false
      },
      {
        "name": "a2",
        "dataType": "integer",
        "nullable": true,
        "unique": false
      }
    ]
  },
  {
    "name": "tb",
    "columns": [
      {
        "name": "b1",
        "dataType": "integer",
        "nullable": true,
        "unique": false
      },
      {
        "name": "b2",
        "dataType": "integer",
        "nullable": true,
        "unique": false
      }
    ],
    "foreignKeys": [
      {
        "schemaName": "public",
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
    "columns": [
      {
        "name": "a",
        "dataType": "integer",
        "nullable": true,
        "unique": false
      }
    ]
  },
  {
    "name": "tb",
    "columns": [
      {
        "name": "b",
        "dataType": "integer",
        "nullable": true,
        "unique": false
      }
    ],
    "foreignKeys": [
      {
        "schemaName": "public",
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
    "columns": [
      {
        "name": "a",
        "dataType": "integer",
        "nullable": true,
        "unique": false
      }
    ]
  },
  {
    "name": "tb",
    "columns": [
      {
        "name": "b",
        "dataType": "integer",
        "nullable": true,
        "unique": false
      }
    ],
    "foreignKeys": [
      {
        "schemaName": "public",
        "tableName": "ta",
        "columnMapping": {
          "b": "a"
        }
      }
    ]
  }
]
```
