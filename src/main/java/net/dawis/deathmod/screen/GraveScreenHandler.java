package net.dawis.deathmod.screen;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class GraveScreenHandler extends ScreenHandler {
    private final Inventory inventory;

    public GraveScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
        this(syncId, playerInventory, playerInventory.player.getWorld().getBlockEntity(pos));
    }

    protected GraveScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity) {
        super(ModScreenHandlers.GRAVE_SCREEN_HANDLER, syncId);
        this.inventory = ((Inventory) blockEntity);

        addCustomInventorySlots();
        addPlayerSlots(playerInventory, 8, 140);

    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    private void addCustomInventorySlots() {
        int indexMain = 0;
        int indexEquipment = 0;
        for (int m = 0; m < 6; m++) {
            if (m >= 2) {
                for (int l = 0; l < 9; l++) {
                    this.addSlot(new Slot(this.inventory, indexMain, 8 + l * 18, 18 + m * 18));
                    indexMain++;
                }
            } else if (m == 0) {
                for (int l = 1; l < 8; l++) {
                    if (l != 5 && l != 6) {
                        this.addSlot(new Slot(this.inventory, 37 + indexEquipment, 8 + l * 18, 18));
                        indexEquipment++;

                        if (indexEquipment + 37 > 40) {
                            indexEquipment = -1;
                        }
                    }
                }
            }
        }
    }
}
