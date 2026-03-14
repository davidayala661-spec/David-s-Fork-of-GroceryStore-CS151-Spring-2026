package customers;

public class VIPCustomer extends Customer {

    private double discountRate;
    private int points;

    public VIPCustomer(int customerId, String firstName, String lastName, double discountRate) {
        super(customerId, firstName, lastName);
        this.discountRate = discountRate;
        this.points = 0;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int pointsToAdd) {
        if (pointsToAdd > 0) {
            points += pointsToAdd;
        }
    }

    public void viewVIPBenefits() {
        System.out.println("VIP Customer: " + firstName + " " + lastName);
        System.out.println("Discount Rate: " + discountRate);
        System.out.println("Points: " + points);
    }
}