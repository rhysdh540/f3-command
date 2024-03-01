package dev.rdh.f3.neoforge;

import com.mojang.blaze3d.platform.TextureUtil;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.common.NeoForge;

import dev.rdh.f3.Abstractions;
import dev.rdh.f3.F3Command;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.nio.file.Path;

@Mod(F3Command.ID)
public class NeoF3 implements Abstractions {
	private static final Minecraft mc = Minecraft.getInstance();
	public NeoF3() {
		NeoForge.EVENT_BUS.addListener(this::onCommandRegister);
	}

	@SubscribeEvent
	void onCommandRegister(RegisterClientCommandsEvent event) {
		F3Command.register(event.getDispatcher(), this);
	}

	@Override
	public String platform() {
		return "NeoForge";
	}

	@Override
	public String minecraftVersion() {
		return LoadingModList.get().getMods().stream()
				.filter(mod -> mod.getModId().equals("minecraft"))
				.findFirst()
				.map(mod -> mod.getVersion().toString())
				.orElseThrow(() -> new IllegalStateException("Minecraft mod not found"));
	}

	@Override
	public void toggleOverlay() {
		mc.gui.getDebugOverlay().toggleOverlay();
	}

	@Override
	public void toggleProfiler() {
		mc.gui.getDebugOverlay().toggleProfilerChart();
	}

	@Override
	public void toggleFps() {
		mc.gui.getDebugOverlay().toggleFpsCharts();
	}

	@Override
	public void toggleNetwork() {
		mc.gui.getDebugOverlay().toggleNetworkCharts();
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
		Path gameDir = mc.gameDirectory.toPath().toAbsolutePath();
		Path outputDir = TextureUtil.getDebugTexturePath(gameDir);
		mc.getTextureManager().dumpAllSheets(outputDir);
		Component component = Component.literal(gameDir.relativize(outputDir).toString())
				.withStyle(ChatFormatting.UNDERLINE)
				.withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, outputDir.toFile().toString())));
		sendMessage("debug.dump_dynamic_textures", component);
	}

	private static void sendMessage(String message, Object... args) {
		mc.gui.getChat().addMessage(Component.empty().append(Component.translatable("debug.prefix").withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD))
				.append(CommonComponents.SPACE).append(Component.translatable(message, args)));
	}
}
