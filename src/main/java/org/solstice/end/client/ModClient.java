package org.solstice.end.client;

import net.fabricmc.api.ClientModInitializer;
import org.solstice.end.registry.ModBlocks;
import org.solstice.end.registry.ModFluids;

public class ModClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModBlocks.clientInit();
		ModFluids.clientInit();
	}

}
