package UI.canvas;

import Game.Controller;
import UI.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

import static Game.Config.BOARD_SIZE;

public class Board extends JComponent implements UI {
    private final Cell[][] cells = new Cell[BOARD_SIZE][BOARD_SIZE];
    private final int cellSize;
    private final int labelsSize;
    private final Controller controller;

    public Board(int size, Controller c) {
        this.cellSize = size;
        this.labelsSize = size;
        this.controller = c;
    }

    @Override
    public void render() {
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D canvas = (Graphics2D) g;

        this.renderBackground(canvas);

        for (int row = 0; row < BOARD_SIZE; row++) {
            this.renderLabels(canvas, row);

            for (int col = 0; col < BOARD_SIZE; col++) {
                this.renderCell(row, col);
            }
        }
    }

    private void renderBackground(Graphics2D canvas) {
        Rectangle2D.Double background = new Rectangle2D.Double(0, 0, cellSize * 10, cellSize * 10);
        Rectangle2D.Double outline = new Rectangle2D.Double(cellSize - 2, cellSize - 2, cellSize * 8 + 4, cellSize * 8 + 4);

        canvas.setColor(Config.COLOR_BLACK);
        canvas.fill(background);

        canvas.setColor(Config.COLOR_GREY);
        canvas.fill(outline);
    }

    private void renderLabels(Graphics2D canvas, int row) {
        canvas.setColor(Config.COLOR_WHITE);
        canvas.setFont(Config.LABEL_FONT);

        this.renderHorizontalLabel(canvas, row);
        this.renderVerticalLabel(canvas, row);
    }

    private void renderHorizontalLabel(Graphics2D canvas, int row) {
        // 65 - utf код заглавной А, 66 - B, 67 - C итд
        String label = Character.toString(65 + row);
        int[] offsets = getLabelsOffsets(canvas, label);

        canvas.drawString(label, row * labelsSize + labelsSize + labelsSize / 2 - offsets[0], labelsSize - offsets[1]);
    }

    private void renderVerticalLabel(Graphics2D canvas, int row) {
        String label = Integer.toString(row + 1);
        int[] offsets = getLabelsOffsets(canvas, label);

        canvas.drawString(label, labelsSize / 2 - offsets[0], (row + 1) * labelsSize + labelsSize / 2 + offsets[1]);
    }

    private int[] getLabelsOffsets(Graphics2D canvas, String label) {
        FontMetrics fm = canvas.getFontMetrics();

        return new int[]{fm.stringWidth(label) / 2,fm.getHeight() / 2};
    }

    private void renderCell(int row, int col) {
        if (this.cells[row][col] == null) {
            this.cells[row][col] = new Cell(row, col, this.controller);
        }

        Cell c = this.cells[row][col];

        c.setSize(cellSize, cellSize);
        c.setBounds(
            cellSize * col + labelsSize,
            cellSize * row + labelsSize,
            cellSize,
            cellSize
        );

        this.add(c);
    }
}
