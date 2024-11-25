package LibraryManagementSystem;

import java.io.*;
import java.util.*;

class Book {
    private String title;
    private String author;
    private boolean isAvailable;
    Date dueDate;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.isAvailable = true;
        this.dueDate = null;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void borrowBook(int days) {
        this.isAvailable = false;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, days);
        this.dueDate = calendar.getTime();
    }

    public void returnBook() {
        this.isAvailable = true;
        this.dueDate = null;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public double calculateFine() {
        if (dueDate != null && new Date().after(dueDate)) {
            long diff = new Date().getTime() - dueDate.getTime();
            long daysOverdue = diff / (1000 * 60 * 60 * 24);
            return daysOverdue * 0.50; // Fine of $0.50 per day
        }
        return 0.0;
    }

    @Override
    public String toString() {
        return title + " by " + author + (isAvailable ? " (Available)" : " (Due on: " + dueDate + ")");
    }
}

class Library {
    private List<Book> books;
    private static final String FILE_NAME = "library.txt";

    public Library() {
        books = new ArrayList<>();
        loadBooks();
    }

    public void addBook(String title, String author) {
        books.add(new Book(title, author));
        saveBooks();
    }

    public void removeBook(String title) {
        books.removeIf(book -> book.getTitle().equalsIgnoreCase(title));
        saveBooks();
    }

    public void updateBook(String oldTitle, String newTitle, String newAuthor) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(oldTitle)) {
                // Update book details
                book = new Book(newTitle, newAuthor);
                saveBooks();
                break;
            }
        }
    }

    public void borrowBook(String title, int days) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title) && book.isAvailable()) {
                book.borrowBook(days);
                saveBooks();
                System.out.println("You borrowed: " + book.getTitle());
                return;
            }
        }
        System.out.println("Book not available for borrowing.");
    }

    public void returnBook(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title) && !book.isAvailable()) {
                double fine = book.calculateFine();
                book.returnBook();
                saveBooks();
                System.out.println("You returned: " + book.getTitle());
                if (fine > 0) {
                    System.out.println("You have a fine of: $" + fine);
                }
                return;
            }
        }
        System.out.println("Book not found or was not borrowed.");
    }

    public void listBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
            return;
        }
        for (Book book : books) {
            System.out.println(book);
        }
    }

    private void saveBooks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Book book : books) {
                writer.write(book.getTitle() + "," + book.getAuthor() + "," + book.isAvailable());
                if (!book.isAvailable()) {
                    writer.write("," + book.getDueDate().getTime());
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    private void loadBooks() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null ) {
                String[] parts = line.split(",");
                String title = parts[0];
                String author = parts[1];
                boolean isAvailable = Boolean.parseBoolean(parts[2]);
                Book book = new Book(title, author);
                if (!isAvailable) {
                    long dueDateMillis = Long.parseLong(parts[3]);
                    book.borrowBook(0); // Set as borrowed
                    book.dueDate = new Date(dueDateMillis);
                }
                books.add(book);
            }
        } catch (IOException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
    }
}

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();
        boolean exit = false;

        while (!exit) {
            System.out.println("\n*******  Library Management System  *******\n");
            System.out.print("1. Add Book");
            System.out.println("\t2. Remove Book");
            System.out.print("3. Update Book");
            System.out.println("\t4. Borrow Book");
            System.out.print("5. Return Book");
            System.out.println("\t6. List Books");
            System.out.println("7. Exit");
            System.out.print("\n*********************************************");
            System.out.print("\nEnter the choice : ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.println("\n*********************************************");
            switch (choice) {
                case 1:
                    System.out.print("Enter book title: ");
                    String titleToAdd = scanner.nextLine();
                    System.out.print("Enter book author: ");
                    String authorToAdd = scanner.nextLine();
                    library.addBook(titleToAdd, authorToAdd);
                    break;
                case 2:
                    System.out.print("Enter book title to remove: ");
                    String titleToRemove = scanner.nextLine();
                    library.removeBook(titleToRemove);
                    break;
                case 3:
                    System.out.print("Enter old book title: ");
                    String oldTitle = scanner.nextLine();
                    System.out.print("Enter new book title: ");
                    String newTitle = scanner.nextLine();
                    System.out.print("Enter new book author: ");
                    String newAuthor = scanner.nextLine();
                    library.updateBook(oldTitle, newTitle, newAuthor);
                    break;
                case 4:
                    System.out.print("Enter book title to borrow: ");
                    String titleToBorrow = scanner.nextLine();
                    System.out.print("Enter number of days to borrow: ");
                    int days = scanner.nextInt();
                    library.borrowBook(titleToBorrow, days);
                    break;
                case 5:
                    System.out.print("Enter book title to return: ");
                    String titleToReturn = scanner.nextLine();
                    library.returnBook(titleToReturn);
                    break;
                case 6:
                    library.listBooks();
                    break;
                case 7:
                    exit = true;
                    System.out.println("Thank you for using the Library Management System!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }
}