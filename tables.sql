CREATE TABLE PhantomVendorPurchaseTable (
  player_uuid VARCHAR(36) NOT NULL,
  player_name VARCHAR(16),
  item VARCHAR(36),
  price SMALLINT NOT NULL DEFAULT 0,
  time BIGINT NOT NULL
);

CREATE TABLE BlacksmithReforgeTable (
  player_uuid VARCHAR(36) NOT NULL,
  player_name VARCHAR(16),
  item_one VARCHAR(36),
  item_two VARCHAR(36),
  item_result VARCHAR(36),
  time BIGINT NOT NULL
);
