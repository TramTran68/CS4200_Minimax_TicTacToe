#include "Player.h"
#include <stdexcept>

Move Player::getMove(const std::string& input, const GameBoard& board) const {
    if (input.length() != 2) throw std::invalid_argument("Invalid input length");

    int row = input[0] - 'A';
    int col = input[1] - '1';
    if (row < 0 || row >= 8 || col < 0 || col >= 8 || board.getValidMoves().empty()) {
        throw std::invalid_argument("Invalid move");
    }
    return {row, col};
}