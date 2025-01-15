import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GameBoard board = new GameBoard();
        Player player = new Player();
        ComputerAI ai = new ComputerAI('X');
        boolean playerTurn;
        Scanner scanner = new Scanner(System.in);
        int maxDepth = 100;

        // Determine who goes first
        System.out.print("Would you like to go first? (y/n): ");
        char choice = scanner.nextLine().trim().toLowerCase().charAt(0);
        playerTurn = (choice == 'y');

        // Set computer time limit for thinking
        System.out.print("How long should the computer think about its moves (in seconds)? ");
        int timeLimit = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.println("\nGame Start! Use moves in the format '<Letter><Number>' (e.g., 'E5').");

        while (!board.isGameOver()) {
            board.display();

            if (playerTurn) {
                System.out.print("\nChoose your next move (e.g., 'E5'): ");
                String moveInput = scanner.nextLine().trim().toUpperCase();

                // Input validation and move application
                while (!isValidMoveInput(moveInput) || !applyPlayerMove(board, moveInput)) {
                    System.out.print("Invalid move. Try again (e.g., 'E5'): ");
                    moveInput = scanner.nextLine().trim().toUpperCase();
                }
            } else {
                Move bestMove = ai.getBestMove(board, maxDepth, timeLimit);
                board.applyMove(bestMove, 'X');
                System.out.println("\nComputer chose: " + moveToString(bestMove));
            }

            playerTurn = !playerTurn;
        }

        board.display();

        // Display the game result
        char winner = board.getWinner();
        if (winner == 'X') {
            System.out.println("Computer wins!");
        } else if (winner == 'O') {
            System.out.println("Player wins!");
        } else {
            System.out.println("It's a draw!");
        }
    }

    //Validates the player's input format for a move.

    private static boolean isValidMoveInput(String input) {
        if (input.length() < 2 || input.length() > 3) {
            return false;
        }
        char row = input.charAt(0);
        String colString = input.substring(1);
        return row >= 'A' && row <= 'H' && colString.matches("[1-8]");
    }

    // Converts the player's move input into a Move object and applies it to the board.
    private static boolean applyPlayerMove(GameBoard board, String input) {
        char rowChar = input.charAt(0);
        int col = Integer.parseInt(input.substring(1)) - 1; // Convert to 0-based index
        int row = rowChar - 'A'; // Convert letter to 0-based index
        return board.applyMove(new Move(row, col), 'O');
    }

    // Converts a Move object into a string format for display (e.g., "E5").
    private static String moveToString(Move move) {
        char rowChar = (char) ('A' + move.getRow());
        int col = move.getCol() + 1; // Convert to 1-based index
        return "" + rowChar + col;
    }
}
