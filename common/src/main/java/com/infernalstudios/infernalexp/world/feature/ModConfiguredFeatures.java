package com.infernalstudios.infernalexp.world.feature;

import com.infernalstudios.infernalexp.IECommon;
import com.infernalstudios.infernalexp.block.DullthornsBlock;
import com.infernalstudios.infernalexp.module.ModBlocks;
import com.infernalstudios.infernalexp.world.feature.config.DullthornsFeatureConfig;
import com.infernalstudios.infernalexp.world.feature.custom.DullthornsFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class ModConfiguredFeatures {
    public static ResourceKey<ConfiguredFeature<?, ?>> create(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, IECommon.id(name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
        context.register(key, new ConfiguredFeature<>(feature, config));
    }

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        register(context, DULLTHORNS, DullthornsFeature.INSTANCE,
                new DullthornsFeatureConfig(UniformInt.of(4, 7),
                        BlockStateProvider.simple(ModBlocks.DULLTHORNS.get()),
                        BlockStateProvider.simple(ModBlocks.DULLTHORNS.get().defaultBlockState().setValue(DullthornsBlock.TIP, true))));
    }



    public static final ResourceKey<ConfiguredFeature<?, ?>> DULLTHORNS = create("dullthorns");
}
