package dev.rdh.f3.bootstrap;

import dev.rdh.f3.F3Command;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(F3Command.ID)
public class F3ForgeBootstrap implements Bootstrap {
	public F3ForgeBootstrap() {
		Bootstrap.bootstrap(this);
	}

	@Override
	 public void init(String className) {
		try {
			Class<?> cls = Class.forName(className);
			cls.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new IllegalStateException("Failed to initialize Forge F3", e);
		}
	}

	@Override
	public String getMinecraftVersion() {
		return FMLLoader.versionInfo().mcVersion();
	}

	@Override
	public String modernF3() {
		return "dev.rdh.f3.forge.ModernForgeF3";
	}

	@Override
	public String lessModernF3() {
		return "dev.rdh.f3.forge.LessModernForgeF3";
	}
}
