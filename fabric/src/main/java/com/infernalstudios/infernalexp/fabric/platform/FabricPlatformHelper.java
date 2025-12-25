package com.infernalstudios.infernalexp.fabric.platform;

import com.infernalstudios.infernalexp.platform.services.IPlatformHelper;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import com.infernalstudios.infernalexp.items.InfernalGeoBlockItem;
import com.infernalstudios.infernalexp.client.item.render.InfernalGeoRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntityFactory<T> factory, Block... blocks) {
        return FabricBlockEntityTypeBuilder.create(factory::create, blocks).build();
    }

    @Override
    public InfernalGeoBlockItem createGeoBlockItem(Block block, Item.Properties properties, ResourceLocation model, ResourceLocation texture) {
        return new InfernalGeoBlockItem(block, properties, model, texture) {
            private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

            @Override
            public void createRenderer(Consumer<Object> consumer) {
                consumer.accept(new RenderProvider() {
                    private InfernalGeoRenderer renderer;

                    @Override
                    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                        if (this.renderer == null)
                            this.renderer = new InfernalGeoRenderer();
                        return this.renderer;
                    }
                });
            }

            @Override
            public Supplier<Object> getRenderProvider() {
                return this.renderProvider;
            }
        };
    }
}
