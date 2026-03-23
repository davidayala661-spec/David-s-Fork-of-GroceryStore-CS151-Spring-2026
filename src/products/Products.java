package products;

public class Products {
    private String name;
    private double price;
    private int quantity;
    private int id;

    public Products(String name, double price, int quantitym, int id) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getID() {
        return id;
    }

    // new methods TBD

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty.");
        }
        this.name = name;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.price = price;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        this.quantity = quantity;
    }

    public void stockToShelf(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Stock amount must be greater than 0.");
        }
        this.quantity += amount;
    }

    public void reduceStock(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }
        if (amount > quantity) {
            throw new IllegalArgumentException("Not enough stock available.");
        }
        this.quantity -= amount;
    }

    @Override
    public String toString() {
        return "Product: " + name +
               ", ID: " + id +
               ", Price: $" + price +
               ", Quantity: " + quantity;
    }

}
