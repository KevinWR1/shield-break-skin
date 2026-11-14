package com.example.shieldbreakskin.mixin;

import com.example.shieldbreakskin.ShieldBreakFlag;
import com.example.shieldbreakskin.ShieldBreakSkinClient;
import com.example.shieldbreakskin.ShieldBreakTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
	@Inject(
		method = "updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V",
		at = @At("TAIL")
	)
	private void shieldbreakskin$flagState(AbstractClientPlayerEntity player, PlayerEntityRenderState state, float tickDelta, CallbackInfo ci) {
		boolean active = player != MinecraftClient.getInstance().player
				&& ShieldBreakTracker.isDisabled(player.getUuid());
		((ShieldBreakFlag) state).shieldbreakskin$setActive(active);
	}

	@Inject(
		method = "getTexture(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;)Lnet/minecraft/util/Identifier;",
		at = @At("RETURN"),
		cancellable = true
	)
	private void shieldbreakskin$swapSkin(PlayerEntityRenderState state, CallbackInfoReturnable<Identifier> cir) {
		if (((ShieldBreakFlag) state).shieldbreakskin$isActive()) {
			cir.setReturnValue(ShieldBreakSkinClient.BROKEN_SKIN);
		}
	}
}
