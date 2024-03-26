package com.paigegoldhagen.starbower.themes;

import com.formdev.flatlaf.FlatLightLaf;

public class StarbowerLAF extends FlatLightLaf {
    public static boolean setup() {
        return setup(new StarbowerLAF());
    }

    @Override
    public String getName() {
        return "StarbowerLAF";
    }
}