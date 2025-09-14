package org.solstice.end;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solstice.end.registry.*;

public class ModCodenamedEnd implements ModInitializer {

	public static final String MOD_ID = "mod_codenamed_end";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier of(String path) {
		return Identifier.of(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		ModBiomes.init();
		ModWorldgen.init();

		ModEntityTypes.init();
		ModFluids.init();
		ModBlocks.init();
		ModItems.init();

		ModParticles.init();
		ModDamageSources.init();
		ModItemGroups.init();
	}

}
