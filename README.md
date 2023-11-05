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

## Limitations and improvements
The current version of Astral has a few limitations which do not affect the core functionality:
+ I have only included event times for the low-level and standard world bosses, as categorised in the [GW2 Wiki](https://wiki.guildwars2.com/wiki/World_boss)
+ When playing in fullscreen mode, notifications may be suppressed by do not disturb settings[^2]

I developed this app for my portfolio, but I also use it on a daily basis while playing GW2. Here's my roadmap for near-future improvements:
- [ ] Add event times for hardcore world bosses and the Ley-Line Anomaly
- [ ] More customisation options (opt-in/out of notifications per event)
- [ ] GUI redesign and other visual updates

[^1]: Astral was made with Java SE 21. Check your system version in Command Prompt with `java --version`
[^2]: On Windows 11, go to Settings > System > Notifications > Turn on do not disturb automatically<br>
Make sure `When playing a game` and `When using an app in full-screen mode` are **un**checked
