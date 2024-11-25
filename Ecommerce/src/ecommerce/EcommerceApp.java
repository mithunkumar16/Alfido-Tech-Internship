package ecommerce;

import java.util.Scanner;

public class EcommerceApp {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        ProductDAO productDAO = new ProductDAO();
        Cart cart = new Cart();
        OrderDAO orderDAO = new OrderDAO();

        Scanner scanner = new Scanner(System.in);
        boolean isLoggedIn = false;
        int currentUserId = -1;

        System.out.println("Welcome to the E-commerce Application!");

        while (true) {
            if (!isLoggedIn) {
                System.out.println("\nMain Menu:");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter username: ");
                        String username = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();
                        System.out.print("Enter email: ");
                        String email = scanner.nextLine();
                        System.out.print("Enter address: ");
                        String address = scanner.nextLine();
                        if (userDAO.registerUser(username, password, email, address)) {
                            System.out.println("Registration successful! You can now log in.");
                        } else {
                            System.out.println("Registration failed. Try again.");
                        }
                        break;

                    case 2:
                        System.out.print("Enter username: ");
                        String loginUsername = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String loginPassword = scanner.nextLine();
                        if (userDAO.loginUser(loginUsername, loginPassword)) {
                            isLoggedIn = true;
                            currentUserId = userDAO.getUserId(loginUsername); // Assume `getUserId` is implemented in UserDAO
                            System.out.println("Login successful! Welcome, " + loginUsername + ".");
                        } else {
                            System.out.println("Invalid credentials. Please try again.");
                        }
                        break;

                    case 3:
                        System.out.println("Thank you for using the E-commerce Application. Goodbye!");
                        System.exit(0);

                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } else {
                System.out.println("\nUser Menu:");
                System.out.println("1. View Products");
                System.out.println("2. Add Product to Cart");
                System.out.println("3. View Cart");
                System.out.println("4. Place Order");
                System.out.println("5. Logout");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        productDAO.viewProducts();
                        break;

                    case 2:
                        System.out.print("Enter Product ID to add to cart: ");
                        int productId = scanner.nextInt();
                        System.out.print("Enter Quantity: ");
                        int quantity = scanner.nextInt();
                        cart.addToCart(productId, quantity);
                        System.out.println("Product added to cart.");
                        break;

                    case 3:
                        cart.viewCart();
                        break;

                    case 4:
                        if (orderDAO.placeOrder(currentUserId, cart.getCartItems())) {
                            System.out.println("Order placed successfully!");
                            cart.clearCart(); // Clear cart after order placement
                        } else {
                            System.out.println("Order placement failed. Try again.");
                        }
                        break;

                    case 5:
                        isLoggedIn = false;
                        currentUserId = -1;
                        System.out.println("Logged out successfully.");
                        break;

                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        }
    }
}


