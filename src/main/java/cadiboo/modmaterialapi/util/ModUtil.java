package cadiboo.modmaterialapi.util;

import cadiboo.modmaterialapi.ModMaterialAPI;
import cadiboo.modmaterialapi.modmaterial.ModMaterial;
import net.minecraft.block.Block;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * Utility Methods for Common Code
 *
 * @author Cadiboo
 */
public final class ModUtil {

	public static final Field BLOCKS_DEFAULT_VALUE = ReflectionHelper.findField(ForgeRegistry.class, "defaultValue");

	/**
	 * Sets the {@link net.minecraftforge.registries.IForgeRegistryEntry.Impl#setRegistryName(net.minecraft.util.ResourceLocation) Registry Name} and the {@link net.minecraft.block.Block#setTranslationKey(String) Translation Key} for the block taking vanilla overriding into account
	 *
	 * @param block       the block to set registry names for
	 * @param modMaterial the {@link ModMaterial} to get the names based on
	 * @param nameSuffix  the string to be appended to the names (for example "ore" or "block")
	 *
	 * @return the block
	 */
	public static Block setRegistryNames(final Block block, final ModMaterial modMaterial, final String nameSuffix) {

		final ResourceLocation registryName = new ResourceLocation(modMaterial.getModId(), getNameWithSuffix(modMaterial.getVanillaNameLowercase(nameSuffix), nameSuffix));
		block.setHardness(modMaterial.getProperties().getHardness());
		setRegistryNames(block, registryName);

		final Block overriddenBlock = ForgeRegistries.BLOCKS.getValue(registryName);

		if ((overriddenBlock != null)) {
			// WHY do you not return null forge, WHY (it returns air)
			try {
				if (overriddenBlock != BLOCKS_DEFAULT_VALUE.get(ForgeRegistries.BLOCKS)) {
					block.setTranslationKey(overriddenBlock.getTranslationKey().replace("tile.", ""));
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		return block;
	}

	/**
	 * Sets the {@link net.minecraftforge.registries.IForgeRegistryEntry.Impl#setRegistryName(net.minecraft.util.ResourceLocation) Registry Name} and the {@link net.minecraft.item.Item#setTranslationKey(String) Translation Key} for the item taking vanilla overriding and vanilla name quirks into account
	 *
	 * @param item        the item to set registry names for
	 * @param modMaterial the {@link ModMaterial} to get the names based on
	 * @param nameSuffix  the string to be appended to the names (for example "shovel" or "helmet")
	 *
	 * @return the item
	 */
	public static Item setRegistryNames(final Item item, final ModMaterial modMaterial, final String nameSuffix) {

		final ResourceLocation registryName = new ResourceLocation(modMaterial.getModId(), getNameWithSuffix(modMaterial.getVanillaNameLowercase(nameSuffix), nameSuffix));
		setRegistryNames(item, registryName);

		final Item overriddenItem = ForgeRegistries.ITEMS.getValue(registryName);
		if (overriddenItem != null) {
			item.setTranslationKey(overriddenItem.getTranslationKey().replace("item.", ""));
		}
		return item;
	}

	/**
	 * Sets the {@link net.minecraftforge.registries.IForgeRegistryEntry.Impl#setRegistryName(net.minecraft.util.ResourceLocation) Registry Name} and the {@link net.minecraft.item.Item#setTranslationKey(String) Translation Key} (if applicable) for the entry
	 *
	 * @param entry the {@link net.minecraftforge.registries.IForgeRegistryEntry.Impl IForgeRegistryEntry.Impl<?>} to set the names for
	 * @param name  the name for the entry that the registry name is derived from
	 *
	 * @return the entry
	 */
	public static <T extends IForgeRegistryEntry.Impl<?>> T setRegistryNames(final T entry, final String name) {

		return setRegistryNames(entry, new ResourceLocation(ModReference.MOD_ID, name));
	}

	/**
	 * Sets the {@link net.minecraftforge.registries.IForgeRegistryEntry.Impl#setRegistryName(net.minecraft.util.ResourceLocation) Registry Name} and the {@link net.minecraft.item.Item#setTranslationKey(String) Translation Key} (if applicable) for the entry
	 *
	 * @param entry        the {@link net.minecraftforge.registries.IForgeRegistryEntry.Impl IForgeRegistryEntry.Impl<?>} to set the names for
	 * @param registryName the registry name for the entry that the translation key is also gotten from
	 *
	 * @return the entry
	 */
	public static <T extends IForgeRegistryEntry.Impl<?>> T setRegistryNames(final T entry, final ResourceLocation registryName) {

		return setRegistryNames(entry, registryName, registryName.getPath());
	}

	/**
	 * Sets the {@link net.minecraftforge.registries.IForgeRegistryEntry.Impl#setRegistryName(net.minecraft.util.ResourceLocation) Registry Name} and the {@link net.minecraft.item.Item#setTranslationKey(String) Translation Key} (if applicable) for the entry
	 *
	 * @param entry          the {@link net.minecraftforge.registries.IForgeRegistryEntry.Impl IForgeRegistryEntry.Impl<?>} to set the names for
	 * @param registryName   the registry name for the entry
	 * @param translationKey the translation key for the entry
	 *
	 * @return the entry
	 */
	public static <T extends IForgeRegistryEntry.Impl<?>> T setRegistryNames(final T entry, final ResourceLocation registryName, final String translationKey) {

		entry.setRegistryName(registryName);
		if (entry instanceof Block) {
			((Block) entry).setTranslationKey(translationKey);
		}
		if (entry instanceof Item) {
			((Item) entry).setTranslationKey(translationKey);
		}
		return entry;
	}

	/**
	 * Gets the game name from a slot<br>
	 * For example {@link net.minecraft.inventory.EntityEquipmentSlot#CHEST EntityEquipmentSlot.CHEST} -> "CHESTPLATE"
	 *
	 * @param slot the {@link net.minecraft.inventory.EntityEquipmentSlot EntityEquipmentSlot} to get the name for
	 *
	 * @return the game name for the slot
	 */
	public static String getSlotGameNameUppercase(final EntityEquipmentSlot slot) {

		switch (slot) {
			case CHEST:
				return "CHESTPLATE";
			case FEET:
				return "BOOTS";
			case HEAD:
				return "HELMET";
			case LEGS:
				return "LEGGINGS";
			default:
				return slot.name().toUpperCase();
		}
	}

	/**
	 * Converts the game name to lowercase as per {@link String#toLowerCase() String.toLowerCase}.
	 *
	 * @param slot the {@link EntityEquipmentSlot} to get the name from
	 *
	 * @return the game name in lowercase as per {@link String#toLowerCase() String.toLowerCase}.
	 */
	public static String getSlotGameNameLowercase(final EntityEquipmentSlot slot) {

		return getSlotGameNameUppercase(slot).toLowerCase();
	}

	/**
	 * Capitalizes the game name formatted as per {@link org.apache.commons.lang3.StringUtils#capitalize(String) StringUtils.capitalize}.
	 *
	 * @param slot the {@link EntityEquipmentSlot} to get the name from
	 *
	 * @return the game name formatted as per {@link org.apache.commons.lang3.StringUtils#capitalize(String) StringUtils.capitalize}.
	 */
	public static String getSlotGameNameFormatted(final EntityEquipmentSlot slot) {

		return StringUtils.capitalize(getSlotGameNameLowercase(slot));
	}

	/**
	 * Maps a value from one range to another range. Taken from https://stackoverflow.com/a/5732117
	 *
	 * @param inputStart  the start of the input's range
	 * @param inputEnd    the end of the input's range
	 * @param outputStart the start of the output's range
	 * @param outputEnd   the end of the output's range
	 * @param input       the input
	 *
	 * @return the newly mapped value
	 */
	public static double map(final double inputStart, final double inputEnd, final double outputStart, final double outputEnd, final double input) {

		final double input_range = inputEnd - inputStart;
		final double output_range = outputEnd - outputStart;

		return (((input - inputStart) * output_range) / input_range) + outputStart;
	}

	/**
	 * Turns a class's name into a registry name<br>
	 * It expects the Class's Name to be in CamelCase format<br>
	 * It returns the registry name in snake_case format<br>
	 * <br>
	 * Examples:<br>
	 * (TileEntitySuperAdvancedFurnace, "TileEntity") -> super_advanced_furnace<br>
	 * (EntityPortableGenerator, "Entity") -> portable_generator<br>
	 * (TileEntityPortableGenerator, "Entity") -> tile_portable_generator<br>
	 * (EntityPortableEntityGeneratorEntity, "Entity") -> portable_generator<br>
	 *
	 * @param clazz      the class
	 * @param removeType the string to be removed from the class's name
	 *
	 * @return the recommended registry name for the class
	 */
	public static String getRegistryNameForClass(final Class<?> clazz, final String removeType) {

		return org.apache.commons.lang3.StringUtils.uncapitalize(clazz.getSimpleName().replace(removeType, "")).replaceAll("([A-Z])", "_$1").toLowerCase();
	}

	/**
	 * @param world the world to get the logical side from
	 *
	 * @return the logical side of the world
	 */
	public static Side getLogicalSide(final World world) {

		if (world.isRemote) {
			return Side.CLIENT;
		} else {
			return Side.SERVER;
		}
	}

	/**
	 * @param world the world to get the logical side from
	 */
	public static void logLogicalSide(final World world) {

		ModMaterialAPI.info("Logical Side: " + getLogicalSide(world));
	}

	/**
	 * "name", "suffix" -> "name_suffix"<br>
	 * "name, "" -> "name"<br>
	 * "", "suffix" -> "_suffix"
	 *
	 * @param name   the name
	 * @param suffix the suffix
	 *
	 * @return name + "_" + suffix if the suffix's length is greater than 0 or just the name
	 */
	public static String getNameWithSuffix(final String name, final String suffix) {

		if (suffix.length() <= 0) {
			return name;
		}
		return name + "_" + suffix;
	}

}
