package org.solstice.end.content.entity;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public interface DissolvableEntity {

	void dissolve(ServerWorld world, BlockPos pos);

}
