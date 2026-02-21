package net.dawis.deathmod.screen;

import net.dawis.deathmod.DeathMod;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ModScreenHandlers {
    public static final ScreenHandlerType<GraveScreenHandler> GRAVE_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(DeathMod.MOD_ID, "grave_screen_handler"),
                    new ExtendedScreenHandlerType<>(GraveScreenHandler::new, BlockPos.PACKET_CODEC));

    public static void init() {
        DeathMod.LOGGER.info("Initializing Modded Screen Handlers");
    }
}
