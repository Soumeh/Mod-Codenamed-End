package org.solstice.end.content.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.*;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.solstice.end.registry.ModTags;

import java.util.Optional;

public class EndSaplingBlock extends PlantBlock implements Fertilizable {

	public static final MapCodec<EndSaplingBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		RegistryKey.createCodec(RegistryKeys.CONFIGURED_FEATURE).fieldOf("feature").forGetter((block) -> block.featureKey),
		createSettingsCodec()
	).apply(instance, EndSaplingBlock::new));

	protected static final VoxelShape SHAPE = Block.createCuboidShape(2, 0, 2, 14, 12, 14);

	private final RegistryKey<ConfiguredFeature<?, ?>> featureKey;

	@Override
	public MapCodec<EndSaplingBlock> getCodec() {
		return CODEC;
	}

	public EndSaplingBlock(RegistryKey<ConfiguredFeature<?, ?>> featureKey, AbstractBlock.Settings settings) {
		super(settings);
		this.featureKey = featureKey;
	}

	@Override
	protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
		return floor.isIn(ModTags.END_SAPLING_PLACEABLE);
	}

	@Override
	public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
		return world.getBlockState(pos.down()).isIn(ModTags.END_SAPLING_PLACEABLE);
	}

	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return (double)random.nextFloat() < 0.4;
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		this.getFeatureEntry(world).ifPresent(entry -> {
			var test = entry.value().generate(world, world.getChunkManager().getChunkGenerator(), random, pos);
			System.out.println(test);
		});
	}

	private Optional<? extends RegistryEntry<ConfiguredFeature<?, ?>>> getFeatureEntry(WorldView world) {
		return world.getRegistryManager().get(RegistryKeys.CONFIGURED_FEATURE).getEntry(this.featureKey);
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

}
