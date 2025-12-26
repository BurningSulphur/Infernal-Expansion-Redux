package com.infernalstudios.infernalexp.fabric.compat;

import com.infernalstudios.infernalexp.config.ClothConfigConstructor;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.loader.api.FabricLoader;

public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (FabricLoader.getInstance().isModLoaded("cloth-config")) {
            return parent -> AutoConfig.getConfigScreen(ClothConfigConstructor.class, parent).get();
        }
        return null;
    }
}