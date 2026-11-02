

import java.util.Arrays;

public class Practice09 {

    public static final class ImmutableGeoPoint {
        private final double latitude;
        private final double longitude;

        public ImmutableGeoPoint(double latitude, double longitude) {
            this.latitude = Math.max(-90.0, Math.min(90.0, latitude));
            this.longitude = Math.max(-180.0, Math.min(180.0, longitude));
        }

        public double getLatitude() { return latitude; }
        public double getLongitude() { return longitude; }

        public ImmutableGeoPoint shift(double dLat, double dLon) {
            return new ImmutableGeoPoint(this.latitude + dLat, this.longitude + dLon);
        }

        @Override
        public String toString() {
            return String.format("GeoPoint[Lat: %.4f, Lon: %.4f]", latitude, longitude);
        }
    }

    public static class LabMeasurement {
        private final String experimentName;
        private final double[] samples;

        public LabMeasurement(String experimentName, double[] samples) {
            this.experimentName = experimentName == null ? "Unknown" : experimentName;
            this.samples = samples == null ? new double[0] : Arrays.copyOf(samples, samples.length);
        }

        public String getExperimentName() { return experimentName; }

        public double[] getSamples() {
            return Arrays.copyOf(this.samples, this.samples.length);
        }
    }

    public static class CryptoVault {
        private final String walletAddress;
        private String secretRecoveryPhrase;
        private double btcBalance;
        private boolean locked;

        public CryptoVault(String walletAddress, String secretRecoveryPhrase, double initialBalance) {
            this.walletAddress = walletAddress;
            this.secretRecoveryPhrase = secretRecoveryPhrase;
            this.btcBalance = Math.max(0.0, initialBalance);
            this.locked = true;
        }

        public boolean unlock(String phrase) {
            if (this.secretRecoveryPhrase.equals(phrase)) {
                this.locked = false;
                return true;
            }
            return false;
        }

        public void lock() { this.locked = true; }

        public boolean sendBtc(double amount, String destination) {
            if (!locked && amount > 0 && btcBalance >= amount) {
                btcBalance -= amount;
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return String.format("CryptoVault[Address: %s, Locked: %b, Balance: %.4f BTC]", walletAddress, locked, btcBalance);
        }
    }

    public static class ClosedInterval {
        private double start;
        private double end;

        public ClosedInterval(double start, double end) {
            resize(start, end);
        }

        public double getStart() { return start; }
        public double getEnd() { return end; }

        public void resize(double newStart, double newEnd) {
            if (newStart > newEnd) {
                this.start = newEnd;
                this.end = newStart;
            } else {
                this.start = newStart;
                this.end = newEnd;
            }
        }

        public double getLength() { return end - start; }
        public double getMidpoint() { return start + (end - start) / 2.0; }

        public boolean overlaps(ClosedInterval other) {
            if (other == null) return false;
            return this.start <= other.end && other.start <= this.end;
        }

        @Override
        public String toString() {
            return String.format("[%s; %s] (центр: %s, довжина: %s)", start, end, getMidpoint(), getLength());
        }
    }

    public static final class RgbColor {
        private final int red;
        private final int green;
        private final int blue;

        public RgbColor(int red, int green, int blue) {
            this.red = clamp(red);
            this.green = clamp(green);
            this.blue = clamp(blue);
        }

        private static int clamp(int val) {
            return Math.max(0, Math.min(255, val));
        }

        public int getRed() { return red; }
        public int getGreen() { return green; }
        public int getBlue() { return blue; }

        public String toHex() {
            return String.format("#%02X%02X%02X", red, green, blue);
        }

        public RgbColor invert() {
            return new RgbColor(255 - red, 255 - green, 255 - blue);
        }

        public RgbColor mixWith(RgbColor other) {
            if (other == null) return this;
            return new RgbColor((this.red + other.red) / 2, (this.green + other.green) / 2, (this.blue + other.blue) / 2);
        }

        @Override
        public String toString() {
            return String.format("RGB(%d,%d,%d) -> %s", red, green, blue, toHex());
        }
    }

    public static class HighSecuritySafe {
        private final int pinCode;
        private double storedCash;
        private int failedAttempts;
        private boolean permanentlyBlocked;

        public HighSecuritySafe(int pinCode, double initialCash) {
            this.pinCode = pinCode;
            this.storedCash = Math.max(0.0, initialCash);
            this.failedAttempts = 0;
            this.permanentlyBlocked = false;
        }

        private boolean checkAccess(int enteredPin) {
            if (permanentlyBlocked) return false;
            if (enteredPin != pinCode) {
                failedAttempts++;
                if (failedAttempts >= 3) {
                    permanentlyBlocked = true;
                }
                return false;
            }
            failedAttempts = 0;
            return true;
        }

        public boolean putCash(int pin, double amount) {
            if (checkAccess(pin) && amount > 0) {
                storedCash += amount;
                return true;
            }
            return false;
        }

        public boolean takeCash(int pin, double amount) {
            if (checkAccess(pin) && amount > 0 && storedCash >= amount) {
                storedCash -= amount;
                return true;
            }
            return false;
        }

        public double getBalance(int pin) {
            if (checkAccess(pin)) return storedCash;
            return -1.0;
        }
    }

    public static class EncapsulatedPolynomial {
        private final double[] coeffs;

        public EncapsulatedPolynomial(double[] coeffs) {
            this.coeffs = coeffs == null || coeffs.length == 0 ? new double[]{0.0} : Arrays.copyOf(coeffs, coeffs.length);
        }

        public int getDegree() { return coeffs.length - 1; }

        public double getCoefficient(int power) {
            if (power >= 0 && power < coeffs.length) return coeffs[power];
            return 0.0;
        }

        public double evaluate(double x) {
            double result = 0.0;
            for (int i = coeffs.length - 1; i >= 0; i--) {
                result = result * x + coeffs[i];
            }
            return result;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = coeffs.length - 1; i >= 0; i--) {
                if (coeffs[i] == 0) continue;
                if (sb.length() > 0 && coeffs[i] > 0) sb.append(" + ");
                if (coeffs[i] < 0) sb.append(" - ");
                double absVal = Math.abs(coeffs[i]);
                if (absVal != 1.0 || i == 0) sb.append(absVal);
                if (i > 0) sb.append("x");
                if (i > 1) sb.append("^").append(i);
            }
            return sb.length() == 0 ? "0.0" : sb.toString();
        }
    }

    public static class MedicalRecord {
        private final String patientId;
        private String fullName;
        private int bloodGroup;
        private boolean rhPositive;
        private final String[] allergies;

        public MedicalRecord(String patientId, String fullName, int bloodGroup, boolean rhPositive, String[] allergies) {
            this.patientId = patientId;
            this.fullName = fullName;
            this.bloodGroup = Math.max(1, Math.min(4, bloodGroup));
            this.rhPositive = rhPositive;
            this.allergies = allergies == null ? new String[0] : Arrays.copyOf(allergies, allergies.length);
        }

        public String[] getAllergies() {
            return Arrays.copyOf(allergies, allergies.length);
        }

        public boolean hasAllergyTo(String substance) {
            if (substance == null) return false;
            for (String allergy : allergies) {
                if (substance.equalsIgnoreCase(allergy)) return true;
            }
            return false;
        }

        @Override
        public String toString() {
            String bgStr = switch (bloodGroup) {
                case 1 -> "I (O)";
                case 2 -> "II (A)";
                case 3 -> "III (B)";
                default -> "IV (AB)";
            };
            return String.format("Patient: %s, Blood: %s %s, Allergies: %s", fullName, bgStr, rhPositive ? "Rh+" : "Rh-", Arrays.toString(allergies));
        }
    }

    public static class Point2D {
        private double x;
        private double y;

        public Point2D(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() { return x; }
        public double getY() { return y; }

        public double distanceTo(Point2D other) {
            if (other == null) return 0.0;
            return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
        }

        @Override
        public String toString() { return String.format("(%.2f; %.2f)", x, y); }
    }

    public static class CompositeCircle {
        private double radius;
        private final Point2D center;

        public CompositeCircle(double centerX, double centerY, double radius) {
            this.center = new Point2D(centerX, centerY);
            this.radius = Math.max(0.1, radius);
        }
        public boolean containsPoint(Point2D p) {
            return center.distanceTo(p) <= radius;
        }
        @Override
        public String toString() {
            return String.format("Circle[Center: %s, Radius: %.2f]", center, radius);
        }
    }
    public static class Engine {
        private final String fuelType;
        private final int volumeCc;
        private int rpm;
        private boolean started;
        public Engine(String fuelType, int volumeCc) {
            this.fuelType = fuelType;
            this.volumeCc = volumeCc;
            this.rpm = 0;
            this.started = false;
        }
        public void start() { this.started = true; this.rpm = 800; }
        public void stop() { this.started = false; this.rpm = 0; }
        public boolean isStarted() { return started; }
        public void setRpm(int rpm) {
            if (started) this.rpm = Math.max(800, Math.min(6500, rpm));
        }
        @Override
        public String toString() {
            return String.format("Engine[Fuel: %s, Vol: %dcc, RPM: %d, Running: %b]", fuelType, volumeCc, rpm, started);
        }
    }
    public static class Automobile {
        private final String model;
        private double speed;
        private final Engine engine;
        public Automobile(String model, String fuelType, int volumeCc) {
            this.model = model;
            this.speed = 0.0;
            this.engine = new Engine(fuelType, volumeCc);
        }
        public Engine getEngine() { return engine; }
        public void accelerate(double deltaSpeed) {
            if (engine.isStarted() && deltaSpeed > 0) {
                this.speed += deltaSpeed;
                int newRpm = 800 + (int)(this.speed * 40);
                engine.setRpm(newRpm);
            }
        }
        @Override
        public String toString() {
            return String.format("Automobile[Model: %s, Speed: %.1f km/h, %s]", model, speed, engine);
        }
    }
    public static class Battery {
        private int capacityMah;
        private int chargePercent;
        public Battery(int capacityMah) {
            this.capacityMah = capacityMah;
            this.chargePercent = 100;
        }
        public int getChargePercent() { return chargePercent; }
        public void drain(int percent) {
            this.chargePercent = Math.max(0, this.chargePercent - percent);
        }
        public void charge(int percent) {
            this.chargePercent = Math.min(100, this.chargePercent + percent);
        }
    }
    public static class Display {
        private double diagonalInches;
        private int brightnessPercent;
        public Display(double diagonalInches) {
            this.diagonalInches = diagonalInches;
            this.brightnessPercent = 50;
        }
        public int getBrightnessPercent() { return brightnessPercent; }
        public void setBrightnessPercent(int brightnessPercent) {
            this.brightnessPercent = Math.max(10, Math.min(100, brightnessPercent));
        }
    }
    public static class SmartDevice {
        private final Battery battery;
        private final Display display;
        private boolean active;
        public SmartDevice(int batteryCapacity, double screenDiagonal) {
            this.battery = new Battery(batteryCapacity);
            this.display = new Display(screenDiagonal);
            this.active = true;
        }
        public Display getDisplay() { return display; }
        public Battery getBattery() { return battery; }
        public void watchVideo(int minutes) {
            if (!active) return;
            int intervals = minutes / 10;
            int drainRate = display.getBrightnessPercent() > 70 ? 2 : 1;
            battery.drain(intervals * drainRate);
            if (battery.getChargePercent() <= 0) {
                active = false;
            }
        }
        @Override
        public String toString() {
            return String.format("Device[Active: %b, Battery: %d%%, Brightness: %d%%]", active, battery.getChargePercent(), display.getBrightnessPercent());
        }
    }
    public static class LineSegment {
        private final Point2D start;
        private final Point2D end;
        public LineSegment(Point2D start, Point2D end) {
            this.start = new Point2D(start.getX(), start.getY());
            this.end = new Point2D(end.getX(), end.getY());
        }
        public double getLength() { return start.distanceTo(end); }
        public Point2D getMidpoint() {
            return new Point2D((start.getX() + end.getX()) / 2.0, (start.getY() + end.getY()) / 2.0);
        }
        @Override
        public String toString() {
            return String.format("Segment[%s -> %s, Length: %.2f]", start, end, getLength());
        }
    }
    public static class BankAccount {
        private final String iban;
        private double balance;
        public BankAccount(String iban, double initialBalance) {
            this.iban = iban;
            this.balance = Math.max(0.0, initialBalance);
        }
        public void deposit(double amount) { if (amount > 0) balance += amount; }
        public boolean withdraw(double amount) {
            if (amount > 0 && balance >= amount) {
                balance -= amount;
                return true;
            }
            return false;
        }
        public double getBalance() { return balance; }
    }
    public static class PlasticCard {
        private final String cardNumber;
        private final String expiryDate;
        private boolean blocked;
        private final BankAccount linkedAccount;
        public PlasticCard(String cardNumber, String expiryDate, BankAccount linkedAccount) {
            this.cardNumber = cardNumber;
            this.expiryDate = expiryDate;
            this.linkedAccount = linkedAccount;
            this.blocked = false;
        }
        public void setBlocked(boolean blocked) { this.blocked = blocked; }
        public boolean payAtTerminal(double amount) {
            if (!blocked && linkedAccount != null) {
                return linkedAccount.withdraw(amount);
            }
            return false;
        }
    }
    public static class WeaponItem {
        private final String name;
        private final int bonusDamage;
        private int durability;
        public WeaponItem(String name, int bonusDamage) {
            this.name = name;
            this.bonusDamage = bonusDamage;
            this.durability = 100;
        }
        public int useWeapon() {
            if (durability <= 0) return 0;
            durability = Math.max(0, durability - 10);
            return bonusDamage;
        }
    }
    public static class HeroCharacter {
        private final String nickname;
        private int baseStrength;
        private WeaponItem equippedWeapon;
        public HeroCharacter(String nickname, int baseStrength) {
            this.nickname = nickname;
            this.baseStrength = baseStrength;
        }
        public void equip(WeaponItem newWeapon) { this.equippedWeapon = newWeapon; }
        public void unequip() { this.equippedWeapon = null; }
        public int attack() {
            int bonus = equippedWeapon != null ? equippedWeapon.useWeapon() : 0;
            return baseStrength + bonus;
        }
    }
    public static class DeliveryAddress {
        private String city;
        private String street;
        private int postOfficeNumber;
        public DeliveryAddress(String city, String street, int postOfficeNumber) {
            this.city = city;
            this.street = street;
            this.postOfficeNumber = postOfficeNumber;
        }
        @Override
        public String toString() { return String.format("%s, %s, ВП №%d", city, street, postOfficeNumber); }
    }
    public static class ParcelOrder {
        private final String orderId;
        private double weightKg;
        private DeliveryAddress destination;
        private boolean shipped;
        public ParcelOrder(String orderId, double weightKg, DeliveryAddress destination) {
            this.orderId = orderId;
            this.weightKg = weightKg;
            this.destination = destination;
            this.shipped = false;
        }
        public void changeDestination(DeliveryAddress newAddress) {
            if (!shipped) this.destination = newAddress;
        }
        public void markAsShipped() { this.shipped = true; }
        @Override
        public String toString() { return String.format("Order %s, Shipped: %b, Delivery to: %s", orderId, shipped, destination); }
    }
    public static class TemperatureSensor {
        private double currentTemp;
        public TemperatureSensor(double initialTemp) { this.currentTemp = initialTemp; }
        public double getCurrentTemp() { return currentTemp; }
        public void setCurrentTemp(double currentTemp) { this.currentTemp = currentTemp; }
    }
    public static class HeatingUnit {
        private boolean heatingOn;
        private double powerKw;
        public HeatingUnit(double powerKw) {
            this.powerKw = powerKw;
            this.heatingOn = false;
        }
        public boolean isHeatingOn() { return heatingOn; }
        public void setHeatingOn(boolean heatingOn) { this.heatingOn = heatingOn; }
    }
    public static class ClimateController {
        private final TemperatureSensor sensor;
        private final HeatingUnit heatingUnit;
        private double targetTemp;
        public ClimateController(TemperatureSensor sensor, HeatingUnit heatingUnit, double targetTemp) {
            this.sensor = sensor;
            this.heatingUnit = heatingUnit;
            this.targetTemp = targetTemp;
        }
        public void regulate() {
            if (sensor.getCurrentTemp() < targetTemp - 0.5) {
                heatingUnit.setHeatingOn(true);
            } else if (sensor.getCurrentTemp() >= targetTemp) {
                heatingUnit.setHeatingOn(false);
            }
        }
        @Override
        public String toString() {
            return String.format("Climate[Current: %.1f°C, Target: %.1f°C, Heater On: %b]", sensor.getCurrentTemp(), targetTemp, heatingUnit.isHeatingOn());
        }
    }
    public static class Polyline {
        private final Point2D[] vertices;
        private int count;
        public Polyline(int capacity) {
            this.vertices = new Point2D[capacity];
            this.count = 0;
        }
        public void addVertex(double x, double y) {
            if (count < vertices.length) {
                vertices[count++] = new Point2D(x, y);
            }
        }
        public double getTotalLength() {
            double length = 0.0;
            for (int i = 0; i < count - 1; i++) {
                length += vertices[i].distanceTo(vertices[i + 1]);
            }
            return length;
        }
        public double getClosedPerimeter() {
            double base = getTotalLength();
            if (count > 2) {
                base += vertices[count - 1].distanceTo(vertices[0]);
            }
            return base;
        }
    }
    public static class OrderItem {
        private final String productName;
        private final double unitPrice;
        private int quantity;
        public OrderItem(String productName, double unitPrice, int quantity) {
            this.productName = productName;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
        }
        public String getProductName() { return productName; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public double getItemTotal() { return unitPrice * quantity; }
        @Override
        public String toString() { return String.format("%s x%d = %.2f грн", productName, quantity, getItemTotal()); }
    }
    public static class ShoppingOrder {
        private final OrderItem[] items;
        private int itemCount;
        public ShoppingOrder() {
            this.items = new OrderItem[10];
            this.itemCount = 0;
        }
        public void addItem(String name, double price, int qty) {
            for (int i = 0; i < itemCount; i++) {
                if (items[i].getProductName().equalsIgnoreCase(name)) {
                    items[i].setQuantity(items[i].getQuantity() + qty);
                    return;
                }
            }
            if (itemCount < items.length) {
                items[itemCount++] = new OrderItem(name, price, qty);
            }
        }
        public double calculateGrandTotal() {
            double total = 0.0;
            for (int i = 0; i < itemCount; i++) {
                total += items[i].getItemTotal();
            }
            return total;
        }
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("--- ФІСКАЛЬНИЙ ЧЕК ---\n");
            for (int i = 0; i < itemCount; i++) {
                sb.append(items[i]).append("\n");
            }
            sb.append("ЗАГАЛОМ: ").append(calculateGrandTotal()).append(" грн");
            return sb.toString();
        }
    }
    public static class BookItem {
        private final String title;
        private final String author;
        private final int pages;
        public BookItem(String title, String author, int pages) {
            this.title = title;
            this.author = author;
            this.pages = pages;
        }
        public int getPages() { return pages; }
    }
    public static class Bookshelf {
        private final String genre;
        private final BookItem[] books;
        private int bookCount;
        public Bookshelf(String genre, int capacity) {
            this.genre = genre;
            this.books = new BookItem[capacity];
            this.bookCount = 0;
        }
        public boolean putBook(BookItem book) {
            if (book != null && bookCount < books.length) {
                books[bookCount++] = book;
                return true;
            }
            return false;
        }
        public BookItem findLongestBook() {
            if (bookCount == 0) return null;
            BookItem longest = books[0];
            for (int i = 1; i < bookCount; i++) {
                if (books[i].getPages() > longest.getPages()) {
                    longest = books[i];
                }
            }
            return longest;
        }
        public int getTotalPagesOnShelf() {
            int total = 0;
            for (int i = 0; i < bookCount; i++) {
                total += books[i].getPages();
            }
            return total;
        }
    }
    public static class AudioTrack {
        private final String title;
        private final String artist;
        private final int durationSeconds;
        public AudioTrack(String title, String artist, int durationSeconds) {
            this.title = title;
            this.artist = artist;
            this.durationSeconds = durationSeconds;
        }
        public int getDurationSeconds() { return durationSeconds; }
        public String getFormattedDuration() {
            return String.format("%02d:%02d", durationSeconds / 60, durationSeconds % 60);
        }
        @Override
        public String toString() { return String.format("%s - %s [%s]", artist, title, getFormattedDuration()); }
    }
    public static class Playlist {
        private String name;
        private final AudioTrack[] tracks;
        private int trackCount;
        public Playlist(String name, int capacity) {
            this.name = name;
            this.tracks = new AudioTrack[capacity];
            this.trackCount = 0;
        }
        public void addTrack(AudioTrack track) {
            if (track != null && trackCount < tracks.length) {
                tracks[trackCount++] = track;
            }
        }
        public int getTotalDurationSeconds() {
            int total = 0;
            for (int i = 0; i < trackCount; i++) {
                total += tracks[i].getDurationSeconds();
            }
            return total;
        }
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("Плейлист: ").append(name).append("\n");
            for (int i = 0; i < trackCount; i++) {
                sb.append(i + 1).append(". ").append(tracks[i]).append("\n");
            }
            int total = getTotalDurationSeconds();
            sb.append(String.format("Загальна тривалість: %d хв %d сек", total / 60, total % 60));
            return sb.toString();
        }
    }
    public static class ServerNode {
        private final String ipAddress;
        private final int totalRamGb;
        private int usedRamGb;
        private boolean online;
        public ServerNode(String ipAddress, int totalRamGb) {
            this.ipAddress = ipAddress;
            this.totalRamGb = totalRamGb;
            this.usedRamGb = 0;
            this.online = true;
        }
        public boolean isOnline() { return online; }
        public int getFreeRamGb() { return totalRamGb - usedRamGb; }
        public void allocateRam(int ram) { this.usedRamGb += ram; }
    }
    public static class DataCenterCluster {
        private final String clusterLocation;
        private final ServerNode[] nodes;
        private int nodeCount;
        public DataCenterCluster(String clusterLocation, int capacity) {
            this.clusterLocation = clusterLocation;
            this.nodes = new ServerNode[capacity];
            this.nodeCount = 0;
        }
        public void addNode(ServerNode node) {
            if (nodeCount < nodes.length) nodes[nodeCount++] = node;
        }
        public boolean deployContainer(int requiredRamGb) {
            for (int i = 0; i < nodeCount; i++) {
                if (nodes[i].isOnline() && nodes[i].getFreeRamGb() >= requiredRamGb) {
                    nodes[i].allocateRam(requiredRamGb);
                    return true;
                }
            }
            return false;
        }
    }
    public static class CourseModule {
        private final String topic;
        private final int lectureHours;
        private final int practiceHours;
        private double maxScore;
        public CourseModule(String topic, int lectureHours, int practiceHours, double maxScore) {
            this.topic = topic;
            this.lectureHours = lectureHours;
            this.practiceHours = practiceHours;
            this.maxScore = maxScore;
        }
        public int getTotalHours() { return lectureHours + practiceHours; }
        public double getMaxScore() { return maxScore; }
    }
    public static class UniversityCourse {
        private final String courseTitle;
        private final CourseModule[] modules;
        private int moduleCount;
        public UniversityCourse(String courseTitle, int maxModules) {
            this.courseTitle = courseTitle;
            this.modules = new CourseModule[maxModules];
            this.moduleCount = 0;
        }
        public void addModule(CourseModule module) {
            if (moduleCount < modules.length) modules[moduleCount++] = module;
        }
        public double calculateEctsCredits() {
            int totalHours = 0;
            for (int i = 0; i < moduleCount; i++) {
                totalHours += modules[i].getTotalHours();
            }
            return totalHours / 30.0;
        }
        public boolean isValidScoreConfiguration() {
            double sum = 0;
            for (int i = 0; i < moduleCount; i++) {
                sum += modules[i].getMaxScore();
            }
            return Math.abs(sum - 100.0) < 1e-9;
        }
    }
    public static class TrainCarriage {
        private final int carriageNumber;
        private final String type;
        private final int totalSeats;
        private int occupiedSeats;
        public TrainCarriage(int carriageNumber, String type, int totalSeats) {
            this.carriageNumber = carriageNumber;
            this.type = type;
            this.totalSeats = totalSeats;
            this.occupiedSeats = 0;
        }
        public int getCarriageNumber() { return carriageNumber; }
        public int getFreeSeats() { return totalSeats - occupiedSeats; }
        public void bookSeats(int count) { this.occupiedSeats += count; }
    }
    public static class PassengerTrain {
        private final String route;
        private final TrainCarriage[] carriages;
        private int carriageCount;
        public PassengerTrain(String route, int capacity) {
            this.route = route;
            this.carriages = new TrainCarriage[capacity];
            this.carriageCount = 0;
        }
        public void attachCarriage(TrainCarriage c) {
            if (carriageCount < carriages.length) carriages[carriageCount++] = c;
        }
        public int getTotalFreeSeats() {
            int total = 0;
            for (int i = 0; i < carriageCount; i++) {
                total += carriages[i].getFreeSeats();
            }
            return total;
        }
        public boolean bookTicketsInCarriage(int carriageNumber, int seatsToBook) {
            for (int i = 0; i < carriageCount; i++) {
                if (carriages[i].getCarriageNumber() == carriageNumber && carriages[i].getFreeSeats() >= seatsToBook) {
                    carriages[i].bookSeats(seatsToBook);
                    return true;
                }
            }
            return false;
        }
    }
    public static class LinearEquation {
        private final double a;
        private final double b;
        private final double c;
        public LinearEquation(double a, double b, double c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
        public boolean isSatisfiedBy(double x, double y) {
            return Math.abs((a * x + b * y) - c) < 1e-6;
        }
    }
    public static class EquationSystem {
        private final LinearEquation[] equations;
        private int count;
        public EquationSystem(int capacity) {
            this.equations = new LinearEquation[capacity];
            this.count = 0;
        }
        public void addEquation(LinearEquation eq) {
            if (count < equations.length) equations[count++] = eq;
        }
        public boolean checkSolution(double x, double y) {
            for (int i = 0; i < count; i++) {
                if (!equations[i].isSatisfiedBy(x, y)) return false;
            }
            return true;
        }
    }
    public static abstract class AbstractShape {
        private String color;
        public AbstractShape(String color) {
            this.color = color == null || color.isBlank() ? "White" : color;
        }
        public String getColor() { return color; }
        public abstract double getArea();
        public abstract double getPerimeter();
        @Override
        public String toString() {
            return String.format("Shape[Color: %s, Area: %.2f, Perimeter: %.2f]", color, getArea(), getPerimeter());
        }
    }
    public static class CircleShape extends AbstractShape {
        private final double radius;
        public CircleShape(String color, double radius) {
            super(color);
            this.radius = Math.max(0.1, radius);
        }
        @Override
        public double getArea() { return Math.PI * radius * radius; }
        @Override
        public double getPerimeter() { return 2 * Math.PI * radius; }
    }
    public static class RectangleShape extends AbstractShape {
        private final double width;
        private final double height;
        public RectangleShape(String color, double width, double height) {
            super(color);
            this.width = Math.max(0.1, width);
            this.height = Math.max(0.1, height);
        }
        @Override
        public double getArea() { return width * height; }
        @Override
        public double getPerimeter() { return 2 * (width + height); }
    }
    public static abstract class AbstractPowerPlant {
        private final String stationName;
        protected double baseCapacityMw;
        public AbstractPowerPlant(String stationName, double baseCapacityMw) {
            this.stationName = stationName;
            this.baseCapacityMw = baseCapacityMw;
        }
        public abstract double generateMegawatts();
        public final double calculateDailyProfit(double tariffPerMwh) {
            return generateMegawatts() * 24 * tariffPerMwh;
        }
    }
    public static class SolarPowerPlant extends AbstractPowerPlant {
        private double sunIntensityFactor;
        public SolarPowerPlant(String stationName, double baseCapacityMw, double sunIntensityFactor) {
            super(stationName, baseCapacityMw);
            this.sunIntensityFactor = Math.max(0.0, Math.min(1.0, sunIntensityFactor));
        }
        @Override
        public double generateMegawatts() { return baseCapacityMw * sunIntensityFactor; }
    }
    public static class WindPowerPlant extends AbstractPowerPlant {
        private double windSpeedMs;
        public WindPowerPlant(String stationName, double baseCapacityMw, double windSpeedMs) {
            super(stationName, baseCapacityMw);
            this.windSpeedMs = windSpeedMs;
        }
        @Override
        public double generateMegawatts() {
            if (windSpeedMs < 3.0 || windSpeedMs > 25.0) return 0.0;
            return baseCapacityMw * (windSpeedMs / 12.0);
        }
    }
    public static abstract class AbstractEmployee {
        private final int id;
        private final String name;
        protected double taxRate;
        public AbstractEmployee(int id, String name, double taxRate) {
            this.id = id;
            this.name = name;
            this.taxRate = taxRate;
        }
        public abstract double calculateGrossSalary();
        public final double calculateNetSalary() {
            return calculateGrossSalary() * (1.0 - taxRate);
        }
    }
    public static class SalariedEmployee extends AbstractEmployee {
        private double monthlySalary;
        private double bonus;
        public SalariedEmployee(int id, String name, double taxRate, double monthlySalary, double bonus) {
            super(id, name, taxRate);
            this.monthlySalary = monthlySalary;
            this.bonus = bonus;
        }
        @Override
        public double calculateGrossSalary() { return monthlySalary + bonus; }
    }
    public static class HourlyContractor extends AbstractEmployee {
        private int hoursWorked;
        private double hourlyRate;
        public HourlyContractor(int id, String name, double taxRate, int hoursWorked, double hourlyRate) {
            super(id, name, taxRate);
            this.hoursWorked = hoursWorked;
            this.hourlyRate = hourlyRate;
        }
        @Override
        public double calculateGrossSalary() {
            if (hoursWorked <= 160) {
                return hoursWorked * hourlyRate;
            }
            return (160 * hourlyRate) + ((hoursWorked - 160) * hourlyRate * 1.5);
        }
    }
    public static abstract class AbstractSequence {
        private final String title;
        public AbstractSequence(String title) { this.title = title; }
        public abstract double getTerm(int index);
        public final double sumFirstN(int n) {
            double sum = 0.0;
            for (int i = 1; i <= n; i++) {
                sum += getTerm(i);
            }
            return sum;
        }
    }
    public static class HarmonicSequence extends AbstractSequence {
        public HarmonicSequence() { super("Harmonic"); }
        @Override
        public double getTerm(int index) { return 1.0 / index; }
    }
    public static class SquareNumbersSequence extends AbstractSequence {
        public SquareNumbersSequence() { super("Square Numbers"); }
        @Override
        public double getTerm(int index) { return (double) index * index; }
    }
    public static abstract class AbstractTransaction {
        private final String txId;
        protected final double amount;
        public AbstractTransaction(String txId, double amount) {
            this.txId = txId;
            this.amount = amount;
        }
        public abstract double calculateCommission();
        public final double getTotalDebit() { return amount + calculateCommission(); }
    }
    public static class DomesticTransfer extends AbstractTransaction {
        public DomesticTransfer(String txId, double amount) { super(txId, amount); }
        @Override
        public double calculateCommission() {
            return Math.max(5.0, amount * 0.005);
        }
    }
    public static class CryptoTransfer extends AbstractTransaction {
        private double networkGasFee;
        public CryptoTransfer(String txId, double amount, double networkGasFee) {
            super(txId, amount);
            this.networkGasFee = networkGasFee;
        }
        @Override
        public double calculateCommission() { return networkGasFee; }
    }
    public static abstract class Solid3D {
        private final String materialName;
        private final double densityKgPerM3;
        public Solid3D(String materialName, double densityKgPerM3) {
            this.materialName = materialName;
            this.densityKgPerM3 = densityKgPerM3;
        }
        public abstract double getVolume();
        public abstract double getSurfaceArea();
        public final double getMassKg() { return densityKgPerM3 * getVolume(); }
    }
    public static class Sphere3D extends Solid3D {
        private final double radius;
        public Sphere3D(String materialName, double densityKgPerM3, double radius) {
            super(materialName, densityKgPerM3);
            this.radius = radius;
        }
        @Override
        public double getVolume() { return (4.0 / 3.0) * Math.PI * Math.pow(radius, 3); }
        @Override
        public double getSurfaceArea() { return 4.0 * Math.PI * radius * radius; }
    }
    public static class Cylinder3D extends Solid3D {
        private final double radius;
        private final double height;
        public Cylinder3D(String materialName, double densityKgPerM3, double radius, double height) {
            super(materialName, densityKgPerM3);
            this.radius = radius;
            this.height = height;
        }
        @Override
        public double getVolume() { return Math.PI * radius * radius * height; }
        @Override
        public double getSurfaceArea() { return 2.0 * Math.PI * radius * (radius + height); }
    }
    public static abstract class AbstractSensor {
        private final String location;
        private final double minSafe;
        private final double maxSafe;
        public AbstractSensor(String location, double minSafe, double maxSafe) {
            this.location = location;
            this.minSafe = minSafe;
            this.maxSafe = maxSafe;
        }
        public abstract double readValue();
        public abstract String getUnit();
        public final boolean isAlarmTriggered() {
            double val = readValue();
            return val < minSafe || val > maxSafe;
        }
    }
    public static class PressureSensor extends AbstractSensor {
        private double currentRawValue;
        private double calibrationMultiplier;
        public PressureSensor(String location, double minSafe, double maxSafe, double calibrationMultiplier) {
            super(location, minSafe, maxSafe);
            this.calibrationMultiplier = calibrationMultiplier;
        }
        public void setRawValue(double raw) { this.currentRawValue = raw; }
        @Override
        public double readValue() { return currentRawValue * calibrationMultiplier; }
        @Override
        public String getUnit() { return "bar"; }
    }
    public static class RadiationSensor extends AbstractSensor {
        private double microSievertsPerHour;
        public RadiationSensor(String location, double minSafe, double maxSafe) {
            super(location, minSafe, maxSafe);
        }
        public void setRadiationValue(double value) { this.microSievertsPerHour = value; }
        @Override
        public double readValue() { return microSievertsPerHour; }
        @Override
        public String getUnit() { return "mcSv/h"; }
    }
    public static abstract class AbstractCompressor {
        private final String algorithmName;
        public AbstractCompressor(String algorithmName) { this.algorithmName = algorithmName; }
        public abstract int estimateCompressedSize(int originalSizeBytes);
        public final double getCompressionRatio(int originalSizeBytes) {
            if (originalSizeBytes <= 0) return 0.0;
            return (double) estimateCompressedSize(originalSizeBytes) / originalSizeBytes;
        }
    }
    public static class ZipCompressor extends AbstractCompressor {
        public ZipCompressor() { super("ZIP"); }
        @Override
        public int estimateCompressedSize(int originalSizeBytes) { return (int) (originalSizeBytes * 0.55); }
    }
    public static class UltraLzmaCompressor extends AbstractCompressor {
        public UltraLzmaCompressor() { super("LZMA"); }
        @Override
        public int estimateCompressedSize(int originalSizeBytes) { return (int) (originalSizeBytes * 0.30) + 128; }
    }
    public static abstract class AbstractPcPart {
        private final String model;
        private final double priceUah;
        public AbstractPcPart(String model, double priceUah) {
            this.model = model;
            this.priceUah = priceUah;
        }
        public abstract int getPowerConsumptionWatts();
    }
    public static class ProcessorPart extends AbstractPcPart {
        private int cores;
        private int watts;
        public ProcessorPart(String model, double priceUah, int cores, int watts) {
            super(model, priceUah);
            this.cores = cores;
            this.watts = watts;
        }
        @Override
        public int getPowerConsumptionWatts() { return watts; }
    }
    public static class VideoCardPart extends AbstractPcPart {
        private int vramGb;
        private int watts;
        public VideoCardPart(String model, double priceUah, int vramGb, int watts) {
            super(model, priceUah);
            this.vramGb = vramGb;
            this.watts = watts;
        }
        @Override
        public int getPowerConsumptionWatts() { return watts; }
    }
    public static class DesktopPC {
        private final int psuMaxWatts;
        private final AbstractPcPart[] parts;
        private int count;
        public DesktopPC(int psuMaxWatts, int maxParts) {
            this.psuMaxWatts = psuMaxWatts;
            this.parts = new AbstractPcPart[maxParts];
            this.count = 0;
        }
        public void addPart(AbstractPcPart part) {
            if (count < parts.length) parts[count++] = part;
        }
        public boolean isSystemStable() {
            int totalWatts = 0;
            for (int i = 0; i < count; i++) {
                totalWatts += parts[i].getPowerConsumptionWatts();
            }
            return psuMaxWatts >= (totalWatts * 1.2);
        }
    }
    public static abstract class AbstractThruster {
        private final String thrusterId;
        public AbstractThruster(String thrusterId) { this.thrusterId = thrusterId; }
        public abstract double getThrustKn();
        public abstract double getFuelBurnRate();
    }
    public static class ChemicalThruster extends AbstractThruster {
        private double thrust;
        public ChemicalThruster(String id, double thrust) { super(id); this.thrust = thrust; }
        @Override
        public double getThrustKn() { return thrust; }
        @Override
        public double getFuelBurnRate() { return thrust * 0.15; }
    }
    public static class IonThruster extends AbstractThruster {
        private double thrust;
        public IonThruster(String id, double thrust) { super(id); this.thrust = thrust; }
        @Override
        public double getThrustKn() { return thrust; }
        @Override
        public double getFuelBurnRate() { return thrust * 0.01; }
    }
    public static class Spacecraft {
        private final String shipName;
        private final double dryMassTons;
        private final AbstractThruster[] thrusters;
        private int count;
        public Spacecraft(String shipName, double dryMassTons, int maxThrusters) {
            this.shipName = shipName;
            this.dryMassTons = dryMassTons;
            this.thrusters = new AbstractThruster[maxThrusters];
            this.count = 0;
        }
        public void addThruster(AbstractThruster t) {
            if (count < thrusters.length) thrusters[count++] = t;
        }
        public double calculateTotalThrust() {
            double total = 0;
            for (int i = 0; i < count; i++) {
                total += thrusters[i].getThrustKn();
            }
            return total;
        }
        public double calculateInitialAcceleration() {
            if (dryMassTons <= 0) return 0.0;
            return calculateTotalThrust() / (dryMassTons * 1000.0);
        }
    }
    public static class LandPlot {
        private final double totalPlotAreaM2;
        private final AbstractShape[] structures;
        private int count;
        public LandPlot(double totalPlotAreaM2, int maxStructures) {
            this.totalPlotAreaM2 = totalPlotAreaM2;
            this.structures = new AbstractShape[maxStructures];
            this.count = 0;
        }
        public boolean addStructure(AbstractShape s) {
            if (s == null || count >= structures.length) return false;
            double currentStructuresArea = 0;
            for (int i = 0; i < count; i++) {
                currentStructuresArea += structures[i].getArea();
            }
            if (currentStructuresArea + s.getArea() <= totalPlotAreaM2 * 0.6) {
                structures[count++] = s;
                return true;
            }
            return false;
        }
        public double getFreeGreenArea() {
            double currentStructuresArea = 0;
            for (int i = 0; i < count; i++) {
                currentStructuresArea += structures[i].getArea();
            }
            return totalPlotAreaM2 - currentStructuresArea;
        }
    }
    public static abstract class AbstractAsset {
        private final String ticker;
        protected double investedAmount;
        public AbstractAsset(String ticker, double investedAmount) {
            this.ticker = ticker;
            this.investedAmount = investedAmount;
        }
        public double getInvestedAmount() { return investedAmount; }
        public abstract double calculateAnnualReturn();
    }
    public static class GovernmentBond extends AbstractAsset {
        private double couponRate;
        public GovernmentBond(String ticker, double investedAmount, double couponRate) {
            super(ticker, investedAmount);
            this.couponRate = couponRate;
        }
        @Override
        public double calculateAnnualReturn() { return investedAmount * couponRate; }
    }
    public static class DividendStock extends AbstractAsset {
        private double expectedGrowthRate;
        private double dividendYield;
        public DividendStock(String ticker, double investedAmount, double expectedGrowthRate, double dividendYield) {
            super(ticker, investedAmount);
            this.expectedGrowthRate = expectedGrowthRate;
            this.dividendYield = dividendYield;
        }
        @Override
        public double calculateAnnualReturn() {
            double growthProfit = investedAmount * expectedGrowthRate;
            double dividendProfit = investedAmount * dividendYield * 0.805;
            return growthProfit + dividendProfit;
        }
    }
    public static class InvestmentPortfolio {
        private final String investorName;
        private final AbstractAsset[] assets;
        private int count;
        public InvestmentPortfolio(String investorName, int maxAssets) {
            this.investorName = investorName;
            this.assets = new AbstractAsset[maxAssets];
            this.count = 0;
        }
        public void addAsset(AbstractAsset asset) {
            if (count < assets.length) assets[count++] = asset;
        }
        public double calculateTotalValue() {
            double total = 0;
            for (int i = 0; i < count; i++) {
                total += assets[i].getInvestedAmount();
            }
            return total;
        }
        public double calculateExpectedAnnualReturn() {
            double totalReturn = 0;
            for (int i = 0; i < count; i++) {
                totalReturn += assets[i].calculateAnnualReturn();
            }
            return totalReturn;
        }
    }
    public static abstract class AbstractHeroUnit {
        private final String name;
        protected int hp;
        public AbstractHeroUnit(String name, int hp) {
            this.name = name;
            this.hp = hp;
        }
        public abstract int getCombatPower();
        public abstract String getRole();
    }
    public static class TankWarrior extends AbstractHeroUnit {
        private int armor;
        public TankWarrior(String name, int hp, int armor) {
            super(name, hp);
            this.armor = armor;
        }
        @Override
        public int getCombatPower() { return hp + (armor * 2); }
        @Override
        public String getRole() { return "Tank"; }
    }
    public static class ArcaneMage extends AbstractHeroUnit {
        private int intelligence;
        public ArcaneMage(String name, int hp, int intelligence) {
            super(name, hp);
            this.intelligence = intelligence;
        }
        @Override
        public int getCombatPower() { return intelligence * 5; }
        @Override
        public String getRole() { return "Mage"; }
    }
    public static class RaidParty {
        private final String guildName;
        private final AbstractHeroUnit[] members;
        private int memberCount;
        public RaidParty(String guildName, int maxMembers) {
            this.guildName = guildName;
            this.members = new AbstractHeroUnit[maxMembers];
            this.memberCount = 0;
        }
        public void addMember(AbstractHeroUnit unit) {
            if (memberCount < members.length) members[memberCount++] = unit;
        }
        public boolean isReadyForRaid(int dungeonDifficulty) {
            boolean hasTank = false;
            int totalPower = 0;
            for (int i = 0; i < memberCount; i++) {
                if (members[i].getRole().equals("Tank")) hasTank = true;
                totalPower += members[i].getCombatPower();
            }
            return hasTank && totalPower > dungeonDifficulty;
        }
    }
    public static class EmergencySiren {
        private boolean sounding;
        public EmergencySiren() { this.sounding = false; }
        public boolean isSounding() { return sounding; }
        public void activate() { this.sounding = true; }
    }
    public static class SmartLab {
        private final EmergencySiren siren;
        private final AbstractSensor[] sensors;
        private int sensorCount;
        public SmartLab(int maxSensors) {
            this.siren = new EmergencySiren();
            this.sensors = new AbstractSensor[maxSensors];
            this.sensorCount = 0;
        }
        public void addSensor(AbstractSensor sensor) {
            if (sensorCount < sensors.length) sensors[sensorCount++] = sensor;
        }
        public void runDiagnostics() {
            for (int i = 0; i < sensorCount; i++) {
                if (sensors[i].isAlarmTriggered()) {
                    siren.activate();
                    System.out.println("ALARM TRIGGERED AT SENSOR " + (i + 1));
                    return;
                }
            }
        }
    }
    public static abstract class AbstractFilter {
        private final String filterName;
        public AbstractFilter(String filterName) { this.filterName = filterName; }
        public abstract double processSample(double x);
    }
    public static class AmplifierFilter extends AbstractFilter {
        private double gain;
        public AmplifierFilter(double gain) { super("Amplifier"); this.gain = gain; }
        @Override
        public double processSample(double x) { return x * gain; }
    }
    public static class ClippingFilter extends AbstractFilter {
        private double threshold;
        public ClippingFilter(double threshold) { super("Clipping"); this.threshold = threshold; }
        @Override
        public double processSample(double x) {
            return Math.max(-threshold, Math.min(threshold, x));
        }
    }
    public static class SignalProcessor {
        private final AbstractFilter[] pipeline;
        private int filterCount;
        public SignalProcessor(int maxFilters) {
            this.pipeline = new AbstractFilter[maxFilters];
            this.filterCount = 0;
        }
        public void addFilter(AbstractFilter filter) {
            if (filterCount < pipeline.length) pipeline[filterCount++] = filter;
        }
        public double[] processSignal(double[] signal) {
            if (signal == null) return new double[0];
            double[] result = Arrays.copyOf(signal, signal.length);
            for (int i = 0; i < result.length; i++) {
                for (int j = 0; j < filterCount; j++) {
                    result[i] = pipeline[j].processSample(result[i]);
                }
            }
            return result;
        }
    }
    public static void main(String[] args) {
        ImmutableGeoPoint point = new ImmutableGeoPoint(50.45, 30.52);
        System.out.println(point);
        ImmutableGeoPoint shifted = point.shift(0.1, -0.1);
        System.out.println(shifted);
        double[] inputs = {1.2, 3.4, 5.6};
        LabMeasurement measurement = new LabMeasurement("Physics", inputs);
        inputs[0] = 99.9;
        System.out.println("Defensive copy verified: " + measurement.getSamples()[0]);
        CryptoVault vault = new CryptoVault("0x123", "secret", 1.5);
        vault.unlock("secret");
        System.out.println(vault);
        ClosedInterval interval = new ClosedInterval(10, 5);
        System.out.println(interval);
        RgbColor color = new RgbColor(300, 150, -20);
        System.out.println(color);
        HighSecuritySafe safe = new HighSecuritySafe(1234, 5000);
        safe.putCash(1111, 100);
        safe.putCash(1111, 100);
        safe.putCash(1111, 100);
        System.out.println("Blocked after 3 attempts: " + !safe.putCash(1234, 100));
        EncapsulatedPolynomial poly = new EncapsulatedPolynomial(new double[]{1, 2, 3});
        System.out.println("P(x) = " + poly + " at x=2 is " + poly.evaluate(2));
        MedicalRecord record = new MedicalRecord("ID1", "John Doe", 2, true, new String[]{"Peanuts"});
        System.out.println(record);
        CompositeCircle compositeCircle = new CompositeCircle(0, 0, 5);
        System.out.println(compositeCircle.containsPoint(new Point2D(3, 4)));
        Automobile auto = new Automobile("Tesla", "Electric", 0);
        auto.getEngine().start();
        auto.accelerate(50);
        System.out.println(auto);
        SmartDevice device = new SmartDevice(4000, 6.7);
        device.getDisplay().setBrightnessPercent(80);
        device.watchVideo(30);
        System.out.println(device);
        LineSegment segment = new LineSegment(new Point2D(0, 0), new Point2D(3, 4));
        System.out.println(segment);
        BankAccount account = new BankAccount("UA123", 1000);
        PlasticCard card1 = new PlasticCard("4444", "12/28", account);
        PlasticCard card2 = new PlasticCard("5555", "12/28", account);
        card1.payAtTerminal(200);
        System.out.println("Shared budget balance: " + account.getBalance());
        HeroCharacter hero = new HeroCharacter("Arthas", 50);
        WeaponItem sword = new WeaponItem("Frostmourne", 30);
        hero.equip(sword);
        System.out.println("Attack power: " + hero.attack());
        DeliveryAddress address = new DeliveryAddress("Kyiv", "Khreshchatyk", 1);
        ParcelOrder order = new ParcelOrder("ORD-99", 2.5, address);
        order.markAsShipped();
        order.changeDestination(new DeliveryAddress("Lviv", "Main St", 2));
        System.out.println(order);
        TemperatureSensor tempSensor = new TemperatureSensor(18.5);
        HeatingUnit heater = new HeatingUnit(2.0);
        ClimateController climate = new ClimateController(tempSensor, heater, 21.0);
        climate.regulate();
        System.out.println(climate);
        Polyline polyline = new Polyline(5);
        polyline.addVertex(0, 0);
        polyline.addVertex(3, 0);
        polyline.addVertex(3, 4);
        System.out.println("Polyline length: " + polyline.getTotalLength());
        ShoppingOrder cart = new ShoppingOrder();
        cart.addItem("Apple", 25, 2);
        cart.addItem("Apple", 25, 3);
        System.out.println(cart);
        Bookshelf shelf = new Bookshelf("Sci-Fi", 5);
        shelf.putBook(new BookItem("Dune", "F. Herbert", 600));
        System.out.println("Total pages: " + shelf.getTotalPagesOnShelf());
        Playlist playlist = new Playlist("Favorites", 5);
        playlist.addTrack(new AudioTrack("In the End", "Linkin Park", 216));
        System.out.println(playlist);
        DataCenterCluster cluster = new DataCenterCluster("Kyiv", 3);
        cluster.addNode(new ServerNode("192.168.1.1", 64));
        System.out.println("Deployed container: " + cluster.deployContainer(16));
        UniversityCourse course = new UniversityCourse("Java Pro", 3);
        course.addModule(new CourseModule("OOP", 15, 15, 100));
        System.out.println("Course configured correctly: " + course.isValidScoreConfiguration());
        PassengerTrain train = new PassengerTrain("Kyiv-Lviv", 5);
        train.attachCarriage(new TrainCarriage(1, "Intercity", 60));
        System.out.println("Tickets booked: " + train.bookTicketsInCarriage(1, 2));
        EquationSystem sys = new EquationSystem(2);
        sys.addEquation(new LinearEquation(1, 1, 5));
        System.out.println("Solution match: " + sys.checkSolution(2, 3));
        AbstractShape rect = new RectangleShape("Red", 4, 5);
        System.out.println(rect);
        SolarPowerPlant solar = new SolarPowerPlant("SolarOne", 50, 0.8);
        System.out.println("Solar profit: " + solar.calculateDailyProfit(5.5));
        AbstractEmployee worker = new HourlyContractor(1, "Bob", 0.195, 170, 200);
        System.out.println("Net Salary: " + worker.calculateNetSalary());
        AbstractSequence seq = new HarmonicSequence();
        System.out.println("Sum of 5 harmonic numbers: " + seq.sumFirstN(5));
        AbstractTransaction tx = new DomesticTransfer("TX-001", 1000);
        System.out.println("Total transaction debt: " + tx.getTotalDebit());
        Solid3D sphere = new Sphere3D("Steel", 7800, 0.5);
        System.out.println("Sphere mass: " + sphere.getMassKg() + " kg");
        PressureSensor pSensor = new PressureSensor("Room 101", 1.0, 5.0, 1.2);
        pSensor.setRawValue(4.5);
        System.out.println("Alarm state: " + pSensor.isAlarmTriggered());
        AbstractCompressor comp = new UltraLzmaCompressor();
        System.out.println("Compression ratio: " + comp.getCompressionRatio(10000));
        DesktopPC pc = new DesktopPC(500, 5);
        pc.addPart(new ProcessorPart("i7", 12000, 8, 125));
        pc.addPart(new VideoCardPart("RTX 4060", 15000, 8, 200));
        System.out.println("PC Power Supply Stable: " + pc.isSystemStable());
        Spacecraft ship = new Spacecraft("Apollo", 10, 2);
        ship.addThruster(new ChemicalThruster("C1", 450));
        System.out.println("Ship initial acceleration: " + ship.calculateInitialAcceleration());
        LandPlot plot = new LandPlot(1000, 5);
        plot.addStructure(new RectangleShape("House", 20, 15));
        System.out.println("Free plot green area: " + plot.getFreeGreenArea() + " m2");
        InvestmentPortfolio portfolio = new InvestmentPortfolio("Investor", 2);
        portfolio.addAsset(new GovernmentBond("OVDP", 50000, 0.15));
        System.out.println("Portfolio expected return: " + portfolio.calculateExpectedAnnualReturn());
        RaidParty party = new RaidParty("Alliance", 5);
        party.addMember(new TankWarrior("Muradin", 120, 45));
        party.addMember(new ArcaneMage("Jaina", 60, 50));
        System.out.println("Raid party matches dungeon criteria: " + party.isReadyForRaid(150));
        SmartLab lab = new SmartLab(2);
        RadiationSensor rad = new RadiationSensor("Lab floor", 0.0, 0.5);
        rad.setRadiationValue(0.8);
        lab.addSensor(rad);
        lab.runDiagnostics();
        SignalProcessor processor = new SignalProcessor(2);
        processor.addFilter(new AmplifierFilter(2.0));
        processor.addFilter(new ClippingFilter(5.0));
        double[] processed = processor.processSignal(new double[]{1.0, 3.0, -4.0});
        System.out.println("Processed pipeline samples: " + Arrays.toString(processed));
        System.out.println("Task 40: Все успішно скомпільовано та готово до 'git push origin main'!");
    }
}