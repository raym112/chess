import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Piece {
    private final boolean color; // true for white, false for black
    private BufferedImage img;
    
    public Piece(boolean isWhite, String img_file) {
        this.color = isWhite;
        
        try {
            if (this.img == null) {
              this.img = ImageIO.read(getClass().getResource(img_file));
            }
        } catch (IOException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }
    
    public boolean getColor() {
        return color;
    }
    
    public Image getImage() {
        return img;
    }
    
    public void draw(Graphics g, Square currentSquare) {
        int x = currentSquare.getX();
        int y = currentSquare.getY();
        
        g.drawImage(this.img, x, y, null);
    }
    
    // TO BE IMPLEMENTED!
    // Return a list of every square that is "controlled" by this piece. A square is controlled
    // if the piece can legally capture into it.
    public ArrayList<Square> getControlledSquares(Square[][] board, Square start) {
        ArrayList<Square> controlledSquares = new ArrayList<>();
        int row = start.getRow();
        int col = start.getCol();
        
        // Check diagonals for controlled squares (for pawns or other pieces that can capture diagonally)
        if (row + 1 < board.length && col - 1 >= 0) {
            Square lowerLeft = board[row + 1][col - 1];
            if (lowerLeft.isOccupied() && lowerLeft.getOccupyingPiece().getColor() != start.getOccupyingPiece().getColor()) {
                controlledSquares.add(lowerLeft);
            }
        }
        if (row + 1 < board.length && col + 1 < board[row].length) {
            Square lowerRight = board[row + 1][col + 1];
            if (lowerRight.isOccupied() && lowerRight.getOccupyingPiece().getColor() != start.getOccupyingPiece().getColor()) {
                controlledSquares.add(lowerRight);
            }
        }
        if (row + 2 < board.length && col - 2 >= 0) {
            Square lowerLeft = board[row + 2][col - 2];
            if (lowerLeft.isOccupied() && lowerLeft.getOccupyingPiece().getColor() != start.getOccupyingPiece().getColor()) {
                controlledSquares.add(lowerLeft);
            }
        }
        if (row + 2 < board.length && col + 2 < board[row].length) {
            Square lowerRight = board[row + 2][col + 2];
            if (lowerRight.isOccupied() && lowerRight.getOccupyingPiece().getColor() != start.getOccupyingPiece().getColor()) {
                controlledSquares.add(lowerRight);
            }
        }
        
        return controlledSquares;
    }

    // Implement the move function here. Returns a list of legal moves for the piece.
    public ArrayList<Square> getLegalMoves(Board b, Square start) {
        ArrayList<Square> moves = new ArrayList<Square>();
        int row = start.getRow();
        int col = start.getCol();
        
        // Get traditional knight L-shaped moves
        int[][] knightMoves = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        // Add all L-shaped knight moves
        for (int[] move : knightMoves) {
            int newRow = row + move[0];
            int newCol = col + move[1];
            
            if (isValidMove(b, newRow, newCol)) {
                moves.add(b.getSquareArray()[newRow][newCol]);
            }
        }
        
        return moves;
    }
    
    // Helper method to check if the new square is valid
    private boolean isValidMove(Board b, int x, int y) {
        // Ensure the move is within bounds of the board
        if (x >= 0 && x < 8 && y >= 0 && y < 8) {
            Square targetSquare = b.getSquareArray()[x][y];
            return !targetSquare.isOccupied() || targetSquare.getOccupyingPiece().getColor() != this.color;
        }
        return false;
    }
}
