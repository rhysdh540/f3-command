package dev.rdh.f3.fabric;

import com.mojang.blaze3d.platform.TextureUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;

import dev.rdh.f3.Abstractions;
import dev.rdh.f3.F3Command;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;

import java.nio.file.Path;

public class LessModernFabricF3 implements ClientModInitializer, Abstractions {
	private static final Minecraft mc = Minecraft.getInstance();

	@Override
	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> F3Command.register(dispatcher, this));
	}

	@Override
	public String platform() {
		return FabricLoader.getInstance().isModLoaded("quilt_loader") ? "Quilt" : "Fabric";
	}

	@Override
	public String minecraftVersion() {
		return FabricLoader.getInstance().getModContainer("minecraft")
				.map(container -> container.getMetadata().getVersion().getFriendlyString())
				.orElseThrow(() -> new IllegalStateException("Minecraft mod not found"));
	}

	@Override
	public void toggleOverlay() {
		mc.options.renderDebug = !mc.options.renderDebug;
	}

	@Override
	public void toggleProfiler() {
		mc.options.renderDebugCharts = !mc.options.renderDebugCharts;
	}

	@Override
	public void toggleFps() {
		mc.options.renderFpsChart = !mc.options.renderFpsChart;
	}

	@Override
	public void toggleNetwork() {
		sendMessage("Network graph not supported in versions before 1.20.2.");
	}

	@Override
	public void toggleChunks() {
		sendMessage(mc.debugRenderer.switchRenderChunkborder()
				? "debug.chunk_boundaries.on" : "debug.chunk_boundaries.off");
	}

	@Override
	public void toggleHitboxes() {
		boolean hitboxes = mc.getEntityRenderDispatcher().shouldRenderHitBoxes();
		mc.getEntityRenderDispatcher().setRenderHitBoxes(!hitboxes);
		sendMessage(hitboxes ? "debug.show_hitboxes.off" : "debug.show_hitboxes.on");
	}

	@Override
	public void toggleTooltips() {
		mc.options.advancedItemTooltips = !mc.options.advancedItemTooltips;
		mc.options.save();
		sendMessage(mc.options.advancedItemTooltips ? "debug.advanced_tooltips.on" : "debug.advanced_tooltips.off");
	}

	@Override
	public void togglePauseOnLostFocus() {
		mc.options.pauseOnLostFocus = !mc.options.pauseOnLostFocus;
		mc.options.save();
		sendMessage(mc.options.pauseOnLostFocus ? "debug.pause_focus.on" : "debug.pause_focus.off");
	}

	@Override
	public void reloadChunks() {
		mc.levelRenderer.allChanged();
		sendMessage("debug.reload_chunks.message");
	}

	@Override
	public void reloadResources() {
		mc.reloadResourcePacks();
		sendMessage("debug.reload_resourcepacks.message");
	}

	@Override
	public void clearChat() {
		mc.gui.getChat().clearMessages(false);
	}

	@Override
	public void dumpTextures() {
		int[] version = parsedMcVersion();
		if(version[0] > 18) {
			if(version[0] != 19 || version[1] >= 4) {
				actuallyDumpTextures();
				return;
			}
		}
		sendMessage("Dumping textures not supported in versions before 1.19.4-pre3.");
	}

	private void actuallyDumpTextures() {
		Path gameDir = mc.gameDirectory.toPath().toAbsolutePath();
		Path outputDir = TextureUtil.getDebugTexturePath(gameDir);
		mc.getTextureManager().dumpAllSheets(outputDir);
		Component component = Component.literal(gameDir.relativize(outputDir).toString())
				.withStyle(ChatFormatting.UNDERLINE)
				.withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, outputDir.toFile().toString())));
		sendMessage("debug.dump_dynamic_textures", component);
	}

	private static void sendMessage(String message, Object... args) {
		mc.gui.getChat().addMessage(Component.empty()
				.append(Component.translatable("debug.prefix")
								.withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD))
				.append(Component.literal(" "))
				.append(Component.translatable(message, args)));
	}
}
