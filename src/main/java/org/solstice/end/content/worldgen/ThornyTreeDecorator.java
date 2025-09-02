package org.solstice.end.content.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import org.solstice.end.content.block.FacingCrossBlock;
import org.solstice.end.registry.ModBlocks;

import java.util.List;

public class ThornyTreeDecorator extends TreeDecorator {

	public static final MapCodec<ThornyTreeDecorator> CODEC = Codec.floatRange(0, 1).fieldOf("probability").xmap(ThornyTreeDecorator::new, ThornyTreeDecorator::getProbability);

	@Override
	protected TreeDecoratorType<?> getType() {
		return TreeDecoratorType.COCOA;
	}

	private final float probability;

	public ThornyTreeDecorator(float probability) {
		this.probability = probability;
	}

	public float getProbability() {
		return this.probability;
	}

	public void generate(TreeDecorator.Generator generator) {
		Random random = generator.getRandom();

		generator.getLogPositions().forEach(pos -> {
			if (random.nextFloat() > this.probability) return;

			Direction direction = Direction.random(random);
			BlockPos newPos = pos.offset(direction);

			if (generator.isAir(newPos)) {
				generator.replace(newPos, ModBlocks.THORNWOOD_SPIKE.getDefaultState().with(FacingCrossBlock.FACING, direction));
			}
		});
	}

}
