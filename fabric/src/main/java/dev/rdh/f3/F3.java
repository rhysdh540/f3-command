package dev.rdh.f3;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class F3 implements ClientModInitializer {
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> F3Command.register((CommandDispatcher) dispatcher));
	}
}