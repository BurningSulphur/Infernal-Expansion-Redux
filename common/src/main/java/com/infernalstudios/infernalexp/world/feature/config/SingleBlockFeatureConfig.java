package com.infernalstudios.infernalexp.world.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record SingleBlockFeatureConfig(BlockStateProvider block) implements FeatureConfiguration {
    public static Codec<SingleBlockFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    BlockStateProvider.CODEC.fieldOf("stem").forGetter(SingleBlockFeatureConfig::block)
            ).apply(instance, SingleBlockFeatureConfig::new));
}
