-- phpMyAdmin SQL Dump
-- version 3.2.0.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jan 29, 2011 at 11:46 AM
-- Server version: 5.1.37
-- PHP Version: 5.3.0

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `webservicerank`
--
CREATE DATABASE IF NOT EXISTS webservicerank;

USE webservicerank;
-- --------------------------------------------------------

--
-- Table structure for table `property`
--

CREATE TABLE IF NOT EXISTS `property` (
  `property_ID` int(11) NOT NULL,
  `property_name` varchar(100) NOT NULL,
  `categorisation` varchar(5) NOT NULL,
  PRIMARY KEY (`property_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `property`
--

INSERT INTO `property` (`property_ID`, `property_name`, `categorisation`) VALUES
(1959224639, 'meter', '00001'),
(1959224735, 'inch', '00001'),
(1959224819, 'feet', '00001'),
(1959224892, 'mile', '00001'),
(1959224982, 'kilometer', '00001'),
(1959225052, 'yard', '00001'),
(1959225119, 'mil', '00001'),
(1959225188, 'rod', '00001'),
(1959225254, 'fathom', '00001'),
(1959448417, 'second', '00001'),
(1959448650, 'hour', '00001'),
(1959448783, 'minute', '00001'),
(1959448901, 'knot', '00001'),
(1959448968, 'mach', '00001'),
(1959622757, 'kilogram', '00001'),
(1959622836, 'ounce', '00001'),
(1959622914, 'pound', '00001'),
(1959622992, 'stone', '00001'),
(1959623070, 'ton', '00001'),
(1959728603, 'conversion', '00001'),
(1959728679, 'unit', '00001'),
(1959728747, 'byte', '00001'),
(1959728826, 'kilobyte', '00001'),
(1959728901, 'megabyte', '00001'),
(1959728975, 'gigabyte', '00001'),
(1960370355, 'squarecentimetre', '00001'),
(1960370431, 'squaremeter', '00001'),
(1960370503, 'squareinch', '00001'),
(1960370572, 'squarefoot', '00001'),
(1960370641, 'squaremile', '00001'),
(1960370708, 'squarekilometer', '00001'),
(1960370780, 'acres', '00001'),
(1960370847, 'circles', '00001'),
(1961036532, 'Joules', '00001'),
(1961036600, 'Btu', '00001'),
(1961036668, 'calories', '00001'),
(1961036734, 'electronvolt', '00001'),
(1961036802, 'erg', '00001'),
(1961036871, 'watthour', '00001'),
(1961036933, 'therm', '00001'),
(1961037000, 'toe', '00001'),
(1961037089, 'tce', '00001'),
(1961473548, 'Dyne', '00001'),
(1961473623, 'gram-force', '00001'),
(1961473691, 'poundals', '00001'),
(1961473760, 'newtons', '00001'),
(1961473868, 'pounds', '00001'),
(1961474002, 'kgm-force', '00001'),
(1961657896, 'Hertz', '00001'),
(1961657973, 'cyclespersecond', '00001'),
(1961658057, 'revolutions', '00001'),
(1961658124, 'perseconds', '00001'),
(1961658207, 'degreesperseconds', '00001'),
(1961658290, 'radiansperseconds', '00001'),
(1961822986, 'celcius', '00001'),
(1961823051, 'fahrenheit', '00001'),
(1961823119, 'rankine', '00001'),
(1961823184, 'reaumur', '00001'),
(1961823264, 'kelvin', '00001'),
(1962261340, 'liquidanddry', '00001'),
(1962261405, 'litre', '00001'),
(1962261471, 'fluidounce', '00001'),
(1962261534, 'pint', '00001'),
(1962261596, 'quart', '00001'),
(1962261658, 'gallon', '00001'),
(1962261719, 'mililitre', '00001'),
(1962261782, 'barrel', '00001'),
(1962261845, 'gill', '00001'),
(1962261911, 'hogshead', '00001'),
(1962385330, 'microgram', '00001'),
(1962385399, 'milligram', '00001'),
(1962385463, 'centigram', '00001'),
(1962385526, 'decigram', '00001'),
(1962385593, 'gram', '00001'),
(1962385658, 'dekagram', '00001'),
(1962385724, 'hectogram', '00001'),
(1962563612, 'pound-force-foot', '00001'),
(1962563676, 'pound-force-inch', '00001'),
(1962563736, 'kilogram-force-meter', '00001'),
(1962563799, 'newtonmetersquare', '00001'),
(1962738772, 'gradient', '00001'),
(1962738838, 'radian', '00001'),
(1962738902, 'degree', '00001'),
(1962739069, 'point', '00001'),
(1962855538, 'watt,btu/hour', '00001'),
(1962855613, 'foot-lbs/second', '00001'),
(1962855679, 'horsepower', '00001'),
(1962855741, 'kilowatt', '00001'),
(1962961987, 'kg/cubicmeter', '00001'),
(1962962053, 'lbm/cubicfoot', '00001'),
(1962962116, 'lbm/gallon', '00001'),
(1962962179, 'aluminum', '00001'),
(1962962239, 'copper', '00001'),
(1962962305, 'gold', '00001'),
(1962962369, 'water', '00001'),
(1963115205, 'dyne/sqcm', '00001'),
(1963115265, 'pascal', '00001'),
(1963115336, 'poundal/sqft', '00001'),
(1963115424, 'torr', '00001'),
(1963115490, 'inchH2O', '00001'),
(1963115554, 'inchmercury', '00001'),
(-1141802001, 'string', '00001'),
(-1141801954, 'palindrome', '00001'),
(-800675768, 'integer', '00001'),
(-800675799, 'division', '00001'),
(-800675846, 'addition', '00001'),
(-800675721, 'float', '00001');

-- --------------------------------------------------------

--
-- Table structure for table `service`
--

CREATE TABLE IF NOT EXISTS `service` (
  `service_ID` int(11) NOT NULL,
  `service_name` varchar(100) NOT NULL,
  `service_WSDL` varchar(200) NOT NULL,
  `home_URL` varchar(200) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `owl_URL` varchar(200) DEFAULT NULL,
  `user_rating` float DEFAULT NULL,
  `exe_duration` double DEFAULT NULL,
  `cost` float DEFAULT NULL,
  `rating_user_count` int(11) DEFAULT NULL,
  `invoke_request_count` int(11) DEFAULT NULL,
  `invoke_success_count` int(11) DEFAULT NULL,
  `execute_success_count` int(11) DEFAULT NULL,
  `published_date` date NOT NULL,
  `publisher` int(11) NOT NULL,
  `checked_by_admin` bit(1) NOT NULL,
  PRIMARY KEY (`service_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `service`
--

INSERT INTO `service` (`service_ID`, `service_name`, `service_WSDL`, `home_URL`, `description`, `owl_URL`, `user_rating`, `exe_duration`, `cost`, `rating_user_count`, `invoke_request_count`, `invoke_success_count`, `execute_success_count`, `published_date`, `publisher`, `checked_by_admin`) VALUES
(1959224521, 'lengthUnit', 'http://www.webservicex.net/length.asmx?WSDL', 'http://www.webservicex.net/ws/WSDetails.aspx?WSID=21&CATID=13', 'Millimetres, Centimetres, Inches, Feet, Yards, Meters, Kilometres, Miles, Mils, Rods, Fathoms, Nautical Miles, More..', '', 3, 1155972826, 34, 1, 20, 15, 15, '2011-01-11', 1, b'1'),
(1959448254, 'ConvertSpeeds', 'http://www.webservicex.net/ConvertSpeed.asmx?WSDL', 'http://www.webservicex.net/ws/WSDetails.aspx?WSID=22&CATID=13', 'centimetres/second, meters/second, kilometres/hour, feet/second, feet/minute, miles/hour, knots, mach, More..', '', 6, 1587352355, 45, 2, 40, 36, 36, '2011-01-11', 2, b'1'),
(1959622644, 'ConvertWeights', 'http://www.webservicex.net/ConvertWeight.asmx?WSDL', 'http://www.webservicex.net/ws/WSDetails.aspx?WSID=22&CATID=13', 'Kilograms, Ounces, Pounds, Troy Pounds, Stones, Tons, More...', '', 10, 1596908510, 45, 4, 54, 35, 35, '2011-01-11', 2, b'1'),
(1959728515, 'ComputerUnit', ' http://www.webservicex.net/ConvertComputer.asmx?WSDL', 'http://www.webservicex.net/ws/WSDetails.aspx', 'Convert bytes, kilobytes, megabytes, gigabytes, and More..', '', 4, 1563205589, 34, 2, 563, 145, 145, '2011-01-11', 2, b'0'),
(1960370250, 'AreaUnit', 'http://www.webservicex.net/ConvertArea.asmx?WSDL', 'http://www.webservicex.net/ws/WSDetails.aspx', 'Square centimetre, Square meter, Square inch, Square foot, Square mile, Square Kilometre, Acres, Circles, More...', '', 7, 1567992929, 23, 2, 122, 100, 100, '2011-01-11', 1, b'1'),
(1961036419, 'EnergyUnit', 'http://www.webservicex.net/ConvertEnergy.asmx?WSDL', '', 'Joules, Btu, calories, electronvolt, erg, watt hour, therm, toe, tce, More..', '', 4, 1157046863, 123, 3, 32, 30, 30, '2011-01-11', 1, b'0'),
(1961473451, 'ForceUnit', 'http://www.webservicex.net/ConvertForec.asmx?WSDL', 'http://www.webservicex.net/ws/WSDetails.aspx?WSID=28&CATID=13', 'Dyne, gram-force, poundals, newtons, pounds, kgm-force, More...', '', 1, 1159137561, 54, 1, 433, 430, 430, '2011-01-11', 2, b'1'),
(1961657788, 'FrequencyUnit', 'http://www.webservicex.net/convertFrequency.asmx?WSDL', 'http://www.webservicex.net/ws/WSDetails.aspx?WSID=28&CATID=13', 'Hertz, cycles per second, revolutions per second, degrees per second, radians per second, many more...', '', 15, 1746891975, 23, 3, 12, 11, 11, '2011-01-11', 1, b'0'),
(1961822904, 'ConvertTemperature', 'http://www.webservicex.net/ConvertTemperature.asmx?WSDL', 'http://www.webservicex.net/ws/WSDetails.aspx?WSID=28&CATID=13', 'Celsius, Fahrenheit, Rankine, Reaumur, and Kelvin.', '', 4, 1175360852, 45, 1, 453, 411, 411, '2011-01-11', 1, b'1'),
(1962261257, 'VolumeUnit', 'http://www.webservicex.net/convertVolume.asmx?WSDL', 'http://www.webservicex.net/ws/WSDetails.aspx?WSID=32&CATID=13', 'Liquid and Dry. Litters, Fluid Ounces, Pints, Quarts, Gallons, Mililitre/cc, Barrels, Gill, Hogshead, More..', '', 9, 1998646022, 32, 3, 453, 423, 423, '2011-01-11', 1, b'1'),
(1962385242, 'MetricWeightUnit', 'http://www.webservicex.net/convertMetricWeight.asmx?WSDL', 'http://www.webservicex.net/ws/WSDetails.aspx?WSID=32&CATID=13', 'microgram, milligram, centigram, decigram, gram, dekagram, hectogram, kilogram, More...', '', 5, 1157093953, 34, 2, 122, 111, 111, '2011-01-11', 2, b'0'),
(1962563532, 'TorqueUnit', 'http://www.webservicex.net/ConvertTorque.asmx?WSDL', 'http://www.webservicex.net/ws/WSDetails.aspx?WSID=32&CATID=13', 'Pound-force Foot, Pound-force Inch, Kilogram-force Meter, and Newton meterÂ²\r', '', 3, 2070978022, 343, 1, 32, 31, 31, '2011-01-11', 2, b'1'),
(1962738692, 'AngleUnit', 'http://www.webservicex.net/ConvertAngle.asmx?WSDL', 'http://www.webservicex.net/ws/WSDetails.aspx?WSID=32&CATID=13', 'Gradients, Radians, Degrees, Minutes, Seconds, Points, More.', '', 2, 1608472602, 342, 4, 45, 45, 45, '2011-01-11', 1, b'0'),
(1962855451, 'PowerUnit', 'http://www.webservicex.net/ConverPower.asmx?WSDL', 'http://www.webservicex.net/ws/WSDetails.aspx?WSID=32&CATID=13', 'Watts, BTU/hour, foot-lbs/second, Horsepower, kilowatts, More...', '', 14, 1235602726, 123, 3, 322, 312, 312, '2011-01-11', 1, b'0'),
(1962961884, 'DensityUnit', 'http://www.webservicex.net/ConvertDensity.asmx?WSDL', 'http://www.webservicex.net/ws/WSDetails.aspx?WSID=32&CATID=13', 'kg/cubic meter, lbm/cubic foot, lbm/gallon, aluminum, copper, gold, water, More..', '', 33, 1339898087, 23, 11, 12, 11, 11, '2011-01-11', 2, b'1'),
(1963115125, 'PressureUnit', 'http://www.webservicex.net/CovertPressure.asmx?WSDL', 'http://www.webservicex.net/ws/WSDetails.aspx?WSID=32&CATID=13', 'dyne/sq cm, Pascal, poundal/sq foot, Torr, inch H2O, inch mercury, More...', '', 4, 1291929089, 234, 2, 32, 32, 32, '2011-01-11', 1, b'0'),
(-800675940, 'CalculatorService', 'http://localhost:8080/Checking/CalculatorService?WSDL', 'http://localhost:8080/Checking/CalculatorService', 'It is a simple calculator that performs calculations', '', 0, 94780482, 0, 0, 1, 1, 1, '2011-01-29', 1, b'0'),
(-1053999710, 'PalindromeService', 'http://localhost:8080/Checking/PalindromeService?WSDL', '', '', '', 2, 8089366.33333333, 45, 1, 3, 3, 2, '2011-01-26', 1, b'0');

-- --------------------------------------------------------

--
-- Table structure for table `servicecat_service`
--

CREATE TABLE IF NOT EXISTS `servicecat_service` (
  `SCS_ID` int(11) NOT NULL,
  `SC_ID` int(11) NOT NULL,
  `service_ID` int(11) NOT NULL,
  PRIMARY KEY (`SCS_ID`),
  KEY `SC_ID` (`SC_ID`),
  KEY `service_ID` (`service_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `servicecat_service`
--

INSERT INTO `servicecat_service` (`SCS_ID`, `SC_ID`, `service_ID`) VALUES
(1959224594, 1, 1959224521),
(1959448356, 6, 1959448254),
(1959622713, 6, 1959622644),
(1959728569, 6, 1959728515),
(1960370309, 6, 1960370250),
(1961036489, 1, 1961036419),
(1961473506, 6, 1961473451),
(1961657856, 6, 1961657788),
(1961822955, 6, 1961822904),
(1962261309, 6, 1962261257),
(1962385297, 6, 1962385242),
(1962563580, 6, 1962563532),
(1962738741, 6, 1962738692),
(1962855506, 6, 1962855451),
(1962961953, 6, 1962961884),
(1963115173, 6, 1963115125),
(-1053999678, 1, -1053999710),
(-800675862, 1, -800675940);

-- --------------------------------------------------------

--
-- Table structure for table `service_category`
--

CREATE TABLE IF NOT EXISTS `service_category` (
  `SC_ID` int(11) NOT NULL,
  `category_name` varchar(100) NOT NULL,
  PRIMARY KEY (`SC_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `service_category`
--
/*
INSERT INTO `service_category` (`SC_ID`, `category_name`) VALUES
(1, 'Business and Commerce'),
(2, 'Communications-Information'),
(3, 'Data Lookup'),
(4, 'Graphics and Multimedia'),
(5, 'Education'),
(6, 'Data Manipulation-Unit Conversion'),
(7, 'Entertainment');
*/
-- --------------------------------------------------------

--
-- Table structure for table `service_property`
--

CREATE TABLE IF NOT EXISTS `service_property` (
  `SP_ID` int(11) NOT NULL,
  `service_ID` int(11) NOT NULL,
  `property_ID` int(11) NOT NULL,
  `property_category` int(11) NOT NULL,
  PRIMARY KEY (`SP_ID`),
  KEY `property_ID` (`property_ID`),
  KEY `service_ID` (`service_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `service_property`
--

INSERT INTO `service_property` (`SP_ID`, `service_ID`, `property_ID`, `property_category`) VALUES
(1959224685, 1959224521, 1959224639, 4),
(1959224783, 1959224521, 1959224735, 4),
(1959224859, 1959224521, 1959224819, 4),
(1959224949, 1959224521, 1959224892, 4),
(1959225015, 1959224521, 1959224982, 4),
(1959225086, 1959224521, 1959225052, 4),
(1959225153, 1959224521, 1959225119, 4),
(1959225221, 1959224521, 1959225188, 4),
(1959225309, 1959224521, 1959225254, 4),
(1959448467, 1959448254, 1959448417, 4),
(1959448540, 1959448254, 1959224639, 4),
(1959448609, 1959448254, 1959224982, 4),
(1959448687, 1959448254, 1959448650, 4),
(1959448748, 1959448254, 1959224819, 4),
(1959448817, 1959448254, 1959448783, 4),
(1959448868, 1959448254, 1959224892, 4),
(1959448935, 1959448254, 1959448901, 4),
(1959449001, 1959448254, 1959448968, 4),
(1959622797, 1959622644, 1959622757, 4),
(1959622877, 1959622644, 1959622836, 4),
(1959622953, 1959622644, 1959622914, 4),
(1959623034, 1959622644, 1959622992, 4),
(1959623107, 1959622644, 1959623070, 4),
(1959728643, 1959728515, 1959728603, 4),
(1959728712, 1959728515, 1959728679, 4),
(1959728782, 1959728515, 1959728747, 4),
(1959728865, 1959728515, 1959728826, 4),
(1959728936, 1959728515, 1959728901, 4),
(1959729024, 1959728515, 1959728975, 4),
(1960370398, 1960370250, 1960370355, 4),
(1960370469, 1960370250, 1960370431, 4),
(1960370536, 1960370250, 1960370503, 4),
(1960370608, 1960370250, 1960370572, 4),
(1960370675, 1960370250, 1960370641, 4),
(1960370745, 1960370250, 1960370708, 4),
(1960370814, 1960370250, 1960370780, 4),
(1960370881, 1960370250, 1960370847, 4),
(1961036566, 1961036419, 1961036532, 4),
(1961036635, 1961036419, 1961036600, 4),
(1961036700, 1961036419, 1961036668, 4),
(1961036770, 1961036419, 1961036734, 4),
(1961036835, 1961036419, 1961036802, 4),
(1961036903, 1961036419, 1961036871, 4),
(1961036964, 1961036419, 1961036933, 4),
(1961037049, 1961036419, 1961037000, 4),
(1961037124, 1961036419, 1961037089, 4),
(-1053999616, -1053999710, -1141802001, 4),
(-1053999647, -1053999710, -1141801954, 4),
(-800675815, -800675940, -800675846, 4),
(-800675784, -800675940, -800675799, 4),
(-800675737, -800675940, -800675768, 4),
(-800675706, -800675940, -800675721, 4),
(1961473585, 1961473451, 1961473548, 4),
(1961473659, 1961473451, 1961473623, 4),
(1961473723, 1961473451, 1961473691, 4),
(1961473810, 1961473451, 1961473760, 4),
(1961473940, 1961473451, 1961473868, 4),
(1961474058, 1961473451, 1961474002, 4),
(1961657936, 1961657788, 1961657896, 4),
(1961658007, 1961657788, 1961657973, 4),
(1961658091, 1961657788, 1961658057, 4),
(1961658161, 1961657788, 1961658124, 4),
(1961658250, 1961657788, 1961658207, 4),
(1961658347, 1961657788, 1961658290, 4),
(1961823020, 1961822904, 1961822986, 4),
(1961823086, 1961822904, 1961823051, 4),
(1961823150, 1961822904, 1961823119, 4),
(1961823230, 1961822904, 1961823184, 4),
(1961823294, 1961822904, 1961823264, 4),
(1962261371, 1962261257, 1962261340, 4),
(1962261440, 1962261257, 1962261405, 4),
(1962261503, 1962261257, 1962261471, 4),
(1962261565, 1962261257, 1962261534, 4),
(1962261627, 1962261257, 1962261596, 4),
(1962261688, 1962261257, 1962261658, 4),
(1962261750, 1962261257, 1962261719, 4),
(1962261813, 1962261257, 1962261782, 4),
(1962261877, 1962261257, 1962261845, 4),
(1962261951, 1962261257, 1962261911, 4),
(1962385364, 1962385242, 1962385330, 4),
(1962385430, 1962385242, 1962385399, 4),
(1962385493, 1962385242, 1962385463, 4),
(1962385559, 1962385242, 1962385526, 4),
(1962385626, 1962385242, 1962385593, 4),
(1962385691, 1962385242, 1962385658, 4),
(1962385758, 1962385242, 1962385724, 4),
(1962385814, 1962385242, 1959622757, 4),
(1962563643, 1962563532, 1962563612, 4),
(1962563706, 1962563532, 1962563676, 4),
(1962563768, 1962563532, 1962563736, 4),
(1962563830, 1962563532, 1962563799, 4),
(1962738807, 1962738692, 1962738772, 4),
(1962738869, 1962738692, 1962738838, 4),
(1962738938, 1962738692, 1962738902, 4),
(1962738983, 1962738692, 1959448783, 4),
(1962739039, 1962738692, 1959448417, 4),
(1962739100, 1962738692, 1962739069, 4),
(1962855581, 1962855451, 1962855538, 4),
(1962855647, 1962855451, 1962855613, 4),
(1962855710, 1962855451, 1962855679, 4),
(1962855772, 1962855451, 1962855741, 4),
(1962962021, 1962961884, 1962961987, 4),
(1962962084, 1962961884, 1962962053, 4),
(1962962147, 1962961884, 1962962116, 4),
(1962962209, 1962961884, 1962962179, 4),
(1962962273, 1962961884, 1962962239, 4),
(1962962338, 1962961884, 1962962305, 4),
(1962962400, 1962961884, 1962962369, 4),
(1963115236, 1963115125, 1963115205, 4),
(1963115297, 1963115125, 1963115265, 4),
(1963115379, 1963115125, 1963115336, 4),
(1963115457, 1963115125, 1963115424, 4),
(1963115522, 1963115125, 1963115490, 4),
(1963115584, 1963115125, 1963115554, 4);

-- --------------------------------------------------------

--
-- Table structure for table `wsr_users`
--

CREATE TABLE IF NOT EXISTS `wsr_users` (
  `userid` int(11) NOT NULL,
  `username` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL,
  `login_type` varchar(30) NOT NULL,
  `name` varchar(30) NOT NULL,
  `address` varchar(30) NOT NULL,
  `email` varchar(30) NOT NULL,
  `reg_date` date NOT NULL,
  `reffered_by` int(11) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `wsr_users`
--

INSERT INTO `wsr_users` (`userid`, `username`, `password`, `login_type`, `name`, `address`, `email`, `reg_date`, `reffered_by`) VALUES
(1, 'vhishma', 'pant', 'admin', 'vhishma pant', 'baneshwor,kathmandu', 'vsmpant@hotmail.com', '2011-01-19', NULL),
(2, 'bikash', 'bikash', 'publisher', 'Bikash Bishwokarma', 'IOE, Pulchowk', 'xclusivebbk@gmail.com', '2011-01-20', NULL);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
