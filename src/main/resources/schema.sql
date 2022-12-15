CREATE TABLE item
(
  item_id        BIGINT NOT NULL AUTO_INCREMENT,
  item_name      VARCHAR(64),
  price          INT    NOT NULL,
  stock_quantity INT    NOT NULL,
  version INT default 1,
CONSTRAINT pk_item PRIMARY KEY (item_id)
);

CREATE TABLE order_table
(
  order_id        BIGINT NOT NULL AUTO_INCREMENT,
  order_price           INT    NOT NULL,
CONSTRAINT pk_order PRIMARY KEY (order_id)
);

CREATE TABLE order_item
(
    order_item_id   BIGINT    NOT NULL AUTO_INCREMENT,
    order_id        BIGINT    NOT NULL,
    item_id         BIGINT    NOT NULL,
    count           INT       NOT NULL,
    CONSTRAINT pk_order_item PRIMARY KEY (order_item_id),
foreign key (order_id) references order_table(order_id),
foreign key (item_id) references item(item_id)
);
