package com.paigegoldhagen.astral;

/**
 * For setting and getting notification message strings.
 */
public class Message {
    private final String TopLine, Lines;

    public Message(String topLine, String lines) {
        this.TopLine = topLine;
        this.Lines = lines;
    }

    public String getTopLine() {return TopLine;}
    public String getLines() {return Lines;}
}
