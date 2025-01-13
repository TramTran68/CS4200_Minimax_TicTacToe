#include "GameBoard.h"
#include <iostream>

GameBoard::GameBoard() : board(8, std::vector<char>(8, ' ')), winner(' ') {}

void GameBoard::display() const {
    for (const auto& row : board) {
        for (const auto& cell : row) {
            std::cout << (cell == ' ' ? '.' : cell) << ' ';
        }
        std::cout << '\n';
    }
}

bool GameBoard::applyMove(const Move& move, char player) {
    return makeMove(move.row, move.col, player); 
}

bool GameBoard::makeMove(int row, int col, char player) {
    if (!isOccupied(row, col)) {
        board[row][col] = player;
        if (checkWinFromPosition(row, col, player)) {
            winner = player;
        }
        return true;
    }
    return false;
}

void GameBoard::undoMove(int row, int col) {
    board[row][col] = ' ';
}

bool GameBoard::isOccupied(int row, int col) const {
    return board[row][col] != ' ';
}

char GameBoard::getPiece(int row, int col) const {
    return board[row][col];
}

bool GameBoard::checkWin(char player) const {
    for (int row = 0; row < 8; ++row) {
        for (int col = 0; col < 8; ++col) {
            if (board[row][col] == player && checkWinFromPosition(row, col, player)) {
                return true;
            }
        }
    }
    return false;
}

bool GameBoard::isGameOver() const {
    return winner != ' ' || getValidMoves().empty();
}

char GameBoard::getWinner() const {
    return winner;
}

std::vector<Move> GameBoard::getValidMoves() const {
    std::vector<Move> moves;
    for (int row = 0; row < 8; ++row) {
        for (int col = 0; col < 8; ++col) {
            if (!isOccupied(row, col)) {
                moves.emplace_back(row, col);
            }
        }
    }
    return moves;
}

bool GameBoard::checkWinFromPosition(int row, int col, char player) const {
    // Check all 4 directions for a win
    int directions[4][2] = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
    for (const auto& dir : directions) {
        int count = 1;
        for (int i = 1; i < 4; ++i) {
            int newRow = row + dir[0] * i;
            int newCol = col + dir[1] * i;
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8 && board[newRow][newCol] == player) {
                ++count;
            } else {
                break;
            }
        }
        for (int i = 1; i < 4; ++i) {
            int newRow = row - dir[0] * i;
            int newCol = col - dir[1] * i;
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8 && board[newRow][newCol] == player) {
                ++count;
            } else {
                break;
            }
        }
        if (count >= 4) {
            return true;
        }
    }
    return false;
}
