package com.example.shieldbreakskin;

/** Duck interface mixed into PlayerEntityRenderState so the renderer knows which skin to use. */
public interface ShieldBreakFlag {
	boolean shieldbreakskin$isActive();

	void shieldbreakskin$setActive(boolean active);
}
