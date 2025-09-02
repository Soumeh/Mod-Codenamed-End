package org.solstice.end.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import org.solstice.end.ModCodenamedEnd;
import org.solstice.end.content.worldgen.ThornyTreeDecorator;
import org.solstice.end.content.worldgen.WindingTrunkPlacer;

public class ModWorldgen {

	public static void init() {}

	public static TrunkPlacerType<?> WINDING_TRUNK_PLACER = trunkPlacer("winding_trunk_placer", WindingTrunkPlacer.CODEC);

	public static TreeDecoratorType<?> TRUNK_SPIKE_DECORATOR = treeDecorator("trunk_spikes", ThornyTreeDecorator.CODEC);

	private static <T extends TrunkPlacer> TrunkPlacerType<T> trunkPlacer(String name, MapCodec<T> codec) {
		return Registry.register(Registries.TRUNK_PLACER_TYPE, ModCodenamedEnd.of(name), new TrunkPlacerType<>(codec));
	}

	private static <T extends TreeDecorator> TreeDecoratorType<T> treeDecorator(String name, MapCodec<T> codec) {
		return Registry.register(Registries.TREE_DECORATOR_TYPE, ModCodenamedEnd.of(name), new TreeDecoratorType<>(codec));
	}

}
