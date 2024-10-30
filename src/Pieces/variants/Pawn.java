package Pieces.variants;

import Game.PieceStateSupplier;
import Pieces.Piece;
import Pieces.PieceColor;
import Pieces.PieceName;
import Pieces.PieceState;
import Pieces.utils.Movement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class Pawn extends Piece {
    public Pawn(PieceColor color, int[] cords) {
        super(color, PieceName.PAWN, cords, "♟");
    }

    @Override
    public ArrayList<ArrayList<int[]>> getMoveSuggestions(PieceStateSupplier pieceSupplier) {
        ArrayList<ArrayList<int[]>> result = new ArrayList<>();
        ArrayList<int[]> single = getStraightSingleMoves(pieceSupplier);
        ArrayList<int[]> attacks = getAttackMoves(pieceSupplier);

        result.add(new ArrayList<>() {
            {
                addAll(single);
                addAll(attacks);
            }
        });

        if (!single.isEmpty() && isAtInitialPosition) {
            result.add(new ArrayList<>() {{
                addAll(getStraightDoubleMoves(pieceSupplier));
            }});
        }

        return result;
    }

    private ArrayList<int[]> getStraightSingleMoves(PieceStateSupplier pieceSupplier) {
        int[] cords = this.getCoordinates();

        boolean isWhite = color == PieceColor.WHITE;

        int [][] variants = isWhite
            ? new int[][]{{cords[0] - 1, cords[1]}}
            : new int[][]{{cords[0] + 1, cords[1]}};

        return Movement.getMovesFromVariants(Arrays
            .stream(variants)
            .filter(pair -> {
                Optional<PieceState> p = pieceSupplier.apply(pair);

                return p.isEmpty();
            })
            .toArray(int[][]::new),
            color,
            pieceSupplier
        );
    }

    private ArrayList<int[]> getAttackMoves(PieceStateSupplier pieceSupplier) {
        int[] cords = this.getCoordinates();

        boolean isWhite = color == PieceColor.WHITE;

        int[][] variants = isWhite
                ? new int[][]{{ cords[0] - 1, cords[1] + 1 }, {cords[0] - 1, cords[1] - 1}}
                : new int[][]{{ cords[0] + 1, cords[1] + 1 }, {cords[0] + 1, cords[1] - 1}};

        return Movement.getMovesFromVariants(
            Arrays
                .stream(variants)
                .filter(pair -> {
                    Optional<PieceState> p = pieceSupplier.apply(pair);

                    return p.isPresent() && p.get().color() != color;
                })
                .toArray(int[][]::new),
                color,
                pieceSupplier
        );
    }

    private ArrayList<int[]> getStraightDoubleMoves(PieceStateSupplier pieceSupplier) {
        int[] cords = this.getCoordinates();
        boolean isWhite = color == PieceColor.WHITE;

        int [][] variants = isWhite
            ? new int[][]{{ cords[0] - 2, cords[1] }}
            : new int[][]{{ cords[0] + 2, cords[1] }};

        return Movement.getMovesFromVariants(
            variants,
            color,
            pieceSupplier
        );
    }
}
