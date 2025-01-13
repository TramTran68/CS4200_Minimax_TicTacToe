#ifndef COMPUTER_AI_H
#define COMPUTER_AI_H

#include "GameBoard.h"
#include "Move.h"
#include <limits>
#include <vector>

class ComputerAI {
public:
    explicit ComputerAI(char computerPiece);
    
    Move getBestMove(GameBoard& board, int maxDepth, int timeLimit);

private:
    char computerPiece;
    char opponentPiece;

    int evaluateBoard(const GameBoard& board);
    int alphaBeta(GameBoard& board, int depth, int alpha, int beta, bool isMaximizing, int startTime, int timeLimit);
    std::vector<Move> getPossibleMoves(const GameBoard& board);
};

#endif // COMPUTER_AI_H