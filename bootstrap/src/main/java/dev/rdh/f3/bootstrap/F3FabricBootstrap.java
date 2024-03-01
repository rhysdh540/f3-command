package dev.rdh.f3.bootstrap;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import dev.rdh.f3.fabric.ModernFabricF3;
import dev.rdh.f3.fabric.LessModernFabricF3;

public class F3FabricBootstrap implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		String mcVer = FabricLoader.getInstance().getModContainer("minecraft")
				.map(container -> container.getMetadata().getVersion().getFriendlyString())
				.orElseThrow(() -> new IllegalStateException("Minecraft mod not found"));
		String[] mcVers = mcVer.split("\\.");

		if(mcVers.length != 3 || !"1".equals(mcVers[0])) {
			throw new IllegalStateException("Invalid Minecraft version: " + mcVer);
		}

		int major = Integer.parseInt(mcVers[1]);
		int minor = Integer.parseInt(mcVers[2]);

		if(major >= 20) {
			if(minor < 2) {
				new ModernFabricF3().onInitializeClient();
			} else {
				new LessModernFabricF3().onInitializeClient();
			}
		} else if(major >= 18) {
			new LessModernFabricF3().onInitializeClient();
		}

		else throw new IllegalStateException("Minecraft version " + mcVer + " not supported yet");
	}
}
