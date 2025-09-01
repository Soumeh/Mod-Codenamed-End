package org.solstice.end.content.item;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.solstice.end.registry.ModBlocks;

public class TickingBucketItem extends BucketItem {

	public static int BUCKET_TICK_COOLDOWN = 20;

	static {
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			BUCKET_TICK_COOLDOWN--;
			if (BUCKET_TICK_COOLDOWN != 0) return;
			BUCKET_TICK_COOLDOWN = 20;
			server.getPlayerManager().getPlayerList().forEach(player -> {
				player.getInventory().main.forEach(stack -> {
					if (!player.isCreative() && stack.getItem() instanceof TickingBucketItem) stack.damage(1, server.getOverworld(), player, item -> {
						player.getWorld().setBlockState(player.getBlockPos(), ModBlocks.ACID.getDefaultState());
					});
				});
			});
		});
	}

	public TickingBucketItem(Fluid fluid, Settings settings) {
		super(fluid, settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		TypedActionResult<ItemStack> result = super.use(world, user, hand);
		if (!user.isCreative() && result.getResult().isAccepted())
			user.setStackInHand(hand, ItemStack.EMPTY);
		return result;
	}

}
