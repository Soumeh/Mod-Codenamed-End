package org.solstice.end.mixin;

import net.minecraft.block.DispenserBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.solstice.end.content.entity.DissolvableEntity;
import org.solstice.end.content.entity.EvilExperienceOrbEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity implements DissolvableEntity {

	public ItemEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Unique private static final int UNCRAFTING_RATE_MULTIPLIER = 8;
	@Unique private static final int UNENCHANTING_RATE_MULTIPLIER = 8;

	@Shadow public abstract ItemStack getStack();

	@Override
	public void dissolve(ServerWorld world, BlockPos pos) {
		ItemStack stack = this.getStack();
		Random random = Random.create();
		if (stack.isDamageable()) this.dissolveDamageable(world, stack, random);
		else this.dissolveCraftable(world, pos, stack, random);
	}

	@Unique
	protected void dissolveCraftable(ServerWorld world, BlockPos pos, ItemStack stack, Random random) {
		DynamicRegistryManager registry = world.getRegistryManager();

		Predicate<RecipeEntry<?>> canBeUncrafted = entry -> {
			ItemStack result = entry.value().getResult(registry);
			if (result.getItem().equals(stack.getItem())) System.out.println(result);
			return result.getItem().equals(stack.getItem()) && result.getCount() == 1;
		};

		List<RecipeEntry<?>> recipes = new ArrayList<>();
		world.getRecipeManager().sortedValues().stream()
			.filter(canBeUncrafted)
			.forEach(recipes::addFirst);

		if (recipes.isEmpty()) return;

		List<ItemStack> ingredients = new ArrayList<>();
		recipes.getFirst().value().getIngredients().stream()
			.map(Ingredient::getMatchingStacks)
			.map(Arrays::stream)
			.map(Stream::findFirst)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.forEach(ingredients::add);

		float uncraftingRate = (float) (UNCRAFTING_RATE_MULTIPLIER / ingredients.size()) / UNCRAFTING_RATE_MULTIPLIER;
		for (ItemStack ingredient : ingredients) {
			if (random.nextFloat() < uncraftingRate)
				DispenserBlock.dropStack(world, pos, Direction.UP, ingredient);
		}
		stack.decrement(1);
	}

	@Unique
	protected void dissolveDamageable(ServerWorld world, ItemStack stack, Random random) {
		stack.damage(3, world, null, item -> {});
		if (!stack.hasEnchantments()) return;

		int disenchantChance = stack.getMaxDamage() / UNENCHANTING_RATE_MULTIPLIER;
		if (random.nextInt(disenchantChance) != 0) return;

		RegistryEntryList<Enchantment> enchantments = RegistryEntryList.of(stack.getEnchantments().getEnchantments().stream().toList());
		RegistryEntry<Enchantment> enchantment = enchantments.getRandom(random).orElseThrow();
		ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(stack.getEnchantments());
		builder.remove(entry -> entry.equals(enchantment));
		stack.set(DataComponentTypes.ENCHANTMENTS, builder.build());

		int experience = enchantment.value().getAnvilCost() * stack.getEnchantments().getLevel(enchantment);
		if (enchantment.isIn(EnchantmentTags.CURSE)) {
			EvilExperienceOrbEntity.spawn(world, this.getPos(), experience * 2);
		} else {
			ExperienceOrbEntity.spawn(world, this.getPos(), experience);
		}

	}

}
