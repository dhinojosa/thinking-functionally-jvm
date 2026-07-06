CREATE TABLE IF NOT EXISTS customers (
  id TEXT PRIMARY KEY,
  name TEXT NOT NULL,
  credit_limit NUMERIC NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
  id TEXT PRIMARY KEY,
  name TEXT NOT NULL,
  price NUMERIC NOT NULL,
  quantity_available INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
  id BIGSERIAL PRIMARY KEY,
  customer_id TEXT NOT NULL REFERENCES customers(id),
  status TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS order_items (
  order_id BIGINT NOT NULL REFERENCES orders(id),
  product_id TEXT NOT NULL REFERENCES products(id),
  quantity INTEGER NOT NULL,
  unit_price NUMERIC NOT NULL,
  PRIMARY KEY (order_id, product_id)
);

INSERT INTO customers (id, name, credit_limit)
VALUES
  ('cust-1', 'Ada Lovelace', 250.00),
  ('cust-2', 'Grace Hopper', 1000.00)
ON CONFLICT (id) DO NOTHING;

INSERT INTO products (id, name, price, quantity_available)
VALUES
  ('prod-1', 'Keyboard', 75.00, 10),
  ('prod-2', 'Monitor', 225.00, 5),
  ('prod-3', 'Dock', 140.00, 3)
ON CONFLICT (id) DO NOTHING;
