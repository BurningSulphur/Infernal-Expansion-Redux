package com.infernalstudios.infernalexp.client.entity.render;

import com.infernalstudios.infernalexp.IEConstants;
import com.infernalstudios.infernalexp.entities.VolineEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class VolineGlowLayer<T extends VolineEntity, M extends EntityModel<T>> extends EyesLayer<T, M> {
    private static final RenderType RENDER_TYPE = RenderType.eyes(new ResourceLocation(IEConstants.MOD_ID,
            "textures/entity/voline_glow.png"));
    private static final RenderType TIRED_RENDER_TYPE = RenderType.eyes(new ResourceLocation(IEConstants.MOD_ID,
            "textures/entity/voline_tired_glow.png"));

    public VolineGlowLayer(RenderLayerParent<T, M> rendererIn) {
        super(rendererIn);
    }

    public RenderType getVolineRenderType(VolineEntity entity) {
        return entity.isSleeping() ? TIRED_RENDER_TYPE : RENDER_TYPE;
    }

    @Override
    public void render(@NotNull PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, @NotNull T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.getVolineRenderType(entitylivingbaseIn));
        this.getParentModel().renderToBuffer(matrixStackIn, ivertexbuilder, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public @NotNull RenderType renderType() {
        return RENDER_TYPE;
    }
}