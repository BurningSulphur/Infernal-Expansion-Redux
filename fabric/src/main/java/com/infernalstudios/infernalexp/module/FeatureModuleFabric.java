package com.infernalstudios.infernalexp.module;

import com.infernalstudios.infernalexp.registration.FuelRegistry;
import com.infernalstudios.infernalexp.registration.holders.ItemDataHolder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;

import java.util.Map;

public class FeatureModuleFabric {
    public static void registerFeatures() {
        for (Map.Entry<ResourceKey<Feature<?>>, Feature<?>> entry : ModFeatures.getFeatureRegistry().entrySet()) {
            // Register feature
            Registry.register(BuiltInRegistries.FEATURE, entry.getKey(), entry.getValue());
        }
    }
}
