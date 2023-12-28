package com.paigegoldhagen.astral;

import com.formdev.flatlaf.FlatLaf;
import com.paigegoldhagen.astral.themes.AstralLaf;

import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * The entry point to the app.
 */
public class Astral {
    /**
     * 1) Get the Event list from the FileHandler class.
     * 2) Get the unique event groups/subgroups and specialty subgroups from the GroupHandler class.
     * 3) Create a new FestivalComponents class instance.
     * 4) Get the user preferences from the PreferenceHandler class.
     * 5) Get the EventComponents list from the EventHandler class.
     * 6) Create a new AppImages class instance.
     * 7) Get the TrayIcon from the TrayHandler class.
     * 8) Display the GUI.
     * <p></p>
     * In the main loop:
     * 1) Get the TrackedEvent list from the TrackedEventHandler class.
     * 2) Get the notification message from the MessageHandler class.
     * 3) Send a notification using the NotificationHandler class.
     *
     * @throws IOException              the current thread was interrupted
     * @throws AWTException             a file couldn't be found or read
     * @throws InterruptedException     an error occurred with the GUI components or window
     */
    public static void main(String[] args) throws IOException, AWTException, InterruptedException {
        List<Event> eventList = FileHandler.mapEventData();

        List<String> uniqueEventGroups = GroupHandler.getUniqueEventGroups(eventList);
        List<String> uniqueEventSubgroups = GroupHandler.getUniqueEventSubgroups(eventList, uniqueEventGroups.getLast());
        List<String> specialtySubgroupList = GroupHandler.getSpecialtySubgroupList(eventList, uniqueEventGroups);

        FestivalComponents festivalComponents = initialiseFestivalComponents(uniqueEventSubgroups);
        UserPreferences userPrefs = PreferenceHandler.getUserPreferences(eventList, uniqueEventSubgroups, specialtySubgroupList);
        List<EventComponents> eventComponentsList = EventHandler.getEventComponentsList(eventList, uniqueEventGroups.getLast(), uniqueEventSubgroups, specialtySubgroupList, userPrefs.getNotifyStates());

        AppImages appImages = loadAppImages();
        TrayIcon trayIcon = TrayHandler.getTrayIcon(appImages.getTrayImage());
        GUI.displayGUI(festivalComponents, eventComponentsList, uniqueEventGroups, specialtySubgroupList, appImages.getAppIcons(), userPrefs);

        while (true) {
            long millisecondsToWait = 1000;

            List<TrackedEvent> trackedEvents = TrackedEventHandler.getTrackedEvents(eventList, userPrefs);

            if (!trackedEvents.isEmpty()) {
                Message notificationMessage = MessageHandler.getNotificationMessage(trackedEvents, userPrefs.getNotifyMinutes(), trackedEvents.size());
                NotificationHandler.sendNotification(notificationMessage, trayIcon);
                millisecondsToWait = 60000;
            }

            Thread.sleep(millisecondsToWait);
        }
    }

    /**
     * Create a new AppImages class instance using Images loaded from the FileHandler class.
     *
     * @return                  a new AppImages instance with a tray image and a list of app icons
     * @throws IOException      a file couldn't be found or read
     */
    private static AppImages loadAppImages() throws IOException {
        Image trayImage = FileHandler.loadImage("icon-24.png");

        String[] files = new String[]{"icon-16.png", "icon-24.png", "icon-32.png", "icon-48.png", "icon-256.png"};
        List<Image> icons = FileHandler.loadImageList(files);

        return new AppImages(trayImage, icons);
    }

    /**
     * Initialise the GUI look and feel, get the Festival list from the FileHandler class,
     * and create a new FestivalComponents class instance using the FestivalHandler class.
     *
     * @param uniqueEventSubgroups      a list of unique event subgroup names
     * @return                          a new FestivalComponents instance with a title label and a countdown label
     */
    private static FestivalComponents initialiseFestivalComponents(List<String> uniqueEventSubgroups) {
        initialiseLookAndFeel();
        List<Festival> festivalList = FileHandler.mapFestivalData();
        return FestivalHandler.getFestivalComponents(festivalList, uniqueEventSubgroups);
    }

    /**
     * Register the location of the look and feel properties file and set up the custom theme.
     */
    private static void initialiseLookAndFeel() {
        FlatLaf.registerCustomDefaultsSource("themes");
        AstralLaf.setup();
    }
}
