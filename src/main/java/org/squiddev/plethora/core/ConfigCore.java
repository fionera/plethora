package org.squiddev.plethora.core;

import net.minecraftforge.common.config.Configuration;
import org.squiddev.configgen.Config;
import org.squiddev.configgen.DefaultDouble;
import org.squiddev.configgen.Range;
import org.squiddev.configgen.RequiresRestart;
import org.squiddev.plethora.core.ConfigCoreForgeLoader;

import java.io.File;
import java.util.ArrayList;

@Config(languagePrefix = "gui.config.plethora.")
public final class ConfigCore {
	public static Configuration configuration;

	public static void init(File file) {
		ConfigCoreForgeLoader.init(file);
		configuration = ConfigCoreForgeLoader.getConfiguration();
	}

	public static void sync() {
		ConfigCoreForgeLoader.doSync();
	}

	/**
	 * Some methods have a particular cost: they
	 * consume a set amount of fuel from their owner.
	 * This level regenerates over time.
	 *
	 * *Note* These values only apply to the default handler.
	 * Other mods may add custom handlers.
	 */
	public static class CostSystem {
		/**
		 * The fuel level all systems start at
		 */
		@DefaultDouble(100)
		@Range(min = 0)
		@RequiresRestart(mc = false, world = true)
		public static double initial;

		/**
		 * The amount of fuel regened each tick
		 */
		@DefaultDouble(10)
		@Range(min = 0)
		@RequiresRestart(mc = false, world = true)
		public static double regen;

		/**
		 * The maximum fuel level an item can have
		 */
		@DefaultDouble(100)
		@Range(min = 0)
		@RequiresRestart(mc = false, world = true)
		public static double limit;
	}

	/**
	 * Blacklist various providers
	 */
	public static class Blacklist {
		/**
		 * List of methods classes or packages which are blacklisted.
		 * For example use "org.squiddev.plethora.integration.vanilla.methods."
		 * to disable all vanilla methods
		 *
		 * This only applies to classes registered through @Method
		 */
		@RequiresRestart
		public static ArrayList<String> blacklistMethods;

		/**
		 * List of meta provider classes or packages which are blacklisted.
		 * For example use "org.squiddev.plethora.integration.vanilla.meta."
		 * to disable all vanilla meta providers.
		 *
		 * This only applies to classes registered through @MetaProvider
		 */
		@RequiresRestart
		public static ArrayList<String> blacklistMeta;

		/**
		 * List of tile entity classes or packages which will not be wrapped
		 * as peripherals. For example use "net.minecraft." to disable wrapping
		 * any vanilla peripheral. This does not blacklist subclasses
		 */
		@RequiresRestart
		public static ArrayList<String> blacklistTileEntities;
	}
}
