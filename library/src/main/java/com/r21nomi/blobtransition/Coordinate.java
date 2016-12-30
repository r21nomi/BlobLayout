package com.r21nomi.blobtransition;

/**
 * Created by Ryota Niinomi on 2016/11/12.
 */

class Coordinate {

    private float x;
    private float y;

    Coordinate(float x, float y) {
        this.x = x;
        this.y = y;
    }

    float getX() {
        return x;
    }

    void setX(float x) {
        this.x = x;
    }

    float getY() {
        return y;
    }

    void setY(float y) {
        this.y = y;
    }
}
