package com.r21nomi.blobtransition;

/**
 * Created by Ryota Niinomi on 2016/11/12.
 */

public class Point {

    private Coordinate currentPoint;
    private Coordinate initialPoint;
    private Coordinate targetPoint;

    public Point(Coordinate initialPoint, Coordinate targetPoint) {
        this.initialPoint = initialPoint;
        this.currentPoint = initialPoint;
        this.targetPoint = targetPoint;
    }

    public Coordinate getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(Coordinate currentPoint) {
        this.currentPoint = currentPoint;
    }

    public Coordinate getInitialPoint() {
        return initialPoint;
    }

    public void setInitialPoint(Coordinate initialPoint) {
        this.initialPoint = initialPoint;
    }

    public Coordinate getTargetPoint() {
        return targetPoint;
    }

    public void setTargetPoint(Coordinate targetPoint) {
        this.targetPoint = targetPoint;
    }
}
