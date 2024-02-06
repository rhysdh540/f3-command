package dev.rdh.f3;

import com.mojang.brigadier.CommandDispatcher;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.common.Mod;

@Mod("f3")
public class F3 {
	public F3() {
		NeoForge.EVENT_BUS.addListener(this::onCommandRegister);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public void onCommandRegister(RegisterClientCommandsEvent event) {
		F3Command.register((CommandDispatcher) event.getDispatcher());
	}
}