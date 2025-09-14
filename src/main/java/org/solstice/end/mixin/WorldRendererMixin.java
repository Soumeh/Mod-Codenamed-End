package org.solstice.end.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

	@Shadow
	@Final
	private MinecraftClient client;

	@Inject(
		method = "renderSky",
		at = @At("HEAD")
	)
	private void getTickDelta(
		Matrix4f v, Matrix4f p, float tickDelta, Camera c, boolean t, Runnable f, CallbackInfo ci,
		@Share("tickDelta") LocalFloatRef tickDeltaRef
	) {
		tickDeltaRef.set(tickDelta);
	}

	@ModifyArg(
		method = "renderEndSky",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/VertexConsumer;color(I)Lnet/minecraft/client/render/VertexConsumer;"
		)
	)
	private int modifyEndSkyColor(int original, @Share("tickDelta") LocalFloatRef tickDeltaRef) {
		Vec3d skyColors = this.client.world.getSkyColor(this.client.gameRenderer.getCamera().getPos(), tickDeltaRef.get());
		int skyColor = (0) | ((int)skyColors.x << 16) | ((int)skyColors.y << 8) | (int)skyColors.z;

//		if (skyColor == 0) return original;
		return skyColor;
	}

}
