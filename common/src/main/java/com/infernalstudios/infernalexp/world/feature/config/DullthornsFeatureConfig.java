package com.infernalstudios.infernalexp.world.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record DullthornsFeatureConfig(IntProvider size, BlockStateProvider stem, BlockStateProvider tip) implements FeatureConfiguration {
    public static Codec<DullthornsFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    IntProvider.POSITIVE_CODEC.fieldOf("size").forGetter(DullthornsFeatureConfig::size),
                    BlockStateProvider.CODEC.fieldOf("stem").forGetter(DullthornsFeatureConfig::stem),
                    BlockStateProvider.CODEC.fieldOf("tip").forGetter(DullthornsFeatureConfig::tip)
            ).apply(instance, DullthornsFeatureConfig::new));
}
