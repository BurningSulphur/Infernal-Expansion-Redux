package com.infernalstudios.infernalexp.mixin.client;

import com.infernalstudios.infernalexp.api.FireTypeAccess;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderDispatcher.class)
public class EntityRendererDispatcherMixin {
    @Redirect(method = "renderFlame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", ordinal = 0))
    private TextureAtlasSprite renderCustomFires0(Material instance, PoseStack posestack, MultiBufferSource source, Entity entity) {
        return ((FireTypeAccess) entity).infernalexp$getFireType().getSprite0().sprite();
    }

    @Redirect(method = "renderFlame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", ordinal = 1))
    private TextureAtlasSprite renderCustomFires1(Material instance, PoseStack posestack, MultiBufferSource source, Entity entity) {
        return ((FireTypeAccess) entity).infernalexp$getFireType().getSprite1().sprite();
    }
}