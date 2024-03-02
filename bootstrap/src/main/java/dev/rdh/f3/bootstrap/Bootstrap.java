package dev.rdh.f3.bootstrap;

public interface Bootstrap {
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
			if(minor < 2) {
				b.init(b.modernF3());
			} else {
				b.init(b.lessModernF3());
			}
		} else if(major >= 18) {
			b.init(b.lessModernF3());
		}

		else throw new IllegalStateException("Minecraft version " + mcVer + " not supported yet");
	}

	void init(String className);
	String getMinecraftVersion();

	String modernF3();
	String lessModernF3();
}
