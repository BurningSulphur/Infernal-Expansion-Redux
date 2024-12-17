package com.infernalstudios.infernalexp.world.feature.custom;

import com.infernalstudios.infernalexp.world.feature.config.DullthornsFeatureConfig;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.Random;

public class DullthornsFeature extends Feature<DullthornsFeatureConfig> {
    public static final Feature<DullthornsFeatureConfig> INSTANCE = new DullthornsFeature(DullthornsFeatureConfig.CODEC);

    public DullthornsFeature(Codec<DullthornsFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<DullthornsFeatureConfig> context) {
        DullthornsFeatureConfig config = context.config();
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();

        for (int i = 0; i < config.size().sample(random); i++) {
            if (level.isEmptyBlock(pos.above(i))) {
                this.setBlock(level, pos, config.stem().getState(random, pos));
            }
            else return true;
            pos = pos.above();
        }
        this.setBlock(level, pos, config.tip().getState(random, pos));

        return true;
    }
}
