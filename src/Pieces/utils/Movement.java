package Pieces.utils;

import Game.PieceStateSupplier;
import Pieces.PieceColor;
import Pieces.PieceState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static Game.Config.BOARD_SIZE;

public class Movement {
    private static boolean predicateMoveAppend(ArrayList<int[]> store, int[] cords, PieceColor color, PieceStateSupplier pieceSupplier) {
        Optional<PieceState> p = pieceSupplier.apply(cords);

        if (p.isPresent() && p.get().color() == color) {
            return true;
        }

        store.add(cords);

        return p.isPresent();
    }

    @SafeVarargs
    public static ArrayList<ArrayList<int[]>> combineMoves(ArrayList<int[]>... items) {
        ArrayList<ArrayList<int[]>> result = new ArrayList<>();

        for (ArrayList<int[]> item : items) {
            if (!item.isEmpty()) {
                result.add(item);
            }
        }

        return result;
    }

    public static ArrayList<int[]> getMovesFromVariants(int[][] variants, PieceColor color, PieceStateSupplier pieceSupplier) {
        ArrayList<int[]> result = new ArrayList<>();

        for (int[] pair : variants) {
            if (Arrays.stream(pair).allMatch(x -> x >= 0) && predicateMoveAppend(result, pair, color, pieceSupplier)) {
                continue;
            }

            result.add(pair);
        }

        return result;
    }

    public static ArrayList<ArrayList<int[]>> getHorizontalMoves(int[] cords, PieceColor color, PieceStateSupplier pieceSupplier) {
        ArrayList<int[]> forward = new ArrayList<>();
        ArrayList<int[]> backward = new ArrayList<>();

        for (int i = cords[1] + 1 ; i < BOARD_SIZE; i++) {
            if (predicateMoveAppend(forward, new int[]{cords[0], i}, color, pieceSupplier)) {
                break;
            }
        }

        for (int i = Math.abs(cords[1] - 1); i >= 0; i--) {
            if (predicateMoveAppend(backward, new int[]{cords[0], i}, color, pieceSupplier)) {
                break;
            }
        }

        return combineMoves(forward, backward);
    }

    public static ArrayList<ArrayList<int[]>> getVerticalMoves(int[] cords, PieceColor color, PieceStateSupplier pieceSupplier) {
        ArrayList<int[]> forward = new ArrayList<>();
        ArrayList<int[]> backward = new ArrayList<>();

        for (int i = cords[0] + 1; i < BOARD_SIZE; i++) {
            if (predicateMoveAppend(forward, new int[]{i, cords[1]}, color, pieceSupplier)) {
                break;
            }
        }

        for (int i = Math.abs(cords[0] - 1); i >= 0; i--) {
            if (predicateMoveAppend(backward, new int[]{i, cords[1]}, color, pieceSupplier)) {
                break;
            }
        }

        return combineMoves(forward, backward);
    }

    public static ArrayList<ArrayList<int[]>> getDiagonalMoves(int[] cords, PieceColor color, PieceStateSupplier pieceSupplier) {
        ArrayList<int[]> forwardUp = new ArrayList<>();
        ArrayList<int[]> forwardDown = new ArrayList<>();
        ArrayList<int[]> backwardUp = new ArrayList<>();
        ArrayList<int[]> backwardDown = new ArrayList<>();

        for (int i = cords[0] + 1, k = cords[1] + 1; i < BOARD_SIZE || k < BOARD_SIZE ; i++, k++) {
            if (predicateMoveAppend(forwardUp, new int[]{i, k}, color, pieceSupplier)) {
                break;
            }
        }

        for (int i = cords[0] + 1, k = Math.abs(cords[1] - 1); i < BOARD_SIZE || k >= 0 ; i++, k--) {
            if (predicateMoveAppend(forwardDown, new int[]{i, k}, color, pieceSupplier)) {
                break;
            }
        }

        for (int i = Math.abs(cords[0] - 1), k = Math.abs(cords[1] - 1); i >= 0 || k > 0; i--, k--) {
            if (predicateMoveAppend(backwardDown, new int[]{i, k}, color, pieceSupplier)) {
                break;
            }
        }

        for (int i = Math.abs(cords[0] - 1), k = cords[1] + 1; i >= 0 || k < BOARD_SIZE; i--, k++) {
            if (predicateMoveAppend(backwardUp, new int[]{i, k}, color, pieceSupplier)) {
                break;
            }
        }

        return combineMoves(forwardDown, forwardUp, backwardDown, backwardUp);
    }
}
