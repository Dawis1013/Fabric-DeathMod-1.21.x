package net.dawis.deathmod.mixin;

import net.dawis.deathmod.block.ModBlocks;
import net.dawis.deathmod.block.entity.custom.GraveBlockEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class DeathMixin {
    @Inject(method = "onDeath", at = @At("HEAD"))
    private void graveInit(CallbackInfo info) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        if (!player.getWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY) && !player.getWorld().isClient()) {
            ServerWorld world = player.getWorld();
            BlockPos deathPos = player.getBlockPos();
            BlockPos gravePos = findSuitableGravePos(world, deathPos);

            // Sets the Grave on the death position (or near it) of the player
            world.setBlockState(gravePos, ModBlocks.GRAVE.getDefaultState());

            // Transfer player's inventory to the grave
            if (world.getBlockEntity(gravePos) instanceof GraveBlockEntity graveBlockEntity) {
                graveBlockEntity.setPlayerName(player.getName());

                DefaultedList<ItemStack> graveInv = DefaultedList.of();
                graveInv.addAll(player.getInventory().getMainStacks());
                graveInv.add(player.getInventory().getStack(PlayerInventory.OFF_HAND_SLOT));
                graveInv.add(player.getInventory().getStack(EquipmentSlot.HEAD.getOffsetEntitySlotId(36)));
                graveInv.add(player.getInventory().getStack(EquipmentSlot.CHEST.getOffsetEntitySlotId(36)));
                graveInv.add(player.getInventory().getStack(EquipmentSlot.LEGS.getOffsetEntitySlotId(36)));
                graveInv.add(player.getInventory().getStack(EquipmentSlot.FEET.getOffsetEntitySlotId(36)));

                graveBlockEntity.setInventory(graveInv);
            }

            // Prevents item scattering
            player.getInventory().clear();
        }
    }

    @Unique
    private BlockPos findSuitableGravePos(ServerWorld world, BlockPos pos) {
        while (!world.getBlockState(pos.down()).isSolidBlock(world, pos.down()) && pos.getY() > world.getBottomY()) {
            pos = pos.down();
        }

        return pos;
    }
}
