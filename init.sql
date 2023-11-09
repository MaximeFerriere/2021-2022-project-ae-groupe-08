DROP SCHEMA IF EXISTS projet_ae_08 CASCADE;
CREATE SCHEMA projet_ae_08;

CREATE TABLE projet_ae_08.addresses
(
    id_address      SERIAL PRIMARY KEY,
    street          varchar(240),
    building_number INTEGER,
    unit_number     varchar(50),
    postcode        INTEGER,
    commune         varchar(240),
    version_address INTEGER
);

CREATE TABLE projet_ae_08.members
(
    id_member                     SERIAL PRIMARY KEY,
    username                      varchar(240),
    last_name                     varchar(50),
    firstname                     varchar(50),
    is_admin                      bool,
    call_number                   varchar(15),
    password                      varchar(240),
    reason_for_connection_refusal varchar(240),
    condition                     varchar(50),
    number_offer_not_picked_up    integer,
    version_member                integer,
    address                       integer references projet_ae_08.addresses (id_address)
);

CREATE TABLE projet_ae_08.types
(
    id_type      SERIAL PRIMARY KEY,
    wording_type varchar(50)
);

CREATE TABLE projet_ae_08.objects
(
    id_object                   SERIAL PRIMARY KEY,
    name                        varchar(50),
    description                 varchar(240),
    url_photo                   varchar(240),
    time_slots_available        varchar(240),
    number_of_people_interested INTEGER,
    state                       varchar(50),
    version_object              INTEGER,
    type                        integer references projet_ae_08.types (id_type),
    id_offeror                  integer references projet_ae_08.members (id_member),
    id_recipient                integer references projet_ae_08.members (id_member)
);

CREATE TABLE projet_ae_08.ratings
(
    note      integer,
    remark    varchar(240),
    id_object integer references projet_ae_08.objects (id_object),
    id_member integer references projet_ae_08.members (id_member),
    PRIMARY KEY (id_object, id_member)
);

CREATE TABLE projet_ae_08.offers
(
    id_offer          SERIAL PRIMARY KEY,
    date_put_on_offer DATE,
    id_object         integer references projet_ae_08.objects (id_object)
);

CREATE TABLE projet_ae_08.interests
(
    id_interest       SERIAL PRIMARY KEY,
    time_slots        varchar(240),
    discussion_needed boolean,
    id_object         integer references projet_ae_08.objects (id_object),
    id_member         integer references projet_ae_08.members (id_member)
);


--INSERT TYPES-------------------------------------------
INSERT INTO projet_ae_08.types
VALUES (default, 'Accessoires pour animaux domestiques');

INSERT INTO projet_ae_08.types
VALUES (default, 'Accessoires pour voiture');

INSERT INTO projet_ae_08.types
VALUES (default, 'Décoration');

INSERT INTO projet_ae_08.types
VALUES (default, 'Jouets');

INSERT INTO projet_ae_08.types
VALUES (default, 'Literie');

INSERT INTO projet_ae_08.types
VALUES (default, 'Matériel de cuisine');

INSERT INTO projet_ae_08.types
VALUES (default, 'Matériel de jardinage');

INSERT INTO projet_ae_08.types
VALUES (default, 'Meuble');

INSERT INTO projet_ae_08.types
VALUES (default, 'Plantes');

INSERT INTO projet_ae_08.types
VALUES (default, 'Produits cosmétiques');

INSERT INTO projet_ae_08.types
VALUES (default, 'Vélo, trottinette');

INSERT INTO projet_ae_08.types
VALUES (default, 'Vêtements');


---ADRESSS
Insert INTO projet_ae_08.addresses
VALUES (Default, 'Rue de l’Eglise', 11, 'B1', 4987, 'Stoumont');

Insert INTO projet_ae_08.addresses
VALUES (Default, 'Rue de Renkin', 7, null, 4800, 'Verviers');

Insert INTO projet_ae_08.addresses
VALUES (Default, 'Rue Haute Folie', 6, 'A103', 4800, 'Verviers');

Insert INTO projet_ae_08.addresses
VALUES (Default, 'Haut-Vinâve', 13, null, 4845, 'Jalhay');

Insert INTO projet_ae_08.addresses
VALUES (Default, 'Rue de Renkin', 7, null, 4800, 'Verviers');

Insert INTO projet_ae_08.addresses
VALUES (Default, 'Rue de Verviers', 47, null, 4000, 'Liège');

Insert INTO projet_ae_08.addresses
VALUES (Default, 'Rue du salpêtré', 789, 'Bis', 1040, 'Bruxelles');

Insert INTO projet_ae_08.addresses
VALUES (Default, 'Rue des Minières', 45, 'Ter', 4800, 'Verviers');


--INSERT MEMBER---------------------------------------------
INSERT INTO projet_ae_08.members
VALUES (DEFAULT, 'caro', 'Line', 'Caroline', true, null,
        '$2a$10$ZLgiQNTy70mv0vePGP2XROh6kQFwDQxLVM.B/LoEMNAWGWbZiRV76',
        null, 'valid', 0, 2, 1);

INSERT INTO projet_ae_08.members
VALUES (DEFAULT, 'achil', 'Ile', 'Achille', false, null,
        '$2a$10$ZLgiQNTy70mv0vePGP2XROh6kQFwDQxLVM.B/LoEMNAWGWbZiRV76',
        'L application n est pas encore ouverte a tous', 'denied', 0, 2, 2);

INSERT INTO projet_ae_08.members
VALUES (DEFAULT, 'bazz', 'Ile', 'Basile', false, null,
        '$2a$10$ZLgiQNTy70mv0vePGP2XROh6kQFwDQxLVM.B/LoEMNAWGWbZiRV76',
        null, 'valid', 0, 2, 3);

INSERT INTO projet_ae_08.members
VALUES (DEFAULT, 'bri', 'Lehmann', 'Brigitte', true, null,
        '$2a$10$ZjMDNam1xkSpg9i8ealQTOxAWDGmMry9hNQcwpKdwSNKY6nyEZis6',
        null, 'valid', 0, 2, 4);

INSERT INTO projet_ae_08.members
VALUES (DEFAULT, 'theo', 'Ile', 'Théophile', false, null,
        '$2a$10$D78C135sXq7KiB0izO.6f.f/kNwHz88bcfKLFc8t8/G/D4.NeR.fO',
        null, 'valid', 0, 2, 5);

INSERT INTO projet_ae_08.members
VALUES (DEFAULT, 'emi', 'Ile', 'Emile', false, null,
        '$2a$10$UGUsPsOrpKTCP2HY48yss..9ANptFq3Eff8wxohVMuqp1mROd699K',
        'L application n est pas encore ouverte a tous', 'denied', 0, 2, 6);

INSERT INTO projet_ae_08.members
VALUES (DEFAULT, 'cora', 'Line', 'Coralie', false, null,
        '$2a$10$WNUtWMU3U9vnUTGsVjrB2.VfC/AH7AUKNKWEuySp2W1rPkusCOEJq',
        'Vous devez encore attendre quelque jours.', 'denied', 0, 2, 7);

INSERT INTO projet_ae_08.members
VALUES (DEFAULT, 'charline', 'Line', 'Charles', false, null,
        '$2a$10$YNnqMAuQvBMZJ88368wCu.KmYg3sSgeULPcdXtr8vKeeWKjYykyB.',
        'inscription en attente', 'waiting', 0, 1, 8);

--INSERT OBJECT---------------------------------------------
INSERT INTO projet_ae_08.objects
VALUES (Default, 'Décorations de Noël', 'Décorations de Noël de couleur rouge',
        'christmas-1869533_640.png', 'Mardi de 17h à 22h',
        0, 'Annulé', 2, 3, 3, null);

INSERT INTO projet_ae_08.objects
VALUES (Default, 'Cadre', 'Cadre représentant un chien noir sur un fond noir',
        'dog-4118585_640.jpg', 'Lundi de 18h à 22h',
        0, 'Offert', 1, 3, 3, null);

INSERT INTO projet_ae_08.objects
VALUES (Default, 'bureau d’écolier', 'Ancien bureau d’écolier.',
        'BureauEcolier-7.JPG', 'Tous les jours de 15h à 18h',
        2, 'Offert', 1, 8, 4, null);


INSERT INTO projet_ae_08.objects
VALUES (Default, 'Brouette a 2 roues',
        'Brouette à deux roues à l’avant. Améliore la stabilité et ne fatigue pas le dos.',
        'wheelbarrows-4566619_640.jpg', 'Tous les matins avant 11h30',
        3, 'Offert', 1, 7, 5, null);

INSERT INTO projet_ae_08.objects
VALUES (Default, 'Scie sur perche', 'Scie sur perche Gardena',
        'R', 'Tous les matins avant 11h30',
        0, 'Offert', 1, 7, 5, null);

INSERT INTO projet_ae_08.objects
VALUES (Default, 'Table jardin', 'Table jardin et deux chaises en bois',
        'Table-jardin.jpg', 'En semaine, de 20h à 21h',
        0, 'Offert', 1, 8, 5, null);

INSERT INTO projet_ae_08.objects
VALUES (Default, 'Table bistro', 'Table bistro',
        'table-bistro.jpg', 'Lundi de 18h à 20h',
        0, 'Offert', 1, 8, 5, null);

INSERT INTO projet_ae_08.objects
VALUES (Default, 'Table bistro', 'Table bistro ancienne de couleur bleue',
        'table-bistro-carree-bleue.jpg', 'Samedi en journée',
        2, 'Offert', 1, 8, 1, null);

INSERT INTO projet_ae_08.objects
VALUES (Default, 'Tableau noir', 'Tableau noir pour enfant',
        'tableau.jpg', 'Lundi de 18h à 20h',
        1, 'Réservé', 2, 4, 5, 1);

INSERT INTO projet_ae_08.objects
VALUES (Default, 'Cadre cottage', 'Cadre cottage naïf',
        'cadre-cottage-1178704_640.jpg', 'Lundi de 18h30 à 20h',
        3, 'Offert', 1, 3, 5, null);

INSERT INTO projet_ae_08.objects
VALUES (Default, 'Tasse de couleur', 'Tasse de couleur claire rose & mauve',
        'tasse-garden-5037113_640.jpg', 'Lundi de 18h30 à 20h',
        2, 'Offert', 1, 6, 5, null);

INSERT INTO projet_ae_08.objects
VALUES (Default, 'Pâquerettes', 'Pâquerettes dans pots rustiques',
        'pots-daisy-181905_640.jpg', 'Lundi de 16h à 17h',
        1, 'Réservé', 2, 9, 1, 3);

INSERT INTO projet_ae_08.objects
VALUES (Default, 'Pots en grès', 'Pots en grès pour petites plantes',
        'pots-plants-6520443_640.jpg', 'Lundi de 16h à 17h',
        0, 'Réservé', 1, 9, 1, null);


--INSERT OFFER-------------------------------------------------
INSERT INTO projet_ae_08.offers
VALUES (DEFAULT, '21-03-22', 1);

INSERT INTO projet_ae_08.offers
VALUES (DEFAULT, '25-03-22', 2);

INSERT INTO projet_ae_08.offers
VALUES (DEFAULT, '25-03-22', 3);

INSERT INTO projet_ae_08.offers
VALUES (DEFAULT, '28-03-22', 4);

INSERT INTO projet_ae_08.offers
VALUES (DEFAULT, '28-03-22', 5);

INSERT INTO projet_ae_08.offers
VALUES (DEFAULT, '29-03-22', 6);

INSERT INTO projet_ae_08.offers
VALUES (DEFAULT, '30-03-22', 7);

INSERT INTO projet_ae_08.offers
VALUES (DEFAULT, '14-04-22', 8);

INSERT INTO projet_ae_08.offers
VALUES (DEFAULT, '14-04-22', 9);

INSERT INTO projet_ae_08.offers
VALUES (DEFAULT, '21-04-22', 10);

INSERT INTO projet_ae_08.offers
VALUES (DEFAULT, '21-04-22', 11);

INSERT INTO projet_ae_08.offers
VALUES (DEFAULT, '21-04-22', 12);

INSERT INTO projet_ae_08.offers
VALUES (DEFAULT, '21-04-22', 13);

--INTERET

INSERT INTO projet_ae_08.interests
VALUES (DEFAULT, '16 mai', false, 3, 5);

INSERT INTO projet_ae_08.interests
VALUES (DEFAULT, '17 mai', false, 3, 3);

INSERT INTO projet_ae_08.interests
VALUES (DEFAULT, '12 mai', false, 4, 3);

INSERT INTO projet_ae_08.interests
VALUES (DEFAULT, '12 mai', false, 4, 1);

INSERT INTO projet_ae_08.interests
VALUES (DEFAULT, '12 mai', false, 4, 4);

INSERT INTO projet_ae_08.interests
VALUES (DEFAULT, '14 mai', false, 8, 4);

INSERT INTO projet_ae_08.interests
VALUES (DEFAULT, '14 mai', false, 8, 5);

INSERT INTO projet_ae_08.interests
VALUES (DEFAULT, '16 mai', false, 9, 1);

INSERT INTO projet_ae_08.interests
VALUES (DEFAULT, '12 mai', false, 10, 1);

INSERT INTO projet_ae_08.interests
VALUES (DEFAULT, '12 mai', false, 10, 4);

INSERT INTO projet_ae_08.interests
VALUES (DEFAULT, '12 mai', false, 10, 3);

INSERT INTO projet_ae_08.interests
VALUES (DEFAULT, '16 mai', false, 11, 3);

INSERT INTO projet_ae_08.interests
VALUES (DEFAULT, '16 mai', false, 11, 1);

INSERT INTO projet_ae_08.interests
VALUES (DEFAULT, '16 mai', false, 12, 3);

--1
SELECT m.id_member, m.username, m.is_admin, m.condition, m.reason_for_connection_refusal
FROM projet_ae_08.members m
ORDER BY m.condition, m.is_admin;
--2
SELECT ob.id_object, ob.description, t.wording_type, ob.state, of.date_put_on_offer
FROM projet_ae_08.objects ob,
     projet_ae_08.offers of, projet_ae_08.types t
WHERE ob.type = t.id_type
  AND ob.id_object = of.id_object
  AND of.date_put_on_offer = ( SELECT min (date_put_on_offer) FROM projet_ae_08.offers WHERE id_object = ob.id_object )
ORDER BY of.date_put_on_offer;
--3
SELECT m.last_name, COUNT(o.id_object)
FROM projet_ae_08.members m,
     projet_ae_08.objects o
WHERE m.id_member = o.id_offeror
group by m.last_name;
--4
SELECT m.username, o.description, o.type, o.state
FROM projet_ae_08.members m,
     projet_ae_08.objects o
WHERE o.id_recipient = m.id_member;

--5
SELECT COUNT(o.id_object), o.state
FROM projet_ae_08.objects o
group by o.state