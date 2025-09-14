package org.solstice.end.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.biome.v1.TheEndBiomes;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.solstice.end.ModCodenamedEnd;
import org.solstice.end.client.content.renderer.EndSkyRenderer;
import org.solstice.end.client.content.renderer.EndWeatherRenderer;

public class ModBiomes {

	public static void init() {
//		TheEndBiomes.addHighlandsBiome(
//			ACIMIRE_BRINE, 1
//		);
		TheEndBiomes.addMidlandsBiome(
			ACIMIRE_BRINE, ACIMIRE_BRINE, 1
		);
		ServerTickEvents.END_WORLD_TICK.register(world -> {
			world.getPlayers().forEach(player -> {
				if (world.getBiome(player.getBlockPos()).equals(ACIMIRE_BRINE)) {

				}
			});
		});
	}

	@Environment(EnvType.CLIENT)
	public static void clientInit() {
//		DimensionRenderingRegistry.registerSkyRenderer(World.END, EndSkyRenderer.INSTANCE);
//		DimensionRenderingRegistry.registerWeatherRenderer(World.END, EndWeatherRenderer.INSTANCE);
	}

	public static final RegistryKey<Biome> ACIMIRE_BRINE = of("acimire_brine");

	public static RegistryKey<Biome> of(String name) {
		return RegistryKey.of(RegistryKeys.BIOME, ModCodenamedEnd.of(name));
	}

}
