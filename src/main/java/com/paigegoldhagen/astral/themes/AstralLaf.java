package com.paigegoldhagen.astral.themes;

import com.formdev.flatlaf.FlatLightLaf;

/**
 * For setting up the custom GUI Look and Feel.
 */
public class AstralLaf extends FlatLightLaf {
    public static boolean setup() {
        return setup(new AstralLaf());
    }

    @Override
    public String getName() {
        return "AstralLaf";
    }
}
