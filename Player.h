#ifndef PLAYER_H
#define PLAYER_H

#include "Move.h"
#include "GameBoard.h"
#include <string>

class Player {
public:
    Move getMove(const std::string& input, const GameBoard& board) const;
};

#endif