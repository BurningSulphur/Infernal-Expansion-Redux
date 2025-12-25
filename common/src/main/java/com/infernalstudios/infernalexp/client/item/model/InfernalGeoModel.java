package com.infernalstudios.infernalexp.client.item.model;

import com.infernalstudios.infernalexp.items.InfernalGeoBlockItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class InfernalGeoModel extends GeoModel<InfernalGeoBlockItem> {
    @Override
    public ResourceLocation getModelResource(InfernalGeoBlockItem item) {
        return item.getModelLocation();
    }

    @Override
    public ResourceLocation getTextureResource(InfernalGeoBlockItem item) {
        return item.getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationResource(InfernalGeoBlockItem item) {
        return null;
    }
}