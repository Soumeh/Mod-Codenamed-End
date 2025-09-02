package org.solstice.end.content.entity;

import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Optional;

public class EvilExperienceOrbEntity extends ExperienceOrbEntity {

	public EvilExperienceOrbEntity(EntityType<? extends ExperienceOrbEntity> entityType, World world) {
		super(entityType, world);
		this.health = 5;
		this.pickingCount = 1;
	}

	public EvilExperienceOrbEntity(World world, double x, double y, double z, int amount) {
		super(world, x, y, z, amount);
	}

	public static void spawn(ServerWorld world, Vec3d pos, int amount) {
		while (amount > 0) {
			int orbAmount = roundToOrbSize(amount);
			amount -= orbAmount;
			if (!wasMergedIntoExistingOrb(world, pos, orbAmount))
				world.spawnEntity(new EvilExperienceOrbEntity(world, pos.getX(), pos.getY(), pos.getZ(), orbAmount));
		}
	}

	@Override
	public void onPlayerCollision(PlayerEntity player) {
		if (!(player instanceof ServerPlayerEntity serverPlayerEntity)) return;
		if (player.experiencePickUpDelay != 0) return;

		player.experiencePickUpDelay = 2;
		player.sendPickup(this, 1);
		this.damagePlayerGears(serverPlayerEntity, this.amount);
		player.addExperience(-this.amount);

		--this.pickingCount;
		if (this.pickingCount == 0) this.discard();
	}

	public final void damagePlayerGears(ServerPlayerEntity player, int amount) {
		Optional<EnchantmentEffectContext> optional = EnchantmentHelper.chooseEquipmentWith(EnchantmentEffectComponentTypes.REPAIR_WITH_XP, player, ItemStack::isDamaged);
		if (optional.isEmpty()) return;

		ItemStack stack = optional.get().stack();
		stack.damage(amount, player.getServerWorld(), player, item -> {});
	}

}
