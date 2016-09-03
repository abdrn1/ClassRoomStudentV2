package com.abd.classroom1;

/**
 * Created by Abd on 8/29/2016.
 */
public class CommandsMessages {
    double zoomFactor =1;


    public CommandsMessages(){

    }

    public CommandsMessages(double zoomFactor) {
        this.zoomFactor = zoomFactor;
    }

    public double getZoomFactor() {
        return zoomFactor;
    }

    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
    }
}
