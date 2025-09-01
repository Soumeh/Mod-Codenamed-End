package org.solstice.end.registry;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;
import org.solstice.end.ModCodenamedEnd;

public class ModDamageSources {

	public static void init() {}

	public static final RegistryKey<DamageType> CORROSION = of("corrosion");

	public static DamageSource corrosion(World world) {
		return new DamageSource(
			world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(CORROSION)
		);
	}

	public static RegistryKey<DamageType> of(String path) {
		return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, ModCodenamedEnd.of(path));
	}

}
