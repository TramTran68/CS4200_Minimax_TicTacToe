// Move.java
public class Move {
    public int row, col;

    public Move(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
