import aisles.Aisles;
import customers.CustomerIdRegistry;
import customers.RegularCustomer;
import customers.VIPCustomer;
import data.StoreDataLoader;
import employee.Employee;
import employee.EmployeeIdRegistry;
import employee.Manager;
import employee.Stocker;
import exceptions.CapacityExceededException;
import exceptions.DuplicateProductException;
import exceptions.InvalidPriceException;
import exceptions.InvalidProductException;
import exceptions.InvalidQuantityException;
import exceptions.InvalidSectionException;
import input.ConsoleInput;
import inventory.Inventory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import menu.StoreMenus;

// main contains unity of all of the classes and functions.
public class Main {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Map<Integer, RegularCustomer> regularCustomers = new HashMap<>();
        Map<Integer, VIPCustomer> vipCustomers = new HashMap<>();
        Map<Integer, Employee> employees = new HashMap<>();

        // Inventory setup
        Inventory inventory = new Inventory();
        List<Aisles> aisles = StoreDataLoader.loadDefaultAisles();

        System.out.println("=================================");
        System.out.println("   Welcome to the Grocery Store  ");
        System.out.println("=================================");

        // Back-room inventory: one stock pool per aisle SKU (separate from floor stock on shelves)
        try {
            StoreDataLoader.loadBackRoomInventory(inventory, aisles, 50);
            System.out.println("Back-room inventory loaded successfully.");
        } catch (CapacityExceededException | InvalidSectionException | InvalidProductException
                | DuplicateProductException | InvalidPriceException | InvalidQuantityException e) {
            System.out.println("Setup error: " + e.getMessage());
        }

        int roleChoice = -1;
        while (roleChoice != 4) {
            System.out.println("\n===== Grocery Store =====");
            System.out.println("Who are you?");
            System.out.println("1. Regular customer");
            System.out.println("2. VIP customer");
            System.out.println("3. Employee");
            System.out.println("4. Exit");
            roleChoice = ConsoleInput.readInt(scanner, "Enter your choice: ");

            switch (roleChoice) {
                case 1:
                    handleRegularCustomerPortal(scanner, regularCustomers, inventory, aisles);
                    break;
                case 2:
                    handleVipCustomerPortal(scanner, vipCustomers, inventory, aisles);
                    break;
                case 3:
                    handleEmployeePortal(scanner, employees, inventory, aisles,
                            regularCustomers, vipCustomers);
                    break;
                case 4:
                    System.out.println("Thank you for using the Grocery Store System!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }


    private static void handleRegularCustomerPortal(Scanner scanner,
            Map<Integer, RegularCustomer> regularCustomers,
            Inventory inventory, List<Aisles> aisles) {

        int choice = -1;
        while (choice != 3) {
            System.out.println("\n--- Regular Customer Portal ---");
            System.out.println("1. Sign up");
            System.out.println("2. Sign in");
            System.out.println("3. Back");
            choice = ConsoleInput.readInt(scanner, "Choose an option: ");

            switch (choice) {
                case 1:
                    RegularCustomer newCustomer = registerRegularCustomer(scanner, regularCustomers);
                    StoreMenus.runRegularCustomerSession(scanner, newCustomer, inventory, aisles);
                    break;
                case 2:
                    RegularCustomer signedInCustomer = signInRegularCustomer(scanner, regularCustomers);
                    if (signedInCustomer != null) {
                        StoreMenus.runRegularCustomerSession(scanner, signedInCustomer, inventory, aisles);
                    }
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void handleVipCustomerPortal(Scanner scanner,
            Map<Integer, VIPCustomer> vipCustomers,
            Inventory inventory, List<Aisles> aisles) {

        int choice = -1;
        while (choice != 3) {
            System.out.println("\n--- VIP Customer Portal ---");
            System.out.println("1. Sign up");
            System.out.println("2. Sign in");
            System.out.println("3. Back");
            choice = ConsoleInput.readInt(scanner, "Choose an option: ");

            switch (choice) {
                case 1:
                    VIPCustomer newCustomer = registerVipCustomer(scanner, vipCustomers);
                    StoreMenus.runVipCustomerSession(scanner, newCustomer, inventory, aisles);
                    break;
                case 2:
                    VIPCustomer signedInCustomer = signInVipCustomer(scanner, vipCustomers);
                    if (signedInCustomer != null) {
                        StoreMenus.runVipCustomerSession(scanner, signedInCustomer, inventory, aisles);
                    }
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void handleEmployeePortal(Scanner scanner,
            Map<Integer, Employee> employees,
            Inventory inventory, List<Aisles> aisles,
            Map<Integer, RegularCustomer> regularCustomers,
            Map<Integer, VIPCustomer> vipCustomers) {

        int choice = -1;
        while (choice != 3) {
            System.out.println("\n--- Employee Portal ---");
            System.out.println("1. Sign up");
            System.out.println("2. Sign in");
            System.out.println("3. Back");
            choice = ConsoleInput.readInt(scanner, "Choose an option: ");

            switch (choice) {
                case 1:
                    Employee newEmployee = registerEmployee(scanner, employees);
                    StoreMenus.runEmployeeSession(scanner, inventory, aisles,
                            regularCustomers, vipCustomers,
                            newEmployee);
                    break;
                case 2:
                    Employee signedInEmployee = signInEmployee(scanner, employees);
                    if (signedInEmployee != null) {
                        StoreMenus.runEmployeeSession(scanner, inventory, aisles,
                                regularCustomers, vipCustomers,
                                signedInEmployee);
                    }
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static RegularCustomer registerRegularCustomer(Scanner scanner,
            Map<Integer, RegularCustomer> regularCustomers) {
        System.out.println("\n--- New Regular Customer ---");
        String first = ConsoleInput.readLine(scanner, "First name: ").trim();
        String last = ConsoleInput.readLine(scanner, "Last name: ").trim();

        if (first.isEmpty()) {
            first = "Guest";
        }
        if (last.isEmpty()) {
            last = "Customer";
        }

        int id = CustomerIdRegistry.nextId();
        RegularCustomer customer = new RegularCustomer(id, first, last);
        regularCustomers.put(id, customer);

        System.out.println("Account created successfully.");
        System.out.println("Name: " + customer.getFullName());
        System.out.println("Customer ID: " + id);

        return customer;
    }

    private static VIPCustomer registerVipCustomer(Scanner scanner,
            Map<Integer, VIPCustomer> vipCustomers) {
        System.out.println("\n--- New VIP Customer ---");
        String first = ConsoleInput.readLine(scanner, "First name: ").trim();
        String last = ConsoleInput.readLine(scanner, "Last name: ").trim();

        if (first.isEmpty()) {
            first = "Guest";
        }
        if (last.isEmpty()) {
            last = "Customer";
        }

        int id = CustomerIdRegistry.nextId();
        VIPCustomer customer = new VIPCustomer(id, first, last, 0.10);
        vipCustomers.put(id, customer);

        System.out.println("VIP account created successfully.");
        System.out.println("Name: " + customer.getFullName());
        System.out.println("Customer ID: " + id);

        return customer;
    }

    private static Employee registerEmployee(Scanner scanner,
            Map<Integer, Employee> employees) {
        System.out.println("\n--- New Employee ---");
        System.out.println("1. Stocker");
        System.out.println("2. Manager");
        int roleChoice = ConsoleInput.readInt(scanner, "Choose employee role: ");

        String first = ConsoleInput.readLine(scanner, "First name: ").trim();
        String last = ConsoleInput.readLine(scanner, "Last name: ").trim();

        if (first.isEmpty()) {
            first = "Employee";
        }
        if (last.isEmpty()) {
            last = "User";
        }

        int id = EmployeeIdRegistry.nextId();
        Employee employee;

        if (roleChoice == 1) {
            employee = new Stocker(first, last, id);
        } else if (roleChoice == 2) {
            employee = new Manager(first, last, id);
        } else {
            System.out.println("Invalid role selected. Defaulting to Stocker.");
            employee = new Stocker(first, last, id);
        }

        employees.put(id, employee);

        System.out.println("Employee account created successfully.");
        System.out.println("Name: " + employee.getFullName());
        System.out.println("Employee ID: " + employee.getEmployeeID());
        System.out.println("Department: " + employee.getDepartment());

        return employee;
    }

    private static RegularCustomer signInRegularCustomer(Scanner scanner,
            Map<Integer, RegularCustomer> regularCustomers) {
        if (regularCustomers.isEmpty()) {
            System.out.println("No regular customer accounts exist yet.");
            return null;
        }

        System.out.println("\n--- Regular Customer Sign In ---");
        String first = ConsoleInput.readLine(scanner, "First name: ").trim();
        String last = ConsoleInput.readLine(scanner, "Last name: ").trim();
        int id = ConsoleInput.readInt(scanner, "Customer ID: ");

        RegularCustomer customer = regularCustomers.get(id);
        if (customer != null
                && customer.getFirstName().equalsIgnoreCase(first)
                && customer.getLastName().equalsIgnoreCase(last)) {
            System.out.println("Sign in successful. Welcome back, " + customer.getFullName() + "!");
            return customer;
        }

        System.out.println("Sign in failed. Name and ID do not match.");
        return null;
    }

    private static VIPCustomer signInVipCustomer(Scanner scanner,
            Map<Integer, VIPCustomer> vipCustomers) {
        if (vipCustomers.isEmpty()) {
            System.out.println("No VIP customer accounts exist yet.");
            return null;
        }

        System.out.println("\n--- VIP Customer Sign In ---");
        String first = ConsoleInput.readLine(scanner, "First name: ").trim();
        String last = ConsoleInput.readLine(scanner, "Last name: ").trim();
        int id = ConsoleInput.readInt(scanner, "Customer ID: ");

        VIPCustomer customer = vipCustomers.get(id);
        if (customer != null
                && customer.getFirstName().equalsIgnoreCase(first)
                && customer.getLastName().equalsIgnoreCase(last)) {
            System.out.println("Sign in successful. Welcome back, " + customer.getFullName() + "!");
            return customer;
        }

        System.out.println("Sign in failed. Name and ID do not match.");
        return null;
    }

    private static Employee signInEmployee(Scanner scanner,
            Map<Integer, Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("No employee accounts exist yet.");
            return null;
        }

        System.out.println("\n--- Employee Sign In ---");
        String first = ConsoleInput.readLine(scanner, "First name: ").trim();
        String last = ConsoleInput.readLine(scanner, "Last name: ").trim();
        int id = ConsoleInput.readInt(scanner, "Employee ID: ");

        Employee employee = employees.get(id);
        if (employee != null
                && employee.getFirstName().equalsIgnoreCase(first)
                && employee.getLastName().equalsIgnoreCase(last)) {
            System.out.println("Sign in successful. Welcome back, " + employee.getFullName() + "!");
            return employee;
        }

        System.out.println("Sign in failed. Name and ID do not match.");
        return null;
    }
}
