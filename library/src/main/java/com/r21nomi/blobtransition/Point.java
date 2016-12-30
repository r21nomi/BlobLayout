package com.r21nomi.blobtransition;

/**
 * Created by Ryota Niinomi on 2016/11/12.
 */

class Point {

    private Coordinate currentPoint;
    private Coordinate initialPoint;
    private Coordinate targetPoint;

    Point(Coordinate initialPoint, Coordinate targetPoint) {
        this.initialPoint = initialPoint;
        this.currentPoint = initialPoint;
        this.targetPoint = targetPoint;
    }

    Coordinate getCurrentPoint() {
        return currentPoint;
    }

    void setCurrentPoint(Coordinate currentPoint) {
        this.currentPoint = currentPoint;
    }

    Coordinate getInitialPoint() {
        return initialPoint;
    }

    void setInitialPoint(Coordinate initialPoint) {
        this.initialPoint = initialPoint;
    }

    Coordinate getTargetPoint() {
        return targetPoint;
    }

    void setTargetPoint(Coordinate targetPoint) {
        this.targetPoint = targetPoint;
    }
}
