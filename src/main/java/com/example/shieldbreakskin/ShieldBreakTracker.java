package com.example.shieldbreakskin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Client-side bookkeeping: which players currently have their shield disabled
 * because of an axe hit from the local player.
 */
public final class ShieldBreakTracker {
	/** How long the shield stays "gone" (20 ticks = 1 second). Vanilla axe disable time is 5 seconds. */
	public static final int DISABLE_TICKS = 100;

	/** How long after my axe swing a shield-break packet still counts as caused by me (covers ping). */
	private static final int HIT_WINDOW_TICKS = 30;

	private static long now = 0;
	private static final Map<UUID, Long> AXE_HITS = new HashMap<>();
	private static final Map<UUID, Long> DISABLED_UNTIL = new HashMap<>();

	private ShieldBreakTracker() {}

	/** Called every client tick. */
	public static void tick() {
		now++;
		AXE_HITS.values().removeIf(t -> now - t > HIT_WINDOW_TICKS);
		for (Iterator<Long> it = DISABLED_UNTIL.values().iterator(); it.hasNext(); ) {
			if (it.next() <= now) {
				it.remove();
			}
		}
	}

	/** The local player just swung an axe at this player. */
	public static void recordAxeHit(UUID target) {
		AXE_HITS.put(target, now);
	}

	/** The server told us this player's shield was broken/disabled (entity status 30). */
	public static void onShieldBreakStatus(UUID target) {
		Long hitTime = AXE_HITS.get(target);
		if (hitTime != null && now - hitTime <= HIT_WINDOW_TICKS) {
			DISABLED_UNTIL.put(target, now + DISABLE_TICKS);
			AXE_HITS.remove(target);
		}
	}

	public static boolean hasAny() {
		return !DISABLED_UNTIL.isEmpty();
	}

	public static boolean isDisabled(UUID player) {
		Long until = DISABLED_UNTIL.get(player);
		return until != null && until > now;
	}

	public static void clear() {
		AXE_HITS.clear();
		DISABLED_UNTIL.clear();
	}
}
