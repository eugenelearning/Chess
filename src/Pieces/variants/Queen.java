package Pieces.variants;

import Game.PieceStateSupplier;
import Pieces.PieceName;
import Pieces.utils.Movement;
import Pieces.Piece;
import Pieces.PieceColor;

import java.util.ArrayList;

public class Queen extends Piece {

    public Queen(PieceColor color, int[] cords) {
        super(color, PieceName.QUEEN, cords, "♛");
    }

    @Override
    public ArrayList<ArrayList<int[]>> getMoveSuggestions(PieceStateSupplier pieceSupplier) {
        int[] cords = this.getCoordinates();

        ArrayList<ArrayList<int[]>> result = Movement.getDiagonalMoves(cords, color, pieceSupplier);

        result.addAll(Movement.getVerticalMoves(cords, color, pieceSupplier));
        result.addAll(Movement.getHorizontalMoves(cords, color, pieceSupplier));

        return result;
    }
}
