# CS4200_Minimax_TicTacToe

## **Project Description**
The Tic Tac Toe Game Solver is a Java-based implementation designed to play an 8x8 Tic Tac Toe game using the **Alpha-Beta Pruning** algorithm with iterative deepening search. The objective of the game is to be the first to place four pieces consecutively in a row or a column, with no diagonal wins considered. The solver provides a playable experience where the computer makes strategic moves using a decision-making algorithm.

---

## **Approach**
1. **Algorithm**: Alpha-Beta Pruning with Iterative Deepening:
This algorithm reduces the search space by pruning branches that cannot affect the outcome, improving decision efficiency. The iterative deepening ensures the computer can evaluate moves progressively until the time limit is reached.

2. **Evaluation Function**: The evaluation function assesses the game board based on several factors:
   - **Winning Threats**: Prioritizes blocking the opponent’s winning moves.
   - **Winning Opportunities**: Users can choose between the two algorithms to solve the problem and compare their results.
   - **Center Control**: Encourages moves closer to the center of the board for strategic advantage.

3. **Features:**
   - Supports an 8x8 grid with a four-in-a-row win condition (no diagonals).
   - Players can choose whether to play first and set a time limit for the computer's decision-making process.
   - Computer plays strategically using Alpha-Beta pruning and a dynamic evaluation function.
---

## **How to Run the Code**
1. Clone or download the repository containing the project files.
2. Compile all `.java` files using your preferred Java IDE.
3. Run the Main.java file to start the game.
4. Follow the on-screen prompts.

## **How to Test**
1. Run the program and try playing against the computer.
2. Test with different time limits for the computer's decision-making process.
3. Experiment with moves to see how the computer reacts in both defensive and offensive scenarios.

---

## **File Structure**
- **ComputerAI.java**: Implements the Alpha-Beta Pruning algorithm and the evaluation function for computer decision-making.
- **GameBoard.java**: Manages the game state, including applying and undoing moves.
- **Player.java**: Handles player input and move validation.
- **Move.java**: Represents a move with row and column positions.
- **Main.java**: The main driver class for running the game.
