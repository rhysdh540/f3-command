package dev.rdh.f3.bootstrap;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class F3FabricBootstrap implements ClientModInitializer, Bootstrap {
	@Override
	public void onInitializeClient() {
		Bootstrap.bootstrap(this);
	}

	@Override
	public String platform() {
		return FabricLoader.getInstance().isModLoaded("quilt_loader") ? "Quilt" : "Fabric";
	}

	@Override
	public void init(String className) {
		try {
			Class<?> cls = Class.forName(className);
			cls.getDeclaredMethod("onInitializeClient").invoke(cls.getConstructor().newInstance());
		} catch (Exception e) {
			throw new IllegalStateException("Failed to initialize Fabric F3", e);
		}
	}

	@Override
	public String getMinecraftVersion() {
		return FabricLoader.getInstance().getModContainer("minecraft")
				.map(container -> container.getMetadata().getVersion().getFriendlyString())
				.orElseThrow(() -> new IllegalStateException("Minecraft mod not found"));
	}

	@Override
	public String modern() {
		return "dev.rdh.f3.fabric.ModernFabricF3";
	}

	@Override
	public String lessModern() {
		return "dev.rdh.f3.fabric.LessModernFabricF3";
	}
}
