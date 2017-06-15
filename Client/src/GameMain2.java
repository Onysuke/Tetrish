package src;

import java.io.*;
import java.util.*;
import java.net.*;
import java.awt.*;

import static constant.Const.*;
import gui.panel.GamePanel;
import gui.paint.GamePainter;

public class GameMain2 extends GameMain {
    private GamePanel gamePanel;
    private GamePainter painter;
    private GameMainOp gameMainOp;
    private GameSystem game;
    private Thread t;
    private Boolean isKeyDown = false;
    private int contKey = -1;

    private Socket socket;

    public GameMain2(GamePanel gamePanel, String address) {
        this.gamePanel = gamePanel;

        painter = new GamePainter();
        game = new GameSystem();

        try {
            InetAddress addr = InetAddress.getByName(address);
            socket = new Socket(addr, 50001);
            System.out.println("Connected: " + addr);
        } catch(IOException e) {
            e.printStackTrace();
        }

        sendAllData();
        gameMainOp = new GameMainOp(socket);

        time = 0;
        onGame = true;

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

                    sendAllData();
                }

                int lv = game.getLineCnt() / 5 + 1;
                if (lv > SPEED_LV.length) lv = SPEED_LV.length;

                if (time > SPEED_LV[lv - 1]) {
                    game.update();
                    time = 0;

                    sendAllData();
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

        //GameOver
        if (game.getIsGameOver()) {
            gameMainOp.setDidEscape(true);

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

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
        sendAllData();
    }

    public void keyReleased() {
        isKeyDown = false;
    }

    public void drawGame(Graphics g) {
        painter.draw(g, game, 0);
        gameMainOp.drawGame(g);
    }

    public void sendAllData() {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            out.println("score");
            out.println(game.getScore());
            out.println("lineCnt");
            out.println(game.getLineCnt());
            out.println("grid");
            for (int i = 0; i < FIELD_H; i++) {
                for (int j = 0; j < FIELD_W; j++) {
                    out.print(game.getField().getGrid(i, j) + " ");
                }
                out.println();
            }
            out.println("hldBlk");
            out.println(game.getHldBlk());
            out.println("nextBlk");
            out.println(game.getNextBlk());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
