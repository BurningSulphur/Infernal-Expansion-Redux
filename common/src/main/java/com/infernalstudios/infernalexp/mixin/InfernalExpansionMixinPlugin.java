package com.infernalstudios.infernalexp.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

public class InfernalExpansionMixinPlugin implements IMixinConfigPlugin {

    private boolean isModLoaded(String modId) {
        try {
            try {
                Class<?> fabricLoaderClass = Class.forName("net.fabricmc.loader.api.FabricLoader");
                Object loaderInstance = fabricLoaderClass.getMethod("getInstance").invoke(null);
                Method isModLoadedMethod = fabricLoaderClass.getMethod("isModLoaded", String.class);
                return (boolean) isModLoadedMethod.invoke(loaderInstance, modId);
            } catch (ClassNotFoundException e) {
                // Not Fabric, fall through to Forge/NeoForge
            }

            Class<?> fmlLoaderClass = Class.forName("net.minecraftforge.fml.loading.FMLLoader");
            Object loadingModList = fmlLoaderClass.getMethod("getLoadingModList").invoke(null);
            Method getModFileById = loadingModList.getClass().getMethod("getModFileById", String.class);
            return getModFileById.invoke(loadingModList, modId) != null;

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        // Fix for Firorize crash
        if (mixinClassName.equals("com.infernalstudios.infernalexp.mixin.client.EntityRendererDispatcherMixin")) {
            return !isModLoaded("firorize");
        }
        return true;
    }

    @Override
    public void onLoad(String mixinPackage) {}
    @Override
    public String getRefMapperConfig() { return null; }
    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}
    @Override
    public List<String> getMixins() { return null; }
    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}