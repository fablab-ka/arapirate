package de.fablab.ara.arapirate;

public class Channel {
    private String name;
    private int color;
    private int trigger;
    private double[] samples;
    private double recordingLength;

    public Channel(int channelColor) {

        this.color = channelColor;
        this.samples = new double[] {
                10, 10, 10, 10, 10, 10, 10, 10, 10, 10
        };
        this.recordingLength = 100;
    }

    public int getTrigger() {
        return trigger;
    }

    public void setTrigger(int t) {
        this.trigger = t;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public double[] getSamples() {
        return samples;
    }

    public void setSamples(double[] samples) {
        this.samples = samples;
    }

    public double getRecordingLength() {
        return recordingLength;
    }

    public void setRecordingLength(double recordingLength) {
        this.recordingLength = recordingLength;
    }
}
