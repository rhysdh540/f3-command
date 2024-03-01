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
}
