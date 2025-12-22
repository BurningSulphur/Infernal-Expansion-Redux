package com.infernalstudios.infernalexp.registration;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FlammabilityRegistry {
    private static final Map<Block, FlammabilityRegistry> REGISTRIES = new ConcurrentHashMap<>();

    private final Map<Block, Entry> blockToFlammability = new HashMap<>();

    private FlammabilityRegistry(Block key) {
    }

    public Entry get(Block block) {
        return this.blockToFlammability.get(block);
    }

    public void register(Block block, Entry entry) {
        this.blockToFlammability.put(block, entry);
    }

    public static FlammabilityRegistry getRegistry(Block fireBlock) {
        if (!(fireBlock instanceof FireBlock)) {
            throw new RuntimeException("Not a fire block: " + fireBlock);
        }

        return REGISTRIES.computeIfAbsent(fireBlock, FlammabilityRegistry::new);
    }

    public record Entry(int igniteChance, int spreadChance) {
    }
}