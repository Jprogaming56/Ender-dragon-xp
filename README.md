# Dragon XP Every Time

A small Forge 1.20.1 mod that makes the Ender Dragon drop the full 12,000 XP on **every** kill.

In vanilla, the dragon drops 12,000 XP the first time it is killed and only 500 XP on later kills. This mod adds the missing 11,500 XP on repeat kills, at the end of the death animation, just like vanilla.

## Requirements

- Minecraft 1.20.1
- Forge 47.x

Works in singleplayer and on servers (install it on the server; clients don't need it).

## Building

Every push builds the jar automatically with GitHub Actions. Open the **Actions** tab, pick the latest run, and download the `dragonxp-jar` artifact.

To build locally, with Java 17 and Gradle 8.x installed:

```
gradle build
```

The jar ends up in `build/libs/`.
