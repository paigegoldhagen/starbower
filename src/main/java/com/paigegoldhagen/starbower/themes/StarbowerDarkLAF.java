package com.paigegoldhagen.starbower.themes;

import com.formdev.flatlaf.FlatDarkLaf;

public class StarbowerDarkLAF extends FlatDarkLaf {
    public static boolean setup() {
        return setup(new StarbowerDarkLAF());
    }

    @Override
    public String getName() {
        return "StarbowerDarkLAF";
    }
}