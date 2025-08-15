package net.dawisx13.deathmod;

import net.dawisx13.deathmod.screen.GraveScreen;
import net.dawisx13.deathmod.screen.ModScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class DeathModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandlers.GRAVE_SCREEN_HANDLER, GraveScreen::new);
    }
}
