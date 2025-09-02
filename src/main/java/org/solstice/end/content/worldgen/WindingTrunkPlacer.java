package org.solstice.end.content.worldgen;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import org.solstice.end.registry.ModWorldgen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class WindingTrunkPlacer extends TrunkPlacer {

	public static final MapCodec<WindingTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(instance -> fillTrunkPlacerFields(instance).apply(instance, WindingTrunkPlacer::new));

	public WindingTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight) {
		super(baseHeight, firstRandomHeight, secondRandomHeight);
	}

	@Override
	protected TrunkPlacerType<?> getType() {
		return ModWorldgen.WINDING_TRUNK_PLACER;
	}

	@Override
	public List<FoliagePlacer.TreeNode> generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, int height, BlockPos startPos, TreeFeatureConfig config) {
		List<FoliagePlacer.TreeNode> result = new ArrayList<>();

		BlockPos.Mutable position = startPos.mutableCopy();
		for (int i = 0; i < height / 2; ++i) {
			this.getAndSetState(world, replacer, random, position, config);
			position.move(Direction.UP);
		}

		Direction direction = Direction.Type.HORIZONTAL.random(random);
		Function<BlockState, BlockState> changeAxis = state -> state.with(PillarBlock.AXIS, direction.getAxis());
		for (int i = 0; i < height / 4; ++i) {
			this.getAndSetState(world, replacer, random, position, config, changeAxis);
			position.move(direction);
		}

		for (int i = 0; i < height / 2; ++i) {
			this.getAndSetState(world, replacer, random, position, config);
			position.move(Direction.UP);
		}
		result.add(new FoliagePlacer.TreeNode(position.toImmutable(), 0, false));

		return result;
	}

}
