package Game;

import Pieces.Piece;
import Pieces.PieceColor;
import Pieces.PieceName;
import Pieces.variants.King;
import Pieces.variants.Rook;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Model {
    public final Map<PieceColor, Player> players = Map.of(
        PieceColor.WHITE, new Player(PieceColor.WHITE),
        PieceColor.BLACK, new Player(PieceColor.BLACK)
    );

    private ArrayList<ArrayList<int[]>> moveSuggestions = new ArrayList<>();
    private Player currentPlayer = players.get(PieceColor.WHITE);
    private Piece activePiece;
    private final Runnable notifier;

    public Model(Runnable notifier) {
        this.notifier = notifier;
    }

    public ArrayList<ArrayList<int[]>> getMoveSuggestions() {
        return moveSuggestions;
    }

    public Optional<Piece> getActivePiece() {
        if (activePiece == null) {
            return Optional.empty();
        }

        return Optional.of(activePiece);
    }

    public boolean isPlayerTurn(PieceColor color) {
        return Objects.equals(currentPlayer.color, color);
    }


    private Player getOpositePlayer(Player current) {
        return Objects.equals(current, players.get(PieceColor.WHITE))
            ? players.get(PieceColor.BLACK)
            : players.get(PieceColor.WHITE);
    }

    public void toggleTurn() {
        this.checkPlayerKing();
        this.currentPlayer = this.getOpositePlayer(currentPlayer);
    }

    public void checkPlayerKing() {
        for (Player player : players.values()) {
            Player opponent = this.getOpositePlayer(player);

            PlayerState playerState = player.getState((int[] cords) -> {
                Optional<Piece> op = findPiece(cords[0], cords[1]);

                return op.map(Piece::getState);
            });

            opponent.checkKing(playerState);
        }
    }

    public void selectPiece(int x, int y) {
        Optional<Piece> p = findPiece(x, y);

        if (p.isEmpty() || !isPlayerTurn(p.get().color)) {
            return;
        }

        Piece piece = p.get();

        if (this.activePiece == null) {
            this.activePiece = piece;
            this.moveSuggestions = piece.getMoveSuggestions(
                (int[] cords) -> {
                    Optional<Piece> op = findPiece(cords[0], cords[1]);

                    return op.map(Piece::getState);
                }
            );

        } else {
            int[] cords = this.activePiece.getCoordinates();

            if (x == cords[0] && y == cords[1]) {
                this.activePiece = piece;
            }
        }

        this.notifier.run();
    }

    public void movePiece(int row, int col) {
        for (ArrayList<int[]> vector : getMoveSuggestions()) {
            for (int[]c : vector) {
                if (row == c[0] && col == c[1]) {
                    Optional<Piece> p = findPiece(row, col);

                    // handle attack
                    if (p.isPresent()) {
                        if (p.get().color != activePiece.color) {
                            dropPiece(p.get());
                        }
                    }

                    //handle castling
                    if (activePiece.name == PieceName.KING) {
                        int[] cords = activePiece.getCoordinates();
                        King king = (King) activePiece;

                        // castling
                        if (col - cords[1] != 1 && row == cords[0]) {
                            int[] rookCords = col - cords[1] > 1
                                    ? king.getShortSideRookCords()
                                    : king.getLongSideRookCords();
                            Optional<Piece> rookPiece = findPiece(rookCords[0], rookCords[1]);

                            if (rookPiece.isPresent()) {
                                Rook rook = (Rook) rookPiece.get();

                                if (rook.isCastlingAvailable() && king.isCastlingAvailable()) {
                                    activePiece.setCoordinates(new int[]{row, col});
                                    rook.setCoordinates(rook.getCastlingMove());
                                }
                            }
                        }
                    }

                    //handle move
                    activePiece.setCoordinates(new int[]{row, col});

                    break;
                }
            }
        }

        this.deselectPiece();
        this.toggleTurn();
        this.notifier.run();
    }

    public void deselectPiece() {
        this.activePiece = null;
        this.notifier.run();
    }

    public void dropPiece(Piece piece) {
        for (Player player : players.values()) {
            if (player.ownPiece(piece)) {
                player.dropPiece(piece);
            }
        }
    }

    public Optional<Piece> findPiece(int x, int y) {
        for (Player player : players.values()) {
            for (Piece p: player.pieces) {
                int[] cords = p.getCoordinates();

                if (cords[0] == x && cords[1] == y) {
                    return Optional.of(p);
                }
            }
        }
        return Optional.empty();
    }
}
