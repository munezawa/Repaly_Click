package com.company1.floating_window;

import org.litepal.crud.DataSupport;

public class Click extends DataSupport {
    public int time;
    public int x;
    public int y;

    public int getTime() {
        return this.time;
    }
    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
}
