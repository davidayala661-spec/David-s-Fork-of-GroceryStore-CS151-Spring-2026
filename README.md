# Grocery Store System – CS151 Project

Project Done by:
Jonas Quiballo  
Roman Lozano  
Vishalkiran Raichur  
Suparn Posina  

## Overview 
This project simulates a grocery store system using Java. It demonstrates object-oriented programming concepts such as inheritance, abstraction, and interfaces.

Users can view customer information, manage a shopping cart, add or remove items, and view VIP customer benefits through a menu-driven interface.

## Design 
- Products
- Employee system
- Customer system
- Shopping cart
- inventory
- Aisles
- Terminal UI 
- exception handling 

## Installation instructions

### Prerequisites
- **Java JDK 17 or newer** (matches `pom.xml` compiler settings)
- **Git** (to clone the repository)

### Get the code from GitHub
Clone the repository, then enter the project folder:

```bash
git clone https://github.com/oops408/GroceryStore-CS151-Spring-2026.git
cd GroceryStore-CS151-Spring-2026
```

(SSH: `git clone git@github.com:oops408/GroceryStore-CS151-Spring-2026.git`)

### Compile
From the project root (where `src` and `pom.xml` live):

**Option A — `javac` (no Maven):**
```bash
javac -cp src -sourcepath src src/Main.java
```

**Option B — Maven:**
```bash
mvn -q compile
```

### Run `Main`
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
    - Jonas Quiballo



    - Roman Lozano 
    Organized the the menu/UI grocery store simulation: ensured the menus had proper flow into eachother and all methods were accesible through terminal. Completed **aisles** with shelf stock and shopping, Compelted the DataLoader to organize main, started the Foundation for products. Compelted the saftely EXIT extra credit.


    - Vishalkiran Raichur  




    -Suparn Posina
