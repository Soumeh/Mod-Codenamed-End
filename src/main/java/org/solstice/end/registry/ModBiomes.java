package org.solstice.end.registry;

import net.fabricmc.fabric.api.biome.v1.TheEndBiomes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.Biome;
import org.solstice.end.ModCodenamedEnd;

public class ModBiomes {

	public static void init() {
		TheEndBiomes.addHighlandsBiome(
			ACIMIRE_BRINE, 1
		);
	}

	public static final RegistryKey<Biome> ACIMIRE_BRINE = of("acimire_brine");

	public static RegistryKey<Biome> of(String name) {
		return RegistryKey.of(RegistryKeys.BIOME, ModCodenamedEnd.of(name));
	}

}
