package org.solstice.end.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.solstice.end.content.entity.DissolvableEntity;
import org.solstice.end.registry.ModDamageSources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.stream.StreamSupport;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements DissolvableEntity {

	@Shadow public abstract Iterable<ItemStack> getArmorItems();
	@Shadow public abstract void damageArmor(DamageSource source, float amount);

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Override
	public void dissolve(ServerWorld world, BlockPos pos) {
		DamageSource source = ModDamageSources.corrosion(world);
		boolean hasArmor = !StreamSupport.stream(this.getArmorItems().spliterator(), false)
			.allMatch(ItemStack::isEmpty);

		if (hasArmor) this.damageArmor(source, 3);
		else this.damage(source, 3);
	}

}
