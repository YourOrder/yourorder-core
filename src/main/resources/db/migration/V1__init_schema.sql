CREATE TABLE category_entity (
                                 id UUID PRIMARY KEY,
                                 name VARCHAR(100) NOT NULL,
                                 description VARCHAR(500),
                                 parent_id UUID,

                                 CONSTRAINT fk_category_parent
                                     FOREIGN KEY (parent_id)
                                         REFERENCES category_entity(id)
);

CREATE TABLE product_entity (
                                id UUID PRIMARY KEY,
                                name VARCHAR(100) NOT NULL,
                                price NUMERIC(10, 2) NOT NULL,
                                supplier_id UUID NOT NULL,
                                category_id UUID NOT NULL,
                                quantity INTEGER NOT NULL DEFAULT 0,
                                reserved_quantity INTEGER NOT NULL DEFAULT 0,

                                CONSTRAINT fk_product_category
                                    FOREIGN KEY (category_id)
                                        REFERENCES category_entity(id)
);

CREATE TABLE order_entity (
                              id UUID PRIMARY KEY,
                              user_id UUID NOT NULL,
                              status VARCHAR(50) NOT NULL,
                              total_amount NUMERIC(10, 2) NOT NULL,
                              created_at TIMESTAMP NOT NULL
);

CREATE TABLE order_item_entity (
                                   id UUID PRIMARY KEY,
                                   order_id UUID NOT NULL,
                                   product_id UUID NOT NULL,
                                   quantity INTEGER NOT NULL,
                                   price NUMERIC(10, 2) NOT NULL,

                                   CONSTRAINT fk_order_item_order
                                       FOREIGN KEY (order_id)
                                           REFERENCES order_entity(id),

                                   CONSTRAINT fk_order_item_product
                                       FOREIGN KEY (product_id)
                                           REFERENCES product_entity(id)
);