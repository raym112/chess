import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.*;

@SuppressWarnings("serial")
public class Board extends JPanel implements MouseListener, MouseMotionListener {
    private static final String RESOURCES_WBISHOP_PNG = "wbishop.png";
    private static final String RESOURCES_BBISHOP_PNG = "bbishop.png";
    private static final String RESOURCES_WKNIGHT_PNG = "wknight.png";
    private static final String RESOURCES_BKNIGHT_PNG = "bknight.png";
    private static final String RESOURCES_WROOK_PNG = "wrook.png";
    private static final String RESOURCES_BROOK_PNG = "brook.png";
    private static final String RESOURCES_WKING_PNG = "wking.png";
    private static final String RESOURCES_BKING_PNG = "bking.png";
    private static final String RESOURCES_BQUEEN_PNG = "bqueen.png";
    private static final String RESOURCES_WQUEEN_PNG = "wqueen.png";
    private static final String RESOURCES_WPAWN_PNG = "wpawn.png";
    private static final String RESOURCES_BPAWN_PNG = "bpawn.png";
    
    private final Square[][] board;
    private final GameWindow g;
    private boolean whiteTurn;
    private Piece currPiece;
    private Square fromMoveSquare;
    private int currX;
    private int currY;

    public Board(GameWindow g) {
        this.g = g;
        board = new Square[8][8];
        setLayout(new GridLayout(8, 8, 0, 0));
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                boolean isWhite = (r + c) % 2 == 0;
                board[r][c] = new Square(this, isWhite, r, c);
                this.add(board[r][c]);
            }
        }

        initializePieces();
        this.setPreferredSize(new Dimension(400, 400));
        this.setMaximumSize(new Dimension(400, 400));
        this.setMinimumSize(this.getPreferredSize());
        this.setSize(new Dimension(400, 400));

        whiteTurn = true;
    }

    private void initializePieces() {
        // Black pieces
        board[0][0].put(new Piece(false, RESOURCES_BROOK_PNG));
        board[0][1].put(new Piece(false, RESOURCES_BKNIGHT_PNG));
        board[0][2].put(new Piece(false, RESOURCES_BBISHOP_PNG));
        board[0][3].put(new Piece(false, RESOURCES_BQUEEN_PNG));
        board[0][4].put(new Piece(false, RESOURCES_BKING_PNG));
        board[0][5].put(new Piece(false, RESOURCES_BBISHOP_PNG));
        board[0][6].put(new Piece(false, RESOURCES_BKNIGHT_PNG));
        board[0][7].put(new Piece(false, RESOURCES_BROOK_PNG));
        for (int i = 0; i < 8; i++) {
            board[1][i].put(new Piece(false, RESOURCES_BPAWN_PNG));
        }

        // White pieces
        board[7][0].put(new Piece(true, RESOURCES_WROOK_PNG));
        board[7][1].put(new Piece(true, RESOURCES_WKNIGHT_PNG));
        board[7][2].put(new Piece(true, RESOURCES_WBISHOP_PNG));
        board[7][3].put(new Piece(true, RESOURCES_WQUEEN_PNG));
        board[7][4].put(new Piece(true, RESOURCES_WKING_PNG));
        board[7][5].put(new Piece(true, RESOURCES_WBISHOP_PNG));
        board[7][6].put(new Piece(true, RESOURCES_WKNIGHT_PNG));
        board[7][7].put(new Piece(true, RESOURCES_WROOK_PNG));
        for (int i = 0; i < 8; i++) {
            board[6][i].put(new Piece(true, RESOURCES_WPAWN_PNG));
        }
    }

    public Square[][] getSquareArray() {
        return this.board;
    }

    public boolean getTurn() {
        return whiteTurn;
    }

    public void setCurrPiece(Piece p) {
        this.currPiece = p;
    }

    public Piece getCurrPiece() {
        return this.currPiece;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Square sq = board[x][y];
                if (sq == fromMoveSquare) {
                    sq.setBorder(BorderFactory.createLineBorder(Color.blue));
                }
                sq.paintComponent(g);
            }
        }

        if (currPiece != null) {
            if ((currPiece.getColor() && whiteTurn) || (!currPiece.getColor() && !whiteTurn)) {
                final Image img = currPiece.getImage();
                g.drawImage(img, currX, currY, null);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        currX = e.getX();
        currY = e.getY();

        Square sq = (Square) this.getComponentAt(new Point(e.getX(), e.getY()));

        if (sq.isOccupied()) {
            currPiece = sq.getOccupyingPiece();
            fromMoveSquare = sq;

            if ((currPiece.getColor() && !whiteTurn) || (!currPiece.getColor() && whiteTurn)) {
                return;
            }
            sq.setDisplay(false);
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Square endSquare = (Square) this.getComponentAt(new Point(e.getX(), e.getY()));

        if (currPiece != null && fromMoveSquare != null && endSquare != null) {
            ArrayList<Square> legalMoves = currPiece.getLegalMoves(this, fromMoveSquare);
            if (legalMoves.contains(endSquare)) {
                endSquare.put(currPiece);
                fromMoveSquare.removePiece();
                whiteTurn = !whiteTurn;
            } else {
                fromMoveSquare.put(currPiece);
            }
        }

        for (Square[] row : board) {
            for (Square sq : row) {
                sq.setBorder(null);
            }
        }

        fromMoveSquare.setDisplay(true);
        currPiece = null;
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currX = e.getX() - 24;
        currY = e.getY() - 24;

        if (currPiece != null) {
            for (Square sq : currPiece.getLegalMoves(this, fromMoveSquare)) {
                sq.setBorder(BorderFactory.createLineBorder(Color.magenta));
            }
        }

        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
