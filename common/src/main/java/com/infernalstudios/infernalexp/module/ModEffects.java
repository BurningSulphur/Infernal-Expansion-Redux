package com.infernalstudios.infernalexp.module;

import com.infernalstudios.infernalexp.IECommon;
import com.infernalstudios.infernalexp.effect.StatusEffect;
import com.infernalstudios.infernalexp.registration.holders.MobEffectDataHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public class ModEffects {
    /** Map of all Effect Resource Locations to their Suppliers. */
    private static final Map<ResourceLocation, MobEffectDataHolder<?>> EFFECT_REGISTRY = new HashMap<>();

    public static MobEffectDataHolder<?> register(String name, MobEffectDataHolder<?> effect) {
        ResourceLocation id = IECommon.makeID(name);
        EFFECT_REGISTRY.put(id, effect);
        return effect;
    }

    public static Map<ResourceLocation, MobEffectDataHolder<?>> getEffectRegistry() {
        return EFFECT_REGISTRY;
    }

    // Called in the mod initializer / constructor in order to make sure that items are registered
    public static void load() {}


    public static final MobEffectDataHolder<?> WARPED = register("warped", MobEffectDataHolder.of(() ->
                    new StatusEffect(MobEffectCategory.BENEFICIAL, 0x00ffba)))
            .withTranslation("Warped")
            .withPotion(() -> Items.WARPED_FUNGUS);
}
