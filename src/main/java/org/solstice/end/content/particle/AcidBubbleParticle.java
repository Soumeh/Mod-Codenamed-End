package org.solstice.end.content.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import org.solstice.end.registry.ModTags;

@Environment(EnvType.CLIENT)
public class AcidBubbleParticle extends SpriteBillboardParticle {

	public AcidBubbleParticle(ClientWorld clientWorld, double x, double y, double z, double deltaX, double deltaY, double deltaZ) {
		super(clientWorld, x, y, z);
		this.gravityStrength = -0.125F;
		this.velocityMultiplier = 0.85F;
		this.setBoundingBoxSpacing(0.02F, 0.02F);
//		this.scale *= this.random.nextFloat() * 0.6F + 0.2F;
		this.velocityX = deltaX * (double)0.2F + (Math.random() * (double)2.0F - (double)1.0F) * (double)0.02F;
		this.velocityY = deltaY * (double)0.2F + (Math.random() * (double)2.0F - (double)1.0F) * (double)0.02F;
		this.velocityZ = deltaZ * (double)0.2F + (Math.random() * (double)2.0F - (double)1.0F) * (double)0.02F;
		this.maxAge = (int)((double)40.0F / (Math.random() * 0.8 + 0.2));
//		super(clientWorld, x, y, z);
//		this.setBoundingBoxSpacing(0.02F, 0.02F);
//		this.scale *= this.random.nextFloat() * 0.6F + 0.2F;
//		this.velocityX = deltaX * (double)0.2F + (Math.random() * (double)2.0F - (double)1.0F) * (double)0.02F;
//		this.velocityY = deltaY * (double)0.2F + (Math.random() * (double)2.0F - (double)1.0F) * (double)0.02F;
//		this.velocityZ = deltaZ * (double)0.2F + (Math.random() * (double)2.0F - (double)1.0F) * (double)0.02F;
//		this.maxAge = (int)((double)8.0F / (Math.random() * 0.8 + 0.2));
//
		this.scale = (float) 1 / 4;
	}

	@Override
	public void tick() {
		super.tick();
		if (this.dead) return;

		BlockPos pos = BlockPos.ofFloored(this.x, this.y, this.z);
		if (!this.world.getFluidState(pos).isIn(ModTags.ACID))
			this.markDead();
	}

//	public void tick() {
//		this.prevPosX = this.x;
//		this.prevPosY = this.y;
//		this.prevPosZ = this.z;
//
//		if (this.maxAge-- <= 0) {
//			this.markDead();
//			return;
//		}
//
//		this.velocityY += 0.002;
//		this.move(this.velocityX, this.velocityY, this.velocityZ);
//		this.velocityX *= 0.85F;
//		this.velocityY *= 0.85F;
//		this.velocityZ *= 0.85F;
//		if (!this.world.getFluidState(BlockPos.ofFloored(this.x, this.y, this.z)).isIn(ModTags.ACID))
//			this.markDead();
//	}

	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<SimpleParticleType> {

		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(SimpleParticleType type, ClientWorld world, double d, double e, double f, double g, double h, double i) {
			AcidBubbleParticle particle = new AcidBubbleParticle(world, d, e, f, g, h, i);
			particle.setSprite(this.spriteProvider);
			return particle;
		}

	}

}
