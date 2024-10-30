package UI.canvas;

import Game.Controller;
import Pieces.Piece;
import Pieces.PieceColor;
import Pieces.PieceName;
import Pieces.variants.King;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Optional;

public class Cell extends JComponent {
    private final int row;
    private final int col;
    private final Controller controller;

    public Cell(int r, int c, Controller g) {
        this.row = r;
        this.col = c;
        this.controller = g;

        this.listenEvents();
    }

    private void listenEvents() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onMousePressed();
            }
        });
    }

    private void onMousePressed() {
        Optional<Piece> active = this.controller.getActivePiece();

        if (active.isEmpty()) {
            this.controller.selectPiece(this.row, this.col);

            return;
        }

        int[] cords = active.get().getCoordinates();

        if (this.row == cords[0] && this.col == cords[1]) {
            this.controller.deselectPiece();
        } else {
            this.controller.movePiece(this.row, this.col);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D canvas = (Graphics2D) g;

        Rectangle2D.Double r = new Rectangle2D.Double(0, 0, this.getWidth(), this.getHeight());
        g.setColor(this.getCellColor());
        canvas.fill(r);

        this.renderCellPiece(canvas);
    }

    private Color getCellColor() {
        boolean isEvenRow = row % 2 == 0;
        boolean isEvenCol = col % 2 == 0;

        Optional<Piece> piece = this.controller.getPiece(row, col);
        Optional<Piece> active = this.controller.getActivePiece();

        if (piece.isPresent() && this.controller.isPieceActive(piece.get())) {
            return Config.CELL_COLOR_ACTIVE;
        }

        if (active.isPresent()) {
            for (ArrayList<int[]> vector : this.controller.getSuggestions()) {
                for (int[] cords : vector) {
                    if (this.row == cords[0] && this.col == cords[1]) {
                        return isEvenRow
                            ? !isEvenCol ? Config.CELL_COLOR_DARK_HINTED: Config.CELL_COLOR_LIGHT_HINTED
                            : isEvenCol ? Config.CELL_COLOR_DARK_HINTED : Config.CELL_COLOR_LIGHT_HINTED;
                    }
                }
            }
        }

        return isEvenRow
            ? !isEvenCol ? Config.CELL_COLOR_DARK : Config.CELL_COLOR_LIGHT
            : isEvenCol ? Config.CELL_COLOR_DARK : Config.CELL_COLOR_LIGHT;
    }

    private void renderCellPiece(Graphics2D canvas) {
        Optional<Piece> p = this.controller.getPiece(row, col);

        if (p.isPresent()) {
            Piece piece = p.get();
            String label = piece.label;
            boolean isWhite = piece.color == PieceColor.WHITE;

            canvas.setFont(Config.PIECE_FONT);
            FontMetrics fm = canvas.getFontMetrics();

            int textXOffset = fm.stringWidth(label) / 2;

            canvas.setColor(isWhite ? Config.PIECE_COLOR_LIGHT : Config.PIECE_COLOR_DARK);

            if (piece.name == PieceName.KING) {
                King king = (King) piece;


                if (king.isUnderAttack()) {
                    canvas.setColor(Config.COLOR_RED);
                }
            }

            canvas.drawString(
                label,
                (this.getWidth() / 2) - textXOffset,
                fm.getAscent()
            );
        }
    }
}
