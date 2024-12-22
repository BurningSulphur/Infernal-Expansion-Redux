package com.infernalstudios.infernalexp.mixin;

import com.infernalstudios.infernalexp.module.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.GlowstoneFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GlowstoneFeature.class)
public class GlowstoneFeatureMixin {
    @Redirect(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 0))
    public boolean allowUnderDullstone(BlockState instance, Block block) {
        if (instance.is(ModBlocks.DULLSTONE.get()) && block == Blocks.NETHERRACK)
            return true;
        return instance.is(block);
    }
}
