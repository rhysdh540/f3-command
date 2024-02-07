package dev.rdh.f3;

import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.nio.file.Path;

public final class F3Command {
	public static final String ID = "f3";
	private static final Minecraft mc = Minecraft.getInstance();

	public static void register(CommandDispatcher<SharedSuggestionProvider> dispatcher) {
		LiteralArgumentBuilder<SharedSuggestionProvider> f3 = literal(ID)
				.executes(context -> {
					mc.gui.getDebugOverlay().toggleOverlay();
					return 1;
				});

		f3.then(literal("toggle")
				.executes(context -> {
					mc.gui.getDebugOverlay().toggleOverlay();
					return 1;
				})
				.then(literal("profiler")
						.executes(context -> {
							mc.gui.getDebugOverlay().toggleProfilerChart();
							return 1;
						})
				)
				.then(literal("fps")
						.executes(context -> {
							mc.gui.getDebugOverlay().toggleFpsCharts();
							return 1;
						})
				)
				.then(literal("network")
						.executes(context -> {
							mc.gui.getDebugOverlay().toggleNetworkCharts();
							return 1;
						})
				)
				.then(literal("chunks")
						.executes(context -> sendMessage(mc.debugRenderer.switchRenderChunkborder()
								? "debug.chunk_boundaries.on" : "debug.chunk_boundaries.off"))
				)
				.then(literal("hitboxes")
						.executes(context -> {
							boolean hitboxes = mc.getEntityRenderDispatcher().shouldRenderHitBoxes();
							mc.getEntityRenderDispatcher().setRenderHitBoxes(!hitboxes);
							return sendMessage(hitboxes ? "debug.show_hitboxes.off" : "debug.show_hitboxes.on");
						})
				)
				.then(literal("tooltips")
						.executes(context -> {
							mc.options.advancedItemTooltips = !mc.options.advancedItemTooltips;
							mc.options.save();
							return sendMessage(mc.options.advancedItemTooltips ? "debug.advanced_tooltips.on" : "debug.advanced_tooltips.off");
						})
				)
				.then(literal("pauseOnLostFocus")
						.executes(context -> {
							mc.options.pauseOnLostFocus = !mc.options.pauseOnLostFocus;
							mc.options.save();
							return sendMessage(mc.options.pauseOnLostFocus ? "debug.pause_focus.on" : "debug.pause_focus.off");
						})
				)
		);

		f3.then(literal("reload")
				.then(literal("chunks")
						.executes(context -> {
							mc.levelRenderer.allChanged();
							return sendMessage("debug.reload_chunks.message");
						})
				)
				.then(literal("resources")
						.executes(context -> {
							mc.reloadResourcePacks();
							return sendMessage("debug.reload_resourcepacks.message");
						})
				)
		);

		f3.then(literal("clear")
				.then(literal("chat")
						.executes(context -> {
							mc.gui.getChat().clearMessages(false);
							return 1;
						})
				)
		);

		f3.then(literal("dump")
				.then(literal("textures")
						.executes(context -> {
							Path gameDir = mc.gameDirectory.toPath().toAbsolutePath();
							Path outputDir = TextureUtil.getDebugTexturePath(gameDir);
							mc.getTextureManager().dumpAllSheets(outputDir);
							Component component = Component.literal(gameDir.relativize(outputDir).toString())
									.withStyle(ChatFormatting.UNDERLINE)
									.withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, outputDir.toFile().toString())));
							return sendMessage("debug.dump_dynamic_textures", component);
						})
				)
		);

		dispatcher.register(literal(ID).redirect(dispatcher.register(f3)));
	}

	private static int sendMessage(String message, Object... args) {
		mc.gui.getChat().addMessage(Component.empty().append(Component.translatable("debug.prefix").withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD))
				.append(CommonComponents.SPACE).append(Component.translatable(message, args)));
		return 1;
	}

	private static LiteralArgumentBuilder<SharedSuggestionProvider> literal(String string) {
		return LiteralArgumentBuilder.literal(string);
	}
}
