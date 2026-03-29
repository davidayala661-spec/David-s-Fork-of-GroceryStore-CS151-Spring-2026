# Grocery Store System – CS151 Project

## Overview 
This project simulates a grocery store system using Java. It demonstrates object-oriented programming concepts such as inheritance, abstraction, and interfaces.

Users can view customer information, manage a shopping cart, add or remove items, and view VIP customer benefits through a menu-driven interface.

The grocery store supports multiple user instances, including Regular Customers, VIP Customers, and Employees. Employees have role-based access as either Managers or Stockers. The application allows customers to browse aisles, add items to a cart, and check out, while employees can manage inventory, restock products, and view customer purchase history.

Key features:
- **Customer Accounts**: Sign up and sign in as Regular or VIP customers
- **Shopping Cart System**: Add, remove, clear, and view cart items
- **Checkout System**: Print receipts, apply tax, apply VIP discounts, and award VIP points
- **Inventory Management**: Add, remove, restock, search, and update products
- **Aisle Browsing**: Browse aisle-based product displays and buy directly from aisles
- **Employee Roles**: Separate workflows for Managers and Stockers
- **Purchase History**: Save receipts to customer history for later review
- **Custom Exceptions**: Input and store validation through dedicated exception classes

---

## Design

### Project Structure

| Package | Classes | Description |
|---------|---------|-------------|
| `default` | `Main` | Entry point of the application |
| `aisles` | `Aisles` | Represents grocery store aisles and shelf groupings of products |
| `auth` | `AccountService` | Handles customer and employee sign-up/sign-in logic |
| `cart` | `ShoppingCart` | Stores cart items for customers |
| `checkout` | `Checkout` | Handles receipt generation, tax, discounts, and checkout flow |
| `customers` | `Customer`, `RegularCustomer`, `VIPCustomer`, `Displayable`, `CustomerIdRegistry` | Customer hierarchy, display behavior, and ID generation |
| `data` | `StoreDataLoader` | Loads default aisle and product data |
| `employee` | `Employee`, `Manager`, `Stocker`, `EmployeeMenu`, `EmployeeIdRegistry` | Employee hierarchy, employee operations, and employee portal |
| `exceptions` | `CapacityExceededException`, `DuplicateProductException`, `InvalidPriceException`, `InvalidProductException`, `InvalidQuantityException`, `InvalidSectionException`, `NotFoundException` | Custom runtime exceptions for validation and error handling |
| `input` | `ConsoleInput` | Shared console input helper with EXIT support |
| `inventory` | `Inventory`, `InventoryConsoleDemo` | Inventory storage, product lookup, stock management, and demo testing |
| `menu` | `StoreMenus` | Contains menu/session flows for all portals |
| `products` | `Products` | Represents a grocery product with ID, price, and quantity |
| `shelf` | `Shelf` | Represents individual store shelves and shelf-level product management |
| `test` | JUnit test classes | Unit tests for system behavior |

### Patterns Used

- **Inheritance**: `Customer` is the abstract base class for `RegularCustomer` and `VIPCustomer`, while `Employee` is the abstract base class for `Manager` and `Stocker`
- **Polymorphism**: Customer and employee objects are used through their parent types, allowing different runtime behavior depending on subclass
- **Composition**: A `Customer` owns a `ShoppingCart`, and the checkout process builds receipts from the customer's cart contents
- **Aggregation**: `Inventory`, `Shelf`, and `Aisles` all manage collections of `Products`
- **Encapsulation**: Fields are kept private/protected and accessed through methods
- **Single Responsibility**: Each class has a focused role — `Checkout` handles receipts, `Inventory` handles stock, `AccountService` handles account actions, etc.
- **Utility Class Design**: Classes like `Checkout`, `ConsoleInput`, `StoreMenus`, and `AccountService` use static methods and private constructors because they are service/helper classes

---

## Installation

### Prerequisites
- Java JDK 17 or higher (21 Preferred for Unit Testing)
- Git
- A Java IDE or command line terminal
- JUnit 5 if running tests manually

### Command Line

# 1. Clone repository from Terminal into project folder
```bash
mkdir GroceryStore-CS151-Spring-2026
cd GroceryStore-CS151-Spring-2026
git clone https://github.com/oops408/GroceryStore-CS151-Spring-2026
cd GroceryStore-CS151-Spring-2026
```

# 2. Compile all source files
From the project root (where `src` and `pom.xml` live):

**Option A — `javac` (no Maven):**
```bash
javac -cp src -sourcepath src src/Main.java
```

**Option B — Maven:**
```bash
mvn -q compile
```

# 3. Run the application
After compiling with **Option A**, run:

```bash
java -cp src Main
```

After **Maven**, compiled classes are under `target/classes`. From the project root:

```bash
java -cp target/classes Main
```

The program starts in the terminal with the grocery store role menu. Type **`EXIT`** at any prompt to quit immediately (case-insensitive).

## Usage

Video Link: https://www.youtube.com/watch?v=gZQiSIkY_ps
This project simulates a grocery store: **customers** shop (cart, aisles, checkout), and **employees** (stockers and managers) manage shelves and inventory.

### First launch
On startup, the program runs built-in inventory checks in the terminal (you will see test output). After that, the **main menu** appears:

| Choice | Role |
|--------|------|
| 1 | Regular customer |
| 2 | VIP customer |
| 3 | Employee |
| 4 | Exit program |

Pick a number, then follow the **submenu** for that role. Use the **Back** option in customer submenus to return here.

### Regular customer
- View profile, manage **your** cart (add / remove / clear items).
- **View aisles** — browse by aisle number, optionally buy (items go to your cart unless you choose otherwise when prompted).
- **Checkout** — prints a receipt (transaction ID, customer ID, subtotal, VIP discount if applicable, 9.5% tax, timestamp) and clears **your** cart.

### VIP customer
Same cart and shopping flow as regular, plus **View VIP benefits** (discount rate applies at checkout). In **View aisles**, you can send purchases to the regular or VIP cart when prompted.

### Employee
Opens the **Employee menu** (`EmployeeMenu`): choose **Stocker** or **Manager**, or **Exit** to return to the main grocery menu.

- **Stocker** — low stock on shelves, restock shelves, view shelves, decrease inventory stock, low stock by aisle, view inventory.
- **Manager** — add/remove/change price/restock **inventory**, view inventory, find product by ID, view customer info.

### Tips
- Enter **numbers** when the menu asks for a choice; enter **text** when adding/removing cart items by name.
- Cart items added by **name** price correctly at checkout if the name matches a product in **inventory** or on an **aisle** (case-insensitive).
- If **`javac`** fails with “major version” warnings, use a JDK version that matches or exceeds the one used to compile existing `.class` files, or delete stray `*.class` under `src` and recompile.


## Example Purchase

| Type | Details |
|------|---------|
| **Aisles** | Aisle 1 (Dairy), Aisle 2 (Fruits), Aisle 3 (Meats) |
| **Fruits (Aisle 2)** | Apples ($1.99), Bananas ($0.79), Oranges ($1.29), Strawberries ($3.99), Blueberries ($4.49), Grapes ($2.99), Mangos ($1.49), Pineapples ($3.49), Peaches ($2.29), Pears ($1.89), Watermelon ($5.99), Kiwi ($0.99), Cherries ($3.79), Plums ($2.19), Lemons ($0.89) |
| **Meats (Aisle 3)** | Chicken Breast ($6.99), Ground Beef ($5.49), Turkey ($4.99), Pork Chops ($6.49), Salmon ($8.99), Tuna ($7.49), Bacon ($5.99), Sausage ($4.49), Ham ($5.29), Steak ($10.99), Lamb Chops ($11.49), Shrimp ($9.99), Tilapia ($7.99), Cod ($8.49), Meatballs ($6.29) |
| **Sample Customer** | John Doe — Customer ID: `1` |
| **Sample Purchase** | 3 × Lemons, 2 × Shrimp |
| **Sample Total** | Subtotal: $22.65, Tax: $2.15, Total: $24.80 |


### Project layout (high level)
| Area | Package / location |
|------|-------------------|
| Entry point | `src/Main.java` |
| Menus | `menu/StoreMenus.java`, `employee/EmployeeMenu.java` |
| Customers & cart | `customers/`, `cart/` |
| Products & aisles | `products/`, `aisles/`, `data/StoreDataLoader.java` |
| Checkout | `checkout/Checkout.java` |
| Input (incl. `EXIT`) | `input/ConsoleInput.java` |
| Inventory & shelves | `inventory/`, `shelf/` |

## Contributions 
**Jonas Quiballo**
- Implemented Employee System of shelf, employee, stocker, and manager
- Developed shelf to add products into shelf, and check for low stocks
- Stocker: view low stock, restock products, and view shelves
- Manager: add/remove product to inventory, change price, and view customer info/history


**Roman Lozano**
- Organized the the menu/UI grocery store simulation: ensured the menus had proper flow into eachother and all methods were accesible through terminal. - Completed **aisles** with shelf stock and shopping,
- Completed the DataLoader to organize main, started the Foundation for products.
- Completed the saftely EXIT extra credit.
- edited the demo video
- Worked on multiple menus to be more visually appealing
- Connected Aisle functionality with restocking


**Vishalkiran Raichur**
- Customer methods and logic (Customer, Displayable, VIP Customer
- **ShoppingCart** Functionality and methods
- **Checkout** system


**Suparn Posina (oops408)**
- Creating initial repository and structure
- Core system architecture and integration (**Main**, **StoreMenu**)
- Full **Inventory** system (product storage, search, stock management, low stock tracking)
- Redesigned **Checkout** system for receipt generation
- Account handling with **AccountService**
- **EmployeeMenu** logic, (restocking, updating price, customer history, control inventory)
- Implemented Full Custom Exception Handling (CapacityExceededException, InvalidProductException, InvalidQuantityException, InvalidSectionException, DuplicateProductException, NotFoundException)
- UML diagram design and updating across full structure
- JUnit Unit Testing and Maven integration for debugging all major components
- Code Integration and Error Handling Across all subsystems
