package customers;

public class RegularCustomer extends Customer {

    public RegularCustomer(int customerId, String firstName, String lastName) {
        super(customerId, firstName, lastName);
    }

    public void signUpForVIP() {
        System.out.println(firstName + " " + lastName + " signed up for VIP.");
    }
}