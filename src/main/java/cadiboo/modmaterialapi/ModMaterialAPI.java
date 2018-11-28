package cadiboo.modmaterialapi;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

import static cadiboo.modmaterialapi.util.ModReference.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = Version.VERSION, acceptedMinecraftVersions = ACCEPTED_VERSIONS, dependencies = DEPENDENCIES, canBeDeactivated = CAN_BE_DEACTIVATED, clientSideOnly = false, serverSideOnly = false, modLanguage = "java")
public class ModMaterialAPI {

	@Instance(MOD_ID)
	public static ModMaterialAPI instance;

	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

	/**
	 * Run before anything else. <s>Read your config, create blocks, items, etc, and register them with the GameRegistry</s>
	 *
	 * @see net.minecraftforge.common.ForgeModContainer#preInit(FMLPreInitializationEvent)
	 */
	@EventHandler
	public void preInit(final FMLPreInitializationEvent event) {

	}

	/**
	 * Do your mod setup. Build whatever data structures you care about. Register recipes, send FMLInterModComms messages to other mods.
	 */
	@EventHandler
	public void init(final FMLInitializationEvent event) {

	}

	/**
	 * Mod compatibility, or anything which depends on other mods’ init phases being finished.
	 *
	 * @see net.minecraftforge.common.ForgeModContainer#postInit(FMLPostInitializationEvent)
	 */
	@EventHandler
	public void postInit(final FMLPostInitializationEvent event) {

	}

	/**
	 * Logs message object(s) with the {@link org.apache.logging.log4j.Level#DEBUG DEBUG} level.
	 *
	 * @param messages the message objects to log.
	 *
	 * @author Cadiboo
	 */
	public static void debug(final Object... messages) {

		for (final Object msg : messages) {
			LOGGER.debug(msg);
		}

	}

	/**
	 * Logs message object(s) with the {@link org.apache.logging.log4j.Level#INFO ERROR} INFO.
	 *
	 * @param messages the message objects to log.
	 *
	 * @author Cadiboo
	 */
	public static void info(final Object... messages) {

		for (final Object msg : messages) {
			LOGGER.info(msg);
		}

	}

	/**
	 * Logs message object(s) with the {@link org.apache.logging.log4j.Level#WARN WARN} level.
	 *
	 * @param messages the message objects to log.
	 *
	 * @author Cadiboo
	 */
	public static void warn(final Object... messages) {

		for (final Object msg : messages) {
			LOGGER.warn(msg);
		}

	}

	/**
	 * Logs message object(s) with the {@link org.apache.logging.log4j.Level#ERROR ERROR} level.
	 *
	 * @param messages the message objects to log.
	 *
	 * @author Cadiboo
	 */
	public static void error(final Object... messages) {

		for (final Object msg : messages) {
			LOGGER.error(msg);
		}

	}

	/**
	 * Logs message object(s) with the {@link org.apache.logging.log4j.Level#FATAL FATAL} level.
	 *
	 * @param messages the message objects to log.
	 *
	 * @author Cadiboo
	 */
	public static void fatal(final Object... messages) {

		for (final Object msg : messages) {
			LOGGER.fatal(msg);

		}

	}

	/**
	 * Logs all {@link Field Field}s and their values of an object with the {@link org.apache.logging.log4j.Level#INFO INFO} level.
	 *
	 * @param objects the objects to dump.
	 *
	 * @author Cadiboo
	 */
	public static void dump(final Object... objects) {

		for (final Object obj : objects) {
			final Field[] fields = obj.getClass().getDeclaredFields();
			info("Dump of " + obj + ":");
			for (int i = 0; i < fields.length; i++) {
				try {
					fields[i].setAccessible(true);
					info(fields[i].getName() + " - " + fields[i].get(obj));

				} catch (IllegalArgumentException | IllegalAccessException e) {
					info("Error getting field " + fields[i].getName());
					info(e.getLocalizedMessage());

				}
			}
		}

	}

}
