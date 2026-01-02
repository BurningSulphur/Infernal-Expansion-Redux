package com.infernalstudios.infernalexp.client.block.render;

import com.infernalstudios.infernalexp.IEConstants;
import com.infernalstudios.infernalexp.block.VolatileGeyserBlock;
import com.infernalstudios.infernalexp.block.entity.VolatileGeyserBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class VolatileGeyserRenderer implements BlockEntityRenderer<VolatileGeyserBlockEntity> {

    private static final ResourceLocation GLOW_TEXTURE = new ResourceLocation(IEConstants.MOD_ID, "textures/block/volatile_geyser_glow.png");

    public VolatileGeyserRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(VolatileGeyserBlockEntity entity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState state = entity.getBlockState();
        if (!(state.getBlock() instanceof VolatileGeyserBlock)) return;

        Direction facing = state.getValue(VolatileGeyserBlock.FACING);

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);

        switch (facing) {
            case DOWN -> poseStack.mulPose(Axis.XP.rotationDegrees(180));
            case UP -> poseStack.mulPose(Axis.XP.rotationDegrees(0));
            case NORTH -> poseStack.mulPose(Axis.XP.rotationDegrees(-90));
            case SOUTH -> poseStack.mulPose(Axis.XP.rotationDegrees(90));
            case WEST -> poseStack.mulPose(Axis.ZP.rotationDegrees(90));
            case EAST -> poseStack.mulPose(Axis.ZP.rotationDegrees(-90));
        }

        poseStack.translate(-0.5, -0.5, -0.5);

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.eyes(GLOW_TEXTURE));

        float minX = 3.0f / 16.0f;
        float minY = 0.0f;
        float minZ = 3.0f / 16.0f;
        float maxX = 13.0f / 16.0f;
        float maxY = 12.0f / 16.0f;
        float maxZ = 13.0f / 16.0f;

        float inflate = 0.005f;

        renderBox(poseStack, consumer, minX - inflate, minY - inflate, minZ - inflate, maxX + inflate, maxY + inflate, maxZ + inflate, packedOverlay);

        poseStack.popPose();
    }

    private void renderBox(PoseStack stack, VertexConsumer builder, float x1, float y1, float z1, float x2, float y2, float z2, int packedOverlay) {
        Matrix4f m = stack.last().pose();
        float tS = 16.0f;

        float uStart1 = 0/tS, uEnd1 = 5/tS;
        float uStart2 = 5/tS, uEnd2 = 10/tS;
        float uStart3 = 10/tS, uEnd3 = 15/tS;

        float vStart1 = 0/tS, vEnd1 = 6/tS;
        float vStart2 = 6/tS, vEnd2 = 12/tS;
        float vStart3 = 0/tS, vEnd3 = 5/tS;
        float vStart4 = 5/tS, vEnd4 = 10/tS;

        // NORTH Face (z1)
        vertex(builder, m, x2, y2, z1, uStart1, vStart1, 0, 0, -1, packedOverlay);
        vertex(builder, m, x2, y1, z1, uStart1, vEnd1,   0, 0, -1, packedOverlay);
        vertex(builder, m, x1, y1, z1, uEnd1,   vEnd1,   0, 0, -1, packedOverlay);
        vertex(builder, m, x1, y2, z1, uEnd1,   vStart1, 0, 0, -1, packedOverlay);

        // SOUTH Face (z2)
        vertex(builder, m, x1, y2, z2, uStart1, vStart2, 0, 0, 1, packedOverlay);
        vertex(builder, m, x1, y1, z2, uStart1, vEnd2,   0, 0, 1, packedOverlay);
        vertex(builder, m, x2, y1, z2, uEnd1,   vEnd2,   0, 0, 1, packedOverlay);
        vertex(builder, m, x2, y2, z2, uEnd1,   vStart2, 0, 0, 1, packedOverlay);

        // EAST Face (x2)
        vertex(builder, m, x2, y2, z2, uStart2, vStart1, 1, 0, 0, packedOverlay);
        vertex(builder, m, x2, y1, z2, uStart2, vEnd1,   1, 0, 0, packedOverlay);
        vertex(builder, m, x2, y1, z1, uEnd2,   vEnd1,   1, 0, 0, packedOverlay);
        vertex(builder, m, x2, y2, z1, uEnd2,   vStart1, 1, 0, 0, packedOverlay);

        // WEST Face (x1)
        vertex(builder, m, x1, y2, z1, uStart2, vStart2, -1, 0, 0, packedOverlay);
        vertex(builder, m, x1, y1, z1, uStart2, vEnd2,   -1, 0, 0, packedOverlay);
        vertex(builder, m, x1, y1, z2, uEnd2,   vEnd2,   -1, 0, 0, packedOverlay);
        vertex(builder, m, x1, y2, z2, uEnd2,   vStart2, -1, 0, 0, packedOverlay);

        // UP (y2)
        addQuadHorizontal(m, builder, x1, x2, z1, z2, y2, uEnd3, uStart3, vEnd3, vStart3, false, packedOverlay);

        // DOWN (y1)
        addQuadHorizontal(m, builder, x1, x2, z1, z2, y1, uStart3, uEnd3, vStart4, vEnd4, true, packedOverlay);
    }

    private void vertex(VertexConsumer builder, Matrix4f m, float x, float y, float z, float u, float v, float nx, float ny, float nz, int packedOverlay) {
        builder.vertex(m, x, y, z)
                .color(255, 255, 255, 255)
                .uv(u, v)
                .overlayCoords(packedOverlay)
                .uv2(15728880)
                .normal(nx, ny, nz)
                .endVertex();
    }

    private void addQuadHorizontal(Matrix4f matrix, VertexConsumer builder, float minX, float maxX, float minZ, float maxZ, float y, float u1, float u2, float v1, float v2, boolean down, int packedOverlay) {
        float ny = down ? -1 : 1;
        if (down) {
            builder.vertex(matrix, minX, y, maxZ).color(255, 255, 255, 255).uv(u1, v2).overlayCoords(packedOverlay).uv2(15728880).normal(0, ny, 0).endVertex();
            builder.vertex(matrix, maxX, y, maxZ).color(255, 255, 255, 255).uv(u2, v2).overlayCoords(packedOverlay).uv2(15728880).normal(0, ny, 0).endVertex();
            builder.vertex(matrix, maxX, y, minZ).color(255, 255, 255, 255).uv(u2, v1).overlayCoords(packedOverlay).uv2(15728880).normal(0, ny, 0).endVertex();
            builder.vertex(matrix, minX, y, minZ).color(255, 255, 255, 255).uv(u1, v1).overlayCoords(packedOverlay).uv2(15728880).normal(0, ny, 0).endVertex();
        } else {
            builder.vertex(matrix, minX, y, minZ).color(255, 255, 255, 255).uv(u1, v1).overlayCoords(packedOverlay).uv2(15728880).normal(0, ny, 0).endVertex();
            builder.vertex(matrix, minX, y, maxZ).color(255, 255, 255, 255).uv(u1, v2).overlayCoords(packedOverlay).uv2(15728880).normal(0, ny, 0).endVertex();
            builder.vertex(matrix, maxX, y, maxZ).color(255, 255, 255, 255).uv(u2, v2).overlayCoords(packedOverlay).uv2(15728880).normal(0, ny, 0).endVertex();
            builder.vertex(matrix, maxX, y, minZ).color(255, 255, 255, 255).uv(u2, v1).overlayCoords(packedOverlay).uv2(15728880).normal(0, ny, 0).endVertex();
        }
    }
}