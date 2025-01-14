// Player.java
import java.util.Scanner;

public class Player {
    private Scanner scanner;

    public Player() {
        scanner = new Scanner(System.in);
    }

    public Move getMove(String moveInput, GameBoard board) {
        // Parse the move string input into a Move object
        String[] parts = moveInput.split(",");
        if (parts.length != 2) {
            System.out.println("Invalid format. Please use row,col format.");
            return null;
        }

        try {
            int row = Integer.parseInt(parts[0].trim());
            int col = Integer.parseInt(parts[1].trim());

            // Check if the move is valid
            if (row < 0 || row >= 8 || col < 0 || col >= 8 || board.getWinner() != '-') {
                System.out.println("Move out of bounds or invalid.");
                return null;
            }

            return new Move(row, col);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please use numeric values for row and column.");
            return null;
        }
    }
}
