package com.paigegoldhagen.astral;

import javax.swing.*;

/**
 * For setting and getting GUI components for a festival.
 */
public class FestivalComponents {
    private final JLabel TitleLabel, CountdownLabel;

    public FestivalComponents(JLabel titleLabel, JLabel countdownLabel) {
        this.TitleLabel = titleLabel;
        this.CountdownLabel = countdownLabel;
    }

    public JLabel getTitleLabel() {return TitleLabel;}
    public JLabel getCountdownLabel() {return CountdownLabel;}
}
