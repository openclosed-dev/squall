# Untitled

## 1. public ![schema]

### 1.1. public.orders ![table]

| No. | Name | Display name | Type | Nullable | Unique | Default | Foreign key | Description |
| --: | :-- | :-- | :-- | :-: | :-: | :-- | :-- | :-- |
| <a id=".public.orders.order_id" name=".public.orders.order_id"></a> 1 | order_id &#x1F511; | - | integer | - | &#x2705; | - | - | - |
| <a id=".public.orders.product_no" name=".public.orders.product_no"></a> 2 | product_no | - | integer | &#x2705; | - | - | products ([product_no](#.public.products.product_no)) | - |
| <a id=".public.orders.quantity" name=".public.orders.quantity"></a> 3 | quantity | - | integer | &#x2705; | - | - | - | - |

### 1.2. public.products ![table]

| No. | Name | Display name | Type | Nullable | Unique | Default | Foreign key | Description |
| --: | :-- | :-- | :-- | :-: | :-: | :-- | :-- | :-- |
| <a id=".public.products.product_no" name=".public.products.product_no"></a> 1 | product_no &#x1F511; | - | integer | - | &#x2705; | - | - | - |
| <a id=".public.products.name" name=".public.products.name"></a> 2 | name | - | text | &#x2705; | - | - | - | - |
| <a id=".public.products.price" name=".public.products.price"></a> 3 | price | - | numeric | &#x2705; | - | - | - | - |
