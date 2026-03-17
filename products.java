public class products {
    private String name;
    private double price;
    private int quantity;
    private int id;

    public products(String name, double price, int quantitym, int id) {
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
}
