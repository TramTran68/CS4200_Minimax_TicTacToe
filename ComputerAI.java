import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ComputerAI {
    private final char computerPiece;
    private final char opponentPiece;

    public ComputerAI(char computerPiece) {
        this.computerPiece = computerPiece;
        this.opponentPiece = (computerPiece == 'X') ? 'O' : 'X';
    }

    // Select the best move for the computer using alpha-beta pruning.
    public Move getBestMove(GameBoard board, int maxDepth, int timeLimit) {
        long startTime = System.currentTimeMillis();
        Move bestMove = null;
        int bestValue = Integer.MIN_VALUE;
    
        // Iterative Deepening: progressively increase search depth
        for (int depth = 1; depth <= maxDepth; depth++) {
            Move currentBestMove = null;
            int currentBestValue = Integer.MIN_VALUE;
    
            // Generate ordered moves
            List<Move> moves = getOrderedMoves(board);
    
            for (Move move : moves) {
                if (!board.isOccupied(move.getRow(), move.getCol())) {
                    board.makeMove(move.getRow(), move.getCol(), computerPiece);
                    int moveValue = alphaBeta(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, false, startTime, timeLimit);
                    board.undoMove(move.getRow(), move.getCol());
    
                    if (moveValue > currentBestValue) {
                        currentBestValue = moveValue;
                        currentBestMove = move;
                    }
                    
                    // Time constraint handling
                    if ((System.currentTimeMillis() - startTime) / 1000.0 >= timeLimit) {
                        // System.out.println("Time limit reached! Returning best move found so far.");
                        return bestMove != null ? bestMove : currentBestMove;
                    }
                }
            }
            // Update the best move after completing each depth level
            bestMove = currentBestMove;
        }
        return bestMove;
    }
    

    // Alpha-Beta Pruning implementation.
    private int alphaBeta(GameBoard board, int depth, int alpha, int beta, boolean isMaximizing, long startTime, int timeLimit) {
        if (depth == 0 || 
            (System.currentTimeMillis() - startTime) / 1000.0 >= timeLimit || 
            board.checkWin(computerPiece) || 
            board.checkWin(opponentPiece)) {
            return evaluateBoard(board);
        }
    
        List<Move> moves = getOrderedMoves(board);
    
        if (isMaximizing) {
            int value = Integer.MIN_VALUE;
            for (Move move : moves) {
                board.makeMove(move.getRow(), move.getCol(), computerPiece);
                value = Math.max(value, alphaBeta(board, depth - 1, alpha, beta, false, startTime, timeLimit));
                board.undoMove(move.getRow(), move.getCol());
                alpha = Math.max(alpha, value);
                if (alpha >= beta) break; // Prune
            }
            return value;
        } else {
            int value = Integer.MAX_VALUE;
            for (Move move : moves) {
                board.makeMove(move.getRow(), move.getCol(), opponentPiece);
                value = Math.min(value, alphaBeta(board, depth - 1, alpha, beta, true, startTime, timeLimit));
                board.undoMove(move.getRow(), move.getCol());
                beta = Math.min(beta, value);
                if (alpha >= beta) break; // Prune
            }
            return value;
        }
    }
    

    // Generate and order moves based on proximity to the center
    private List<Move> getOrderedMoves(GameBoard board) {
        List<Move> moves = new ArrayList<>();
    
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (!board.isOccupied(row, col)) {
                    moves.add(new Move(row, col));
                }
            }
        }
    
        // Sort moves closer to the exact center
        moves.sort((a, b) -> {
            int distanceA = Math.abs(a.getRow() - 4) + Math.abs(a.getCol() - 4); 
            int distanceB = Math.abs(b.getRow() - 4) + Math.abs(b.getCol() - 4);
            return Integer.compare(distanceA, distanceB); // Sort closer moves first
        });
        return moves;
    }
    
    // Evaluation function
    private int evaluateBoard(GameBoard board) {
        int score = 0;
        int center = 4; // Center of an 8x8 board
    
        // Full Board Evaluation considering entire positions
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board.isOccupied(row, col)) {
                    if (board.getPiece(row, col) == computerPiece) {
                        // Strong bonus for center control
                        if ((row == center && col == center) || (row == center - 1 && col == center - 1) || 
                            (row == center - 1 && col == center) || (row == center && col == center - 1)) {
                            score += 10;  // Encourage center control more directly
                        }
                    } else if (board.getPiece(row, col) == opponentPiece) {
                        if ((row == center && col == center) || (row == center - 1 && col == center - 1) || 
                            (row == center - 1 && col == center) || (row == center && col == center - 1)) {
                            score -= 10; // Block center for opponent
                        }
                    }
                }
            }
        }
    
        // Row and column control with stronger threat/block handling
        for (int row = 0; row < 8; row++) {
            int rowCountComputer = 0, rowCountOpponent = 0;
            for (int col = 0; col < 8; col++) {
                if (board.isOccupied(row, col)) {
                    if (board.getPiece(row, col) == computerPiece) rowCountComputer++;
                    else if (board.getPiece(row, col) == opponentPiece) rowCountOpponent++;
                }
            }
            score += scoreLine(rowCountComputer, rowCountOpponent);
        }
    
        for (int col = 0; col < 8; col++) {
            int colCountComputer = 0, colCountOpponent = 0;
            for (int row = 0; row < 8; row++) {
                if (board.isOccupied(row, col)) {
                    if (board.getPiece(row, col) == computerPiece) colCountComputer++;
                    else if (board.getPiece(row, col) == opponentPiece) colCountOpponent++;
                }
            }
            score += scoreLine(colCountComputer, colCountOpponent);
        }
    
        return score;
    }    
    
    // Helper function to evaluate threat levels with stronger values
    private int scoreLine(int countComputer, int countOpponent) {
        int score = 0;
    
        // Offensive scoring
        if (countComputer == 4) score += 1000;  // Winning move
        else if (countComputer == 3) score += 300;  // Strong offensive move
        else if (countComputer == 2) score += 50;   // Moderate control
    
        // Defensive scoring (blocking opponent's win)
        if (countOpponent == 4) score -= 1000;  // Block immediate win
        else if (countOpponent == 3) score -= 500;  // Block crucial threats
        else if (countOpponent == 2) score -= 100;   // Block moderate threats
    
        return score;
    }    
}
