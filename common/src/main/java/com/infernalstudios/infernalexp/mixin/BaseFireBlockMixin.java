package com.infernalstudios.infernalexp.mixin;

import com.infernalstudios.infernalexp.api.FireType;
import com.infernalstudios.infernalexp.api.FireTypeAccess;
import com.infernalstudios.infernalexp.block.GlowlightFireBlock;
import com.infernalstudios.infernalexp.module.ModBlocks;
import com.infernalstudios.infernalexp.module.ModFireTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseFireBlock.class)
public class BaseFireBlockMixin {
    @Inject(method = "getState", at = @At("HEAD"), cancellable = true)
    private static void addGlowFire(BlockGetter world, BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
        if (GlowlightFireBlock.canSurviveOnBlock(world.getBlockState(pos.below())))
            cir.setReturnValue(ModBlocks.GLOWLIGHT_FIRE.get().defaultBlockState());
    }

    @Inject(method = "entityInside", at = @At("TAIL"))
    public void setFireType(BlockState state, Level world, BlockPos pos, Entity entity, CallbackInfo ci) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        ((FireTypeAccess) entity).infernalexp$setFireType(FireType.getOrDefault(id, ModFireTypes.FIRE));
    }
}
