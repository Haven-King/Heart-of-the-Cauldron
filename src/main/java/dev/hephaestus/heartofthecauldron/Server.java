package dev.hephaestus.heartofthecauldron;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Server implements ModInitializer {
    public static final Block INFINITE_CAULDRON = new InfiniteCauldron(FabricBlockSettings.of(Material.METAL, MaterialColor.STONE).strength(2.0F, 2.0F).build());

    @Override
    public void onInitialize()
    {
        Registry.register(Registry.BLOCK, new Identifier("heartofthecauldron", "infinite_cauldron"), INFINITE_CAULDRON);
    }
}
