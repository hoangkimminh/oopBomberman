package oop;

import oop.Audio.AudioManager;
import oop.GameController.Board;
import oop.GameState.GameStateManager;
import oop.Graphic.Render;
import oop.Input.InputHandle;
import oop.Setting.Size;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.*;


public class Bomberman extends Canvas {

    public static InputHandle inputHandle = new InputHandle();
    public static JFrame frame = new JFrame();
    public static GameStateManager gameStateManager = new GameStateManager();
    public static Board board = new Board();
    public static BufferStrategy bufferStrategy;
    public static Graphics graphics;
    public static Render render = new Render();
    public static Font font;

    private Bomberman() {
        frame.setLayout(new BorderLayout());
        frame.setMaximumSize(new Dimension(Size.SCREEN_WIDTH, Size.SCREEN_HEIGHT));
        frame.setMinimumSize(new Dimension(Size.SCREEN_WIDTH, Size.SCREEN_HEIGHT));
        frame.setPreferredSize(new Dimension(Size.SCREEN_WIDTH, Size.SCREEN_HEIGHT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Bomberman");
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.add(this, BorderLayout.CENTER);
        frame.pack();
        addKeyListener(inputHandle);
        if (getBufferStrategy() == null) {
            createBufferStrategy(3);
        }
        bufferStrategy = getBufferStrategy();
        graphics = bufferStrategy.getDrawGraphics();
        requestFocus();
        while (true) {
            gameStateManager.start();
        }
    }

    public static byte[] serialize(Object obj) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    public static Object deserialize(byte[] data) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    public static void main(String[] args) {
        try {
            InputStream in = ClassLoader.getSystemResourceAsStream("Font/Pixel Emulator.otf");
            font = Font.createFont(Font.TRUETYPE_FONT, in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Bomberman();
    }
}
