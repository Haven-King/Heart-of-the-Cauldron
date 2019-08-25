package dev.hephaestus.heartofthecauldron;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.block.BlockColorProvider;

public class Client implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ColorProviderRegistry.BLOCK.register((block, pos, world, layer) -> {
            BlockColorProvider provider = ColorProviderRegistry.BLOCK.get(Blocks.WATER);
            return provider == null ? -1 : provider.getColor(block, pos, world, layer);
        }, Server.INFINITE_CAULDRON);
    }
}
