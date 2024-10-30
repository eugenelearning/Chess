package Game;

import Pieces.*;
import UI.UI;

import java.util.*;

public class Controller {
    private UI ui;
    private final Model model = new Model(this::refresh);

    private void refresh() {
        if (this.ui != null) {
            this.ui.render();
        }
    }

    public void setUI(UI variant) {
        this.ui = variant;
    }

    public void selectPiece(int x, int y) {
        this.model.selectPiece(x, y);
    }

    public void deselectPiece() {
        this.model.deselectPiece();
    }

    public Optional<Piece> getActivePiece() {
        return this.model.getActivePiece();
    }

    public void movePiece(int x, int y) {
        this.model.movePiece(x, y);
    }

    public Optional<Piece> getPiece(int x, int y) {
        return this.model.findPiece(x, y);
    }

    public boolean isPieceActive(Piece piece) {
        Optional<Piece> active = getActivePiece();

        return active.filter(value -> Objects.equals(value, piece)).isPresent();
    }
    
    public AbstractList<ArrayList<int[]>> getSuggestions() {
        return this.model.getMoveSuggestions();
    }
}
