package com.example.shieldbreakskin.mixin;

import com.example.shieldbreakskin.ShieldBreakFlag;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntityRenderState.class)
public class PlayerEntityRenderStateMixin implements ShieldBreakFlag {
	@Unique
	private boolean shieldbreakskin$active;

	@Override
	public boolean shieldbreakskin$isActive() {
		return this.shieldbreakskin$active;
	}

	@Override
	public void shieldbreakskin$setActive(boolean active) {
		this.shieldbreakskin$active = active;
	}
}
