package org.solstice.end.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.entity.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.solstice.end.ModCodenamedEnd;
import org.solstice.end.content.entity.EvilExperienceOrbEntity;
import org.solstice.end.content.entity.renderer.EvilExperienceOrbRenderer;

public class ModEntityTypes {

    public static void init() {
//		FabricDefaultAttributeRegistry.register(CENTURION, CenturionEntity.createAttributes());
	}

	@Environment(EnvType.CLIENT)
	public static void clientInit() {
		EntityRendererRegistry.register(EVIL_EXPERIENCE_ORB, EvilExperienceOrbRenderer::new);
	}

    public static final EntityType<EvilExperienceOrbEntity> EVIL_EXPERIENCE_ORB = register("evil_experience_orb",
		EntityType.Builder.<EvilExperienceOrbEntity>create(EvilExperienceOrbEntity::new, SpawnGroup.MISC)
			.dimensions(0.5F, 0.5F)
			.maxTrackingRange(6)
			.trackingTickInterval(20)
    );

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        Identifier id = ModCodenamedEnd.of(name);
        RegistryKey<EntityType<?>> key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, id);
        return Registry.register(Registries.ENTITY_TYPE, key, builder.build());
    }

}
