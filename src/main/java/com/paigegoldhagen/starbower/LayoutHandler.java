package com.paigegoldhagen.starbower;

import java.awt.*;

/**
 * Interface handling the layout for GUI components.
 */
public interface LayoutHandler {
    /**
     * Set the frame anchor, fill, insets and Y padding.
     *
     * @param layout    a class for setting visual constraints for GUI components
     */
    static void initialiseFrameLayout(GridBagConstraints layout) {
        layout.anchor = GridBagConstraints.NORTH;
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.insets = new Insets(5, 5, 12, 10);
        layout.ipady = 15;
    }

    /**
     * Update the frame fill, insets, Y grid position and X/Y weights.
     *
     * @param layout    a class for setting visual constraints for GUI components
     */
    static void updateFrameLayout(GridBagConstraints layout) {
        layout.fill = GridBagConstraints.BOTH;
        layout.insets = new Insets(0, 0, 0, 0);
        layout.gridy = 1;
        layout.weightx = 1.0;
        layout.weighty = 1.0;
    }

    /**
     * Set the dropdown panel anchor, insets and X weight.
     *
     * @param layout    a class for setting visual constraints for GUI components
     */
    static void setDropdownPanelLayout(GridBagConstraints layout) {
        layout.anchor = GridBagConstraints.WEST;
        layout.insets = new Insets(0, 8, 0, 0);
        layout.weightx = 0.0;
    }

    /**
     * Set the viewport anchor, fill and X/Y weights.
     *
     * @param layout    a class for setting visual constraints for GUI components
     */
    static void setViewportLayout(GridBagConstraints layout) {
        layout.anchor = GridBagConstraints.FIRST_LINE_START;
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.weightx = 1.0;
        layout.weighty = 1.0;
    }

    /**
     * Set the Expansion panel anchor, fill, X/Y weights and insets.
     *
     * @param layout    a class for setting visual constraints for GUI components
     */
    static void setExpansionPanelLayout(GridBagConstraints layout) {
        layout.anchor = GridBagConstraints.FIRST_LINE_START;
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.weightx = 1.0;
        layout.weighty = 1.0;
        layout.insets = new Insets(0, 0, 50, 0);
    }

    /**
     * Set the Category panel anchor, insets, X grid position and X weight.
     *
     * @param layout    a class for setting visual constraints for GUI components
     */
    static void setCategoryPanelLayout(GridBagConstraints layout) {
        layout.anchor = GridBagConstraints.FIRST_LINE_START;
        layout.insets = new Insets(11, 25, -5, 15);
        layout.gridx = 0;
        layout.weightx = 1.0;
    }

    /**
     * Update the Y spacing insets.
     *
     * @param layout    a class for setting visual constraints for GUI components
     */
    static void updateYSpacing(GridBagConstraints layout) {
        layout.insets = new Insets(31, 25, -4, 15);
    }

    /**
     * Update the Category panel fill, insets and grid width.
     *
     * @param layout    a class for setting visual constraints for GUI components
     */
    static void updateCategoryPanelLayout(GridBagConstraints layout) {
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.insets = new Insets(20, 25, 0, 15);
        layout.gridwidth = 1;
    }

    /**
     * Set the separator fill, grid width, insets and Y grid position.
     *
     * @param layout    a class for setting visual constraints for GUI components
     * @param rowCount  the current row within the layout
     */
    static void setSeparatorLayout(GridBagConstraints layout, Integer rowCount) {
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.gridwidth = GridBagConstraints.REMAINDER;
        layout.insets = new Insets(18, 25, 0, 10);
        layout.gridy = rowCount;
    }

    /**
     * Set the Category checkbox anchor, insets, Y weight and Y grid position.
     *
     * @param layout    a class for setting visual constraints for GUI components
     * @param rowCount  the current row within the layout
     */
    static void setCategoryCheckboxLayout(GridBagConstraints layout, Integer rowCount) {
        layout.anchor = GridBagConstraints.FIRST_LINE_START;
        layout.insets = new Insets(15, 25, -4, 15);
        layout.weighty = 0.0;
        layout.gridy = rowCount;
    }

    /**
     * Set the DynamicEvent checkbox X/Y grid position and Y weight.
     *
     * @param layout    a class for setting visual constraints for GUI components
     * @param rowCount  the current row within the layout
     */
    static void setDynamicEventCheckboxLayout(GridBagConstraints layout, Integer rowCount) {
        layout.gridx = 0;
        layout.gridy = rowCount;
        layout.weighty = 0.0;
    }

    /**
     * Set the location label anchor, X/Y grid position and Y weight.
     *
     * @param layout    a class for setting visual constraints for GUI components
     * @param startRow  the current row within the layout
     */
    static void setLocationLabelLayout(GridBagConstraints layout, Integer startRow) {
        layout.anchor = GridBagConstraints.EAST;
        layout.gridx = 1;
        layout.gridy = startRow;
        layout.weighty = 0.0;
    }

    /**
     * Set the Category label insets, Y weight and Y grid position.
     *
     * @param layout    a class for setting visual constraints for GUI components
     * @param rowCount  the current row within the layout
     */
    static void setCategoryLabelLayout(GridBagConstraints layout, Integer rowCount) {
        layout.insets = new Insets(19, 25, -2, 15);
        layout.weighty = 0.0;
        layout.gridy = rowCount;
    }

    /**
     * Set the countdown label insets, Y weight and Y grid position.
     *
     * @param layout    a class for setting visual constraints for GUI components
     * @param rowCount  the current row within the layout
     */
    static void setCountdownLabelLayout(GridBagConstraints layout, Integer rowCount) {
        layout.insets = new Insets(22, 25, 0, 15);
        layout.weighty = 0.0;
        layout.gridy = rowCount;
    }
}