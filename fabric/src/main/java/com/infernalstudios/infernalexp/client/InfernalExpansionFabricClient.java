package com.infernalstudios.infernalexp.client;

import com.infernalstudios.infernalexp.client.particle.GlowstoneSparkleParticle;
import com.infernalstudios.infernalexp.client.particle.InfectionParticle;
import com.infernalstudios.infernalexp.module.ModEntityRenderers;
import com.infernalstudios.infernalexp.module.ModParticleTypes;
import com.infernalstudios.infernalexp.registration.holders.BlockDataHolder;
import com.infernalstudios.infernalexp.registration.holders.EntityTypeDataHolder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import java.util.Map;

public class InfernalExpansionFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        IECommonClient.init();
        registerEntityRenderers();
        registerBlockRenderTypes();
        registerParticleProviders();
    }

    private void registerEntityRenderers() {
        for (Map.Entry<EntityTypeDataHolder, EntityRendererProvider> entry : ModEntityRenderers.getEntityRendererRegistry().entrySet()) {
            // Register entity renderers
            EntityRendererRegistry.register(entry.getKey().get(), entry.getValue());
        }
    }

    private void registerBlockRenderTypes() {
        for (BlockDataHolder<?> block : BlockDataHolder.getCutoutBlocks()) {
            BlockRenderLayerMap.INSTANCE.putBlock(block.get(), RenderType.cutout());
        }
    }

    private void registerParticleProviders() {
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.GLOWSTONE_SPARKLE, GlowstoneSparkleParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.INFECTION, InfectionParticle.Factory::new);
    }
}
