import java.util.ArrayList;
import java.util.List;

public class GameBoard {
    private char[][] board;
    private char winner;
    private static final int SIZE = 8;

    public GameBoard() {
        board = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = '-';
            }
        }
        winner = '-';
    }

    public void display() {
        // Print column numbers
        System.out.print("  "); // Space for row headers
        for (int col = 1; col <= SIZE; col++) {
            System.out.print(col + " ");
        }
        System.out.println();
    
        // Print rows with row letters
        for (int i = 0; i < SIZE; i++) {
            char rowLabel = (char) ('A' + i); // Convert row index to letter
            System.out.print(rowLabel + " "); // Row label
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
    

    // Apply a move and check for a win condition
    public boolean applyMove(Move move, char player) {
        if (!isOccupied(move.getRow(), move.getCol())) {
            board[move.getRow()][move.getCol()] = player;
            if (checkWin(move.getRow(), move.getCol(), player)) {
                winner = player;
            }
            return true;
        }
        return false;
    }

    // Make a move without checking for a win condition (for AI simulation)
    public boolean makeMove(int row, int col, char player) {
        if (!isOccupied(row, col)) {
            board[row][col] = player;
            return true;
        }
        return false;
    }

    // Undo a move
    public void undoMove(int row, int col) {
        board[row][col] = '-';
    }

    public void undoMove(Move move) {
        board[move.getRow()][move.getCol()] = '-';
    }

    // Check if the game is over
    public boolean isGameOver() {
        return winner != '-' || getValidMoves().isEmpty();
    }

    // Return the winner of the game
    public char getWinner() {
        return winner;
    }

    // Check if a cell is occupied
    public boolean isOccupied(int row, int col) {
        return board[row][col] != '-';
    }

    // Return the piece at a specific position
    public char getPiece(int row, int col) {
        return board[row][col];
    }

    // Generate a list of all valid moves
    public List<Move> getValidMoves() {
        List<Move> moves = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (!isOccupied(i, j)) {
                    moves.add(new Move(i, j));
                }
            }
        }
        return moves;
    }

    // Check if a win condition is met
    public boolean checkWin(char player) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == player) {
                    if (checkWin(row, col, player)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Check win condition for a specific position
    private boolean checkWin(int row, int col, char player) {
        // Check only rows and columns
        return checkDirection(row, col, 1, 0, player) || // Row
               checkDirection(row, col, 0, 1, player);  // Column
    }    

    // Check a specific direction for a win condition
    private boolean checkDirection(int row, int col, int dRow, int dCol, char player) {
        int count = 0;
    
        for (int i = -3; i <= 3; i++) {
            int newRow = row + i * dRow;
            int newCol = col + i * dCol;
    
            // Check boundaries and matching pieces
            if (newRow >= 0 && newRow < SIZE && newCol >= 0 && newCol < SIZE && board[newRow][newCol] == player) {
                count++;
                if (count == 4) return true; // Found 4 in a row/column
            } else {
                count = 0; // Reset count if sequence breaks
            }
        }
        return false;
    }      
}
