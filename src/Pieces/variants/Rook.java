package Pieces.variants;

import Game.PieceStateSupplier;
import Pieces.*;
import Pieces.utils.Movement;

import java.util.ArrayList;

public class Rook extends Piece {
    public Rook(PieceColor color, int[] cords) {
        super(color, PieceName.ROOK, cords, "♜");
    }

    @Override
    public ArrayList<ArrayList<int[]>> getMoveSuggestions(PieceStateSupplier pieceSupplier) {
        int[] cords = this.getCoordinates();

        ArrayList<ArrayList<int[]>> result = Movement.getHorizontalMoves(cords, color, pieceSupplier);

        result.addAll(
            Movement.getVerticalMoves(cords, color, pieceSupplier)
        );

        return result;
    }

    public boolean isCastlingAvailable() {
        return this.isAtInitialPosition;
    }

    public int[] getCastlingMove() {
        CastlingType type = this.getCastlingType();
        boolean isWhite =  this.color == PieceColor.WHITE;

        return isWhite
            ? type == CastlingType.SHORT ? new int[]{7, 5} : new int[]{7, 3}
            : type == CastlingType.SHORT ? new int[]{0, 5} : new int[]{0, 3};
    }

    public CastlingType getCastlingType() {
        int[] cords = getCoordinates();
        boolean isWhite =  this.color == PieceColor.WHITE;

        return isWhite
            ? cords[0] == 7 && cords[1] == 7 ? CastlingType.SHORT : CastlingType.LONG
            : cords[0] == 0 && cords[1] == 7 ? CastlingType.SHORT : CastlingType.LONG;
    }
}
