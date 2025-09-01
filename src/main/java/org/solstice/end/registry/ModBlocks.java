package org.solstice.end.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.solstice.end.ModCodenamedEnd;

import java.util.function.Function;

public class ModBlocks {

    public static void init() {}

	@Environment(EnvType.CLIENT)
	public static void clientInit() {
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
				THORNWOOD_LEAVES,
				THORNWOOD_SPIKE
		);
	}

	public static final Block THORNWOOD_LOG = register("thornwood_log",
            PillarBlock::new,
			AbstractBlock.Settings.create()
					.instrument(NoteBlockInstrument.BASS)
					.strength(2.0F)
					.sounds(BlockSoundGroup.WOOD)
					.burnable()
	);
	public static final Block THORNWOOD_LEAVES = register("thornwood_leaves",
			LeavesBlock::new,
			AbstractBlock.Settings.create()
					.mapColor(MapColor.DARK_GREEN)
					.strength(0.2F)
					.ticksRandomly()
					.sounds(BlockSoundGroup.GRASS)
					.nonOpaque()
					.allowsSpawning(Blocks::canSpawnOnLeaves)
					.suffocates(Blocks::never)
					.blockVision(Blocks::never)
					.burnable()
					.pistonBehavior(PistonBehavior.DESTROY)
					.solidBlock(Blocks::never)
	);
	public static final Block THORNWOOD_SPIKE = register("thornwood_spike",
			settings -> new AmethystClusterBlock(7.0F, 3.0F, settings),
			AbstractBlock.Settings.create()
	);

	public static final Block ACID = register("acid",
			settings -> new FluidBlock(ModFluids.ACID, settings),
			AbstractBlock.Settings.create()
					.mapColor(MapColor.EMERALD_GREEN)
					.replaceable()
					.noCollision()
					.strength(100.0F)
					.pistonBehavior(PistonBehavior.DESTROY)
					.dropsNothing()
					.liquid()
					.sounds(BlockSoundGroup.INTENTIONALLY_EMPTY)
	);

	public static Block register(String name, AbstractBlock.Settings settings) {
		return register(name, Block::new, settings);
	}

	public static Block register(String name, Function<AbstractBlock.Settings, Block> function, AbstractBlock.Settings settings) {
		return register(name, function, settings, new Item.Settings());
	}

	public static Block register(String name, Function<AbstractBlock.Settings, Block> function, AbstractBlock.Settings blockSettings, Item.Settings itemSettings) {
        Identifier id = ModCodenamedEnd.of(name);
        RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, id);
        Block block = function.apply(blockSettings);
        Registry.register(Registries.BLOCK, blockKey, block);
		RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, id);
		Item item = new BlockItem(block, itemSettings);
		Registry.register(Registries.ITEM, itemKey, item);
		return block;
    }

}
