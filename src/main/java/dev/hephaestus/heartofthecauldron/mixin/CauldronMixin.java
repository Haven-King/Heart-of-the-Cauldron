package dev.hephaestus.heartofthecauldron.mixin;

import dev.hephaestus.heartofthecauldron.Server;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(CauldronBlock.class)
public class CauldronMixin {
	@Inject(at = @At("HEAD"), method = "activate")
	private void activateHead(BlockState blockState_1, World world_1, BlockPos blockPos_1, PlayerEntity playerEntity_1, Hand hand_1, BlockHitResult blockHitResult_1, CallbackInfoReturnable info) {
		ItemStack itemStack_1 = playerEntity_1.getStackInHand(hand_1);
		if (itemStack_1.getItem() == Items.HEART_OF_THE_SEA) {
			if (!world_1.isClient) {
				itemStack_1.decrement(1);
				playerEntity_1.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0f, 1.0f);
				playerEntity_1.sendMessage(new LiteralText("This cauldron has been blessed with the heart of the sea").setStyle(new Style().setColor(Formatting.AQUA).setItalic(true)));

				world_1.setBlockState(blockPos_1, Server.INFINITE_CAULDRON.getDefaultState());
			} else {
				for(int int_1 = -2; int_1 <= 2; ++int_1) {
					for(int int_2 = -2; int_2 <= 2; ++int_2) {
						if (int_1 > -2 && int_1 < 2 && int_2 == -1) {
							int_2 = 2;
						}

						Random random_1 = world_1.getRandom();
						for(int int_3 = 0; int_3 <= 1; ++int_3) {
							world_1.addParticle(ParticleTypes.ENCHANT, (double)blockPos_1.getX() + 0.5D, (double)blockPos_1.getY() + 2.0D, (double)blockPos_1.getZ() + 0.5D, (double)((float)int_1 + random_1.nextFloat()) - 0.5D, (double)((float)int_3 - random_1.nextFloat() - 1.0F), (double)((float)int_2 + random_1.nextFloat()) - 0.5D);
						}
					}
				}
			}
		}
	}
}
