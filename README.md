# Shield Break Skin (Fabric, Minecraft Java 1.21.11, client-side only)

When YOU disable another player's shield with an axe:
- their shield disappears (only on your screen) for 5 seconds (100 ticks)
- their skin is swapped to `broken_skin.png` for those 5 seconds
- then both go back to normal

Only players you hit with an axe are affected. Nothing is sent to the server, so nobody else sees it.

## Build
1. Install JDK 21 and Gradle (or copy the Gradle wrapper from the Fabric template mod).
2. Go to https://fabricmc.net/develop/, pick 1.21.11, and copy the Yarn / Loader / Fabric API / Loom
   versions into `gradle.properties` if the ones there don't resolve.
3. Run `gradle build`. The jar is in `build/libs/shield-break-skin-1.0.0.jar` (not the `-sources` one).
4. Drop it in your `mods` folder with Fabric Loader and Fabric API for 1.21.11.

## Tweaks
- Duration: `DISABLE_TICKS` in `ShieldBreakTracker.java` (20 ticks = 1 second).
- Skin: replace `src/main/resources/assets/shieldbreakskin/textures/entity/broken_skin.png` (64x64).
