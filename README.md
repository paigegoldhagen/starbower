# Astral
Astral is a customisable notification app that sends real-time alerts for upcoming events in Guild Wars 2. Developed for Windows using:
+ Java
+ Apache Maven
+ OpenCSV
+ Java AWT

![Astral GUI](/assets/images/GUI.png)

The documentation for Astral can be found [here](https://paigegoldhagen.github.io/astral-docs)

## Running the app (Windows)
1. Download the [latest release](https://github.com/paigegoldhagen/astral/releases/latest)
2. Launch `astral.jar`[^1]

## Troubleshooting
When playing in fullscreen mode, notifications may be suppressed by do not disturb settings. On Windows 11, go to `Settings > System > Notifications > Turn on do not disturb automatically` and uncheck the following:
+ When playing a game
+ When using an app in full-screen mode

## Roadmap
I developed this app for my portfolio, but I also use it on a daily basis while playing GW2. Here's my roadmap for near-future improvements:
- [x] Add event times for hardcore world bosses and the Ley-Line Anomaly ✨ Added in Version 1.1.0! ✨
- [x] More customisation options (opt-in/out of notifications per event) ✨ Added in Version 1.2.0! ✨
- [ ] GUI redesign and other visual updates

[^1]: Astral was made with Java SE 21. Check your system version in Command Prompt with `java --version`
