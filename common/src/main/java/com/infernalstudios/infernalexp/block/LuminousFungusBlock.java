package com.infernalstudios.infernalexp.block;

import com.infernalstudios.infernalexp.IEConstants;
import com.infernalstudios.infernalexp.block.parent.NetherPlantBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LuminousFungusBlock extends NetherPlantBlock {
    public static final BooleanProperty LIT = BooleanProperty.create("is_lit");
    public static final BooleanProperty FLOOR = BooleanProperty.create("is_floor");

    public static final VoxelShape BOX = Block.box(4, 0, 4, 12, 8, 12);
    public static final VoxelShape BOX_REVERSED = Block.box(4, 6, 4, 12, 16, 12);

    public LuminousFungusBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(LIT, false).setValue(FLOOR, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT).add(FLOOR);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(FLOOR) ? BOX : BOX_REVERSED;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return state.getValue(FLOOR) ? level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP)
                : level.getBlockState(pos.above()).isFaceSturdy(level, pos.above(), Direction.DOWN);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelReader world = context.getLevel();
        BlockPos pos = context.getClickedPos();

        boolean up = world.getBlockState(pos.above()).isFaceSturdy(world, pos, Direction.DOWN);
        boolean down = world.getBlockState(pos.below()).isFaceSturdy(world, pos, Direction.UP);

        IEConstants.LOG.info(down + " " + up);

        if (down && up) {
            if (context.getNearestLookingVerticalDirection() == Direction.UP)
                return this.defaultBlockState().setValue(FLOOR, false);
        }
        else if (up)
            return this.defaultBlockState().setValue(FLOOR, false);
        return this.defaultBlockState();
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        super.entityInside(state, world, pos, entity);
        if (!state.getValue(LIT) && entity instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0));
            world.setBlock(pos, state.setValue(LIT, true), 3);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        super.randomTick(state, world, pos, random);
        if (state.getValue(LIT)) world.setBlock(pos, state.setValue(LIT, false), 3);
    }
}
