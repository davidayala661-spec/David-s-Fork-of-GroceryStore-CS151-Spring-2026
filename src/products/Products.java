package products;
/* Products are the items that are sold in the store.
Products contain a name, price, quantity, and id.
Products interact with the inventory and shelves to add and remove stock.
*/
public class Products {
    private String name;
    private double price;
    private int quantity;
    private int id;
   
    public Products(String name, double price, int quantity, int id) {
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

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.price = price;
    }
     // cannot be negative
    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        this.quantity = quantity;
    }

    // used by Stocker
    public void stockToShelf(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Stock amount must be greater than 0.");
        }
        this.quantity += amount;
    }

    @Override
    public String toString() {
        return "Product: " + name + ", ID: " + id + ", Price: $" + price + ", Quantity: " + quantity;
    }
}