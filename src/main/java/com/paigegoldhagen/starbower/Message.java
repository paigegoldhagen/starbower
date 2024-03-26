package com.paigegoldhagen.starbower;

/**
 * Setting and getting notification message strings.
 */
public class Message {
    public String Caption;
    public String Text;

    public Message(String caption, String text) {
        this.Caption = caption;
        this.Text = text;
    }

    public String getCaption() {return Caption;}
    public String getText() {return Text;}
}