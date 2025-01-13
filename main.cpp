#include "GameBoard.h"
#include "Player.h"
#include "ComputerAI.h"
#include "Utility.h"
#include <iostream>

int main() {
    GameBoard board;
    Player player;
    ComputerAI ai('X');
    bool playerTurn;
    int timeLimit;

    std::cout << "Would you like to go first? (y/n): ";
    char choice;
    std::cin >> choice;
    playerTurn = (choice == 'y' || choice == 'Y');

    std::cout << "How long should the computer think about its moves (in seconds)? : ";
    std::cin >> timeLimit;

    while (!board.isGameOver()) {
        board.display();
        if (playerTurn) {
            std::cout << "Choose your next move: ";
            std::string move;
            std::cin >> move;
            while (!board.applyMove(player.getMove(move, board), 'O')) {
                std::cout << "Invalid move. Try again: ";
                std::cin >> move;
            }
        } else {
            Move bestMove = ai.getBestMove(board, timeLimit);
            board.applyMove(bestMove, 'X');
            std::cout << "Computer chose: " << Utility::moveToString(bestMove) << "\n";
        }
        playerTurn = !playerTurn;
    }

    board.display();
    if (board.getWinner() == 'X') {
        std::cout << "Computer wins!\n";
    } else if (board.getWinner() == 'O') {
        std::cout << "You win!\n";
    } else {
        std::cout << "It's a draw!\n";
    }

    return 0;
}
