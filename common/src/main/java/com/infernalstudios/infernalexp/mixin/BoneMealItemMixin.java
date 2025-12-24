package com.infernalstudios.infernalexp.mixin;

import com.infernalstudios.infernalexp.block.ShroomlightTearBlock;
import com.infernalstudios.infernalexp.module.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    public void useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (world.getBlockState(pos).is(Blocks.SHROOMLIGHT)) {
            boolean isWarped = world.getBiome(pos).is(Biomes.WARPED_FOREST);
            BlockPos targetPos = isWarped ? pos.above() : pos.below();

            if (world.getBlockState(targetPos).isAir()) {
                if (!world.isClientSide) {
                    context.getItemInHand().shrink(1);

                    BlockState tear = ModBlocks.SHROOMLIGHT_TEAR.get().defaultBlockState();
                    if (isWarped)
                        world.setBlock(targetPos, tear.setValue(ShroomlightTearBlock.UP, true), Block.UPDATE_ALL);
                    else
                        world.setBlock(targetPos, tear, Block.UPDATE_ALL);

                    if (world instanceof ServerLevel serverLevel) {

                        double spawnX = targetPos.getX() + 0.5D;
                        double spawnZ = targetPos.getZ() + 0.5D;
                        double spawnY = targetPos.getY() + 0.1D;

                        serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                                spawnX, spawnY, spawnZ,
                                15,
                                0.25D, 0.25D, 0.25D,
                                0.05D);
                    }
                }
                cir.setReturnValue(InteractionResult.sidedSuccess(world.isClientSide));
            }
        }
    }
}