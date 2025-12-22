package com.infernalstudios.infernalexp.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class VolatileGeyserBlock extends Block {
    public VolatileGeyserBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof LivingEntity) {
            double jumpHeight = 0.9D;

            if (level.getBlockState(pos.below()).is(Blocks.MAGMA_BLOCK)) {
                jumpHeight = 1.3D;
            }

            entity.setDeltaMovement(entity.getDeltaMovement().x, jumpHeight, entity.getDeltaMovement().z);
            entity.hurtMarked = true;
        }
        super.stepOn(level, pos, state, entity);
    }
}