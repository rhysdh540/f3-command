package dev.rdh.f3.bootstrap;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class F3FabricBootstrap implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		String mcVer = FabricLoader.getInstance().getModContainer("minecraft")
				.map(container -> container.getMetadata().getVersion().getFriendlyString())
				.orElseThrow(() -> new IllegalStateException("Minecraft mod not found"));
		String[] mcVers = mcVer.split("\\.");
		if(mcVers.length == 2) {
			mcVers = new String[]{ mcVers[0], mcVers[1], "0" };
		}

		if(mcVers.length != 3 || !"1".equals(mcVers[0])) {
			throw new IllegalStateException("Invalid Minecraft version: " + mcVer);
		}

		int major = Integer.parseInt(mcVers[1]);
		int minor = Integer.parseInt(mcVers[2]);

		if(major >= 20) {
			if(minor < 2) {
				init("dev.rdh.f3.fabric.ModernFabricF3");
			} else {
				init("dev.rdh.f3.fabric.LessModernFabricF3");
			}
		} else if(major >= 18) {
			init("dev.rdh.f3.fabric.LessModernFabricF3");
		}

		else throw new IllegalStateException("Minecraft version " + mcVer + " not supported yet");
	}

	private void init(String className) {
		try {
			Class<?> cls = Class.forName(className);
			cls.getDeclaredMethod("onInitializeClient").invoke(cls.getConstructor().newInstance());
		} catch (Exception e) {
			throw new IllegalStateException("Failed to initialize modern F3", e);
		}
	}
}
