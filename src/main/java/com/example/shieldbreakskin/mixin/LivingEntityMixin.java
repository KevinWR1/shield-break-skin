package com.example.shieldbreakskin.mixin;

import com.example.shieldbreakskin.ShieldBreakTracker;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	/**
	 * While a player's shield is disabled (by me), make their client-side copy report an empty
	 * stack instead of the shield. Nothing is sent to the server and nobody else is affected.
	 */
	@Inject(method = "getEquippedStack", at = @At("RETURN"), cancellable = true)
	private void shieldbreakskin$hideShield(EquipmentSlot slot, CallbackInfoReturnable<ItemStack> cir) {
		if (!ShieldBreakTracker.hasAny()) {
			return;
		}
		if (!((Object) this instanceof AbstractClientPlayerEntity player)) {
			return;
		}
		if (!ShieldBreakTracker.isDisabled(player.getUuid())) {
			return;
		}
		ItemStack stack = cir.getReturnValue();
		if (stack.isOf(Items.SHIELD) || stack.contains(DataComponentTypes.BLOCKS_ATTACKS)) {
			cir.setReturnValue(ItemStack.EMPTY);
		}
	}
}
