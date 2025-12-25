package com.infernalstudios.infernalexp.mixin.accessor;

import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SurfaceRules.Context.class)
public interface SurfaceRulesContextAccessor {
    @Accessor("randomState")
    RandomState infernalexp$getRandomState();

    @Accessor("blockX")
    int infernalexp$getBlockX();

    @Accessor("blockY")
    int infernalexp$getBlockY();

    @Accessor("blockZ")
    int infernalexp$getBlockZ();
}