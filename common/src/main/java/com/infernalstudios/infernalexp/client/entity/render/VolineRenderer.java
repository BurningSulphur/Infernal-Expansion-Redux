package com.infernalstudios.infernalexp.client.entity.render;

import com.infernalstudios.infernalexp.IEConstants;
import com.infernalstudios.infernalexp.client.entity.model.VolineModel;
import com.infernalstudios.infernalexp.entities.VolineEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class VolineRenderer extends MobRenderer<VolineEntity, VolineModel<VolineEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(IEConstants.MOD_ID,
            "textures/entity/voline.png");
    protected static final ResourceLocation TIRED_TEXTURE = new ResourceLocation(IEConstants.MOD_ID,
            "textures/entity/voline_tired.png");

    public VolineRenderer(EntityRendererProvider.Context context) {
        super(context, new VolineModel<>(context.bakeLayer(VolineModel.LAYER_LOCATION)), 0.7F);
        this.addLayer(new VolineGlowLayer<>(this));
    }

    @Override
    protected void scale(VolineEntity entity, PoseStack poseStack, float partialTickTime) {
        float size = entity.getSizeFactor();
        poseStack.scale(size, size, size);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(VolineEntity entity) {
        return entity.isSleeping() ? TIRED_TEXTURE : TEXTURE;
    }
}