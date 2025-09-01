package org.solstice.end.registry;

import com.mojang.serialization.Codec;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import org.solstice.end.ModCodenamedEnd;
import org.solstice.euclidsElements.tag.api.MapTagKey;

public class ModTags {

	public static void init() {}

	public static final TagKey<Fluid> ACID = of("acid",
		RegistryKeys.FLUID
	);

	protected static <T> TagKey<T> of(String name, RegistryKey<Registry<T>> key) {
		return TagKey.of(key, ModCodenamedEnd.of(name));
	}

	protected static <T, R> MapTagKey<T, R> ofMap(String name, RegistryKey<Registry<T>> key, Codec<R> codec) {
		return MapTagKey.of(key, codec, ModCodenamedEnd.of(name));
	}

}
