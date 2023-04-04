CREATE TABLE `account` (
  `id_account` int NOT NULL AUTO_INCREMENT,
  `ref_account` varchar(250) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `password` varchar(250) NOT NULL,
  `email` varchar(100) NOT NULL,
  `civility` varchar(50) DEFAULT NULL,
  `active` bit(1) NOT NULL DEFAULT 1,
  `reset_password_token` varchar(100) DEFAULT NULL,
  `reset_password_token_creation_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id_account`)
);

CREATE TABLE `role` (
  `id_role` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id_role`)
);

CREATE TABLE `account_role` (
  `id_account` int NOT NULL,
  `id_role` int NOT NULL,
  PRIMARY KEY (`id_account`,`id_role`),
  KEY `id_role` (`id_role`),
  CONSTRAINT `account_role_ibfk_1` FOREIGN KEY (`id_account`) REFERENCES `account` (`id_account`),
  CONSTRAINT `account_role_ibfk_2` FOREIGN KEY (`id_role`) REFERENCES `role` (`id_role`)
);

CREATE TABLE `activation_token` (
  `id_account` int NOT NULL,
  `token` varchar(250) DEFAULT NULL,
  `expiration_date` datetime NOT NULL,
  PRIMARY KEY (`id_account`),
  FOREIGN KEY (`id_account`) REFERENCES account(`id_account`)
);

CREATE TABLE `address` (
  `id_address` int NOT NULL AUTO_INCREMENT,
  `type_address` varchar(50) NOT NULL,
  `street` varchar(50) NOT NULL,
  `city` varchar(50) NOT NULL,
  `postal_code` int NOT NULL,
  `active` bit(1) NOT NULL DEFAULT 1,
  `id_account` INT NOT NULL,
  KEY `id_account` (`id_account`),
  CONSTRAINT `id_address_ibfk_1` FOREIGN KEY (`id_account`) REFERENCES `account` (`id_account`),
  PRIMARY KEY (`id_address`)
);

CREATE TABLE `category` (
  `id_category` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `picture_url` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`id_category`)
);

CREATE TABLE `promotion` (
  `id_promotion` int NOT NULL AUTO_INCREMENT,
  `designation` varchar(250) DEFAULT NULL,
  `promo_start` datetime DEFAULT NULL,
  `promo_end` datetime DEFAULT NULL,
  `discount` float DEFAULT NULL,
  PRIMARY KEY (`id_promotion`)
);

CREATE TABLE `product` (
  `id_product` int NOT NULL AUTO_INCREMENT,
  `ref_product` varchar(100) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `price_ttc` double NOT NULL,
  `product_inventory` int NOT NULL,
  `id_category` int NOT NULL,
  `id_promotion` int DEFAULT NULL,
  `product_description` varchar(350) DEFAULT NULL,
  `present` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id_product`),
  KEY `id_category` (`id_category`),
  KEY `id_promotion` (`id_promotion`),
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`id_category`) REFERENCES `category` (`id_category`),
  CONSTRAINT `promotion_ibfk_1` FOREIGN KEY (`id_promotion`) REFERENCES `promotion` (`id_promotion`)
);

CREATE TABLE `picture` (
  `id_picture` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `url` varchar(300) DEFAULT NULL,
  `id_product` int NOT NULL,
  PRIMARY KEY (`id_picture`),
  KEY `id_product` (`id_product`),
  CONSTRAINT `picture_ibfk_1` FOREIGN KEY (`id_product`) REFERENCES `product` (`id_product`)
);

CREATE TABLE `contact` (
  `id_contact` int NOT NULL,
  `name` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `message` varchar(300) NOT NULL,
  PRIMARY KEY (`id_contact`)
);

CREATE TABLE `delivery_mode` (
  `name` varchar(50) NOT NULL,
  `price` double NOT NULL,
  PRIMARY KEY (`name`)
);

CREATE TABLE `ordered` (
  `id_ordered` int NOT NULL AUTO_INCREMENT,
  `ref_ordered` varchar(250) DEFAULT NULL,
  `ordered_date` date NOT NULL,
  `order_status` varchar(50) NOT NULL,
  `total_price` double DEFAULT 0,
  `delivery_ordered` date NOT NULL,
  `id_address_delivery` int NOT NULL,
  `id_address_invoiced` int NOT NULL,
  `id_account` int NOT NULL,
  PRIMARY KEY (`id_ordered`),
  KEY `id_address_delivery` (`id_address_delivery`),
  KEY `id_address_invoiced` (`id_address_invoiced`),
  KEY `id_account` (`id_account`),
  CONSTRAINT `ordered_ibfk_1` FOREIGN KEY (`id_address_delivery`) REFERENCES `address` (`id_address`),
  CONSTRAINT `ordered_ibfk_2` FOREIGN KEY (`id_address_invoiced`) REFERENCES `address` (`id_address`),
  CONSTRAINT `ordered_ibfk_3` FOREIGN KEY (`id_account`) REFERENCES `account` (`id_account`)
);

CREATE TABLE `order_details` (
  `id_product` int NOT NULL,
  `id_ordered` int NOT NULL,
  `amount` int NOT NULL,
  `price` double NOT NULL,
  PRIMARY KEY (`id_product`,`id_ordered`),
  KEY `id_ordered` (`id_ordered`),
  CONSTRAINT `order_details_ibfk_1` FOREIGN KEY (`id_product`) REFERENCES `product` (`id_product`),
  CONSTRAINT `order_details_ibfk_2` FOREIGN KEY (`id_ordered`) REFERENCES `ordered` (`id_ordered`)
);

CREATE TABLE `invoice` (
  `id_invoice` int NOT NULL AUTO_INCREMENT,
  `ref_invoice` varchar(250) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `billing_date` date NOT NULL,
  `id_ordered` int NOT NULL,
  PRIMARY KEY (`id_invoice`),
  KEY `id_ordered` (`id_ordered`),
  CONSTRAINT `invoice_ibfk_1` FOREIGN KEY (`id_ordered`) REFERENCES `ordered` (`id_ordered`)
);

CREATE TABLE `uuid` (
  `id` int NOT NULL AUTO_INCREMENT,
  `uuid_generate` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);