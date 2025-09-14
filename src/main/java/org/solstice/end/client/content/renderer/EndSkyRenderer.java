package org.solstice.end.client.content.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.awt.*;

public class EndSkyRenderer implements DimensionRenderingRegistry.SkyRenderer {

	public static final EndSkyRenderer INSTANCE = new EndSkyRenderer();

	@Override
	public void render(WorldRenderContext context) {
		Camera camera = context.camera();
		ClientWorld world = context.world();
		GameRenderer gameRenderer = context.gameRenderer();
		PlayerEntity player = camera.getFocusedEntity() instanceof PlayerEntity ? (PlayerEntity)camera.getFocusedEntity() : null;
		float tickDelta = context.tickCounter().getTickDelta(true);



		CameraSubmersionType submersion = camera.getSubmersionType();
		if (submersion == CameraSubmersionType.POWDER_SNOW || submersion == CameraSubmersionType.LAVA) return;
		if (this.hasBlindnessOrDarkness(camera)) return;

//		MatrixStack matrixStack = context.matrixStack();
		MatrixStack matrixStack = new MatrixStack();
		matrixStack.multiplyPositionMatrix(context.positionMatrix());

		Vec3d skyColors = world.getSkyColor(gameRenderer.getCamera().getPos(), tickDelta);
		int skyColor = (0) | ((int)skyColors.x << 16) | ((int)skyColors.y << 8) | (int)skyColors.z;



		RenderSystem.enableBlend();
		RenderSystem.depthMask(false);
		RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
		RenderSystem.setShaderTexture(0, END_SKY);
		Tessellator tessellator = Tessellator.getInstance();

		for(int i = 0; i < 6; ++i) {
			matrixStack.push();
			if (i == 1) matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
			if (i == 2) matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
			if (i == 3) matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
			if (i == 4) matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0F));
			if (i == 5) matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-90.0F));

			Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
			BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).texture(0.0F, 0.0F).color(skyColor);
			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).texture(0.0F, 16.0F).color(skyColor);
			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).texture(16.0F, 16.0F).color(skyColor);
			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).texture(16.0F, 0.0F).color(skyColor);
			BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
			matrixStack.pop();
		}

		RenderSystem.depthMask(true);
		RenderSystem.disableBlend();



//		BackgroundRenderer.applyFogColor();
//		Tessellator tessellator = Tessellator.getInstance();
//		RenderSystem.depthMask(false);
//		RenderSystem.setShaderColor((float)skyColor.x, (float)skyColor.y, (float)skyColor.z, 1.0F);
//		ShaderProgram shaderProgram = RenderSystem.getShader();
//		this.lightSkyBuffer.bind();
//		this.lightSkyBuffer.draw(matrixStack.peek().getPositionMatrix(), projectionMatrix, shaderProgram);
//		VertexBuffer.unbind();
//		RenderSystem.enableBlend();
//		float[] fs = world.getDimensionEffects().getFogColorOverride(world.getSkyAngle(tickDelta), tickDelta);
//		if (fs != null) {
//			RenderSystem.setShader(GameRenderer::getPositionColorProgram);
//			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//			matrixStack.push();
//			matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
//			float i = MathHelper.sin(world.getSkyAngleRadians(tickDelta)) < 0.0F ? 180.0F : 0.0F;
//			matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(i));
//			matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0F));
//			float j = fs[0];
//			float k = fs[1];
//			float l = fs[2];
//			Matrix4f matrix4f2 = matrixStack.peek().getPositionMatrix();
//			BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
//			bufferBuilder.vertex(matrix4f2, 0.0F, 100.0F, 0.0F).color(j, k, l, fs[3]);
//			int m = 16;
//
//			for(int n = 0; n <= 16; ++n) {
//				float o = (float)n * ((float)Math.PI * 2F) / 16.0F;
//				float p = MathHelper.sin(o);
//				float q = MathHelper.cos(o);
//				bufferBuilder.vertex(matrix4f2, p * 120.0F, q * 120.0F, -q * 40.0F * fs[3]).color(fs[0], fs[1], fs[2], 0.0F);
//			}
//
//			BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
//			matrixStack.pop();
//		}
//
//		RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
//		matrixStack.push();
//		float i = 1.0F - world.getRainGradient(tickDelta);
//		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, i);
//		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90.0F));
//		matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(world.getSkyAngle(tickDelta) * 360.0F));
//		Matrix4f matrix4f3 = matrixStack.peek().getPositionMatrix();
//		float k = 30.0F;
//		RenderSystem.setShader(GameRenderer::getPositionTexProgram);
//		RenderSystem.setShaderTexture(0, SUN);
//		BufferBuilder bufferBuilder2 = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
//		bufferBuilder2.vertex(matrix4f3, -k, 100.0F, -k).texture(0.0F, 0.0F);
//		bufferBuilder2.vertex(matrix4f3, k, 100.0F, -k).texture(1.0F, 0.0F);
//		bufferBuilder2.vertex(matrix4f3, k, 100.0F, k).texture(1.0F, 1.0F);
//		bufferBuilder2.vertex(matrix4f3, -k, 100.0F, k).texture(0.0F, 1.0F);
//		BufferRenderer.drawWithGlobalProgram(bufferBuilder2.end());
//		k = 20.0F;
//		RenderSystem.setShaderTexture(0, MOON_PHASES);
//		int r = world.getMoonPhase();
//		int s = r % 4;
//		int m = r / 4 % 2;
//		float t = (float)(s + 0) / 4.0F;
//		float o = (float)(m + 0) / 2.0F;
//		float p = (float)(s + 1) / 4.0F;
//		float q = (float)(m + 1) / 2.0F;
//		bufferBuilder2 = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
//		bufferBuilder2.vertex(matrix4f3, -k, -100.0F, k).texture(p, q);
//		bufferBuilder2.vertex(matrix4f3, k, -100.0F, k).texture(t, q);
//		bufferBuilder2.vertex(matrix4f3, k, -100.0F, -k).texture(t, o);
//		bufferBuilder2.vertex(matrix4f3, -k, -100.0F, -k).texture(p, o);
//		BufferRenderer.drawWithGlobalProgram(bufferBuilder2.end());
//		float u = world.getStarBrightness(tickDelta) * i;
//		if (u > 0.0F) {
//			RenderSystem.setShaderColor(u, u, u, u);
//			BackgroundRenderer.clearFog();
//			this.starsBuffer.bind();
//			this.starsBuffer.draw(matrixStack.peek().getPositionMatrix(), projectionMatrix, GameRenderer.getPositionProgram());
//			VertexBuffer.unbind();
//			fogCallback.run();
//		}
//
//		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//		RenderSystem.disableBlend();
//		RenderSystem.defaultBlendFunc();
//		matrixStack.pop();
//		RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
//		double d = player.getCameraPosVec(tickDelta).y - world.getLevelProperties().getSkyDarknessHeight(world);
//		if (d < (double)0.0F) {
//			matrixStack.push();
//			matrixStack.translate(0.0F, 12.0F, 0.0F);
//			this.darkSkyBuffer.bind();
//			this.darkSkyBuffer.draw(matrixStack.peek().getPositionMatrix(), projectionMatrix, shaderProgram);
//			VertexBuffer.unbind();
//			matrixStack.pop();
//		}
//
//		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//		RenderSystem.depthMask(true);

	}

	private boolean hasBlindnessOrDarkness(Camera camera) {
		Entity entity = camera.getFocusedEntity();
		if (!(entity instanceof LivingEntity livingEntity)) return false;

		return livingEntity.hasStatusEffect(StatusEffects.BLINDNESS) || livingEntity.hasStatusEffect(StatusEffects.DARKNESS);
	}

	private static final Identifier END_SKY = Identifier.ofVanilla("textures/environment/end_sky.png");

	private void renderEndSky(MatrixStack matrices) {
		RenderSystem.enableBlend();
		RenderSystem.depthMask(false);
		RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
		RenderSystem.setShaderTexture(0, END_SKY);
		Tessellator tessellator = Tessellator.getInstance();

		for(int i = 0; i < 6; ++i) {
			matrices.push();
			if (i == 1) matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
			if (i == 2) matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
			if (i == 3) matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
			if (i == 4) matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0F));
			if (i == 5) matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-90.0F));

			Matrix4f matrix4f = matrices.peek().getPositionMatrix();
			BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).texture(0.0F, 0.0F).color(-14145496);
			bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).texture(0.0F, 16.0F).color(-14145496);
			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).texture(16.0F, 16.0F).color(-14145496);
			bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).texture(16.0F, 0.0F).color(-14145496);
			BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
			matrices.pop();
		}

		RenderSystem.depthMask(true);
		RenderSystem.disableBlend();
	}

}
