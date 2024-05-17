# Starbower ![Current Version](https://img.shields.io/github/v/release/paigegoldhagen/starbower?color=%233F4FAE) ![Downloads](https://img.shields.io/github/downloads/paigegoldhagen/starbower/total?color=%236BC3FF)


An app for receiving timely notifications on upcoming events in *Guild Wars 2*. Customise which events to be notified about, and how far in advance to be notified. Starbower also includes a nifty festival countdown tab, for those moments when you're anticipating the next festival or scrambling to complete all the achievements before it ends :)

Download the [latest version of Starbower](https://github.com/paigegoldhagen/starbower/releases/latest) or read the [source code documentation](https://paigegoldhagen.github.io/starbower-docs/com/paigegoldhagen/starbower/package-summary.html)
![GUI](/assets/GUI.png)
## What's new in Version 1.4.0
Starbower has a shiny new name, and with it, a completely redesigned backend! The scope of the project has grown from a semi-structured data seedling into a relational database sapling :)
+ App name changed from Astral to Starbower
+ New app icon
+ New GUI colour scheme and fonts
+ New SQL database backend
+ Added Awakened Invasion event
+ Adjusted notification reminder time choices

<br>

Version 1.4.1
+ Added code for updating the Festival table with current festival start and end dates

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
- [ ] Dark mode option
- [ ] Waypoint button to copy the chat link to the clipboard

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

### Database design (ERD)

![ERD](/assets/ERD.png)

<br>

## Feedback & code usage
Starbower is in active development and I'm always learning and improving. You can [send feedback](https://github.com/paigegoldhagen/starbower/issues) using the `bug`, `enhancement` or `question` labels - I'll respond to issues and make changes as best I can!

The entire Starbower repository is open source, so please feel free to use the source code for your own projects :)