# table in declared database and schema

```sql
CREATE DATABASE database1;

CREATE SCHEMA schema1;

/**
 * A description of table 1.
 * @label table 1
 */
CREATE TABLE schema1.table1 (
  /**
   * A description of column A.
   * @label column A
   */
  a varchar(64) PRIMARY KEY,
  /**
   * A description of column B.
   * @label column B
   */
  b numeric(9, 4) NOT NULL,
  /**
   * A description of column C.
   * @label column C
   */
  c integer
);
```

# Untitled

## 1. schema1 ![schema]

### 1.1. table 1 `schema1.table1` ![table]

A description of table 1.

| No. | Name | Display name | Type | Nullable | Unique | Default | Foreign key | Description |
| --: | :-- | :-- | :-- | :-: | :-: | :-- | :-- | :-- |
| <a id="database1.schema1.table1.a"></a> 1 | a &#x1F511; | column A | varchar(64) | - | &#x2705; | - | - | A description of column A. |
| <a id="database1.schema1.table1.b"></a> 2 | b | column B | numeric(9, 4) | - | - | - | - | A description of column B. |
| <a id="database1.schema1.table1.c"></a> 3 | c | column C | integer | &#x2705; | - | - | - | A description of column C. |

---

# table with default schema

```sql
/**
 * A description of table 1.
 * @label table 1
 */
CREATE TABLE table1 (
  /**
   * A description of column A.
   * @label column A
   */
  a integer PRIMARY KEY
);
```

# Untitled

## 1. public ![schema]

### 1.1. table 1 `public.table1` ![table]

A description of table 1.

| No. | Name | Display name | Type | Nullable | Unique | Default | Foreign key | Description |
| --: | :-- | :-- | :-- | :-: | :-: | :-- | :-- | :-- |
| <a id=".public.table1.a"></a> 1 | a &#x1F511; | column A | integer | - | &#x2705; | - | - | A description of column A. |

---

# multiple lines for column description

```sql
/**
 * A description of table 1.
 * @label table 1
 */
CREATE TABLE public.table1 (
  /**
   * first line.
   * second line.
   * @label column c1
   */
  c1 varchar(64) PRIMARY KEY
);
```

# Untitled

## 1. public ![schema]

### 1.1. table 1 `public.table1` ![table]

A description of table 1.

| No. | Name | Display name | Type | Nullable | Unique | Default | Foreign key | Description |
| --: | :-- | :-- | :-- | :-: | :-: | :-- | :-- | :-- |
| <a id=".public.table1.c1"></a> 1 | c1 &#x1F511; | column c1 | varchar(64) | - | &#x2705; | - | - | first line. second line. |

---

# blank line in column description

```sql
/**
 * A description of table 1.
 * @label table 1
 */
CREATE TABLE public.table1 (
  /**
   * first line.
   *
   * second line.
   * @label column c1
   */
  c1 varchar(64) PRIMARY KEY
);
```

# Untitled

## 1. public ![schema]

### 1.1. table 1 `public.table1` ![table]

A description of table 1.

| No. | Name | Display name | Type | Nullable | Unique | Default | Foreign key | Description |
| --: | :-- | :-- | :-- | :-: | :-: | :-- | :-- | :-- |
| <a id=".public.table1.c1"></a> 1 | c1 &#x1F511; | column c1 | varchar(64) | - | &#x2705; | - | - | first line. second line. |

---

# deprecated table

```sql
/**
 * A description of table 1.
 * @label table 1
 * @deprecated Do not use this table.
 */
CREATE TABLE public.table1 ();
```

# Untitled

## 1. public ![schema]

### 1.1. ~~table 1~~ ~~`public.table1`~~ ![table]

**Deprecated.** Do not use this table.

A description of table 1.

---

# deprecated column

```sql
/**
 * A description of table 1.
 * @label table 1
 */
CREATE TABLE schema1.table1 (
  /**
   * A description of column A.
   * @label column A
   */
  a varchar(64) PRIMARY KEY,
  /**
   * A description of column B.
   * @label column B
   * @deprecated Do not use this column.
   */
  b numeric(9, 4) NOT NULL
);
```

# Untitled

## 1. schema1 ![schema]

### 1.1. table 1 `schema1.table1` ![table]

A description of table 1.

| No. | Name | Display name | Type | Nullable | Unique | Default | Foreign key | Description |
| --: | :-- | :-- | :-- | :-: | :-: | :-- | :-- | :-- |
| <a id=".schema1.table1.a"></a> 1 | a &#x1F511; | column A | varchar(64) | - | &#x2705; | - | - | A description of column A. |
| <a id=".schema1.table1.b"></a> 2 | ~~b~~ | ~~column B~~ | numeric(9, 4) | - | - | - | - | **Deprecated.** Do not use this column.<br>A description of column B. |

---

# foreign key

```sql
CREATE TABLE products (
  product_no integer PRIMARY KEY,
  name text,
  price numeric
);

CREATE TABLE orders (
    order_id integer PRIMARY KEY,
    product_no integer REFERENCES products (product_no),
    quantity integer
);
```

# Untitled

## 1. public ![schema]

### 1.1. public.orders ![table]

| No. | Name | Display name | Type | Nullable | Unique | Default | Foreign key | Description |
| --: | :-- | :-- | :-- | :-: | :-: | :-- | :-- | :-- |
| <a id=".public.orders.order_id"></a> 1 | order_id &#x1F511; | - | integer | - | &#x2705; | - | - | - |
| <a id=".public.orders.product_no"></a> 2 | product_no | - | integer | &#x2705; | - | - | public.products ([product_no](#.public.products.product_no)) | - |
| <a id=".public.orders.quantity"></a> 3 | quantity | - | integer | &#x2705; | - | - | - | - |

### 1.2. public.products ![table]

| No. | Name | Display name | Type | Nullable | Unique | Default | Foreign key | Description |
| --: | :-- | :-- | :-- | :-: | :-: | :-- | :-- | :-- |
| <a id=".public.products.product_no"></a> 1 | product_no &#x1F511; | - | integer | - | &#x2705; | - | - | - |
| <a id=".public.products.name"></a> 2 | name | - | text | &#x2705; | - | - | - | - |
| <a id=".public.products.price"></a> 3 | price | - | numeric | &#x2705; | - | - | - | - |

