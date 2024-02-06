package dev.rdh.f3;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod("f3")
public class F3 {
	public F3() {
		MinecraftForge.EVENT_BUS.addListener(this::onCommandRegister);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public void onCommandRegister(RegisterClientCommandsEvent event) {
		F3Command.register((CommandDispatcher) event.getDispatcher());
	}
}