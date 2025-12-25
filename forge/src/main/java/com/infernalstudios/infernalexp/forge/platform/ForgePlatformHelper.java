package com.infernalstudios.infernalexp.forge.platform;

import com.infernalstudios.infernalexp.client.item.render.InfernalGeoRenderer;
import com.infernalstudios.infernalexp.items.InfernalGeoBlockItem;
import com.infernalstudios.infernalexp.platform.services.IPlatformHelper;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntityFactory<T> factory, Block... blocks) {
        return BlockEntityType.Builder.of(factory::create, blocks).build(null);
    }

    @Override
    public InfernalGeoBlockItem createGeoBlockItem(Block block, Item.Properties properties, ResourceLocation model, ResourceLocation texture) {
        return new InfernalGeoBlockItem(block, properties, model, texture) {
            @Override
            public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
                consumer.accept(new IClientItemExtensions() {
                    private InfernalGeoRenderer renderer;

                    @Override
                    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                        if (this.renderer == null)
                            this.renderer = new InfernalGeoRenderer();
                        return this.renderer;
                    }
                });
            }
        };
    }
}