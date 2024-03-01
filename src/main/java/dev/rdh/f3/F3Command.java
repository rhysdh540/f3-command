package dev.rdh.f3;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings({"unchecked", "rawtypes"})
public final class F3Command {
	public static final String ID = "f3";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	private static final AtomicBoolean registered = new AtomicBoolean(false);

	public static void register(CommandDispatcher dispatcher, Abstractions abstractions) {
		if(registered.get()) {
			return;
		}
		registered.set(true);
		LOGGER.info("Registering F3 command for {} on minecraft {}",
				abstractions.platform(), abstractions.minecraftVersion());


		LiteralArgumentBuilder f3 = (LiteralArgumentBuilder) literal(ID)
				.executes(context -> {
					abstractions.toggleOverlay();
					return 1;
				});

		f3.then(literal("toggle")
				.executes(context -> {
					abstractions.toggleOverlay();
					return 1;
				})
				.then(literal("profiler")
						.executes(context -> {
							abstractions.toggleProfiler();
							return 1;
						})
				)
				.then(literal("fps")
						.executes(context -> {
							abstractions.toggleFps();
							return 1;
						})
				)
				.then(literal("network")
						.executes(context -> {
							abstractions.toggleNetwork();
							return 1;
						})
				)
				.then(literal("chunks")
						.executes(context -> {
							abstractions.toggleChunks();
							return 1;
						})
				)
				.then(literal("hitboxes")
						.executes(context -> {
							abstractions.toggleHitboxes();
							return 1;
						})
				)
				.then(literal("tooltips")
						.executes(context -> {
							abstractions.toggleTooltips();
							return 1;
						})
				)
				.then(literal("pauseOnLostFocus")
						.executes(context -> {
							abstractions.togglePauseOnLostFocus();
							return 1;
						})
				)
		);

		f3.then(literal("reload")
				.then(literal("chunks")
						.executes(context -> {
							abstractions.reloadChunks();
							return 1;
						})
				)
				.then(literal("resources")
						.executes(context -> {
							abstractions.reloadResources();
							return 1;
						})
				)
		);

		f3.then(literal("clear")
				.then(literal("chat")
						.executes(context -> {
							abstractions.clearChat();
							return 1;
						})
				)
		);

		f3.then(literal("dump")
				.then(literal("textures")
						.executes(context -> {
							abstractions.dumpTextures();
							return 1;
						})
				)
		);

		dispatcher.register((LiteralArgumentBuilder) literal(ID).redirect(dispatcher.register(f3)));
	}

	private static LiteralArgumentBuilder literal(String string) {
		return LiteralArgumentBuilder.literal(string);
	}
}
