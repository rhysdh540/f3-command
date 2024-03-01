package dev.rdh.f3;

public interface Abstractions {
	void toggleOverlay();
	void toggleProfiler();
	void toggleFps();
	void toggleNetwork();

	void toggleChunks();
	void toggleHitboxes();
	void toggleTooltips();
	void togglePauseOnLostFocus();

	void reloadChunks();
	void reloadResources();

	void clearChat();

	void dumpTextures();

	String platform();
	String minecraftVersion();

	/**
	 * probably breaks on snapshots and pre-releases
	 * @return array formatted as [major, minor] (for example 1.8.9 -> [8, 9], 1.20 -> [20, 0])
	 */
	default int[] parsedMcVersion() {
		String[] mcVer = minecraftVersion().split("\\.");
		int major = Integer.parseInt(mcVer[1]);
		int minor = mcVer.length > 2 ? Integer.parseInt(mcVer[2]) : 0;
		return new int[]{major, minor};
	}
}
