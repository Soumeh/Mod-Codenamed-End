package org.solstice.end.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.solstice.end.ModCodenamedEnd;
import org.solstice.end.content.particle.AcidBubbleParticle;

public class ModParticles {

	public static void init() {}

	@Environment(EnvType.CLIENT)
	public static void clientInit() {
		ParticleFactoryRegistry.getInstance().register(ACID_BUBBLE, AcidBubbleParticle.Factory::new);
	}

	public static final SimpleParticleType ACID_BUBBLE = register("acid_bubble", false);

	private static SimpleParticleType register(String name, boolean alwaysShow) {
		return Registry.register(Registries.PARTICLE_TYPE, ModCodenamedEnd.of(name), FabricParticleTypes.simple(alwaysShow));
	}

}
