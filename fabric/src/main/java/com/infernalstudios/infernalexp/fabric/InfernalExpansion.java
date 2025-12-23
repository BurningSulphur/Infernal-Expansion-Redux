package com.infernalstudios.infernalexp.fabric;

import com.infernalstudios.infernalexp.IECommon;
import com.infernalstudios.infernalexp.fabric.module.*;
import com.infernalstudios.infernalexp.fabric.platform.FabricPlatformHelper;
import com.infernalstudios.infernalexp.fabric.registration.FabricRegistrationFactory;
import com.infernalstudios.infernalexp.platform.Services;
import net.fabricmc.api.ModInitializer;

public class InfernalExpansion implements ModInitializer {

    @Override
    public void onInitialize() {
        Services.PLATFORM = new FabricPlatformHelper();
        Services.REGISTRATION_FACTORY = new FabricRegistrationFactory();

        IECommon.init();

        BlockModuleFabric.registerBlocks();
        ItemModuleFabric.registerItems();
        EntityTypeModuleFabric.registerEntities();
        EffectModuleFabric.registerEffects();
        FeatureModuleFabric.registerFeatures();
        CarverModuleFabric.registerCarvers();
        SpawnPlacementModuleFabric.registerSpawnPlacements();
    }
}