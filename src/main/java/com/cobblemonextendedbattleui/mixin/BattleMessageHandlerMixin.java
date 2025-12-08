package com.cobblemonextendedbattleui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.cobblemon.mod.common.api.net.NetworkPacket;
import com.cobblemon.mod.common.client.net.battle.BattleMessageHandler;
import com.cobblemon.mod.common.net.messages.client.battle.BattleMessagePacket;
import com.cobblemonextendedbattleui.BattleMessageInterceptor;
import net.minecraft.client.MinecraftClient;

/**
 * Mixin to intercept battle messages and extract stat change information.
 * Fixed for Cobblemon 1.6.1+: Uses NetworkPacket to match exact bytecode signature.
 */
@Mixin(value = BattleMessageHandler.class, remap = false)
public class BattleMessageHandlerMixin {

    /**
     * Inject at the start of handle() to process messages before they're displayed.
     */
    @Inject(method = "handle", at = @At("HEAD"))
    private void onHandle(NetworkPacket packet, MinecraftClient client, CallbackInfo ci) {
        // Cast to access BattleMessagePacket-specific methods
        BattleMessagePacket battlePacket = (BattleMessagePacket) packet;
        // Process each message for stat changes
        BattleMessageInterceptor.INSTANCE.processMessages(battlePacket.getMessages());
    }
}