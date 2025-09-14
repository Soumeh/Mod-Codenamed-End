package org.solstice.end.content.fluid;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.solstice.end.content.entity.DissolvableEntity;
import org.solstice.end.registry.*;

import java.util.*;

public abstract class AcidFluid extends FlowableFluid {

	protected final Random random;

	public AcidFluid() {
		this.random = Random.create();
	}

	@Override
	public Fluid getFlowing() {
		return ModFluids.FLOWING_ACID;
	}

	@Override
	public Fluid getStill() {
		return ModFluids.ACID;
	}

	@Override
	public boolean matchesType(Fluid fluid) {
		return fluid.isIn(ModTags.ACID);
	}

	public Item getBucketItem() {
		return ModItems.ACID_BUCKET;
	}

	@Override
	protected boolean isInfinite(World world) {
		return false;
	}

	@Override
	protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {}

	@Override
	protected int getMaxFlowDistance(WorldView world) {
		return 3;
	}

	@Override
	protected int getLevelDecreasePerBlock(WorldView world) {
		return 1;
	}

	@Override
	protected boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
		return false;
	}

	@Override
	public int getTickRate(WorldView world) {
		return 15;
	}

	@Override
	protected float getBlastResistance() {
		return 100;
	}

	@Override
	protected BlockState toBlockState(FluidState state) {
		return ModBlocks.ACID.getDefaultState().with(FluidBlock.LEVEL, getBlockStateLevel(state));
	}

	@Override
	public void onScheduledTick(World rawWorld, BlockPos pos, FluidState state) {
		super.onScheduledTick(rawWorld, pos, state);
		if (!(rawWorld instanceof ServerWorld world)) return;

		rawWorld.scheduleFluidTick(pos, this, this.getTickRate(rawWorld));

		Box box = new Box(pos);
		List<Entity> entities = rawWorld.getEntitiesByClass(Entity.class, box, item -> true);
		for (Entity entity : entities) {
			if (entity instanceof DissolvableEntity dissolvable) {
				dissolvable.dissolve(world, pos);
				Vec3d vec = entity.getPos();
				world.spawnParticles(ModParticles.ACID_BUBBLE, vec.x, vec.y, vec.z, 3, 0.1, 0.25, 0.1, 0);
			}
		}
	}

	public static class Still extends AcidFluid {

		public Still() {
			super();
		}

		@Override
		public int getLevel(FluidState state) {
			return 8;
		}

		@Override
		public boolean isStill(FluidState state) {
			return true;
		}

	}

	public static class Flowing extends AcidFluid {

		public Flowing() {
			super();
		}

		@Override
		protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
			super.appendProperties(builder);
			builder.add(LEVEL);
		}

		@Override
		public int getLevel(FluidState state) {
			return state.get(LEVEL);
		}

		@Override
		public boolean isStill(FluidState state) {
			return false;
		}

	}

}
