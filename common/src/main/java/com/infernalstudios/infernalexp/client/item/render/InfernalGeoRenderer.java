package com.infernalstudios.infernalexp.client.item.render;

import com.infernalstudios.infernalexp.client.item.model.InfernalGeoModel;
import com.infernalstudios.infernalexp.items.InfernalGeoBlockItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer; // Import this

public class InfernalGeoRenderer extends GeoItemRenderer<InfernalGeoBlockItem> {
    public InfernalGeoRenderer() {
        super(new InfernalGeoModel());

        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}