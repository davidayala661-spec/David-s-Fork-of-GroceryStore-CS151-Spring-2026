package employee;


public abstract class Employee {
    private String firstName;
    private String lastName;
    private int employeeID;
    private String department;
    private double discountRate;

    public Employee(String firstName, String lastName, int employeeID, String department, double discountRate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeID = employeeID;
        this.department = department;
        this.discountRate = discountRate;
    }

    // getters for the sub classes
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void viewEmployeeInfo() {
        System.out.println("Employee ID: " + employeeID);
        System.out.println("Name: " + getFullName());
        System.out.println("Department: " + department);
    }

    public double applyDiscount(double price) {
        return price * (1 - discountRate);
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public String getDepartment() {
        return department;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public boolean canChangeQuantity() {
        return false; // by default, employees cannot change quantity, but manager can change it
    }

    public boolean canCheckHistory() {
        return false; // by default, employees cannot check history, but manager can check it
    }
}