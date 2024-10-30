package Game;

import Pieces.*;
import Pieces.variants.*;

import java.util.ArrayList;

import static Game.Config.BOARD_SIZE;

public class Player {
    private final int frontRow;
    private final int backRow;

    public final ArrayList<Piece> pieces = new ArrayList<>();
    public final PieceColor color;
    private final King king;

    public Player(PieceColor color) {
        boolean isWhite = color == PieceColor.WHITE;
        this.color = color;
        this.frontRow = isWhite ? BOARD_SIZE - 2 : 1;
        this.backRow = isWhite ? BOARD_SIZE - 1 : 0;
        //закешируем чтобы потом не искать в массиве на проверке шаха
        this.king = new King(color, new int[]{this.backRow, 4});

        this.placePieces();
    }

    public void placePieces() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            this.pieces.add(new Pawn(color, new int[]{this.frontRow, i}));
        }

        this.pieces.add(new Rook(color, new int[]{this.backRow, 0}));
        this.pieces.add(new Horse(color, new int[]{this.backRow, 1}));
        this.pieces.add(new Bishop(color, new int[]{this.backRow, 2}));
        this.pieces.add(new Queen(color, new int[]{this.backRow, 3}));
        this.pieces.add(this.king);
        this.pieces.add(new Bishop(color, new int[]{this.backRow, 5}));
        this.pieces.add(new Horse(color, new int[]{this.backRow, 6}));
        this.pieces.add(new Rook(color, new int[]{this.backRow, 7}));
    }

    public void checkKing(PlayerState opponentState) {
        int[] kingCords = this.king.getCoordinates();

        for(ArrayList<int[]> pairs: opponentState.piecesCords()) {
            for(int[] pair: pairs) {
                if (pair[0] == kingCords[0] && pair[1] == kingCords[1]) {
                    king.setUnderAttack(true);

                    return;
                }
            }
        }

        king.setUnderAttack(false);
    }

    public PlayerState getState(PieceStateSupplier pieceSupplier) {
        ArrayList<ArrayList<int[]>> result = new ArrayList<>();

        for (Piece p: pieces) {
            result.addAll(p.getMoveSuggestions(pieceSupplier));
        }

        return new PlayerState(result);
    }

    public boolean ownPiece(Piece piece) {
        return this.pieces.contains(piece);
    }

    public void dropPiece(Piece piece) {
        this.pieces.remove(piece);
    }
}
