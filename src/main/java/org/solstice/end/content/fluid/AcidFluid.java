package org.solstice.end.content.fluid;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.solstice.end.registry.ModBlocks;
import org.solstice.end.registry.ModFluids;

public abstract class AcidFluid extends FlowableFluid {

	@Override
	public Fluid getFlowing() {
		return ModFluids.FLOWING_ACID;
	}

	@Override
	public Fluid getStill() {
		return ModFluids.ACID;
	}

	@Override
	protected boolean isInfinite(World world) {
		return false;
	}

	@Override
	protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {

	}

	@Override
	protected int getMaxFlowDistance(WorldView world) {
		return 4;
	}

	@Override
	protected int getLevelDecreasePerBlock(WorldView world) {
		return 1;
	}

	@Override
	public Item getBucketItem() {
		return null;
	}

	@Override
	protected boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
		return false;
	}

	@Override
	public int getTickRate(WorldView world) {
		return 0;
	}

	@Override
	protected float getBlastResistance() {
		return 128;
	}

	@Override
	protected BlockState toBlockState(FluidState state) {
		return ModBlocks.ACID.getDefaultState().with(FluidBlock.LEVEL, getBlockStateLevel(state));
	}

	public static class Still extends AcidFluid {

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
