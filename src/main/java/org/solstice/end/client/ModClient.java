package org.solstice.end.client;

import net.fabricmc.api.ClientModInitializer;
import org.solstice.end.registry.ModBlocks;
import org.solstice.end.registry.ModFluids;
import org.solstice.end.registry.ModParticles;

public class ModClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModBlocks.clientInit();
		ModFluids.clientInit();
		ModParticles.clientInit();
	}

}
