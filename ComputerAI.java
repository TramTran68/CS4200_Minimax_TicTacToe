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

        /* Ideally blocks is when opponent pieces are in a row or column greater than 2, "Blocking" is perfered when boths side are blocked  */
        /* Checking for any rows */
        int blocked = 0;
        for (int row = 0; row < 8; row++) {
            int colCountComputer = 0, colCountOpponent = 0;
            for (int col = 0; col < 8; col++) {

                if (board.isOccupied(row, col) && board.getPiece(row, col) == opponentPiece) {

                    int start = col;
                    for (int i = col; i < 8; i++) {
                        if (!board.isOccupied(row, i) || board.getPiece(row, i) == computerPiece) {

                            if (board.getPiece(row, i) == computerPiece && colCountOpponent > 1)
                                blocked++; 

                            if (start - 1 >= 0 && board.getPiece(row, start - 1) == computerPiece && colCountOpponent > 1) 
                                blocked++; 

                            if (start - 1 >= 0 
                                && board.getPiece(row, start - 1) == computerPiece 
                                && board.getPiece(row, i) == computerPiece 
                                && colCountOpponent > 1) {
                                colCountOpponent = 0;
                            }

                            break;
                        }

                        col++; 
                        colCountOpponent++;
                    }
                }

                if (col >= 8)
                    continue;

                if (board.isOccupied(row, col) && board.getPiece(row, col) == computerPiece) {

                    for (int i = col; i < 8; i++) {
                        if (!board.isOccupied(row, i) || board.getPiece(row, i) == opponentPiece)
                            break;

                        col++;
                        colCountComputer++;
                    }
                }
            }

            score += scoreLine(colCountComputer, colCountOpponent, blocked);
        
        }

        /* Checking for any columns of the opponents pieces */
        for (int col = 0; col < 8; col++) {
            int rowCountComputer = 0, rowCountOpponent = 0; 
            for (int row = 0; row < 8; row++) {
                if (board.isOccupied(row, col) && board.getPiece(row, col) == opponentPiece) {

                    int start = row; 
                    for (int i = row; i < 8; i++) {

                        if (!board.isOccupied(i, col) || board.getPiece(i, col) == computerPiece) {

                            if (board.getPiece(i, col) == computerPiece && rowCountOpponent > 1)
                                blocked++;

                            if (start - 1 >= 0 && board.getPiece(start - 1, col) == computerPiece && rowCountOpponent > 1) 
                                blocked++;
                

                            break;
                        }
                        row++;
                        rowCountOpponent++;
                    }
                }

                if (row >= 8)
                    continue;
            
                if (board.isOccupied(row, col) && board.getPiece(row, col) == computerPiece) {
                    for (int i = row; i < 8; i++) {
                        if (!board.isOccupied(i, col) || board.getPiece(i, col) == opponentPiece) {
                            break;
                        }
                        row++;
                        rowCountComputer++;
                    }
                }
            }

            score += scoreLine(rowCountComputer, rowCountOpponent, blocked);
        }

        return score;
    }    
    
    // Helper function to evaluate threat levels with stronger values
    private int scoreLine(int countComputer, int countOpponent, int blocked) {
        int score = 0;
    
        // Offensive scoring
        if (countComputer == 4) score += 2000;  // Winning move
        if (countComputer == 3) score += 400;  // Strong offensive move
        if (countComputer == 2) score += 200;   // Moderate control

        // Increase score if blocked is successful 
        score += blocked * 100;
    
        // Defensive scoring (blocking opponent's win)
        if (countOpponent == 4) score -= 2000;  // Block immediate win
        if (countOpponent == 3) score -= 600;  // Block crucial threats
        if (countOpponent == 2) score -= 400;   // Block moderate threats
    
        return score;
    }    
}
