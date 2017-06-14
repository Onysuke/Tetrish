package gui.panel;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.ImageIO;

import static constant.Const.*;
import src.*;
import gui.MainFrame;
import gui.paint.GamePainter;

public class GamePanel extends MyPanel implements Runnable {
    private MainFrame mainFrame;
    private GamePainter painter;
    private Thread t;
    private int player;
    private Image bgImage;
    private ArrayList<GameSystem> game;

    public GamePanel(MainFrame mainFrame, int player, ArrayList<GameSystem> game) {
        this.mainFrame = mainFrame;
        this.player = player;
        this.game = game;

        try {
            bgImage = ImageIO.read(new File("gui/img/background.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }

        painter = new GamePainter();

        t = new Thread(this);
        t.start();
    }

    public void drawGame(Graphics g, GameSystem game, int player) {
        painter.drawScore(g, player, game.score, game.lineCnt);
        painter.drawField(g, player, game.field);
        painter.drawHoldBlock(g, player, game.hldBlk);
        painter.drawNextBlockList(g, player, game.nextBlk);
    }

    @Override
    public void run() {
        while (true) {
            repaint();

            try{
                Thread.sleep(10); //0.01sec
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(bgImage, 0, 0, this);

        for (int i = 0; i < MAXP && game.size() == MAXP; i++) {
            drawGame(g, game.get(i), i);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
