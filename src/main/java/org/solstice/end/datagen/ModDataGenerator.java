package org.solstice.end.datagen;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.PlantBlock;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.solstice.end.content.block.FacingCrossBlock;
import org.solstice.euclidsElements.autoDatagen.api.generator.AutoLanguageGenerator;
import org.solstice.euclidsElements.autoDatagen.api.generator.AutoLootTableGenerator;
import org.solstice.euclidsElements.autoDatagen.api.generator.AutoModelGenerator;
import org.solstice.euclidsElements.autoDatagen.api.supplier.BlockModelSupplier;

public class ModDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();

		pack.addProvider(AutoLanguageGenerator::new);
		pack.addProvider(AutoModelGenerator::new);
		pack.addProvider(AutoLootTableGenerator::new);

		BlockModelSupplier.register(FacingCrossBlock.class, ModDataGenerator::registerFacingCross);
		BlockModelSupplier.register(PlantBlock.class, ModDataGenerator::registerPlant);
	}

	public static void registerFacingCross(BlockStateModelGenerator generator, Block block, Identifier id) {
		generator.registerItemModel(block);

		VariantsBlockStateSupplier supplier = VariantsBlockStateSupplier.create(block,
			BlockStateVariant.create().put(VariantSettings.MODEL, Models.CROSS.upload(block, TextureMap.cross(block), generator.modelCollector))
		).coordinate(generator.createUpDefaultFacingVariantMap());

		generator.blockStateCollector.accept(supplier);
	}

	public static void registerPlant(BlockStateModelGenerator generator, Block block, Identifier id) {
		generator.registerItemModel(block);
		Models.CROSS.upload(block, TextureMap.cross(block), generator.modelCollector);
		generator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(block, id.withPrefixedPath("block/")));
	}

}
