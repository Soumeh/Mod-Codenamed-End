package org.solstice.end.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.solstice.end.content.entity.DissolvableEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public abstract class EntityMixin implements DissolvableEntity {

	@Shadow
	public abstract void remove(Entity.RemovalReason reason);

	@Override
	public void dissolve(ServerWorld world, BlockPos pos) {
		this.remove(Entity.RemovalReason.KILLED);
	}

}
