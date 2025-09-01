package org.solstice.end.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.solstice.end.ModCodenamedEnd;

public class ModItemGroups {

	public static void init() {}

	public static final ItemGroup MAIN = register("main", FabricItemGroup.builder()
		.icon(() -> new ItemStack(ModItems.ACID_BUCKET))
		.entries((context, entries) -> {
			entries.add(ModBlocks.THORNWOOD_LOG);
			entries.add(ModBlocks.THORNWOOD_LEAVES);
			entries.add(ModBlocks.THORNWOOD_SPIKE);
			entries.add(ModBlocks.POPLOOM_SHRUB);
			entries.add(ModBlocks.MIRITE);
			entries.add(ModBlocks.STAINED_MIRITE);
			entries.add(ModItems.ACID_BUCKET);
		})
	);

	public static ItemGroup register(String name, ItemGroup.Builder builder) {
		Identifier id = ModCodenamedEnd.of(name);
		Text displayName = Text.translatable(id.toTranslationKey("item_group"));
		ItemGroup group = builder
			.displayName(displayName)
			.build();
		return register(id, group);
	}

	public static ItemGroup register(Identifier id, ItemGroup group) {
		return Registry.register(Registries.ITEM_GROUP, id, group);
	}

}
