package Pieces.variants;

import Game.PieceStateSupplier;
import Pieces.Piece;
import Pieces.PieceColor;
import Pieces.PieceName;
import Pieces.utils.Movement;

import java.util.ArrayList;

public class Horse extends Piece {
    public Horse(PieceColor color, int[] cords) {
        super(color, PieceName.HORSE, cords, "♞");
    }

    @Override
    public ArrayList<ArrayList<int[]>> getMoveSuggestions(PieceStateSupplier pieceSupplier) {
        int[] cords = this.getCoordinates();

        int[][] variants = new int[][]{
            {cords[0] + 1, cords[1] + 2},
            {cords[0] + 1, cords[1] - 2},

            {cords[0] - 1, cords[1] + 2},
            {cords[0] - 1, cords[1] - 2},

            {cords[0] + 2, cords[1] + 1},
            {cords[0] + 2, cords[1] - 1},

            {cords[0] - 2, cords[1] + 1},
            {cords[0] - 2, cords[1] - 1},
        };

        ArrayList<ArrayList<int[]>> result = new ArrayList<>();

        result.add(new ArrayList<>() {
            {
                addAll(Movement.getMovesFromVariants(
                    variants,
                    color,
                    pieceSupplier
                ));
            }
        });

        return result;
    }
}
