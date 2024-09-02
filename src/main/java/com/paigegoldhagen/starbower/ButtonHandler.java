package com.paigegoldhagen.starbower;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Adding listeners to JButton components and handling copy-to-clipboard functionality.
 */
public class ButtonHandler {
    /**
     * Add an action listener and a mouse listener to a JButton.
     *
     * @param frame             the visual window for GUI components
     * @param locationButton    the JButton to add the listeners onto
     */
    public static void addLocationButtonListeners(JFrame frame, JButton locationButton) {
        addLocationButtonActionListener(frame, locationButton);
        addLocationButtonMouseListener(locationButton);
    }

    /**
     * Add an action listener that copies the waypoint link to the clipboard
     * and displays user feedback when the link is copied.
     *
     * @param frame             the visual window for GUI components
     * @param locationButton    the JButton to add the action listener onto
     */
    private static void addLocationButtonActionListener(JFrame frame, JButton locationButton) {
        locationButton.addActionListener(actionReceiver -> {
            copyWaypointLinkToClipboard(locationButton.getName());
            displayCopyFeedback(frame, locationButton);
        });
    }

    /**
     * Make a new StringSelection from the waypoint link string, get the system clipboard,
     * and set the clipboard contents to the StringSelection.
     *
     * @param waypointLink  the string for the StringSelection
     */
    private static void copyWaypointLinkToClipboard(String waypointLink) {
        StringSelection stringSelection = new StringSelection(waypointLink);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, stringSelection);
    }

    /**
     * Set the JButton text to the user feedback for when a link is copied to the clipboard,
     * and schedule the text to change back to the original text.
     *
     * @param frame             the visual window for GUI components
     * @param locationButton    the JButton being changed
     */
    private static void displayCopyFeedback(JFrame frame, JButton locationButton) {
        String originalText = locationButton.getText();
        locationButton.setText("Waypoint copied!");
        scheduleLocationButtonTextChange(frame, locationButton, originalText);
    }

    /**
     * Change the location button text when the scheduled time elapses.
     *
     * @param frame             the visual window for GUI components
     * @param locationButton    the JButton being changed
     * @param originalText      the original text appearance of the JButton
     */
    private static void scheduleLocationButtonTextChange(JFrame frame, JButton locationButton, String originalText) {
        Runnable changeLocationButtonText = changeLocationButtonText(frame, locationButton, originalText);
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(changeLocationButtonText, 2, 2, TimeUnit.SECONDS);
    }

    /**
     * Set the JButton text to the original text string and revalidate/repaint the JFrame.
     *
     * @param frame             the JFrame to revalidate/repaint
     * @param locationButton    the JButton being changed
     * @param originalText      the original text appearance of the JButton
     *
     * @return                  the methods to run
     */
    private static Runnable changeLocationButtonText(JFrame frame, JButton locationButton, String originalText) {
        return () -> {
            locationButton.setText(originalText);
            frame.revalidate();
            frame.repaint();
        };
    }

    /**
     * Add a mouse listener that changes the appearance of the mouse cursor when hovered over.
     *
     * @param locationButton    the JButton to add the listener onto
     */
    private static void addLocationButtonMouseListener(JButton locationButton) {
        locationButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                locationButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                locationButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }
}