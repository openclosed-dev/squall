# Untitled

## 1. schema1 ![schema]

### 1.1. schema1.orders ![table]

| No. | Name | Display name | Type | Nullable | Unique | Default | Foreign key | Description |
| --: | :-- | :-- | :-- | :-: | :-: | :-- | :-- | :-- |
| <a id="db1.schema1.orders.order_id" name="db1.schema1.orders.order_id"></a> 1 | order_id &#x1F511; | - | integer | - | &#x2705; | - | - | - |
| <a id="db1.schema1.orders.product_no" name="db1.schema1.orders.product_no"></a> 2 | product_no | - | integer | &#x2705; | - | - | products ([product_no](#db1.schema1.products.product_no)) | - |

### 1.2. schema1.products ![table]

| No. | Name | Display name | Type | Nullable | Unique | Default | Foreign key | Description |
| --: | :-- | :-- | :-- | :-: | :-: | :-- | :-- | :-- |
| <a id="db1.schema1.products.product_no" name="db1.schema1.products.product_no"></a> 1 | product_no &#x1F511; | - | integer | - | &#x2705; | - | - | - |
