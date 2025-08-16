package net.dawisx13.deathmod.block.entity.custom;

import net.dawisx13.deathmod.block.entity.ImplementedInventory;
import net.dawisx13.deathmod.block.entity.ModBlockEntities;
import net.dawisx13.deathmod.screen.GraveScreenHandler;
import net.dawisx13.deathmod.util.TickableBlockEntity;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class GraveBlockEntity extends BlockEntity implements ImplementedInventory, ExtendedScreenHandlerFactory<BlockPos>, TickableBlockEntity {

    // 41 = 27 main slots + 9 hotbar slots + 1 secondary arm slot + 4 armor slots
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(41, ItemStack.EMPTY);
    private Text playerName;
    private int ticks = 6000;

    public GraveBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GRAVE_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        Inventories.writeData(view, inventory);
        if (this.playerName != null) {
            view.putString("playerName", this.playerName.getLiteralString());
        }
        view.putInt("ticks", this.ticks);
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        Inventories.readData(view, inventory);
        this.playerName = Text.literal(view.getString("playerName", "Player"));
        this.ticks = view.getInt("ticks", 6000);
    }

    @Override
    public void tick() {
        if (world != null && !world.isClient()) {
            this.ticks--;

            if (this.ticks <= 0) {
                world.breakBlock(pos, false);
            }
        }
    }

    @Override
    public void onBlockReplaced(BlockPos pos, BlockState oldState) {
        if (this.ticks > 0) {
            ItemScatterer.spawn(world, pos, (this));
            super.onBlockReplaced(pos, oldState);
        }
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        return createNbt(registries);
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new GraveScreenHandler(syncId, playerInventory, this.pos);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        return this.pos;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    public void setInventory(DefaultedList<ItemStack> inv) {
        this.inventory = inv;
        markDirty();
    }

    @Override
    public Text getDisplayName() {
        return Text.literal(this.playerName.getLiteralString() + "'s Grave");
    }

    public void setPlayerName(Text playerName) {
        this.playerName = playerName;
    }
}
