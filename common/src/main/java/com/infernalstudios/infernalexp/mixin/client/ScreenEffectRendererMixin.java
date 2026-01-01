package com.infernalstudios.infernalexp.mixin.client;

import com.infernalstudios.infernalexp.api.FireTypeAccess;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {
    @Redirect(method = "renderFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"))
    private static TextureAtlasSprite renderCustomFireOverlay(Material instance, Minecraft minecraft, PoseStack posestack) {
        return ((FireTypeAccess) minecraft.player).infernalexp$getFireType().getSprite1().sprite();
    }
}