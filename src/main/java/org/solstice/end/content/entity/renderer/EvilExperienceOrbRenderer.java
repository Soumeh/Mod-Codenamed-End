package org.solstice.end.content.entity.renderer;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ExperienceOrbEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.util.Identifier;
import org.solstice.end.ModCodenamedEnd;

public class EvilExperienceOrbRenderer extends ExperienceOrbEntityRenderer {

	private static final Identifier TEXTURE = ModCodenamedEnd.of("textures/entity/evil_experience_orb.png");
	private static final RenderLayer LAYER = RenderLayer.getItemEntityTranslucentCull(TEXTURE);

	public EvilExperienceOrbRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public void render(ExperienceOrbEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumer, int light) {
		matrices.push();
		int j = entity.getOrbSize();
		float u1 = (float)(j % 4 * 16) / 64.0F;
		float u2 = (float)(j % 4 * 16 + 16) / 64.0F;
		float v1 = (float)(j / 4 * 16) / 64.0F;
		float v2 = (float)(j / 4 * 16 + 16) / 64.0F;
		float delta = ((float)entity.age + tickDelta) / 2.0F;
		int green = 0;
		int blue = 0;
//		int green = (int)((MathHelper.sin(delta + 0.0F) + 1.0F) * 0.5F * 255.0F);
//		int blue = (int)((MathHelper.sin(delta + 4.1887903F) + 1.0F) * 0.1F * 255.0F);
		matrices.translate(0.0F, 0.1F, 0.0F);
		matrices.multiply(this.dispatcher.getRotation());
		matrices.scale(0.3F, 0.3F, 0.3F);
		VertexConsumer vertices = vertexConsumer.getBuffer(LAYER);
		MatrixStack.Entry entry = matrices.peek();
		vertex(vertices, entry, -0.5F, -0.25F, 255, green, blue, u1, v2, light);
		vertex(vertices, entry, 0.5F, -0.25F, 255, green, blue, u2, v2, light);
		vertex(vertices, entry, 0.5F, 0.75F, 255, green, blue, u2, v1, light);
		vertex(vertices, entry, -0.5F, 0.75F, 255, green, blue, u1, v1, light);
		matrices.pop();
		super.render(entity, yaw, tickDelta, matrices, vertexConsumer, light);
	}

	private static void vertex(VertexConsumer vertexConsumer, MatrixStack.Entry matrix, float x, float y, int red, int green, int blue, float u, float v, int light) {
		vertexConsumer.vertex(matrix, x, y, 0.0F)
			.color(red, green, blue, 128)
			.texture(u, v)
			.overlay(OverlayTexture.DEFAULT_UV)
			.light(light)
			.normal(matrix, 0.0F, 1.0F, 0.0F);
	}

	@Override
	public Identifier getTexture(ExperienceOrbEntity experienceOrbEntity) {
		return TEXTURE;
	}

}
