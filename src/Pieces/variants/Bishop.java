package Pieces.variants;

import Game.PieceStateSupplier;
import Pieces.PieceName;
import Pieces.utils.Movement;
import Pieces.Piece;
import Pieces.PieceColor;

import java.util.ArrayList;

public class Bishop extends Piece {
    public Bishop(PieceColor color, int[] cords) {
        super(color, PieceName.BISHOP, cords, "♝");
    }

    @Override
    public ArrayList<ArrayList<int[]>> getMoveSuggestions(PieceStateSupplier pieceSupplier) {
        int[] cords = this.getCoordinates();

        return Movement.getDiagonalMoves(cords, color, pieceSupplier);
    }
}
