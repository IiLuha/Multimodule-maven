INSERT INTO author (id, firstname, lastname, patronymic, birthday)
VALUES (1, 'Кей', 'Хорстманн'   , 'Ivanovich', '1974-9-23'),
       (2, 'Гари', 'Корнелл'    , 'Ivanovich', '1974-9-23'),
       (3, 'Стивен', 'Кови'     , 'Ivanovich', '1974-9-23'),
       (4, 'Тони', 'Роббинс'    , 'Ivanovich', '1974-9-23'),
       (5, 'Наполеон', 'Хилл'   , 'Ivanovich', '1974-9-23'),
       (6, 'Роберт', 'Кийосаки' , 'Ivanovich', '1974-9-23'),
       (7, 'Дейл', 'Карнеги'    , 'Ivanovich', '1974-9-23');
SELECT SETVAL('author_id_seq', (SELECT MAX(id) FROM author));

INSERT INTO book (id, name, description, price, quantity, issue_year)
VALUES (1,  'Java. Библиотека профессионала. Том 1',              'description', 1102.44, 25, 2010),
       (2,  'Java. Библиотека профессионала. Том 2',              'description', 954.22,  25, 2012),
       (3,  'Java SE 8. Вводный курс',                            'description', 203.83,  25, 2015),
       (4,  '7 навыков высокоэффективных людей',                  'description', 396.13,  25, 1989),
       (5,  'Разбуди в себе исполина',                            'description', 576.00,  25, 1991),
       (6,  'Думай и богатей',                                    'description', 336.70,  25, 1937),
       (7,  'Богатый папа, бедный папа',                          'description', 352.88,  25, 1997),
       (8,  'Квадрант денежного потока',                          'description', 368.99,  25, 1998),
       (9,  'Как перестать беспокоиться и начать жить',           'description', 368.00,  25, 1948),
       (10, 'Как завоевывать друзей и оказывать влияние на людей','description', 352.00,  25, 1936);
SELECT SETVAL('book_id_seq', (SELECT MAX(id) FROM book));

INSERT INTO book_author (author_id, book_id)
VALUES  (1, 1 ),
        (2, 1 ),
        (1, 2 ),
        (2, 2 ),
        (1, 3 ),
        (3, 4 ),
        (4, 5 ),
        (5, 6 ),
        (6, 7 ),
        (6, 8 ),
        (7, 9 ),
        (7, 10);

INSERT INTO users (id, email, password, role)
VALUES  (1, 'Bill@Email.com',   'pass', 'USER'),
        (2, 'Steve@Email.com',  'pass', 'USER'),
        (3, 'Sergey@Email.com', 'pass', 'USER'),
        (4, 'testSecurity@Email.com', 'pass', 'ADMIN');
SELECT SETVAL('users_id_seq', (SELECT MAX(id) FROM users));

INSERT INTO user_details (id, user_id, firstname, lastname, patronymic, phone)
VALUES  (1, 1, 'Bill',   'Gates', 'Ivanovich', '+418885553535'),
        (2, 2, 'Steve',  'Jobs',  'Ivanovich', '+418885553535'),
        (3, 3, 'Sergey', 'Brin',  'Ivanovich', '+418885553535');
SELECT SETVAL('user_details_id_seq', (SELECT MAX(id) FROM user_details));

INSERT INTO user_address (user_id, region, district, population_center, street, house, is_private)
VALUES  (1, 'Краснодарский край', 'Геленджикский район', 'село Пшада', 'улица Красной армии', '14A',  true),
        (2, 'Краснодарский край', 'Геленджикский район', 'село Пшада', 'улица Красной армии', '15',   true),
        (3, 'Краснодарский край', 'Геленджикский район', 'село Пшада', 'улица Красной армии', '15K4', true);

INSERT INTO orders (id, created_at, end_at, status, price, client_id)
VALUES  (1, to_timestamp('04 Mar 2022', 'DD Mon YYYY'), NULL, 'OPEN', 0.0, 1),
        (2, to_timestamp('05 Aug 2012', 'DD Mon YYYY'), NULL, 'OPEN', 0.0, 1),
        (3, to_timestamp('23 Dec 2002', 'DD Mon YYYY'), NULL, 'OPEN', 0.0, 1),
        (4, to_timestamp('14 Feb 2018', 'DD Mon YYYY'), NULL, 'OPEN', 0.0, 3);
SELECT SETVAL('orders_id_seq', (SELECT MAX(id) FROM orders));

INSERT INTO order_product (id, order_id, book_id, quantity, total_price)
VALUES  (1,  1, 1,  25, 25 * (SELECT price FROM book WHERE book.id = 1 )),
        (2,  1, 2,  25, 25 * (SELECT price FROM book WHERE book.id = 2 )),
        (3,  2, 7,  25, 25 * (SELECT price FROM book WHERE book.id = 7 )),
        (4,  2, 8,  25, 25 * (SELECT price FROM book WHERE book.id = 8 )),
        (5,  2, 6,  25, 25 * (SELECT price FROM book WHERE book.id = 6 )),
        (6,  3, 5,  15, 15 * (SELECT price FROM book WHERE book.id = 5 )),
        (7,  4, 9,  25, 25 * (SELECT price FROM book WHERE book.id = 9 )),
        (8,  4, 10, 25, 25 * (SELECT price FROM book WHERE book.id = 10)),
        (9,  4, 3,  25, 25 * (SELECT price FROM book WHERE book.id = 3 )),
        (10,  4, 4,  25, 25 * (SELECT price FROM book WHERE book.id = 4 )),
        (11, 4, 5,  10, 10 * (SELECT price FROM book WHERE book.id = 5 ));
SELECT SETVAL('order_product_id_seq', (SELECT MAX(id) FROM order_product));

UPDATE orders SET price = (SELECT sum(op.total_price) FROM order_product op WHERE op.order_id = orders.id);