package com.paigegoldhagen.starbower.themes;

import com.formdev.flatlaf.FlatLightLaf;

public class StarbowerLightLAF extends FlatLightLaf {
    public static boolean setup() {
        return setup(new StarbowerLightLAF());
    }

    @Override
    public String getName() {
        return "StarbowerLightLAF";
    }
}