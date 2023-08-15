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
  "parents": ["", ""],
  "columns": [
    {
      "name": "product_no",
      "parents": ["", "", "products"],
      "typeName": "integer"
    },
    {
      "name": "name",
      "parents": ["", "", "products"],
      "typeName": "text"
    },
    {
      "name": "price",
      "parents": ["", "", "products"],
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
  "parents": ["", ""],
  "columns": [
    {
      "name": "price",
      "parents": ["", "", "products"],
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
  "parents": ["", ""],
  "columns": [
    {
      "name": "product_no",
      "parents": ["", "", "products"],
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
  "parents": ["", ""],
  "columns": [
    {
      "name": "product_no",
      "parents": ["", "", "products"],
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
  "parents": ["", ""],
  "columns": [
    {
      "name": "product_no",
      "parents": ["", "", "products"],
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
  "parents": ["", ""],
  "columns": [
    {
      "name": "a",
      "parents": ["", "", "example"],
      "typeName": "integer",
      "isPrimaryKey": true,
      "isRequired": true
    },
    {
      "name": "b",
      "parents": ["", "", "example"],
      "typeName": "integer"
    },
    {
      "name": "c",
      "parents": ["", "", "example"],
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
  "parents": ["", ""],
  "columns": [
    {
      "name": "a",
      "parents": ["", "", "example"],
      "typeName": "integer",
      "isRequired": true,
      "isPrimaryKey": true
    },
    {
      "name": "b",
      "parents": ["", "", "example"],
      "typeName": "integer"
    },
    {
      "name": "c",
      "parents": ["", "", "example"],
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
  "parents": ["", ""],
  "columns": [
    {
      "name": "product_no",
      "parents": ["", "", "products"],
      "typeName": "integer",
      "isUnique": true
    },
    {
      "name": "name",
      "parents": ["", "", "products"],
      "typeName": "text"
    },
    {
      "name": "price",
      "parents": ["", "", "products"],
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
  "parents": ["", ""],
  "columns": [
    {
      "name": "a",
      "parents": ["", "", "products"],
      "typeName": "integer"
    },
    {
      "name": "b",
      "parents": ["", "", "products"],
      "typeName": "integer"
    },
    {
      "name": "c",
      "parents": ["", "", "products"],
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
  "parents": ["", ""],
  "columns": [
    {
      "name": "product_no",
      "parents": ["", "", "products"],
      "typeName": "integer",
      "isUnique": true
    },
    {
      "name": "name",
      "parents": ["", "", "products"],
      "typeName": "text"
    },
    {
      "name": "price",
      "parents": ["", "", "products"],
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
  "parents": ["", ""],
  "columns": [
    {
      "name": "product_no",
      "parents": ["", "", "products"],
      "typeName": "integer",
      "isUnique": true
    },
    {
      "name": "name",
      "parents": ["", "", "products"],
      "typeName": "text"
    },
    {
      "name": "price",
      "parents": ["", "", "products"],
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
  "parents": ["", ""],
  "columns": [
    {
      "name": "price",
      "parents": ["", "", "products"],
      "typeName": "numeric"
    },
    {
      "name": "discounted_price",
      "parents": ["", "", "products"],
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
  "parents": ["", ""],
  "columns": [
    {
      "name": "price",
      "parents": ["", "", "products"],
      "typeName": "numeric"
    },
    {
      "name": "discounted_price",
      "parents": ["", "", "products"],
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
    "parents": ["", ""],
    "columns": [
      {
        "name": "a",
        "parents": ["", "", "ta"],
        "typeName": "integer"
      }
    ]
  },
  {
    "name": "tb",
    "parents": ["", ""],
    "columns": [
      {
        "name": "b",
        "parents": ["", "", "tb"],
        "typeName": "integer"
      }
    ],
    "foreignKeys": [
      {
        "tableName": "ta",
        "tableParents": ["", ""],
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
    "parents": ["", ""],
    "columns": [
      {
        "name": "a1",
        "parents": ["", "", "ta"],
        "typeName": "integer"
      },
      {
        "name": "a2",
        "parents": ["", "", "ta"],
        "typeName": "integer"
      }
    ]
  },
  {
    "name": "tb",
    "parents": ["", ""],
    "columns": [
      {
        "name": "b1",
        "parents": ["", "", "tb"],
        "typeName": "integer"
      },
      {
        "name": "b2",
        "parents": ["", "", "tb"],
        "typeName": "integer"
      }
    ],
    "foreignKeys": [
      {
        "tableName": "ta",
        "tableParents": ["", ""],
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
    "parents": ["", ""],
    "columns": [
      {
        "name": "a",
        "parents": ["", "", "ta"],
        "typeName": "integer"
      }
    ]
  },
  {
    "name": "tb",
    "parents": ["", ""],
    "columns": [
      {
        "name": "b",
        "parents": ["", "", "tb"],
        "typeName": "integer"
      }
    ],
    "foreignKeys": [
      {
        "tableName": "ta",
        "tableParents": ["", ""],
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
    "parents": ["", ""],
    "columns": [
      {
        "name": "a",
        "parents": ["", "", "ta"],
        "typeName": "integer"
      }
    ]
  },
  {
    "name": "tb",
    "parents": ["", ""],
    "columns": [
      {
        "name": "b",
        "parents": ["", "", "tb"],
        "typeName": "integer"
      }
    ],
    "foreignKeys": [
      {
        "tableName": "ta",
        "tableParents": ["", ""],
        "columnMapping": {
          "b": "a"
        }
      }
    ]
  }
]
```
