package src;

import java.util.*;
import java.awt.*;

import static constant.Const.*;
import gui.panel.GamePanel;
import gui.paint.GamePainter;

public class GameMain1 extends GameMain {
    private GamePanel gamePanel;
    private GamePainter painter;
    private GameSystem game;
    private Thread t;
    private Boolean isKeyDown = false;
    private int contKey = -1;
    private ArrayList<Integer> keyCommand;

    public GameMain1(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        painter = new GamePainter();
        game = new GameSystem();
        keyCommand = new ArrayList<Integer>(Collections.nCopies(7, 0));

        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        while (!game.getIsGameOver()) {
            if (onGame) {
                if (isKeyDown && time > 5) {
                    time = 0;
                    game.command(contKey);
                }

                int lv = game.getLineCnt() / 5 + 1;
                if (lv > SPEED_LV.length) lv = SPEED_LV.length;

                if (time > SPEED_LV[lv - 1]) {
                    game.update();
                    time = 0;
                }

                time++;
            }

            try{
                Thread.sleep(10); //0.01sec
            }catch (InterruptedException e){
                e.printStackTrace();
            }

            gamePanel.repaint();
        }

        onGame = false;
        while (!onGame) {
            gamePanel.repaint();

            try{
                Thread.sleep(10); //0.01sec
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void keyAction(int key) {
        if (key == SPACE && !onGame && !game.getIsGameOver()) {
            onGame = true;
            return;
        }

        //Pause
        if (key == ESC && onGame) {
            onGame = false;
            time = -1;
            return;
        }
        //GameOver
        if (game.getIsGameOver()) {
            if (key == ESC) gamePanel.backToMenu();
            return;
        }

        if (isKeyDown) return;
        else if (key == DOWN) {
            isKeyDown = true;
            contKey = DOWN;
            return;
        }

        game.command(key);
    }

    public void keyReleased() {
        isKeyDown = false;
    }

    public void drawGame(Graphics g) {
        painter.draw(g, game, 0, 1);
    }

    public Boolean checkCommand() {
        for (int i = 0; i < keyCommand.size(); i++) {
            if (TETRISH[i] != keyCommand.get(i)) return false;
        }

        return true;
    }
}
