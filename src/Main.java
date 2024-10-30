import Game.Controller;
import UI.canvas.Board;

import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        JFrame f = new JFrame("Game");
        Controller game = new Controller();
        Board ui = new Board(50, game);

        game.setUI(ui);

        f.setSize(500, 525);
        f.add(ui);
        f.setVisible(true);
        f.setResizable(false);
    }
}