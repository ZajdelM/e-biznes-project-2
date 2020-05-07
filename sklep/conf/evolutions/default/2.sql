
# --- !Ups

INSERT INTO "category"("name") VALUES("horror");
INSERT INTO "category"("name") VALUES("thriller");
INSERT INTO "category"("name") VALUES("komedia");

INSERT INTO "product"("name", "description", "price", "category") VALUES("Dziecko Rosemary", "Fajny film", 19.99, 1);
INSERT INTO "product"("name", "description", "price", "category") VALUES("Halloween", "Też fajny film", 24.99, 1);
INSERT INTO "product"("name", "description", "price", "category") VALUES("Psychoza", "Fajny film również", 19.99, 2);
INSERT INTO "product"("name", "description", "price", "category") VALUES("Milczenie owiec", "asdsd", 14.99, 2);
INSERT INTO "product"("name", "description", "price", "category") VALUES("Miś", "Dsasdc", 9.99, 3);

INSERT INTO "address"("street", "building", "city", "postalCode") VALUES("Krakowska", 1, "Warszawa", "00-001");
INSERT INTO "address"("street", "building", "city", "postalCode") VALUES("Warszawaska", 15, "Kraków", "30-001");
INSERT INTO "address"("street", "building", "city", "postalCode") VALUES("Batorego", 8, "Kraków", "30-014");

INSERT INTO "client"("name", "surname", "address") VALUES("Jan", "Kowalski", 1);
INSERT INTO "client"("name", "surname", "address") VALUES("Arkadiusz", "Nowak", 2);
INSERT INTO "client"("name", "surname", "address") VALUES("Janina", "Krawczyk", 3);

INSERT INTO "discount"("discount_size", "product") VALUES(5, 1);

INSERT INTO "news"("message") VALUES("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.");
INSERT INTO "news"("message") VALUES("It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");

INSERT INTO "payment"("amount", "method") VALUES(24.99, "Przelew");

INSERT INTO "shipment"("status", "method") VALUES("Wysłane", "Kurier");

INSERT INTO "cart"("product", "quantity", "client") VALUES(2, 1, 1);

INSERT INTO "order"("client", "payment", "shipment") VALUES(1, 1, 1);

# --- !Downs

DELETE FROM "category";
DELETE FROM "product";
DELETE FROM "address";
DELETE FROM "client";
DELETE FROM "discount";
DELETE FROM "news";
DELETE FROM "payment";
DELETE FROM "shipment";
DELETE FROM "cart";
DELETE FROM "order";

