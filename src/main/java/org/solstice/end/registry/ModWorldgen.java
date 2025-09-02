package org.solstice.end.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import org.solstice.end.ModCodenamedEnd;
import org.solstice.end.content.worldgen.ThornyTrunkPlacer;

public class ModWorldgen {

	public static void init() {}

	public static TrunkPlacerType<?> THORNY_TRUNK_PLACER = register("thorny_trunk_placer", ThornyTrunkPlacer.CODEC);

	private static <T extends TrunkPlacer> TrunkPlacerType<T> register(String name, MapCodec<T> codec) {
		return Registry.register(Registries.TRUNK_PLACER_TYPE, ModCodenamedEnd.of(name), new TrunkPlacerType<>(codec));
	}

}
