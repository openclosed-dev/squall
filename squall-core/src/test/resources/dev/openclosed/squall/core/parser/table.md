# Create a table

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

# Check constraints for column

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

# Unique constraint for a column

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

# Unique constraint with NULLS NOT DISTINCT for a column

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

# Primary key constraint for column

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

# Primary key constraint for table

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

# Named primary key constraint for table

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
    "name": "unique_identifier",
    "columns": [ "a", "c" ]
  }
}
```

# Unique constraint for a column in table

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

# Unique constraint for a group of columns in table

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

# Named unique constraint for a column in table

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
      "name": "must_be_different",
      "columns": [ "product_no" ]
    } 
  ]
}
```

# Unique constraint with NULLS NOT DISTINCT for a column in table 

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

# Check constraint for table

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

# Named check constraint for table

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
