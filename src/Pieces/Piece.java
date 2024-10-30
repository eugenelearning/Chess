package Pieces;

import Game.PieceStateSupplier;

import java.util.ArrayList;

public abstract class Piece {
    public final PieceColor color;
    public final String label;
    public final PieceName name;
    private int[] coordinates;
    protected boolean isAtInitialPosition = true;

    public Piece(PieceColor color, PieceName name, int[] initialCords, String label) {
        this.color = color;
        this.label = label;
        this.name = name;

        this.coordinates = initialCords;
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(int[] coordinates) {
        this.isAtInitialPosition = false;
        this.coordinates = coordinates;
    }

    public PieceState getState() {
        return new PieceState(this.color, this.name, this.getCoordinates(), isAtInitialPosition);
    }

    /*
    *  Ключевой момент.
    *  Расчитываем возможные ходы в виде отдельных массивов,
    *  а - ля векторов, от точки текущего располодения фигуры.
    *
    *  Имея такой вектор, мы сможем ограничить список возможных ходов случаями
    *  когда клетка в таком "векторе" занята другой фигурой, запретить ли ход или
    *  или считать такой ход атакой
    *
    *  также по набору векторов фигур соперника можно единообразно расчитывать шах
    *
    *  Например ладья находится в точке (3, 3)
    *           A
    *    |_|_|_|*|_|_|_|_|
    *    |_|_|_|*|_|_|_|_|
    *    |_|_|_|*|_|_|_|_|
    *  C |*|*|*|R|*|*|*|*| D
    *    |_|_|_|*|_|_|_|_|
    *    |_|_|_|*|_|_|_|_|
    *    |_|_|_|*|_|_|_|_|
    *    |_|_|_|*|_|_|_|_|
    *           B
    *
    *  получим 4 "вектора"
    *
    * A) {3, 0}, {3, 1}, {3, 2}
    * B) {3, 4}, {3, 5}, {3, 6}, {3, 7}
    * C) {2, 3}, {1, 3}, {0, 3}
    * D) {4, 3}, {5, 3}, {6, 3}, {7, 3}
    * */
    public abstract ArrayList<ArrayList<int[]>> getMoveSuggestions(PieceStateSupplier pieceSupplier);
}
