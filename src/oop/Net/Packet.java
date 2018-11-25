package oop.Net;

import oop.Bomberman;

import java.io.Serializable;

public class Packet implements Serializable {

    private boolean[] keys = Bomberman.inputHandle.keys;
    private int id;

    public Packet() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean[] getKeys() {
        return keys;
    }

    public int getId() {
        return id;
    }
}
