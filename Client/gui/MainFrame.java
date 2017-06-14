package gui;

import java.awt.*;
import javax.swing.*;

import static constant.Const.*;
import gui.panel.*;

public class MainFrame extends JFrame {
    private Container CP;

    public MainFrame(String title) {
        CP = getContentPane();

        setFrame(title, MENU_W, MENU_H, new MenuPanel(this));
        //setFrame(title, GAME_W / 2, GAME_H, new GamePanel(this, 1));
    }

    private void setFrame(String title, int width, int height, MyPanel panel) {
        setTitle(title);
        setSize(width, height);
        setResizable(false);
        setLocationRelativeTo(null); //center window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close


        panel.setLayout(null);

        // if (title.equals("Menu")) {
        //     JTextArea tx = new JTextArea();
        //     tx.setBounds(350, 342, 100, 18);
        //     panel.add(tx);
        // }

        addKeyListener(panel);
        addMouseListener(panel);
        addMouseMotionListener(panel);

        CP.add(panel);
    }

    public void switchPanel(JPanel panel, String str) {
        removeKeyListener((MyPanel)panel);
        removeMouseListener((MyPanel)panel);
        removeMouseMotionListener((MyPanel)panel);
        panel.removeAll();
        CP.remove(panel);

        if (str.equals("1 PLAYER")) {
            System.out.println("1 PLAYER GAME");
            setFrame("Game", GAME_W / 2, GAME_H, new GamePanel(this, 1));
        }
        else if (str.equals("2 PLAYER")) {
            System.out.println("2 PLAYER GAME");
            setFrame("Game", GAME_W, GAME_H, new GamePanel(this, 2));
        }
        else if (str.equals("MENU")) {
            System.out.println("MENU SELECTION");
            setFrame("Menu", MENU_W, MENU_H, new MenuPanel(this));
        }
        else if (str.equals("EXIT")) {
            System.out.println("See ya!");
            System.exit(0);
        }
    }
}
