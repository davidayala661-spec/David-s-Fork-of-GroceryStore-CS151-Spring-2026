package aisles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import products.Products;

public class Aisles 
{
    private String aisleType;
    private int aisleNumber;
    private HashMap<Integer, List<Products>> shelves;

    public Aisles(String aisleType, int aisleNumber) 
    {
        if (aisleType == null || aisleType.trim().isEmpty())     
        {
            throw new IllegalArgumentException("Aisle type cannot be empty.");
        }
        if (aisleNumber <= 0) 
        {
            throw new IllegalArgumentException("Aisle number must be greater than 0.");
        }

        this.aisleType = aisleType;
        this.aisleNumber = aisleNumber;
        this.shelves = new HashMap<>();
    }

    public String getAisleType() 
    {
        return aisleType;
    }

    public int getAisleNumber() 
    {
        return aisleNumber;
    }

    public void setAisleType(String aisleType) 
    {
        if (aisleType == null || aisleType.trim().isEmpty()) 
        {
            throw new IllegalArgumentException("Aisle type cannot be empty.");
        }
        this.aisleType = aisleType;
    }

    public void addShelf(int shelfNumber) 
    {
        if (shelfNumber <= 0)
        {
            throw new IllegalArgumentException("Shelf number must be greater than 0.");
        }
        shelves.putIfAbsent(shelfNumber, new ArrayList<>());
    }

    public void addProductToShelf(int shelfNumber, Products product) 
    {
        if (product == null)
        {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (shelfNumber <= 0) 
        {
            throw new IllegalArgumentException("Shelf number must be greater than 0.");
        }

        shelves.putIfAbsent(shelfNumber, new ArrayList<>());
        shelves.get(shelfNumber).add(product);
    }

    public boolean removeProductFromShelf(int shelfNumber, int productId) 
    {
        if (!shelves.containsKey(shelfNumber)) 
        {
            return false;
        }

        List<Products> shelfProducts = shelves.get(shelfNumber);
        boolean removed = shelfProducts.removeIf(product -> product.getID() == productId);

        if (shelfProducts.isEmpty())
        {
            shelves.remove(shelfNumber);
        }

        return removed;
    }

    public List<Products> getProductsOnShelf(int shelfNumber) 
    {
        if (!shelves.containsKey(shelfNumber)) 
        {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(shelves.get(shelfNumber));
    }

    public List<Products> getAllProducts()
     {
        List<Products> allProducts = new ArrayList<>();
        for (List<Products> shelfProducts : shelves.values()) 
        {
            allProducts.addAll(shelfProducts);
        }
        return allProducts;
    }

    public Map<Integer, List<Products>> getShelves()
    {
        return Collections.unmodifiableMap(shelves);
    }

    public void printAisle() 
    {
        System.out.println("Aisle " + aisleNumber + " (" + aisleType + ")");

        if (shelves.isEmpty()) 
        {
            System.out.println("No shelves or products in this aisle.");
            return;
        }

        for (Map.Entry<Integer, List<Products>> entry : shelves.entrySet())
        {
            int shelfNumber = entry.getKey();
            List<Products> shelfProducts = entry.getValue();

            System.out.println(" Shelf " + shelfNumber + ":");
            for (Products product : shelfProducts) 
            {
                System.out.println("  - " + product);
            }
        }
    }

    @Override
    public String toString() 
    {
        return "Aisle " + aisleNumber + " (" + aisleType + ") with " + shelves.size() + " shelves.";
    }
}
