#ifndef GAMEBOARD_H
#define GAMEBOARD_H

#include <vector>
#include <string>
#include "Move.h"

class GameBoard {
public:
    GameBoard();
    void display() const;
    bool makeMove(int row, int col, char player);
    bool applyMove(const Move& move, char player);
    void undoMove(int row, int col);
    bool isOccupied(int row, int col) const;
    char getPiece(int row, int col) const;
    bool checkWin(char player) const;
    bool isGameOver() const;
    char getWinner() const;
    std::vector<Move> getValidMoves() const;

private:
    std::vector<std::vector<char>> board;
    char winner;
    bool checkWinFromPosition(int row, int col, char player) const;
};

#endif
