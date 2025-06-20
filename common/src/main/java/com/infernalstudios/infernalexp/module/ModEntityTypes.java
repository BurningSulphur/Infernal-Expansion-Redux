package com.infernalstudios.infernalexp.module;

import com.infernalstudios.infernalexp.IECommon;
import com.infernalstudios.infernalexp.registration.holders.EntityTypeDataHolder;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ModEntityTypes {
    /** Map of all EntityType Resource Locations to their EntityTypeDataHolders. */
    private static final Map<ResourceLocation, EntityTypeDataHolder> ENTITY_TYPE_REGISTRY = new HashMap<>();

/*
    public static final EntityTypeDataHolder<KitsuneEntity> KITSUNE = register("kitsune", EntityTypeDataHolder.of(() ->
            EntityTypeDataHolder.Builder.of(KitsuneEntity::new, MobCategory.CREATURE)
                    .sized(1.0f, 1.75f)
                    .build()
            )
            .attributes(KitsuneEntity::createKitsuneAttributes));
*/

    public static EntityTypeDataHolder register(String name, EntityTypeDataHolder entityTypeDataHolder) {
        ResourceLocation id = IECommon.makeID(name);
        ENTITY_TYPE_REGISTRY.put(id, entityTypeDataHolder);
        return entityTypeDataHolder;
    }

    public static Map<ResourceLocation, EntityTypeDataHolder> getEntityTypeRegistry() {
        return ENTITY_TYPE_REGISTRY;
    }

    // Called in the mod initializer / constructor in order to make sure that items are registered
    public static void load() {}
}
