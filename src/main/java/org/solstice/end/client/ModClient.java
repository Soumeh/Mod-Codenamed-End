package org.solstice.end.client;

import net.fabricmc.api.ClientModInitializer;
import org.solstice.end.registry.*;

public class ModClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModEntityTypes.clientInit();
		ModBlocks.clientInit();
		ModFluids.clientInit();
		ModParticles.clientInit();
		ModBiomes.clientInit();
	}

}
