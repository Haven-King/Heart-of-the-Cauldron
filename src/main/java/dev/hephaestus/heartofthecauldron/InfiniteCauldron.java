package dev.hephaestus.heartofthecauldron;

import net.minecraft.block.*;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.container.Container;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BannerItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BooleanBiFunction;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class InfiniteCauldron extends Block {
    public static final IntProperty LEVEL;
    private static final VoxelShape RAY_TRACE_SHAPE;
    protected static final VoxelShape OUTLINE_SHAPE;

    public InfiniteCauldron(Block.Settings block$Settings_1) {
        super(block$Settings_1);
        this.setDefaultState((BlockState)((BlockState)this.stateFactory.getDefaultState()).with(LEVEL, 3));
    }

    public VoxelShape getOutlineShape(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, EntityContext entityContext_1) {
        return OUTLINE_SHAPE;
    }

    public boolean isOpaque(BlockState blockState_1) {
        return false;
    }

    public VoxelShape getRayTraceShape(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1) {
        return RAY_TRACE_SHAPE;
    }

    public void onEntityCollision(BlockState blockState_1, World world_1, BlockPos blockPos_1, Entity entity_1) {
        int int_1 = (Integer)blockState_1.get(LEVEL);
        float float_1 = (float)blockPos_1.getY() + (6.0F + (float)(3 * int_1)) / 16.0F;
        if (!world_1.isClient && entity_1.isOnFire() && int_1 > 0 && entity_1.getBoundingBox().minY <= (double)float_1) {
            entity_1.extinguish();
        }

    }

    public boolean activate(BlockState blockState_1, World world_1, BlockPos blockPos_1, PlayerEntity playerEntity_1, Hand hand_1, BlockHitResult blockHitResult_1) {
        ItemStack itemStack_1 = playerEntity_1.getStackInHand(hand_1);
        if (itemStack_1.isEmpty()) {
            return true;
        } else {
            int int_1 = (Integer)blockState_1.get(LEVEL);
            Item item_1 = itemStack_1.getItem();
            if (item_1 == Items.BUCKET) {
                if (int_1 == 3 && !world_1.isClient) {
                    if (!playerEntity_1.abilities.creativeMode) {
                        itemStack_1.decrement(1);
                        if (itemStack_1.isEmpty()) {
                            playerEntity_1.setStackInHand(hand_1, new ItemStack(Items.WATER_BUCKET));
                        } else if (!playerEntity_1.inventory.insertStack(new ItemStack(Items.WATER_BUCKET))) {
                            playerEntity_1.dropItem(new ItemStack(Items.WATER_BUCKET), false);
                        }
                    }

                    playerEntity_1.incrementStat(Stats.USE_CAULDRON);
                    world_1.playSound((PlayerEntity)null, blockPos_1, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return true;
            } else {
                ItemStack itemStack_4;
                if (item_1 == Items.GLASS_BOTTLE) {
                    if (int_1 > 0 && !world_1.isClient) {
                        if (!playerEntity_1.abilities.creativeMode) {
                            itemStack_4 = PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER);
                            playerEntity_1.incrementStat(Stats.USE_CAULDRON);
                            itemStack_1.decrement(1);
                            if (itemStack_1.isEmpty()) {
                                playerEntity_1.setStackInHand(hand_1, itemStack_4);
                            } else if (!playerEntity_1.inventory.insertStack(itemStack_4)) {
                                playerEntity_1.dropItem(itemStack_4, false);
                            } else if (playerEntity_1 instanceof ServerPlayerEntity) {
                                ((ServerPlayerEntity)playerEntity_1).openContainer((Container)playerEntity_1.playerContainer);
                            }
                        }

                        world_1.playSound((PlayerEntity)null, blockPos_1, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    }

                    return true;
                } else {
                    if (int_1 > 0 && item_1 instanceof DyeableItem) {
                        DyeableItem dyeableItem_1 = (DyeableItem)item_1;
                        if (dyeableItem_1.hasColor(itemStack_1) && !world_1.isClient) {
                            dyeableItem_1.removeColor(itemStack_1);
                            playerEntity_1.incrementStat(Stats.CLEAN_ARMOR);
                            return true;
                        }
                    }

                    if (int_1 > 0 && item_1 instanceof BannerItem) {
                        if (BannerBlockEntity.getPatternCount(itemStack_1) > 0 && !world_1.isClient) {
                            itemStack_4 = itemStack_1.copy();
                            itemStack_4.setCount(1);
                            BannerBlockEntity.loadFromItemStack(itemStack_4);
                            playerEntity_1.incrementStat(Stats.CLEAN_BANNER);
                            if (!playerEntity_1.abilities.creativeMode) {
                                itemStack_1.decrement(1);
                            }

                            if (itemStack_1.isEmpty()) {
                                playerEntity_1.setStackInHand(hand_1, itemStack_4);
                            } else if (!playerEntity_1.inventory.insertStack(itemStack_4)) {
                                playerEntity_1.dropItem(itemStack_4, false);
                            } else if (playerEntity_1 instanceof ServerPlayerEntity) {
                                ((ServerPlayerEntity)playerEntity_1).openContainer((Container)playerEntity_1.playerContainer);
                            }
                        }

                        return true;
                    } else if (int_1 > 0 && item_1 instanceof BlockItem) {
                        Block block_1 = ((BlockItem)item_1).getBlock();
                        if (block_1 instanceof ShulkerBoxBlock && !world_1.isClient()) {
                            ItemStack itemStack_5 = new ItemStack(Blocks.SHULKER_BOX, 1);
                            if (itemStack_1.hasTag()) {
                                itemStack_5.setTag(itemStack_1.getTag().method_10553());
                            }

                            playerEntity_1.setStackInHand(hand_1, itemStack_5);
                            playerEntity_1.incrementStat(Stats.CLEAN_SHULKER_BOX);
                        }

                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
    }

    public void onRainTick(World world_1, BlockPos blockPos_1) {
        if (world_1.random.nextInt(20) == 1) {
            float float_1 = world_1.getBiome(blockPos_1).getTemperature(blockPos_1);
            if (float_1 >= 0.15F) {
                BlockState blockState_1 = world_1.getBlockState(blockPos_1);
                if ((Integer)blockState_1.get(LEVEL) < 3) {
                    world_1.setBlockState(blockPos_1, (BlockState)blockState_1.cycle(LEVEL), 2);
                }

            }
        }
    }

    public boolean hasComparatorOutput(BlockState blockState_1) {
        return true;
    }

    public int getComparatorOutput(BlockState blockState_1, World world_1, BlockPos blockPos_1) {
        return (Integer)blockState_1.get(LEVEL);
    }

    protected void appendProperties(StateFactory.Builder<Block, BlockState> stateFactory$Builder_1) {
        stateFactory$Builder_1.add(LEVEL);
    }

    public boolean canPlaceAtSide(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, BlockPlacementEnvironment blockPlacementEnvironment_1) {
        return false;
    }


    static {
        LEVEL = Properties.LEVEL_3;
        RAY_TRACE_SHAPE = createCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
        OUTLINE_SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.union(createCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), createCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), RAY_TRACE_SHAPE), BooleanBiFunction.ONLY_FIRST);
    }
}