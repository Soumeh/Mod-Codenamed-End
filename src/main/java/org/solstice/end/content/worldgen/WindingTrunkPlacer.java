package org.solstice.end.content.worldgen;

import com.mojang.serialization.Codec;
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

	public static final MapCodec<WindingTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.intRange(0, 32).fieldOf("base_height").forGetter((placer) -> placer.baseHeight),
		Codec.intRange(0, 24).fieldOf("height_rand_a").forGetter((placer) -> placer.firstRandomHeight),
		Codec.intRange(0, 24).fieldOf("height_rand_b").forGetter((placer) -> placer.secondRandomHeight),
		Codec.FLOAT.fieldOf("branch_chance").forGetter(e -> e.branchChance)
	).apply(instance, WindingTrunkPlacer::new));

//	public static final MapCodec<WindingTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
//		Range.CODEC.fieldOf("height_range").forGetter(e -> e.heightRange),
//		Range.CODEC.fieldOf("width_range").forGetter(e -> e.heightRange),
//		Codec.FLOAT.fieldOf("branch_chance").forGetter(e -> e.branchChance)
//	).apply(instance, WindingTrunkPlacer::new));

//	protected final Range heightRange;
//	protected final Range widthRange;
	protected final float branchChance;

//	public WindingTrunkPlacer(Range heightRange, Range widthRange, float branchChance) {
//		super(0, 0, 0);
//		this.heightRange = heightRange;
//		this.widthRange = widthRange;
//		this.branchChance = branchChance;
//	}

	public WindingTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight, float branchChance) {
		super(baseHeight, firstRandomHeight, secondRandomHeight);
		this.branchChance = branchChance;
	}

	@Override
	protected TrunkPlacerType<?> getType() {
		return ModWorldgen.WINDING_TRUNK_PLACER;
	}

//	@Override
//	public int getHeight(Random random) {
//		return this.heightRange.getValue(random);
//	}

	@Override
	public List<FoliagePlacer.TreeNode> generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, int height, BlockPos startPos, TreeFeatureConfig config) {
		List<FoliagePlacer.TreeNode> result = new ArrayList<>();

		BlockPos.Mutable pos = startPos.mutableCopy();
		for (int i = 0; i < height; ++i) {
			this.getAndSetState(world, replacer, random, pos, config);
			pos.move(Direction.UP);
		}

		Direction direction = Direction.Type.HORIZONTAL.random(random);
		this.generateBranch(pos.mutableCopy(), direction, result, world, replacer, random, height, config);
		if (random.nextFloat() < this.branchChance) this.generateBranch(pos.mutableCopy(), direction.getOpposite(), result, world, replacer, random, height, config);

		return result;
	}

	public void generateBranch(BlockPos.Mutable pos, Direction direction, List<FoliagePlacer.TreeNode> result, TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, int height, TreeFeatureConfig config) {
		int width = height / 2;

		Function<BlockState, BlockState> changeAxis = state -> state.with(PillarBlock.AXIS, direction.getAxis());
		for (int i = 0; i < width; ++i) {
			this.getAndSetState(world, replacer, random, pos, config, changeAxis);
			pos.move(direction);
		}

		for (int i = 0; i < height; ++i) {
			this.getAndSetState(world, replacer, random, pos, config);
			pos.move(Direction.UP);
		}
		result.add(new FoliagePlacer.TreeNode(pos.toImmutable(), 0, false));
	}

//	public record Range(int min, int max) {
//
//		public static final Codec<Range> CODEC = Codec.INT.listOf().xmap(Range::fromList, Range::toList);
//
//		public static Range fromList(List<Integer> list) {
//			return new Range(list.get(0), list.get(1));
//		}
//
//		public List<Integer> toList() {
//			return List.of(this.min, this.max);
//		}
//
//		public int getValue(Random random) {
//			return random.nextInt(this.max - this.min + 1) + this.min;
//		}
//
//		public Range withMin(int min) {
//			return new Range(min, this.max);
//		}
//
//		public Range withMax(int max) {
//			return new Range(this.min, max);
//		}
//
//	}

}
