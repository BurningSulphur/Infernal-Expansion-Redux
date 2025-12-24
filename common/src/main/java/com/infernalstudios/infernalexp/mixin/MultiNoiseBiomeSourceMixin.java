package com.infernalstudios.infernalexp.mixin;

import com.infernalstudios.infernalexp.module.ModBiomes;
import com.infernalstudios.infernalexp.platform.Services;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mixin(MultiNoiseBiomeSource.class)
public class MultiNoiseBiomeSourceMixin {

    @ModifyVariable(
            method = "createFromList(Lnet/minecraft/world/level/biome/Climate$ParameterList;)Lnet/minecraft/world/level/biome/MultiNoiseBiomeSource;",
            at = @At("HEAD"),
            argsOnly = true
    )
    private static Climate.ParameterList<Holder<Biome>> infernalexp$injectBiomes(Climate.ParameterList<Holder<Biome>> parameters) {
        if (Services.PLATFORM.isModLoaded("terrablender")) {
            return parameters;
        }

        boolean isNether = parameters.values().stream().anyMatch(pair ->
                pair.getSecond().is(Biomes.NETHER_WASTES)
        );

        if (isNether) {
            @SuppressWarnings("unchecked")
            Registry<Biome> biomeRegistry = (Registry<Biome>) BuiltInRegistries.REGISTRY.get(Registries.BIOME.location());

            if (biomeRegistry == null) {
                return parameters;
            }

            List<Pair<Climate.ParameterPoint, Holder<Biome>>> newEntries = new ArrayList<>(parameters.values());

            for (Map.Entry<ResourceKey<Biome>, Climate.ParameterPoint> entry : ModBiomes.getBiomeRegistry().entrySet()) {
                Optional<Holder.Reference<Biome>> biomeHolderOpt = biomeRegistry.getHolder(entry.getKey());

                biomeHolderOpt.ifPresent(biomeHolder -> newEntries.add(new Pair<>(entry.getValue(), biomeHolder)));
            }

            return new Climate.ParameterList<>(newEntries);
        }

        return parameters;
    }
}