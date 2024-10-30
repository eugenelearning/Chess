package Pieces.variants;

import Game.PieceStateSupplier;
import Pieces.*;
import Pieces.utils.Movement;

import java.util.ArrayList;
import java.util.Optional;

public class King extends Piece {

    private boolean isUnderAttack = false;

    public King(PieceColor color, int[] cords) {
        super(color, PieceName.KING, cords, "♚");
    }

    @Override
    public ArrayList<ArrayList<int[]>> getMoveSuggestions(PieceStateSupplier pieceSupplier) {
        int[] cords = this.getCoordinates();

        int[][] variants = new int[][]{
            {cords[0], cords[1] + 1},
            {cords[0], cords[1] - 1},
            {cords[0] + 1, cords[1]},
            {cords[0] - 1, cords[1]},
            {cords[0] + 1, cords[1] + 1},
            {cords[0] - 1, cords[1] - 1},
            {cords[0] + 1, cords[1] - 1},
            {cords[0] - 1, cords[1] + 1},
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

        // castling
        if (isAtInitialPosition) {
            this.getShortCastlingMoves(pieceSupplier).ifPresent(pair -> result.add(new ArrayList<>() {{
                add(pair);
            }}));

            this.getLongCastlingMoves(pieceSupplier).ifPresent(pair -> result.add(new ArrayList<>() {{
                add(pair);
            }}));
        }

        return result;
    }

    public int[] getLongSideRookCords() {
        return color == PieceColor.WHITE ? new int[]{7, 0} : new int[]{0, 0};
    }

    public int[] getShortSideRookCords() {
        return color == PieceColor.WHITE ? new int[]{7, 7} : new int[]{0, 7};
    }

    public Optional<int[]> getLongCastlingMoves(PieceStateSupplier pieceSupplier) {
        int[] cords = this.getCoordinates();
        int[] pair = new int[]{cords[0], cords[1] - 2 };

        Optional<PieceState> target = pieceSupplier.apply(pair);
        Optional<PieceState> interClose = pieceSupplier.apply(new int[]{cords[0], cords[1] + 1 });
        Optional<PieceState> interFar = pieceSupplier.apply(new int[]{cords[0], cords[1] + 1 });

        if (target.isEmpty() && interClose.isEmpty() && interFar.isEmpty()) {
            Optional<PieceState> rook = pieceSupplier.apply(this.getLongSideRookCords());

            if (rook.isPresent() && rook.get().initial()) {
                return Optional.of(pair);
            }
        }

        return Optional.empty();
    }

    public Optional<int[]> getShortCastlingMoves(PieceStateSupplier pieceSupplier) {
        int[] cords = this.getCoordinates();
        int[] pair = new int[]{cords[0], cords[1] + 2 };

        Optional<PieceState> target = pieceSupplier.apply(pair);
        Optional<PieceState> inter = pieceSupplier.apply(new int[]{cords[0], cords[1] + 1 });

        if (target.isEmpty() && inter.isEmpty()) {
            Optional<PieceState> rook = pieceSupplier.apply(this.getShortSideRookCords());

            if (rook.isPresent() && rook.get().initial()) {
                return Optional.of(pair);
            }
        }

        return Optional.empty();
    }

    public boolean isCastlingAvailable() {
        return this.isAtInitialPosition && !isUnderAttack;
    }

    public boolean isUnderAttack() {
        return isUnderAttack;
    }

    public void setUnderAttack(boolean underAttack) {
        isUnderAttack = underAttack;
    }
}
