-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 28, 2017 at 11:45 PM
-- Server version: 5.7.18-0ubuntu0.16.04.1
-- PHP Version: 7.0.15-0ubuntu0.16.04.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `grocery_ordering_system`
--

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

CREATE TABLE `account` (
  `id` int(11) NOT NULL,
  `type_account_id` int(11) NOT NULL,
  `user_name` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

--
-- Dumping data for table `account`
--

INSERT INTO `account` (`id`, `type_account_id`, `user_name`, `password`) VALUES
(1, 1, 'root', 'password'),
(2, 2, 'bohao', 'password');

-- --------------------------------------------------------

--
-- Table structure for table `address`
--

CREATE TABLE `address` (
  `id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `address_line_1` varchar(50) NOT NULL,
  `address_line_2` varchar(50) NOT NULL,
  `city` varchar(20) NOT NULL,
  `country` varchar(20) NOT NULL,
  `zip_code` varchar(10) NOT NULL,
  `address_complement` varchar(50) NOT NULL,
  `is_address_by_default` char(1) NOT NULL,
  `active` char(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

--
-- Dumping data for table `address`
--

INSERT INTO `address` (`id`, `customer_id`, `address_line_1`, `address_line_2`, `city`, `country`, `zip_code`, `address_complement`, `is_address_by_default`, `active`) VALUES
(1, 1, '1 Rue Dauphin', '2eme etage, porte gauche', 'Villejuif', 'France', '94800', 'enter code: 94A01', 'Y', 'Y'),
(2, 1, '47 Rue d\'Hellieule', 'Logement 34', 'Saint-Die-des-Vosges', 'France', '88100', 'enter code: 75844', 'N', 'Y'),
(3, 2, '1907 Dogwood Lane', 'room 308', 'Tucson', 'US', '79844', 'enter code: 36078', 'Y', 'Y'),
(4, 3, '3449 Myra Street', 'room 855', 'EAST CANDIA', 'US', '09876', 'enter code: 34256', 'Y', 'Y');

-- --------------------------------------------------------

--
-- Table structure for table `bought_item`
--

CREATE TABLE `bought_item` (
  `id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `weight` float NOT NULL,
  `price` float NOT NULL,
  `date_bought` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

--
-- Dumping data for table `bought_item`
--

INSERT INTO `bought_item` (`id`, `customer_id`, `product_id`, `quantity`, `weight`, `price`, `date_bought`) VALUES
(1, 1, 1, 1, 100, 8.0535, '2017-05-27'),
(2, 1, 2, 2, 6400, 32.151, '2017-05-27'),
(3, 1, 3, 3, 9000, 84.2625, '2017-05-27');

-- --------------------------------------------------------

--
-- Table structure for table `cart`
--

CREATE TABLE `cart` (
  `id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `price_of_products` float NOT NULL,
  `number_of_items` tinyint(5) NOT NULL,
  `weight` float NOT NULL,
  `active` char(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

-- --------------------------------------------------------

--
-- Table structure for table `cart_product`
--

CREATE TABLE `cart_product` (
  `id` int(11) NOT NULL,
  `cart_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` tinyint(5) NOT NULL,
  `weight` float NOT NULL,
  `price_of_products` float NOT NULL,
  `active` char(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE `category` (
  `id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL,
  `description` varchar(255) NOT NULL,
  `image_url` varchar(50) NOT NULL,
  `active` char(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`id`, `name`, `description`, `image_url`, `active`) VALUES
(1, 'Beverages', 'Coffee/tea, juice, soda', '', 'Y'),
(2, 'Bread/Bakery', 'Sandwich loaves, dinner rolls, tortillas, bagels', '', 'Y'),
(3, 'Canned/Jarred Goods', 'Vegetables, spaghetti sauce, ketchup', '', 'Y'),
(4, 'Dairy', 'Cheeses, eggs, milk, yogurt, butter', '', 'Y'),
(5, 'Dry/Baking Goods', 'Cereals, flour, sugar, pasta, mixes', '', 'Y'),
(6, 'Frozen Goods', 'Waffles, vegetables, individual meals, ice cream', '', 'Y'),
(7, 'Meat', 'Lunch meat, poultry, beef, pork', '', 'Y'),
(8, 'Produce', 'Fruits, vegetables', '', 'Y');

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `id` int(11) NOT NULL,
  `user_name` varchar(20) NOT NULL,
  `first_name` varchar(20) NOT NULL,
  `last_name` varchar(20) NOT NULL,
  `email_address` varchar(50) NOT NULL,
  `phone_number` varchar(20) NOT NULL,
  `has_address` char(1) NOT NULL,
  `has_shopping_list` char(1) NOT NULL,
  `has_order` char(1) NOT NULL,
  `active` char(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`id`, `user_name`, `first_name`, `last_name`, `email_address`, `phone_number`, `has_address`, `has_shopping_list`, `has_order`, `active`) VALUES
(1, 'bohao', 'LI', 'Bohao', 'bohao.li.20160103@efrei.net', '0033661498769', 'Y', 'N', 'Y', 'Y'),
(2, 'antony', 'Liebin', 'Antony', 'antony.liebin.20160104@efrei.net', '0033661498770', 'Y', 'N', 'N', 'Y'),
(3, 'lucas', 'Nogas', 'Lucas', 'lucas.nogas.20160105@efrei.net', '0033661498771', 'Y', 'N', 'N', 'Y');

-- --------------------------------------------------------

--
-- Table structure for table `order`
--

CREATE TABLE `order` (
  `id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `order_status_id` int(11) NOT NULL,
  `date_placed` date NOT NULL,
  `number_of_items` tinyint(5) NOT NULL,
  `weight` float NOT NULL,
  `comments` varchar(255) NOT NULL,
  `price_of_products` float NOT NULL,
  `price_of_shipping` float NOT NULL,
  `price_in_total` float NOT NULL,
  `active` char(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

--
-- Dumping data for table `order`
--

INSERT INTO `order` (`id`, `customer_id`, `order_status_id`, `date_placed`, `number_of_items`, `weight`, `comments`, `price_of_products`, `price_of_shipping`, `price_in_total`, `active`) VALUES
(2, 1, 2, '2017-05-28', 3, 15500, 'the comments of the order', 118.54, 15.483, 134.023, 'Y');

-- --------------------------------------------------------

--
-- Table structure for table `order_product`
--

CREATE TABLE `order_product` (
  `id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` tinyint(5) NOT NULL,
  `weight` float NOT NULL,
  `price_of_products` float NOT NULL,
  `price_of_shipping` float NOT NULL,
  `price_in_total` float NOT NULL,
  `active` char(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

--
-- Dumping data for table `order_product`
--

INSERT INTO `order_product` (`id`, `order_id`, `product_id`, `quantity`, `weight`, `price_of_products`, `price_of_shipping`, `price_in_total`, `active`) VALUES
(1, 2, 1, 1, 100, 7.67, 0.3835, 8.0535, 'Y'),
(2, 2, 2, 2, 6400, 30.62, 3.062, 33.682, 'Y'),
(3, 2, 3, 3, 9000, 80.25, 12.0375, 92.2875, 'Y');

-- --------------------------------------------------------

--
-- Table structure for table `order_status`
--

CREATE TABLE `order_status` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `description` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

--
-- Dumping data for table `order_status`
--

INSERT INTO `order_status` (`id`, `name`, `description`) VALUES
(1, 'to_be_confirmed', 'The user hasn\'t confirmed the order yet.'),
(2, 'confirmed_by_user', 'The order has been confirmed by the user.');

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `id` int(11) NOT NULL,
  `store_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `short_description` varchar(255) NOT NULL,
  `long_description` varchar(2048) NOT NULL,
  `small_image_url` varchar(50) NOT NULL,
  `big_image_url` varchar(50) NOT NULL,
  `weight` float NOT NULL,
  `price` float NOT NULL,
  `active` char(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`id`, `store_id`, `category_id`, `name`, `short_description`, `long_description`, `small_image_url`, `big_image_url`, `weight`, `price`, `active`) VALUES
(1, 1, 1, 'Organic Chicory Root Powder Drink 2 x 100g jars', 'Naturally caffeine and gluten free\r\nContains inulin - a natural pre-biotic\r\nSmooth roasted flavour. Great alternative to coffee.', 'General Information\r\nProduct Dimensions	18.8 x 16.8 x 8.8 cm\r\nManufacturer reference	FPW3250\r\nProduct Name	beverages\r\nStorage Instructions	Always use a clean dry spoon\r\nCountry of origin	France\r\nBrand	Prewetts\r\nCuisine	English\r\nSpeciality	Organic\r\nCertification	FR:BIO-001\r\nCaffeine content	Caffeine Free\r\nManufacturer/Producer	Prewetts Health Foods Ltd\r\nExpand\r\nNutrition Facts\r\nServing Size	100 Grams\r\nEnergy (kJ)	1520.00 kJ\r\nEnergy (kcal)	363.28 kcal\r\nFat	0.2 Grams\r\nof which:	\r\n- Saturates	0.1 Grams\r\nCarbohydrate	84.9 Grams\r\nof which:	\r\n- Sugars	21.4 Grams\r\nFibre	4.5 Grams\r\nProtein	4.1 Grams\r\nSodium	0.2 Grams', '/resources/image/product/1_small.jpg', '/resources/image/product/1_big.jpg', 100, 7.67, 'Y'),
(2, 1, 2, 'Everfresh Sprouted Wheat Organic Bread 400 g (Pack of 8)', 'Baked with benefits of whole grain\r\nMade naturally\r\nLess than 5 percent fat\r\nPure way of eating\r\nHighly nutritious', 'General Information\r\nProduct Dimensions	31.4 x 16.4 x 15 cm\r\nItem model number	OG-SWB\r\nAdditives	Free From Yeast\r\nWeight	3.2 Kilograms\r\nUnits	8 each\r\nCountry of origin	United Kingdom\r\nBrand	Everfresh\r\nSpeciality	Organic, No Added Sugar\r\nCertification	Organic as per directive 834/2007.\r\nPackage Information	Film Wrap\r\nManufacturer/Producer	Everfresh\r\nExpand\r\nNutrition Facts\r\nServing Size	100 Grams\r\nEnergy (kJ)	783 kJ\r\nEnergy (kcal)	187.137 kcal\r\nFat	0.8 Grams\r\nCarbohydrate	47 Grams\r\nProtein	10.3 Grams', '/resources/image/product/2_small.jpg', '/resources/image/product/2_big.jpg', 3200, 15.31, 'Y'),
(3, 1, 3, 'Plumrose Pork Luncheon Meat 12x250g', 'Pack of 12\r\n200g Each', 'General Information\nProduct Dimensions	31 x 19.6 x 7.4 cm\nProduct Name	food\nWeight	1 Grams\nBrand	Plumrose\nCuisine	English\nSpeciality	Alcohol Free\nManufacturer/Producer	Tulip Ltd', '/resources/image/product/3_small.jpg', '/resources/image/product/3_big.jpg', 3000, 26.75, 'Y'),
(4, 1, 4, 'Candia UHT Semi-Skimmed Milk, 6 x 1L (4 Packs)', 'Candia UHT Semi-Skimmed Milk, 6 x 1L (4 Packs.', 'General Information\r\nManufacturer reference	C342928\r\nBrand	Candia\r\nManufacturer/Producer	Candia', '/resources/image/product/4_small.jpg', '/resources/image/product/4_big.jpg', 6000, 24.42, 'Y'),
(5, 1, 5, 'Kellogg\'s Rice Krispies Multigrain, 350g', 'Vitamin D with wholegrain', 'General Information\r\nProduct Dimensions	6.9 x 19.2 x 30 cm\r\nItem model number	59677\r\nWeight	350 Grams\r\nStorage Instructions	Ambient\r\nUse By Recommendation	Store in a cool dry place.\r\nManufacturer contact	Kellogg Marketing and Sales Company (UK) Limited, Manchester, M16 0PU.\r\nBrand	Kellogg\'s\r\nCuisine	English\r\nSpeciality	Kosher, Halal, Suitable for Vegetarians\r\nManufacturer/Producer	Kellogg\'s\r\nExpand\r\nNutrition Facts\r\nServing Size	100 Grams\r\nEnergy (kJ)	1602 kJ\r\nEnergy (kcal)	382.878 kcal\r\nFat	2.5 Grams\r\nof which:	\r\n- Saturates	0.5 Grams\r\nCarbohydrate	77 Grams\r\nof which:	\r\n- Sugars	21 Grams\r\nFibre	8 Grams\r\nProtein	8 Grams', '/resources/image/product/5_small.jpg', '/resources/image/product/5_big.jpg', 350, 7.76, 'Y'),
(6, 1, 6, '[10 pack] 2 Compartment BPA Free Meal Prep Containers. Reusable Plastic Food Containers with Lids. Stackable, Microwavable, Freezer & Dishwasher Safe Bento Lunch Box Set + EBook [800mL]', '\r\nBrand	Emerald Living\r\nColour	Black\r\nItem Weight	699 g\r\nProduct Dimensions	15 x 22 x 6 cm\r\nMaterial	Plastic\r\nAuto Shutoff	No', '★ LIFETIME GUARANTEE - UNMIXED FRESH FOOD STORAGE TO GO OR YOUR MONEY BACK! ★ Guarantee ONLY available from brand owner Emerald Commerce. Risk free buy. Your happiness is our top priority. If for any reason you\'re unhappy with our meal containers we will give you a no questions asked refund or replacement item as preferred.\r\n★ SAFE TO USE AGAIN & AGAIN! ★ Our Meal prep containers are reusable & certified BPA free so you can be meal prepping time & again. Microwavable & dishwasher safe, they\'re also fine freezer containers as they withstand cold temperatures for frozen food storage. They can serve for example as freeze dried food tubs, ice cream freezer or frozen meal containers. Their plastic food container bases are from eco-friendly polypropylene plastic making them recyclable once they become disposable.\r\n★ EASIEST FOR YOU TO OPEN ★ Our Bento box set lids have tabs on each corner. Open using any corner - handy for your kids & elderly adults. This black rectangle bento box set is easier for you to open than 1 tab and tabless food prep containers. Firm lids make these food storage containers airtight to keep your food fresh. They store great in work and meal prep bags so you can take these healthy lunch box containers on the go! Children also love these as kids lunch containers for school\r\n★ GRAB YOUR BONUS MEAL PREP E-BOOK WORTH £9.99 ★ Add to cart now for your valuable free food prep e-book! Great meal prep ideas & tips for all with resources including meal plan calendar and shopping lists! The best meal plan set to support your bodybuilding, weight loss diet & fitness gym plan. Use our food trays for weekly meal prep & slim in style! These plastic storage containers don\'t slim your precious kitchen/fridge-freezer space however, as they easily stack together when not in use.', '/resources/image/product/6_small.jpg', '/resources/image/product/6_big.jpg', 699, 12.99, 'Y'),
(7, 1, 7, 'Gluten Free Extra Large Meat Box\r\n', 'General Information\r\nBrand	Bearfaced Groceries\r\nManufacturer/Producer	Bearfaced Groceries', '2× 2 Pack Farm Fresh Chicken Breast Fillets (200g each) 1× 2 of Fresh Whole Chicken 2× 500g Gluten-free Sausages 2× 250g Dry Cured Back Bacon - Unsmoked 1× 500g Farm Fresh Chicken Thighs 1× 500g Farm Fresh Chicken Drumsticks\r\n2× 500g Grass Fed 28 Day Aged Beef Diced Stewing Steak 2× 2 Pack Pork Loin Steak (180g each, pack of 2) 1× 1kg Grass Fed 28 Day Aged Beef Brisket Joint Roasting\r\n2× 400g Grass Fed Beef Mince (5% Lean) 1× 4 Pack Gluten Free Grass Fed Beef Burgers (4oz each)', '/resources/image/product/7_small.jpg', '/resources/image/product/7_big.jpg', 10000, 70, 'Y'),
(8, 1, 8, 'The Ultimate Fruit and Veg Box', 'Brand	Bearfaced Groceries\r\nManufacturer/Producer	Bearfaced Groceries', '1× 4 Pack Jacket Potatoes 1× 3 Pack Sweet Potatoes 1× 2 Pack XL White Onions 1× 200g Kale Green 1× 1 head Broccoli 1× 1 Pack Red Cabbage 1× 1kg (approx 4 - 5) Parsnips 1× Bag (approx 300g) White Mushrooms 1× 1 Pack Swede 1× 1 Pack Cauliflower 1× 2 Pack Courgettes 1× 200g Baby Spinach 1× 1 Pack Aubergines 1× 3 Pack Beef Tomatoes 1× 1 Pack Butternut Squash 1× 2 Pack Garlic Bulbs 2× Bag (approx 750g) Carrots 3× 1 of Leeks', '/resources/image/product/8_small.jpg', '/resources/image/product/8_big.jpg', 7856, 25, 'Y'),
(9, 2, 1, 'Fanta Grape Soda Can 355 ml (Pack of 12)', 'Great tasting soda\r\nArtificially flavoured\r\nCaffeine free\r\nVery low sodium', 'Expand\r\nGeneral Information\r\nProduct Dimensions	39.8 x 13.2 x 12.6 cm\r\nManufacturer reference	0032\r\nProduct Name	Beverages\r\nAdditives	May Contain Additives, May Contain Artificial Flavours, May Contain Artificial Preservatives, May Contain Artificial Sweeteners\r\nVolume	4260 ml\r\nUnits	12 can\r\nCountry of origin	USA\r\nBrand	Fanta\r\nCuisine	American\r\nPackage Information	Can\r\nManufacturer/Producer	Coca‑Cola Enterprises Ltd', '/resources/image/product/9_small.jpg', '/resources/image/product/9_big.jpg', 4840, 14.95, 'Y'),
(10, 2, 2, 'Landbrot - artisan sourdough 800g', 'rye, sourdough, stone baked,', 'Expand\r\nGeneral Information\r\nIngredients	80% rye flour, 20% wheat, natural sourdough, salt, water. No additives, chemicals or preservatives added.\r\nAllergen Information	Contains: Rye\r\nWeight	800 Grams\r\nBrand	Rye plain 800g\r\nCuisine	german\r\nSpeciality	Dairy Free, Lactose Free, No preservatives, Fat Free, No Added Sugar\r\nManufacturer/Producer	The German Bakery of Windsor', '/resources/image/product/10_small.jpg', '/resources/image/product/10_big.jpg', 800, 6.49, 'Y'),
(11, 2, 3, 'FERRERO Nutella Hazelnut Chocolate Spread, 1kg', 'FERRERO Nutella Hazelnut Chocolate Spread, 1kg', 'Expand\r\nGeneral Information\r\nProduct Dimensions	30.8 x 23.6 x 11.8 cm\r\nAllergen Information	Contains: Hazelnuts, Milk\r\nWeight	2 Kilograms\r\nUnits	1000 grams\r\nCountry of origin	France\r\nBrand	Nutella\r\nExpand\r\nNutrition Facts\r\nServing Size	15 Grams\r\nEnergy (kJ)	339 kJ\r\nEnergy (kcal)	81.021 kcal\r\nFat	4.7 Grams\r\nof which:	\r\n- Saturates	1.7 Grams\r\nCarbohydrate	8.6 Grams\r\nof which:	\r\n- Sugars	8.5 Grams\r\nProtein	0.9 Grams\r\nSalt	0.02 Grams', '/resources/image/product/11_small.jpg', '/resources/image/product/11_big.jpg', 1000, 10.79, 'Y'),
(12, 2, 4, 'Gouda Cheese Shop Emmentaler Cheese Swiss | +/- 500 Grams / 1.1 lbs', 'Delicious fresh from the knife Emmentaler Cheese. Emmentaler cheese has a nutty sweet taste.\r\nThe cheese is so delicious to eat for bread or so out of the hand. But Emmentaler with Gruyere cheese is also an ideal combination for a tasty cheese fondue!', 'Expand\r\nGeneral Information\r\nProduct Name	food\r\nIngredients	Fat 40 +, pasteurised cowmilk, rennet, salt, sour, sodium (E251), annatto (E160b), vitamins A, B and D\r\nAllergen Information	Contains: Peanut Free, Gluten Free\r\nStorage Instructions	Refrigerated necessary once parcel is received.\r\nUse By Recommendation	1-2 weeks after opening\r\nCountry of origin	Switzerland\r\nBrand	Emmentaler\r\nFormat	Fresh\r\nPackage Information	Your cheese is freshly cut and vacuum-packaged!\r\nManufacturer/Producer	Gouda Cheese Shop\r\nExpand\r\nNutrition Facts\r\nServing Size	100 Grams\r\nFat	32.5 Grams\r\nof which:	\r\n- Saturates	20.00 Grams\r\n- Poly-unsaturates	12.5 Grams\r\nCarbohydrate	0 Grams\r\nProtein	25.5 Grams\r\nSodium	708.00 milligrams\r\nCalcium	815.00 milligrams (101.88% NRV)', '/resources/image/product/12_small.jpg', '/resources/image/product/12_big.jpg', 500, 17.5, 'Y'),
(13, 2, 5, 'Tala 700g Ceramic Baking Beans', 'Tala Ceramic Baking Beans 700g\r\nTala\r\n700g\r\nQuantity: 1', '\r\nBrand	Tala\r\nModel Number	10A04775\r\nColour	Grey\r\nItem Weight	721 g\r\nProduct Dimensions	8 x 12 x 12 cm\r\nMaterial	Ceramic', '/resources/image/product/13_small.jpg', '/resources/image/product/13_big.jpg', 700, 5.97, 'Y'),
(14, 2, 6, '3 bags (each 400g net) Frozen Raw Shell off Peeled King Prawns', '3 bags (each 400g net) Frozen Raw Shell off Peeled King Prawns', 'Expand\r\nGeneral Information\r\nIngredients	Raw Frozen Peeled King Prawns\r\nStorage Instructions	freeze\r\nDirections	Supplied raw - cook from frozen or defrost. Work great in a curry or stir-fry.\r\nServing Recommendation	Cook from frozen or defrost\r\nBrand	Regal Fish Supplies\r\nFormat	Frozen\r\nSpeciality	High in Protein\r\nManufacturer/Producer	Regal Fish Supplies', '/resources/image/product/14_small.jpg', '/resources/image/product/14_big.jpg', 1200, 45, 'Y'),
(15, 2, 7, 'Butchers Selection Meat Pack Combi Wrap 6', 'General Information\r\nBrand	Hazeldines\r\nManufacturer/Producer	Hazeldines', 'Produce from Hazeldines traditional family butchers\r\nEstablished for over 80 years\r\nAll our packs are shipped chilled in specially insulated containers with ice packs\r\nSuitable for home freezing', '/resources/image/product/15_small.jpg', '/resources/image/product/15_big.jpg', 7894, 63.25, 'Y'),
(16, 2, 8, 'The Fruit Lovers Gift Tray', 'Expand\r\nGeneral Information\r\nManufacturer reference	DS041\r\nWeight	3.8 Kilograms\r\nBrand	First4Hampers\r\nManufacturer/Producer	First4Hampers', 'A high quality wooden tray filled with succulent and colourful fruit. This vivid Fruit Tray includes a juicy melon, vibrant satsumas, and a vitamin packed selection of visually appetizing fruit.\r\nNext Day Delivery on orders placed before 1pm Monday-Friday\r\nWe are unable to offer a Monday Delivery on this item as it contains fresh produce\r\nWe are unable to dispatch this item to Southern Ireland', '/resources/image/product/16_small.jpg', '/resources/image/product/16_big.jpg', 3800, 29, 'Y'),
(17, 3, 1, 'Coca Cola 72 cans', 'value pack of 72 cans.', 'General Information\r\nWeight	2.5 Kilograms\r\nVolume	23.76 litres\r\nBrand	Coca Cola', '/resources/image/product/17_small.jpg', '/resources/image/product/17_big.jpg', 2500, 36.4, 'Y'),
(18, 3, 2, 'Dunn\'s Traditional Short Crust Mince Pies (Box of 8)', 'Delicious Luxury Mince Pies from Dunn\'s Bakery - Craft Bakers Since 1820\r\nHandmade in our Craft Bakery using the highest quality ingredients\r\nMade with our own traditional sweet mince meat, a recipe past down through the generations', 'General Information\r\nProduct Name	food\r\nIngredients	Mincemeat: California Raisins, Currants, Mixed Peel, Dark Brown Sugar, Apple, Vegetable Suet, Lemon Pulp, Lemon Zest, Malt Vinegar, Brandy, Mixed Spice, Nutmeg, Salt. Shortcrust Pastry: Wheat Flour, Butter, Sugar, Egg.\r\nStorage Instructions	Store in a cool dry place.\r\nBrand	Dunn\'s Bakery\r\nCuisine	English\r\nSpeciality	Suitable for Vegetarians, No artificial colors, No artificial flavour\r\nManufacturer/Producer	Dunn\'s Bakery', '/resources/image/product/18_small.jpg', '/resources/image/product/18_big.jpg', 100, 7.99, 'Y'),
(19, 3, 3, 'Libby\'s Pumpkin Pie Filling 425 g (Pack of 3)\r\nClick to open expanded view\r\nLibby\'s Pumpkin Pie Filling 425 g (Pack of 3)', '100% pure pumpkin\r\nLibby\'s invented their own strain of pumpkin, called the libby\'s select dickinson pumpkin, which they use for canning\r\nAmerican import', 'Expand\r\nGeneral Information\r\nProduct Dimensions	22.6 x 12 x 7 cm\r\nWeight	1.3 Kilograms\r\nUse By Recommendation	Store in a cool, dry place\r\nCountry of origin	USA\r\nBrand	Libby\'s\r\nCuisine	American\r\nAge Range Description	Adult\r\nManufacturer/Producer	Nestle Usa\r\nExpand\r\nNutrition Facts\r\nServing Size	100 Grams\r\nFat	11 Grams\r\nof which:	\r\n- Saturates	5 Grams\r\nCarbohydrate	40 Grams\r\nof which:	\r\n- Sugars	25 Grams\r\nFibre	2 Grams\r\nProtein	6 Grams\r\nSodium	350 milligrams', '/resources/image/product/19_small.jpg', '/resources/image/product/19_big.jpg', 1300, 6.25, 'Y'),
(20, 3, 4, '', 'Anchor Butter Individual Portions Size 7 x 100 Portions\r\nNext Day Delivery - Mainland UK', 'General Information\r\nManufacturer reference	252201\r\nAllergen Information	Contains: Milk\r\nSpeciality	Suitable for Vegetarians\r\nManufacturer/Producer	Anchor', '/resources/image/product/20_small.jpg', '/resources/image/product/20_big.jpg', 700, 14.95, 'Y'),
(21, 3, 5, 'PINK SUN Bicarbonate of Soda 1kg - Edible Food Grade Baking Soda 1000g - Bulk Buy', 'Food grade baking soda 1kg\r\nGluten free, soy free, aluminium free\r\nUse with home made biscuit and cake recipes\r\nSuitable for home made personal health care products\r\nHigh quality large bag', '\r\nGeneral Information\r\nProduct Dimensions	8 x 15 x 20 cm\r\nManufacturer reference	BCS1000\r\nIngredients	Sodium bicarbonate.\r\nWeight	1 Kilograms\r\nSolid Net Weight	1 Kilograms\r\nStorage Instructions	Store airsealed out of direct sunlight.\r\nManufacturer contact	www.pinksun.co.uk\r\nBrand	PINK SUN\r\nCuisine	English\r\nSpeciality	Gluten Free, No preservatives, Suitable for Vegans, Suitable for Vegetarians, Low Carb\r\nPackage Information	Sleeve\r\nManufacturer/Producer	PINK SUN Ltd\r\nExpand\r\nNutrition Facts\r\nServing Size	100 Grams\r\nEnergy (kJ)	0 kJ\r\nEnergy (kcal)	0 kcal\r\nFat	0 Grams\r\nof which:	\r\n- Saturates	0 Grams\r\nCarbohydrate	0 Grams\r\nof which:	\r\n- Sugars	0 Grams\r\nFibre	0 Grams\r\nProtein	0 Grams\r\nSodium	28 Grams', '/resources/image/product/21_small.jpg', '/resources/image/product/21_big.jpg', 1000, 5.95, 'Y'),
(22, 3, 6, 'Tilapia Fillets Frozen 1kg Seafood Fish', 'Skinless and boneless\r\nEach fillet weighs 85-140g\r\n5-7 fillets per pack\r\nFirm white flesh\r\nNext day delivery available, See Description For Details', 'Expand\r\nGeneral Information\r\nAllergen Information	Contains: Fish\r\nStorage Instructions	Store on or below -18C. Once defrosted store in the fridge and do not re-freeze\r\nCountry of origin	China\r\nBrand	Ocean Classic\r\nSpeciality	Low Fat\r\nManufacturer/Producer	China\r\nExpand\r\nNutrition Facts\r\nServing Size	100 Grams\r\nEnergy (kJ)	586.00 kJ\r\nEnergy (kcal)	140.054 kcal\r\nFat	5.00 Grams\r\nof which:	\r\n- Saturates	1.00 Grams\r\n- Mono-unsaturates	0 Grams\r\n- Poly-unsaturates	0 Grams\r\nCarbohydrate	0 Grams\r\nof which:	\r\n- Sugars	0 Grams\r\nFibre	0 Grams\r\nProtein	16.00 Grams', '/resources/image/product/22_small.jpg', '/resources/image/product/22_big.jpg', 623, 16.55, 'Y'),
(23, 3, 7, 'Mountain\'s Boston BBQ Pack - 1kg Boston sausage, 4 x Marinated Chicken Breast Skewers, 4 x 4oz Beefburgers - 4 people', 'Suitable for home freezing.', 'General Information\r\nProduct Name	Food\r\nWeight	1.5 Kilograms\r\nCountry of origin	uk\r\nBrand	Mountain\'s Boston Sausage\r\nCuisine	English\r\nFormat	Fresh\r\nSpeciality	High in Protein\r\nPackage Information	Pack\r\nManufacturer/Producer	Mountain\'s Boston Sausage', '/resources/image/product/23_small.jpg', '/resources/image/product/23_big.jpg', 1500, 14.8, 'Y'),
(24, 3, 8, 'Royal Clear Plastic Mesh Produce Bags, 24", Package of 100', 'MEASUREMENTS AND SIZE: The length of this product is 60.96 cm. Width of this product is 20.32 cm flat, and stretches up to 35.56 cm. When fully stretched, the mesh holes measure approximately .64 cm.', '\r\nBrand	Royal\r\nModel Number	RMB1000CL-IN\r\nColour	Clear\r\nItem Weight	431 g\r\nProduct Dimensions	26.7 x 25.4 x 5.1 cm', '/resources/image/product/24_small.jpg', '/resources/image/product/24_big.jpg', 431, 9.99, 'Y');

-- --------------------------------------------------------

--
-- Table structure for table `shipping`
--

CREATE TABLE `shipping` (
  `id` int(11) NOT NULL,
  `order_product_id` int(11) NOT NULL,
  `shipping_method_id` int(11) NOT NULL,
  `address_id` int(11) NOT NULL,
  `active` char(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

--
-- Dumping data for table `shipping`
--

INSERT INTO `shipping` (`id`, `order_product_id`, `shipping_method_id`, `address_id`, `active`) VALUES
(1, 1, 1, 1, 'Y'),
(2, 2, 2, 1, 'Y'),
(3, 3, 3, 1, 'Y');

-- --------------------------------------------------------

--
-- Table structure for table `shipping_method`
--

CREATE TABLE `shipping_method` (
  `id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL,
  `description` varchar(2048) NOT NULL,
  `percentage` float NOT NULL,
  `shipping_days` int(11) NOT NULL,
  `active` char(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

--
-- Dumping data for table `shipping_method`
--

INSERT INTO `shipping_method` (`id`, `name`, `description`, `percentage`, `shipping_days`, `active`) VALUES
(1, 'DHL', 'It is a division of German logistics company Deutsche Post DHL. It is the world’s top delivery services company when it comes to sea and air mail. It was founded in the year 1969 and today is the undisputed market leader in its sector.  Its global headquarter is based in the Deutsche Post headquarters in Bonn. It is also provides services in countries such as Iraq, Afghanistan and Burma so it really isn’t surprising that it is in the number one position.', 0.05, 3, 'Y'),
(2, 'UPS', 'It is commonly referred as UPS and is an American package delivery company with global operations. Headquartered in Sandy Springs, Georgia, United States it was founded in Seattle in the year 1907. It is known to deliver over 15 million packages a day to 6.1 million customers in more than 220 countries around the world. Its courier express service is one of the best in the world.', 0.1, 2, 'Y'),
(3, 'FedEx', 'It is headquartered in Memphis, Tennessee, US and is an American global courier delivery services company. The name FedEx is actually an abbreviation for Federal Express. It was founded in the year 1971 and is today one of the top most courier services in the world. Its major competitor around the world is DHL Express. Frankly both are excellent and the competition between the two only aids the customers.', 0.15, 1, 'Y');

-- --------------------------------------------------------

--
-- Table structure for table `store`
--

CREATE TABLE `store` (
  `id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL,
  `drescription` varchar(255) NOT NULL,
  `image_url` varchar(50) NOT NULL,
  `active` char(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

--
-- Dumping data for table `store`
--

INSERT INTO `store` (`id`, `name`, `drescription`, `image_url`, `active`) VALUES
(1, 'Bohao\'s store', 'Bohao\'s store is a store owend by Bohao. He is a very good guy. He started to sell groceries when he was 18 years old.', '', 'Y'),
(2, 'Antony\'s store', 'Antony\'s grocery store provides you with grocery of very very high quality. Antony is a little bit fat.', '', 'Y'),
(3, 'Thomas\'s store', 'Thomas\'s grocery store is quite a good store. He took over the store from his father when he was 19 years old. Thomas has long hair.', '', 'Y');

-- --------------------------------------------------------

--
-- Table structure for table `type_account`
--

CREATE TABLE `type_account` (
  `id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL,
  `description` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

--
-- Dumping data for table `type_account`
--

INSERT INTO `type_account` (`id`, `name`, `description`) VALUES
(1, 'administrator', 'Super super super user, can do anything he wants.'),
(2, 'customer', 'Customer willing to buy groceries via the grocery ordering system provided by the supermarket chain.');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`id`),
  ADD KEY `type_account_id` (`type_account_id`);

--
-- Indexes for table `address`
--
ALTER TABLE `address`
  ADD PRIMARY KEY (`id`),
  ADD KEY `customer_id` (`customer_id`);

--
-- Indexes for table `bought_item`
--
ALTER TABLE `bought_item`
  ADD PRIMARY KEY (`id`),
  ADD KEY `customer_id` (`customer_id`),
  ADD KEY `product_id` (`product_id`);

--
-- Indexes for table `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`id`),
  ADD KEY `customer_id` (`customer_id`);

--
-- Indexes for table `cart_product`
--
ALTER TABLE `cart_product`
  ADD PRIMARY KEY (`id`),
  ADD KEY `cart_id` (`cart_id`),
  ADD KEY `product_id` (`product_id`);

--
-- Indexes for table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `order`
--
ALTER TABLE `order`
  ADD PRIMARY KEY (`id`),
  ADD KEY `customer_id` (`customer_id`),
  ADD KEY `order_status_id` (`order_status_id`);

--
-- Indexes for table `order_product`
--
ALTER TABLE `order_product`
  ADD PRIMARY KEY (`id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `product_id` (`product_id`);

--
-- Indexes for table `order_status`
--
ALTER TABLE `order_status`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`id`),
  ADD KEY `store_id` (`store_id`),
  ADD KEY `category_id` (`category_id`);

--
-- Indexes for table `shipping`
--
ALTER TABLE `shipping`
  ADD PRIMARY KEY (`id`),
  ADD KEY `order_product_id` (`order_product_id`),
  ADD KEY `shipping_method_id` (`shipping_method_id`),
  ADD KEY `address_id` (`address_id`);

--
-- Indexes for table `shipping_method`
--
ALTER TABLE `shipping_method`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `store`
--
ALTER TABLE `store`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `type_account`
--
ALTER TABLE `type_account`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `account`
--
ALTER TABLE `account`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `address`
--
ALTER TABLE `address`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `bought_item`
--
ALTER TABLE `bought_item`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `cart`
--
ALTER TABLE `cart`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `cart_product`
--
ALTER TABLE `cart_product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;
--
-- AUTO_INCREMENT for table `category`
--
ALTER TABLE `category`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `order`
--
ALTER TABLE `order`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `order_product`
--
ALTER TABLE `order_product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `order_status`
--
ALTER TABLE `order_status`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `product`
--
ALTER TABLE `product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;
--
-- AUTO_INCREMENT for table `shipping`
--
ALTER TABLE `shipping`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `shipping_method`
--
ALTER TABLE `shipping_method`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `store`
--
ALTER TABLE `store`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `type_account`
--
ALTER TABLE `type_account`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `account`
--
ALTER TABLE `account`
  ADD CONSTRAINT `account_ibfk_1` FOREIGN KEY (`type_account_id`) REFERENCES `type_account` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `address`
--
ALTER TABLE `address`
  ADD CONSTRAINT `address_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `bought_item`
--
ALTER TABLE `bought_item`
  ADD CONSTRAINT `bought_item_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `bought_item_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `cart`
--
ALTER TABLE `cart`
  ADD CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `cart_product`
--
ALTER TABLE `cart_product`
  ADD CONSTRAINT `cart_product_ibfk_1` FOREIGN KEY (`cart_id`) REFERENCES `cart` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `cart_product_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `order`
--
ALTER TABLE `order`
  ADD CONSTRAINT `order_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `order_ibfk_2` FOREIGN KEY (`order_status_id`) REFERENCES `order_status` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `order_product`
--
ALTER TABLE `order_product`
  ADD CONSTRAINT `order_product_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `order_product_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `product`
--
ALTER TABLE `product`
  ADD CONSTRAINT `product_ibfk_1` FOREIGN KEY (`store_id`) REFERENCES `store` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `product_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON UPDATE CASCADE;

--
-- Constraints for table `shipping`
--
ALTER TABLE `shipping`
  ADD CONSTRAINT `shipping_ibfk_1` FOREIGN KEY (`order_product_id`) REFERENCES `order_product` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `shipping_ibfk_2` FOREIGN KEY (`shipping_method_id`) REFERENCES `shipping_method` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `shipping_ibfk_3` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`) ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
