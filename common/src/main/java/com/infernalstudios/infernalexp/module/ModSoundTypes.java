package com.infernalstudios.infernalexp.module;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;

public class ModSoundTypes {

    public static final SoundType DULLSTONE = new SoundType(
            1.0F,
            1.0F,
            ModSounds.BLOCK_DULLSTONE_BREAK.get(),
            ModSounds.BLOCK_DULLSTONE_STEP.get(),
            ModSounds.BLOCK_DULLSTONE_PLACE.get(),
            ModSounds.BLOCK_DULLSTONE_HIT.get(),
            ModSounds.BLOCK_DULLSTONE_FALL.get()
    );

    public static final SoundType DIMSTONE = new SoundType(
            1.0F,
            1.0F,
            SoundEvents.GLASS_BREAK,
            ModSounds.BLOCK_DULLSTONE_STEP.get(),
            SoundEvents.GLASS_PLACE,
            SoundEvents.GLASS_HIT,
            SoundEvents.GLASS_FALL
    );
}