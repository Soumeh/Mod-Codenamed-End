package org.solstice.end.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.solstice.end.ModCodenamedEnd;
import org.solstice.end.content.fluid.AcidFluid;

public class ModFluids {

	public static void init() {}

	@Environment(EnvType.CLIENT)
	public static void clientInit() {
		FluidRenderHandlerRegistry.INSTANCE.register(ACID, FLOWING_ACID, new SimpleFluidRenderHandler(
			ModCodenamedEnd.of("block/acid_still"),
			ModCodenamedEnd.of("block/acid_flow"),
			0x4CC248
		));
		BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), ACID, FLOWING_ACID);
	}

	public static final FlowableFluid FLOWING_ACID = (FlowableFluid) register("flowing_acid", new AcidFluid.Flowing());
	public static final FlowableFluid ACID = (FlowableFluid) register("acid", new AcidFluid.Still());

	public static Fluid register(String name, Fluid fluid) {
		return Registry.register(Registries.FLUID, ModCodenamedEnd.of(name), fluid);
	}

}
