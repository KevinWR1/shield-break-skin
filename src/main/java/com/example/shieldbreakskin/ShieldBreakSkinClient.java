package com.example.shieldbreakskin;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

public class ShieldBreakSkinClient implements ClientModInitializer {
	public static final String MOD_ID = "shieldbreakskin";
	public static final Identifier BROKEN_SKIN = Identifier.of(MOD_ID, "textures/entity/broken_skin.png");

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> ShieldBreakTracker.tick());

		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> ShieldBreakTracker.clear());

		// Remember when *I* swing an axe at another player.
		AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			MinecraftClient mc = MinecraftClient.getInstance();
			if (world.isClient()
					&& player == mc.player
					&& entity instanceof PlayerEntity target
					&& target != mc.player
					&& player.getMainHandStack().isIn(ItemTags.AXES)) {
				ShieldBreakTracker.recordAxeHit(target.getUuid());
			}
			return ActionResult.PASS;
		});
	}
}
