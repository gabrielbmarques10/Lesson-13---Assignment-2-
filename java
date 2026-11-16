import java.io.*;
import java.util.*;

public class LibraryManager {

    private static final String FILE_NAME = "books.txt";
    private static final ArrayList<String> books = new ArrayList<>();

    // Custom exception
    static class InvalidBookException extends Exception {
        public InvalidBookException(String message) {
            super(message);
        }
    }

    // Check if a book name is valid
    public static void validateBook(String name)
            throws InvalidBookException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidBookException(
                "Book name cannot be empty."
            );
        }
    }

    // Load saved books when program starts
    public static void loadBooks() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println(
                "No saved file found. Starting empty."
            );
            return;
        }

        try (BufferedReader reader =
                new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    // Each valid line starts with BOOK:
                    if (!line.startsWith("BOOK:")) {
                        throw new InvalidBookException(
                            "Corrupted line: " + line
                        );
                    }

                    String name = line.substring(5);
                    validateBook(name);
                    books.add(name);

                } catch (InvalidBookException e) {
                    System.out.println(
                        "Skipping invalid data: " + e.getMessage()
                    );
                }
            }

            System.out.println("Books loaded successfully.");

        } catch (IOException e) {
            System.out.println(
                "Error loading file: " + e.getMessage()
            );
        }
    }

    // Save books to a text file
    public static void saveBooks() {
        try (PrintWriter writer =
                new PrintWriter(new FileWriter(FILE_NAME))) {

            for (String book : books) {
                writer.println("BOOK:" + book);
            }

            System.out.println("Books saved successfully.");

        } catch (IOException e) {
            System.out.println(
                "Error saving file: " + e.getMessage()
            );
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        loadBooks();

        boolean running = true;

        while (running) {
            System.out.println("\n--- LIBRARY MENU ---");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Save Books");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter book name: ");
                    String name = scanner.nextLine();

                    try {
                        validateBook(name);
                        books.add(name);
                        System.out.println("Book added.");

                    } catch (InvalidBookException e) {
                        System.out.println(
                            "Error: " + e.getMessage()
                        );
                    }
                    break;

                case "2":
                    if (books.isEmpty()) {
                        System.out.println("No books available.");
                    } else {
                        for (String book : books) {
                            System.out.println("- " + book);
                        }
                    }
                    break;

                case "3":
                    saveBooks();
                    break;

                case "4":
                    saveBooks();
                    running = false;
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }

        scanner.close();
    }
}
