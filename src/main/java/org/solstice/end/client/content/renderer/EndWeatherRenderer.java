package org.solstice.end.client.content.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.solstice.end.ModCodenamedEnd;

@Environment(EnvType.CLIENT)
public class EndWeatherRenderer implements DimensionRenderingRegistry.WeatherRenderer {

	public static final EndWeatherRenderer INSTANCE = new EndWeatherRenderer();

	private static final Identifier ACID_RAIN = ModCodenamedEnd.of("textures/environment/acid_rain.png");

	private final float[] NORMAL_LINE_DX = new float[1024];
	private final float[] NORMAL_LINE_DZ = new float[1024];

	@Override
	public void render(WorldRenderContext context) {
		World world = context.world();
		context.lightmapTextureManager().enable();
		Vec3d cameraPos = context.camera().getPos();
		float ticks = context.tickCounter().getLastDuration();
		float tickDelta = context.tickCounter().getTickDelta(true);

		int i = MathHelper.floor(cameraPos.x);
		int j = MathHelper.floor(cameraPos.y);
		int k = MathHelper.floor(cameraPos.z);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = null;
		RenderSystem.disableCull();
		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();

		int l = 5;
		if (MinecraftClient.isFancyGraphicsOrBetter()) {
			l = 10;
		}

		RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());
		int m = -1;
		float g = ticks + tickDelta;
		RenderSystem.setShader(GameRenderer::getParticleProgram);
		BlockPos.Mutable mutable = new BlockPos.Mutable();

		for (int n = k - l; n <= k + l; n++) {
			for (int o = i - l; o <= i + l; o++) {
				int p = (n - k + 16) * 32 + o - i + 16;
				double d = this.NORMAL_LINE_DX[p] * 0.5;
				double e = this.NORMAL_LINE_DZ[p] * 0.5;
				mutable.set(o, cameraPos.y, n);
				Biome biome = world.getBiome(mutable).value();
				if (biome.hasPrecipitation()) {
					int q = world.getTopY(Heightmap.Type.MOTION_BLOCKING, o, n);
					int r = j - l;
					int s = j + l;
					if (r < q) {
						r = q;
					}

					if (s < q) {
						s = q;
					}

					int t = q;
					if (q < j) {
						t = j;
					}

					if (r != s) {
						Random random = Random.create(o * o * 3121 + o * 45238971 ^ n * n * 418711 + n * 13761);
						mutable.set(o, r, n);

//						Biome.Precipitation precipitation = biome.getPrecipitation(mutable);
//						if (precipitation == Biome.Precipitation.RAIN) {

						if (m != 0) {
							if (m >= 0) {
								BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
							}

							m = 0;
							RenderSystem.setShaderTexture(0, ACID_RAIN);
							bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
						}

						int u = (int) ticks & 131071;
						int v = o * o * 3121 + o * 45238971 + n * n * 418711 + n * 13761 & 0xFF;
						float h = 3.0F + random.nextFloat();
						float w = -(u + v + tickDelta) / 32.0F * h;
						float x = w % 32.0F;
						double y = o + 0.5 - cameraPos.x;
						double z = n + 0.5 - cameraPos.z;
						float aa = (float)Math.sqrt(y * y + z * z) / l;
						float ab = ((1.0F - aa * aa) * 0.5F + 0.5F);
						mutable.set(o, t, n);
						int ac = WorldRenderer.getLightmapCoordinates(world, mutable);
						bufferBuilder.vertex((float)(o - cameraPos.x - d + 0.5), (float)(s - cameraPos.y), (float)(n - cameraPos.z - e + 0.5))
							.texture(0.0F, r * 0.25F + x)
							.color(1.0F, 1.0F, 1.0F, ab)
							.light(ac);
						bufferBuilder.vertex((float)(o - cameraPos.x + d + 0.5), (float)(s - cameraPos.y), (float)(n - cameraPos.z + e + 0.5))
							.texture(1.0F, r * 0.25F + x)
							.color(1.0F, 1.0F, 1.0F, ab)
							.light(ac);
						bufferBuilder.vertex((float)(o - cameraPos.x + d + 0.5), (float)(r - cameraPos.y), (float)(n - cameraPos.z + e + 0.5))
							.texture(1.0F, s * 0.25F + x)
							.color(1.0F, 1.0F, 1.0F, ab)
							.light(ac);
						bufferBuilder.vertex((float)(o - cameraPos.x - d + 0.5), (float)(r - cameraPos.y), (float)(n - cameraPos.z - e + 0.5))
							.texture(0.0F, s * 0.25F + x)
							.color(1.0F, 1.0F, 1.0F, ab)
							.light(ac);
					}
				}
			}
		}

		if (m >= 0) {
			BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
		}

		RenderSystem.enableCull();
		RenderSystem.disableBlend();
		context.lightmapTextureManager().disable();
	}

}
