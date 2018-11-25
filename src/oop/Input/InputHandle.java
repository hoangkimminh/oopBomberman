package oop.Input;

import oop.Setting.Value;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;
import java.util.Arrays;

public class InputHandle implements KeyListener, Serializable {

    public boolean[] keys = new boolean[7];
    public boolean[] keyRelease = new boolean[7];
    public StringBuilder stringBuilder = new StringBuilder();

    public InputHandle() {

    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == '\b') {
            if (stringBuilder.length() > 0) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
            return;
        }
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            return;
        }
        if (Character.isDefined(e.getKeyChar())) {
            stringBuilder.append(e.getKeyChar());
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_SPACE:
                keys[Value.PLANT] = true;
                break;
            case KeyEvent.VK_UP:
                keys[Value.UP] = true;
                break;
            case KeyEvent.VK_RIGHT:
                keys[Value.RIGHT] = true;
                break;
            case KeyEvent.VK_DOWN:
                keys[Value.DOWN] = true;
                break;
            case KeyEvent.VK_LEFT:
                keys[Value.LEFT] = true;
                break;
            case KeyEvent.VK_CONTROL:
                keys[Value.DETONATE] = true;
                break;
            case KeyEvent.VK_ESCAPE:
                keys[Value.QUIT] = true;
                break;
            default: break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_SPACE:
                keys[Value.PLANT] = false;
                keyRelease[Value.PLANT] = true;
                break;
            case KeyEvent.VK_UP:
                keys[Value.UP] = false;
                keyRelease[Value.UP] = true;
                break;
            case KeyEvent.VK_RIGHT:
                keys[Value.RIGHT] = false;
                keyRelease[Value.RIGHT] = true;
                break;
            case KeyEvent.VK_DOWN:
                keys[Value.DOWN] = false;
                keyRelease[Value.DOWN] = true;
                break;
            case KeyEvent.VK_LEFT:
                keys[Value.LEFT] = false;
                keyRelease[Value.LEFT] = true;
                break;
            case KeyEvent.VK_CONTROL:
                keys[Value.DETONATE] = false;
                break;
            case KeyEvent.VK_ESCAPE:
                keys[Value.QUIT] = false;
                break;
            case KeyEvent.VK_P:
                keyRelease[Value.PAUSE] = true;
                break;
            default: break;
        }
    }

    public void reset() {
        Arrays.fill(keys, false);
        Arrays.fill(keyRelease, false);
    }
}
