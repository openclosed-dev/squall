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
  "parents": ["", "defaultschema"],
  "columns": [
    {
      "name": "product_no",
      "parents": ["", "defaultschema", "products"],
      "typeName": "integer"
    },
    {
      "name": "name",
      "parents": ["", "defaultschema", "products"],
      "typeName": "text"
    },
    {
      "name": "price",
      "parents": ["", "defaultschema", "products"],
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
  "parents": ["", "defaultschema"],
  "columns": [
    {
      "name": "price",
      "parents": ["", "defaultschema", "products"],
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
  "parents": ["", "defaultschema"],
  "columns": [
    {
      "name": "product_no",
      "parents": ["", "defaultschema", "products"],
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
  "parents": ["", "defaultschema"],
  "columns": [
    {
      "name": "product_no",
      "parents": ["", "defaultschema", "products"],
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
  "parents": ["", "defaultschema"],
  "columns": [
    {
      "name": "product_no",
      "parents": ["", "defaultschema", "products"],
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
  "parents": ["", "defaultschema"],
  "columns": [
    {
      "name": "a",
      "parents": ["", "defaultschema", "example"],
      "typeName": "integer",
      "isPrimaryKey": true,
      "isRequired": true
    },
    {
      "name": "b",
      "parents": ["", "defaultschema", "example"],
      "typeName": "integer"
    },
    {
      "name": "c",
      "parents": ["", "defaultschema", "example"],
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
  "parents": ["", "defaultschema"],
  "columns": [
    {
      "name": "a",
      "parents": ["", "defaultschema", "example"],
      "typeName": "integer",
      "isRequired": true,
      "isPrimaryKey": true
    },
    {
      "name": "b",
      "parents": ["", "defaultschema", "example"],
      "typeName": "integer"
    },
    {
      "name": "c",
      "parents": ["", "defaultschema", "example"],
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
  "parents": ["", "defaultschema"],
  "columns": [
    {
      "name": "product_no",
      "parents": ["", "defaultschema", "products"],
      "typeName": "integer",
      "isUnique": true
    },
    {
      "name": "name",
      "parents": ["", "defaultschema", "products"],
      "typeName": "text"
    },
    {
      "name": "price",
      "parents": ["", "defaultschema", "products"],
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
  "parents": ["", "defaultschema"],
  "columns": [
    {
      "name": "a",
      "parents": ["", "defaultschema", "products"],
      "typeName": "integer"
    },
    {
      "name": "b",
      "parents": ["", "defaultschema", "products"],
      "typeName": "integer"
    },
    {
      "name": "c",
      "parents": ["", "defaultschema", "products"],
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
  "parents": ["", "defaultschema"],
  "columns": [
    {
      "name": "product_no",
      "parents": ["", "defaultschema", "products"],
      "typeName": "integer",
      "isUnique": true
    },
    {
      "name": "name",
      "parents": ["", "defaultschema", "products"],
      "typeName": "text"
    },
    {
      "name": "price",
      "parents": ["", "defaultschema", "products"],
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
  "parents": ["", "defaultschema"],
  "columns": [
    {
      "name": "product_no",
      "parents": ["", "defaultschema", "products"],
      "typeName": "integer",
      "isUnique": true
    },
    {
      "name": "name",
      "parents": ["", "defaultschema", "products"],
      "typeName": "text"
    },
    {
      "name": "price",
      "parents": ["", "defaultschema", "products"],
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
  "parents": ["", "defaultschema"],
  "columns": [
    {
      "name": "price",
      "parents": ["", "defaultschema", "products"],
      "typeName": "numeric"
    },
    {
      "name": "discounted_price",
      "parents": ["", "defaultschema", "products"],
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
  "parents": ["", "defaultschema"],
  "columns": [
    {
      "name": "price",
      "parents": ["", "defaultschema", "products"],
      "typeName": "numeric"
    },
    {
      "name": "discounted_price",
      "parents": ["", "defaultschema", "products"],
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
    "parents": ["", "defaultschema"],
    "columns": [
      {
        "name": "a",
        "parents": ["", "defaultschema", "ta"],
        "typeName": "integer"
      }
    ]
  },
  {
    "name": "tb",
    "parents": ["", "defaultschema"],
    "columns": [
      {
        "name": "b",
        "parents": ["", "defaultschema", "tb"],
        "typeName": "integer"
      }
    ],
    "foreignKeys": [
      {
        "tableName": ["", "defaultschema", "ta"],
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
    "parents": ["", "defaultschema"],
    "columns": [
      {
        "name": "a1",
        "parents": ["", "defaultschema", "ta"],
        "typeName": "integer"
      },
      {
        "name": "a2",
        "parents": ["", "defaultschema", "ta"],
        "typeName": "integer"
      }
    ]
  },
  {
    "name": "tb",
    "parents": ["", "defaultschema"],
    "columns": [
      {
        "name": "b1",
        "parents": ["", "defaultschema", "tb"],
        "typeName": "integer"
      },
      {
        "name": "b2",
        "parents": ["", "defaultschema", "tb"],
        "typeName": "integer"
      }
    ],
    "foreignKeys": [
      {
        "tableName": ["", "defaultschema", "ta"],
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
    "parents": ["", "defaultschema"],
    "columns": [
      {
        "name": "a",
        "parents": ["", "defaultschema", "ta"],
        "typeName": "integer"
      }
    ]
  },
  {
    "name": "tb",
    "parents": ["", "defaultschema"],
    "columns": [
      {
        "name": "b",
        "parents": ["", "defaultschema", "tb"],
        "typeName": "integer"
      }
    ],
    "foreignKeys": [
      {
        "tableName": ["", "defaultschema", "ta"],
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
    "parents": ["", "defaultschema"],
    "columns": [
      {
        "name": "a",
        "parents": ["", "defaultschema", "ta"],
        "typeName": "integer"
      }
    ]
  },
  {
    "name": "tb",
    "parents": ["", "defaultschema"],
    "columns": [
      {
        "name": "b",
        "parents": ["", "defaultschema", "tb"],
        "typeName": "integer"
      }
    ],
    "foreignKeys": [
      {
        "tableName": ["", "defaultschema", "ta"],
        "columnMapping": {
          "b": "a"
        }
      }
    ]
  }
]
```
