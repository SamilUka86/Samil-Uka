import java.io.*;
import java.sql.*;
import java.util.*;
public class FoodOrderingSystem1
{
    static Scanner scanner;
    public static void main(String[] args) throws Exception {
        String dburl="jdbc:mysql://localhost:3306/food_ordering_database";
        String dbuser="root";
        String dbpass="";
        String driver="com.mysql.cj.jdbc.Driver";

        Class.forName(driver);
        Connection connect =DriverManager.getConnection(dburl,dbuser,dbpass);
        System.out.println(connect != null ? "Connected":"Not Connected");

        scanner = new Scanner(System.in);
//        System.out.println("Enter your id:");
//        int id=sc.nextInt();
//        System.out.println("Enter you name:");
//        String name=sc.next();
//        System.out.println("Enter pass:");
//        String pass=sc.next();

//        System.out.println("Enter your id:");
//        int id1 = scanner.nextInt();
//        System.out.println("Enter you name:");
//        String name1 = scanner.next();
//        System.out.println("Enter pass:");
//        String pass1 = scanner.next();
//        OwnerManagement um = new OwnerManagement(id1,name1,pass1);
//        um.checkingDetails(id1,name1,pass1);

        //Menu Management
        MenuManagement menuManager = new MenuManagement();
        int choice;
        do {
            System.out.println("Choose how you want to see the menu:");
            System.out.println("1. Full Menu");
            System.out.println("2. Menu by Category");
            System.out.println("3. Menu by Restaurant");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    menuManager.showFullMenu(connect, scanner);
                    break;
                case 2:
                    menuManager.menuByCategory(connect, scanner);
                    break;
                case 3:
                    menuManager.menuByRestaurant(connect, scanner);
                    break;
                case 4:
                    System.out.println("You are exited." +
                            "Thank you fro visiting!!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while(choice != 4);
    }
}
class OwnerManagement {
    Scanner sc = new Scanner(System.in);

    private int ownerId;
    private String ownerName;
    private String ownerPass;

    public OwnerManagement(int ownerId, String ownerName, String ownerPass) {
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.ownerPass = ownerPass;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerPass() {
        return ownerPass;
    }
//    void enteringDetails() throws Exception
//    {
//        FileWriter fileWriter=new FileWriter("ownerData.txt");
//        BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
//        bufferedWriter.write(getOwnerId()+",");
//        bufferedWriter.write(getOwnerName()+",");
//        bufferedWriter.write(getOwnerPass());
//
//        bufferedWriter.flush();
//        bufferedWriter.close();
//    }

    static void checkingDetails(int id, String name, String pass) throws Exception {
        File ownerData = new File("ownerData.txt");
        ownerData.createNewFile();
        FileReader fileReader = new FileReader(ownerData);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String lineReader;
        boolean found = false;

        while ((lineReader = bufferedReader.readLine()) != null) {
            String parts[] = lineReader.split(",");
            if (parts.length == 3) {
                int oId = Integer.parseInt(parts[0].trim());
                String oName = parts[1];
                String oPass = parts[2];
                if (oId == id && oName.equals(name) && oPass.equals(pass)) {
                    found = true;
                    break;
                }
            }
        }
        bufferedReader.close();
        if (found) {
            System.out.println("Value matched");
            updatePass();
        } else {
            System.out.println("No");
        }
    }

    static void updatePass() throws Exception {
        Scanner sc = new Scanner(System.in);
        int choice;
        System.out.println("Do you want to update password ?, If yes");
        System.out.println("then please select from below options");
        System.out.println("1. Yes");
        System.out.println("2. No");
        System.out.print("Enter Your Choice : ");
        choice = sc.nextInt();
        switch (choice) {
            case 1:
                System.out.print("Enter new password : ");
                String password = sc.next();
                FileWriter fw1 = new FileWriter("ownerData");
                fw1.write("");
                fw1.close();
                OwnerManagement om1 = new OwnerManagement(12, "FoodO", password);
                BufferedWriter bw1 = new BufferedWriter(new FileWriter("ownerData.txt"));
                bw1.write("12,FoodO," + password + "");
                bw1.close();
                System.out.println("Password Updated Successfully !");
                break;
            case 2:
                System.out.println("Redirecting back to main menu...");
                break;
        }
    }
}
class MenuManagement {
//    static Scanner scanner = new Scanner(System.in);
    static final String[] CATEGORIES = {"menu_beverages", "menu_desserts", "menu_indianbread",
            "menu_main_course", "menu_pastanoodles", "menu_rice",
            "menu_salad", "menu_starters"};

    static void showFullMenu(Connection connection, Scanner scanner) throws Exception {
        System.out.println("\n----- FULL MENU -----");
        for (String table : CATEGORIES) {
            displayMenu(connection, table);
        }
        takeOrderByDishName(connection, scanner);
    }

    static void menuByCategory(Connection connection, Scanner scanner) throws Exception {
        int categoryChoice = -1;
        do {
            System.out.println("\n--- MENU BY CATEGORY ---");
            for (int i = 0; i < CATEGORIES.length; i++) {
                System.out.println((i + 1) + ". " + CATEGORIES[i].replace("menu_", "").replace("_", " ").toUpperCase());
            }
            System.out.println("0. Exit");
            System.out.print("Select a category: ");
            categoryChoice = scanner.nextInt();
            scanner.nextLine();

            if (categoryChoice == 0) {
                System.out.println("Returning to main menu.");
                return;
            } else if (categoryChoice >= 1 && categoryChoice <= CATEGORIES.length) {
                displayMenu(connection, CATEGORIES[categoryChoice - 1]);
                takeOrderByDishName(connection, scanner);
                break;
            } else {
                System.out.println("Invalid category selection. Try again.");
            }
        } while (true);
    }

    static void menuByRestaurant(Connection connection, Scanner scanner) throws Exception {
        String sql = "SELECT * FROM restaurant_details";
        int restaurantId = -1;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
            System.out.println("\n--- RESTAURANT LIST ---");
            while (resultSet.next()) {
                int id = resultSet.getInt("restaurant_id");
                String name = resultSet.getString("restaurant_name");
                String address = resultSet.getString("restaurant_address");
                System.out.println(id + " - " + name + " (" + address + ")");
            }
        while(true) {
            System.out.print("\nEnter the Restaurant ID you want to order from: ");
            restaurantId = scanner.nextInt();
            scanner.nextLine();
            if (restaurantId < 1 || restaurantId > 15) {
                System.out.println("Enter valid id for restaurant");
            } else {
                System.out.println("\nMenu available: ");
                menuByCategory(connection, scanner);
                break;
            }
        }
    }

    static void displayMenu(Connection connection, String tableName) throws Exception {
        String sql = "SELECT * FROM " + tableName;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("\n-- " + tableName.replace("menu_", "").replace("_", " ").toUpperCase() + " --");
            while (resultSet.next()) {
                int id = resultSet.getInt("dish_id");
                String name = resultSet.getString("dish_name");
                double price = resultSet.getDouble("price");

                System.out.print("ID: " + id + " | Name: " + name + " | Price: ₹" + price);

                if (hasColumn(resultSet, "description")) {
                    System.out.print(" | Description: " + resultSet.getString("description"));
                }
                if (hasColumn(resultSet, "veg/non_veg")) {
                    System.out.print(" | Type: " + resultSet.getString("veg/non_veg"));
                }
                if (hasColumn(resultSet, "egg_eggless")) {
                    System.out.print(" | Eggless: " + resultSet.getString("egg_eggless"));
                }
                System.out.println();
            }
        }

    static void takeOrderByDishName(Connection connection, Scanner scanner) throws Exception {
        System.out.print("\nHow many items would you like to order? ");
        int count = scanner.nextInt();
        scanner.nextLine();

        if(count <= 0) {
            System.out.println("Enter valid numbers !");
            return;
        }

        double totalAmount = 0.0;

        for (int i = 1; i <= count; i++) {
            System.out.print("Enter dish id #" + i + ": ");
            String dishId = scanner.nextLine();
            double price = searchAndConfirmDish(connection, dishId); // returns price
            if (price > 0) {
                totalAmount += price;
            }
        }

        // Call payment processing
        PaymentManagement paymentThread = new PaymentManagement(connection, scanner, totalAmount);
        // Start the thread instead of calling processPayment()
        paymentThread.start();

        // Wait for payment to complete
        paymentThread.join();
       // PaymentManagement.processPayment();
    }


    static double searchAndConfirmDish(Connection connection, String dishId) throws SQLException {
        for (String table : CATEGORIES) {
            String sql = "SELECT * FROM " + table + " WHERE dish_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, dishId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString("dish_name");
                double price = rs.getDouble("price");
                System.out.println("\nFound in " + table + " | " + name + " | ₹" + price);
                return price;
            }
        }
        System.out.println("Dish not found in the menu : (id) " + dishId);
        return 0.0;
    }

    static boolean hasColumn(ResultSet resultSet, String column) {
        try {
            resultSet.findColumn(column);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
----------------------------------------------------------------------------------------------
//PaymentManagement class
import java.io.*;
import java.sql.*;
import java.util.*;

class PaymentManagement extends Thread {
    private Connection connection;
    private Scanner scanner;
    private double billAmount;

    public PaymentManagement(Connection connection, Scanner scanner, double billAmount) {
        this.connection = connection;
        this.scanner = scanner;
        this.billAmount = billAmount;
    }

    @Override
    public void run() {
        try {
            processPayment();
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    // Main payment logic
    private void processPayment() throws SQLException {
        connection.setAutoCommit(false); // Start transaction

        double enteredAmount;
        boolean paymentDone = false;

        while (!paymentDone) {
            System.out.println("\nYour bill is: ₹" + billAmount);
            System.out.print("Enter the amount to pay: ");
            enteredAmount = scanner.nextDouble();

            if (enteredAmount < billAmount) {
                System.out.println("Entered amount is less than bill. Try again.");
                continue;
            }
            if (enteredAmount > billAmount) {
                System.out.println("Entered amount is greater than bill. Try again.");
                continue;
            }

            String methodType = null;
            System.out.println("Choose payment method:");
            System.out.println("1. UPI");
            System.out.println("2. Cash");
            System.out.println("3. Card");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    payViaUPI();
                    methodType = "UPI";
                    break;
                case 2:
                    payViaCash();
                    methodType = "Cash";
                    break;
                case 3:
                    payViaCard();
                    methodType = "Card";
                    break;
                default:
                    System.out.println("Invalid payment method. Try again.");
                    continue;
            }

            // Insert into DB
            String sql = "INSERT INTO payment_process (amount, method_type, payment_date) VALUES (?, ?, NOW())";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setDouble(1, enteredAmount);
            pstmt.setString(2, methodType);
           // pstmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            pstmt.executeUpdate();

            // Commit transaction
            connection.commit();
            System.out.println("Payment of ₹" + enteredAmount + " via " + methodType + " successful! Thank you.");
            paymentDone = true;
        }
    }

    private void payViaUPI() {
        System.out.println("Processing UPI payment...");
        delay();
        System.out.println("UPI payment completed.");
    }

    private void payViaCash() {
        System.out.println("Processing cash payment...");
        delay();
        System.out.println("Cash payment completed.");
    }

    private void payViaCard() {
        System.out.println("Processing card payment...");
        delay();
        System.out.println("Card payment completed.");
    }

    private void delay() {
        try {
            Thread.sleep(2000); // simulate delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
