package org.solstice.end.registry;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.solstice.end.ModCodenamedEnd;
import org.solstice.end.content.item.TickingBucketItem;

import java.util.function.Function;

public class ModItems {

    public static void init() {}

	public static final Item ACID_BUCKET = register("acid_bucket",
		settings -> new TickingBucketItem(ModFluids.ACID, settings),
		new Item.Settings()
			.recipeRemainder(Items.BUCKET)
			.maxDamage(16)
	);

	public static Item register(String name) {
		return register(name, Item::new);
	}

	public static Item register(String name, Function<Item.Settings, Item> function) {
		return register(name, function, new Item.Settings());
	}

	public static Item register(String name, Item.Settings settings) {
		return register(name, Item::new, settings);
	}

	public static Item register(String name, Function<Item.Settings, Item> function, Item.Settings settings) {
        Identifier id = ModCodenamedEnd.of(name);
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);
        Item item = function.apply(settings);
        return Registry.register(Registries.ITEM, key, item);
    }

}
