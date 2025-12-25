package com.infernalstudios.infernalexp.client.block.render;

import com.infernalstudios.infernalexp.block.entity.VolatileGeyserBlockEntity;
import com.infernalstudios.infernalexp.client.block.model.VolatileGeyserModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class VolatileGeyserRenderer extends GeoBlockRenderer<VolatileGeyserBlockEntity> {
    public VolatileGeyserRenderer(BlockEntityRendererProvider.Context context) {
        super(new VolatileGeyserModel());
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}