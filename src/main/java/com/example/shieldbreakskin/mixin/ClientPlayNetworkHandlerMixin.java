package com.example.shieldbreakskin.mixin;

import com.example.shieldbreakskin.ShieldBreakTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
	/**
	 * Entity status 30 = "shield broken / disabled". The server sends it when an axe
	 * disables someone's shield. TAIL so we only run once, on the main thread.
	 */
	@Inject(method = "onEntityStatus", at = @At("TAIL"))
	private void shieldbreakskin$onEntityStatus(EntityStatusS2CPacket packet, CallbackInfo ci) {
		if (packet.getStatus() != 30) {
			return;
		}
		MinecraftClient mc = MinecraftClient.getInstance();
		if (mc.world == null) {
			return;
		}
		Entity entity = packet.getEntity(mc.world);
		if (entity instanceof AbstractClientPlayerEntity player && player != mc.player) {
			ShieldBreakTracker.onShieldBreakStatus(player.getUuid());
		}
	}
}
