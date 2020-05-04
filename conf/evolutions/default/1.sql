# --- !Ups

CREATE TABLE "category" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "name" VARCHAR NOT NULL
);

CREATE TABLE "product" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "name" VARCHAR NOT NULL,
 "description" VARCHAR NOT NULL,
 "price" DOUBLE NOT NULL,
 "category" INT NOT NULL,
 FOREIGN KEY(category) references category(id)
);

CREATE TABLE "address" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "street" VARCHAR NOT NULL,
 "building" INT NOT NULL,
 "city" VARCHAR NOT NULL,
 "postalCode" VARCHAR NOT NULL
);

CREATE TABLE "client" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "name" VARCHAR NOT NULL,
 "surname" VARCHAR NOT NULL,
 "address" INT NOT NULL,
 FOREIGN KEY(address) references address(id)
);

CREATE TABLE "discount" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "discount_size" INT NOT NULL,
 "product" INT NOT NULL,
 FOREIGN KEY(product) references product(id)
);

CREATE TABLE "news" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "message" VARCHAR NOT NULL
);

CREATE TABLE "payment" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "amount" DOUBLE NOT NULL,
 "method" VARCHAR NOT NULL
);

CREATE TABLE "shipment" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "status" VARCHAR NOT NULL,
 "method" VARCHAR NOT NULL
);

CREATE TABLE "cart" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "product" INT NOT NULL,
 "quantity" INT NOT NULL,
 "client" INT NOT NULL,
 FOREIGN KEY(product) references product(id),
 FOREIGN KEY(client) references client(id)
);

CREATE TABLE "order" (
 "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
 "client" INT NOT NULL,
 "payment" INT NOT NULL,
 "shipment" INT NOT NULL,
 FOREIGN KEY(client) references client(id),
 FOREIGN KEY(payment) references payment(id),
 FOREIGN KEY(shipment) references shipment(id)
);

# --- !Downs
DROP TABLE "category"
DROP TABLE "product"
DROP TABLE "address"
DROP TABLE "client"
DROP TABLE "discount"
DROP TABLE "news"
DROP TABLE "payment"
DROP TABLE "shipment"
DROP TABLE "cart"
DROP TABLE "order"