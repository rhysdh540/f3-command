package dev.rdh.f3.bootstrap;

import dev.rdh.f3.F3Command;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(F3Command.ID)
public class F3ForgeBootstrap {
	public F3ForgeBootstrap() {
		String mcVer = FMLLoader.versionInfo().mcVersion();

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
				init("dev.rdh.f3.fabric.ModernForgeF3");
			} else {
				init("dev.rdh.f3.fabric.LessModernForgeF3");
			}
		} else if(major >= 18) {
			init("dev.rdh.f3.fabric.LessModernForgeF3");
		}

		else throw new IllegalStateException("Minecraft version " + mcVer + " not supported yet");
	}

	private void init(String className) {
		try {
			Class<?> cls = Class.forName(className);
			cls.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new IllegalStateException("Failed to initialize modern F3", e);
		}
	}
}
