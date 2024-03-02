package dev.rdh.f3.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Bootstrap {
	Logger log = LoggerFactory.getLogger(Bootstrap.class);

	static void bootstrap(Bootstrap b) {
		String mcVer = b.getMinecraftVersion();

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
			if(minor >= 2) {
				b.initWithMessage(b.modern());
			} else {
				b.initWithMessage(b.lessModern());
			}
		} else if(major >= 18) {
			b.initWithMessage(b.lessModern());
		}

		else throw new IllegalStateException("Minecraft version " + mcVer + " not supported yet");
	}

	default void initWithMessage(String className) {
		log.info("Initializing F3 for {} on mc{} with class {}", platform(), getMinecraftVersion(), className);
		init(className);
	}

	String platform();

	void init(String className);
	String getMinecraftVersion();

	String modern();
	String lessModern();
}
