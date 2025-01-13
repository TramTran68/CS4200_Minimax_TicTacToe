#include "ComputerAI.h"
#include "GameBoard.h"
#include <ctime>
#include <algorithm>

ComputerAI::ComputerAI(char computerPiece) : computerPiece(computerPiece) {
    opponentPiece = (computerPiece == 'X') ? 'O' : 'X';
}

Move ComputerAI::getBestMove(GameBoard& board, int maxDepth, int timeLimit) {
    int startTime = std::clock();
    Move bestMove;
    int bestValue = std::numeric_limits<int>::min();

    for (const auto& move : getPossibleMoves(board)) {
        board.makeMove(move.row, move.col, computerPiece);
        int moveValue = alphaBeta(board, maxDepth, std::numeric_limits<int>::min(), std::numeric_limits<int>::max(), false, startTime, timeLimit);
        board.undoMove(move.row, move.col);

        if (moveValue > bestValue) {
            bestValue = moveValue;
            bestMove = move;
        }

        if ((std::clock() - startTime) / CLOCKS_PER_SEC >= timeLimit) {
            break;
        }
    }

    return bestMove;
}

int ComputerAI::alphaBeta(GameBoard& board, int depth, int alpha, int beta, bool isMaximizing, int startTime, int timeLimit) {
    if (depth == 0 || (std::clock() - startTime) / CLOCKS_PER_SEC >= timeLimit || board.checkWin(computerPiece) || board.checkWin(opponentPiece)) {
        return evaluateBoard(board);
    }

    if (isMaximizing) {
        int value = std::numeric_limits<int>::min();
        for (const auto& move : getPossibleMoves(board)) {
            board.makeMove(move.row, move.col, computerPiece);
            value = std::max(value, alphaBeta(board, depth - 1, alpha, beta, false, startTime, timeLimit));
            board.undoMove(move.row, move.col);
            alpha = std::max(alpha, value);
            if (alpha >= beta) {
                break;
            }
        }
        return value;
    } else {
        int value = std::numeric_limits<int>::max();
        for (const auto& move : getPossibleMoves(board)) {
            board.makeMove(move.row, move.col, opponentPiece);
            value = std::min(value, alphaBeta(board, depth - 1, alpha, beta, true, startTime, timeLimit));
            board.undoMove(move.row, move.col);
            beta = std::min(beta, value);
            if (alpha >= beta) {
                break;
            }
        }
        return value;
    }
}

int ComputerAI::evaluateBoard(const GameBoard& board) {
    // Simple evaluation: Count rows, columns with 2+ pieces
    int score = 0;

    for (int row = 0; row < 8; ++row) {
        int rowCountComputer = 0, rowCountOpponent = 0;
        for (int col = 0; col < 8; ++col) {
            if (board.isOccupied(row, col)) {
                if (board.getPiece(row, col) == computerPiece) {
                    ++rowCountComputer;
                } else if (board.getPiece(row, col) == opponentPiece) {
                    ++rowCountOpponent;
                }
            }
        }
        score += (rowCountComputer >= 2) ? rowCountComputer : 0;
        score -= (rowCountOpponent >= 2) ? rowCountOpponent : 0;
    }

    for (int col = 0; col < 8; ++col) {
        int colCountComputer = 0, colCountOpponent = 0;
        for (int row = 0; row < 8; ++row) {
            if (board.isOccupied(row, col)) {
                if (board.getPiece(row, col) == computerPiece) {
                    ++colCountComputer;
                } else if (board.getPiece(row, col) == opponentPiece) {
                    ++colCountOpponent;
                }
            }
        }
        score += (colCountComputer >= 2) ? colCountComputer : 0;
        score -= (colCountOpponent >= 2) ? colCountOpponent : 0;
    }

    return score;
}

std::vector<Move> ComputerAI::getPossibleMoves(const GameBoard& board) {
    std::vector<Move> moves;
    for (int row = 0; row < 8; ++row) {
        for (int col = 0; col < 8; ++col) {
            if (!board.isOccupied(row, col)) {
                moves.emplace_back(row, col);
            }
        }
    }
    return moves;
}