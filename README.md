# Starbower ![Current Version](https://img.shields.io/github/v/release/paigegoldhagen/starbower?color=%233F4FAE) ![Downloads](https://img.shields.io/github/downloads/paigegoldhagen/starbower/total?color=%236BC3FF)


An app for receiving timely notifications on upcoming events in *Guild Wars 2*. Customise which events to be notified about, and how far in advance to be notified. Starbower also includes a nifty festival countdown tab, for those moments when you're anticipating the next festival or scrambling to complete all the achievements before it ends :)

Download the [latest version of Starbower](https://github.com/paigegoldhagen/starbower/releases/latest) or read the [source code documentation](https://paigegoldhagen.github.io/starbower-docs/com/paigegoldhagen/starbower/package-summary.html)

<br>

![GUI](/assets/GUI.png)

<br>

## What's new in Version 1.5.0
Janthir Wilds is here! The exciting new expansion happily coincides with Starbower's 1.5 update, complete with added events and a shiny new Waypoint copy functionality. Simply click a location, and the Waypoint (or nearest Waypoint) chat link will be copied to your clipboard.
+ Location labels are now clickable, copying the Waypoint chat link to your clipboard
+ Changed Hologram Stampede Waypoints to reflect in-game suggested Waypoints
+ Added Water Balloons event to the Festival of the Four Winds
+ Added new tab for Janthir Wilds and the *Of Mists and Monsters* meta event

<br>

Version 1.5.1
+ Added Mount Balrior Convergence event

<br>

## Troubleshooting
### Notifications not working
When playing in fullscreen mode, notifications may be suppressed by do not disturb settings. On Windows 11, go to `Settings > System > Notifications > Turn on do not disturb automatically` and uncheck the following:
+ When playing a game
+ When using an app in full-screen mode

### Launch issues
1. Make sure your Java version is Java SE 21 or above. Check your system version in Command Prompt with `java --version`
2. Put Starbower in a local directory, i.e. somewhere that doesn't need admin permissions, otherwise the app won't be able to create the embedded database. This is something I endeavour to improve in the future!

<br>

## Upcoming features
- [ ] Windows native executable for future releases
- [x] Dark mode option ✨ Added in Version 1.4.2! ✨
- [x] Waypoint button to copy the chat link to the clipboard ✨ Added in Version 1.5.0! ✨

<br>

## Technical info
#### Developed for Windows

Languages & technologies
+ Java
+ Apache Maven
+ Swing and AWT
+ FlatLaf
+ H2 RDBMS
+ SQL
+ OpenCSV

<br>

### Database design (ERD)

![ERD](/assets/ERD.png)

<br>

## Feedback & code usage
Starbower is in active development and I'm always learning and improving. You can [send feedback](https://github.com/paigegoldhagen/starbower/issues) using the `bug`, `enhancement` or `question` labels - I'll respond to issues and make changes as best I can!

The entire Starbower repository is open source, so please feel free to use the source code for your own projects :)