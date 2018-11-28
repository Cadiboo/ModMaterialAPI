package cadiboo.modmaterialapi.modmaterial;

import cadiboo.modmaterialapi.ModMaterialAPI;
import cadiboo.modmaterialapi.block.BlockModMaterialOre;
import cadiboo.modmaterialapi.block.BlockModMaterialResource;
import cadiboo.modmaterialapi.item.ModMaterialItemBlock;
import cadiboo.modmaterialapi.util.ModReference;
import net.minecraft.block.Block;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ModMaterial {

	private final String                name;
	private final String                modId;
	private final ModMaterialProperties properties;
	private final ArmorMaterial         armorMaterial;
	private final ToolMaterial          toolMaterial;
	private final HorseArmorType        horseArmorType;

	private BlockModMaterialOre      ore;
	private BlockModMaterialResource block;

	private ModMaterialItemBlock itemBlockOre;
	private ModMaterialItemBlock itemBlockBlock;

	private ItemModMaterialResource      resource;
	private ItemModMaterialResourcePiece resourcePiece;
	private ItemModMaterialArmor         helmet;
	private ItemModMaterialArmor         chestplate;
	private ItemModMaterialArmor         leggings;
	private ItemModMaterialArmor         boots;
	private ItemModMaterialHorseArmor    horseArmor;
	private ItemModMaterialPickaxe       pickaxe;
	private ItemModMaterialAxe           axe;
	private ItemModMaterialSword         sword;
	private ItemModMaterialShovel        shovel;
	private ItemModMaterialHoe           hoe;

	public ModMaterial(final String name, final String modId, final ModMaterialProperties properties) {

		this.name = name;
		this.modId = modId;
		this.properties = properties;
		this.armorMaterial = this.generateArmorMaterial();
		this.toolMaterial = this.generateToolMaterial();
		this.horseArmorType = this.generateHorseArmorType();
	}

	public String getName() {

		return this.name;
	}

	public ModMaterialProperties getProperties() {

		return this.properties;
	}

	public String getModId() {

		return this.modId;
	}

	public ArmorMaterial getArmorMaterial() {

		return this.armorMaterial;
	}

	public ToolMaterial getToolMaterial() {

		return this.toolMaterial;
	}

	public HorseArmorType getHorseArmorType() {

		return this.horseArmorType;
	}

	/**
	 * Converts the name to lowercase as per {@link String#toLowerCase() String.toLowerCase}.
	 */
	public String getNameLowercase() {

		return this.getName().toLowerCase();
	}

	/**
	 * Converts the name to uppercase as per {@link String#toUpperCase() String.toUpperCase}.
	 */
	public String getNameUppercase() {

		return this.getNameLowercase().toUpperCase();
	}

	private ToolMaterial generateToolMaterial() {

		boolean hasTools = false;
		hasTools |= this.getProperties().hasPickaxe();
		hasTools |= this.getProperties().hasAxe();
		hasTools |= this.getProperties().hasSword();
		hasTools |= this.getProperties().hasShovel();
		hasTools |= this.getProperties().hasHoe();

		if (! hasTools) {
			return null;
		} else {
			final String name = this.getNameUppercase();
			final int harvestLevel = Math.min(3, Math.round(this.getProperties().getHardness() / 3f));
			final int maxUses = (int) Math.ceil(this.getProperties().getHardness() * 150f);
			final float efficiency = this.getProperties().getHardness();
			final float damageVsEntity = this.getProperties().getHardness();
			final int enchantability = (int) Math.ceil(this.getProperties().getConductivity() / 10f);

			final ToolMaterial toolMaterial = EnumHelper.addToolMaterial(name, harvestLevel, maxUses, efficiency, damageVsEntity, enchantability);
			return toolMaterial;
		}
	}

	private ArmorMaterial generateArmorMaterial() {

		boolean hasArmor = false;
		hasArmor |= this.getProperties().hasHelmet();
		hasArmor |= this.getProperties().hasChestplate();
		hasArmor |= this.getProperties().hasLeggings();
		hasArmor |= this.getProperties().hasBoots();

		if (! hasArmor) {
			return null;
		} else {
			final String name = this.getNameUppercase();

			String nameSuffix = null;
			if ((nameSuffix == null) && this.getProperties().hasHelmet()) {
				nameSuffix = "helmet";
			}
			if ((nameSuffix == null) && this.getProperties().hasChestplate()) {
				nameSuffix = "chestplate";
			}
			if ((nameSuffix == null) && this.getProperties().hasLeggings()) {
				nameSuffix = "leggings";
			}
			if ((nameSuffix == null) && this.getProperties().hasBoots()) {
				nameSuffix = "boots";
			}

			final String textureName = new ModResourceLocation(this.getResouceLocationDomainWithOverrides(nameSuffix, ForgeRegistries.ITEMS), new ModResourceLocationPath(this.getNameLowercase())).toString();

			final int durability = (int) Math.ceil(this.getProperties().getHardness() * ModReference.ARMOR_MATERIAL_HARDNESS_MULTIPLIER);

			final int[] reductionAmounts = new int[4];
			Arrays.fill(reductionAmounts, (int) Math.ceil(this.getProperties().getHardness() / 2f));

			final int enchantability = (int) Math.ceil(this.getProperties().getConductivity() / 10f);

			final SoundEvent soundOnEquip = SoundEvents.ITEM_ARMOR_EQUIP_IRON;

			final float toughness = (int) Math.ceil(this.getProperties().getHardness() / 5f);

			final ArmorMaterial armorMaterial = EnumHelper.addArmorMaterial(name, textureName, durability, reductionAmounts, enchantability, soundOnEquip, toughness);
			// TODO TEST THIS!!
			armorMaterial.setRepairItem(new ItemStack(this.getResource()));
			return armorMaterial;

		}
	}

	private HorseArmorType generateHorseArmorType() {

		boolean hasArmor = false;
		hasArmor |= this.getProperties().hasHelmet();
		hasArmor |= this.getProperties().hasChestplate();
		hasArmor |= this.getProperties().hasLeggings();
		hasArmor |= this.getProperties().hasBoots();

		if (! hasArmor) {
			return HorseArmorType.NONE;
		} else {
			final String name = this.getNameUppercase();

			final String textureLocation = new ResourceLocation(this.getModId(), "textures/entity/horse/armor/horse_armor_" + this.getNameLowercase()) + ".png";

			final int armorStrength = (int) Math.ceil(this.getProperties().getHardness());

			return EnumHelper.addHorseArmor(name, textureLocation, armorStrength);
		}
	}

	public String getVanillaNameLowercase(final String suffix) {

		switch (suffix.toLowerCase()) {
			case "sword":
			case "shovel":
			case "pickaxe":
			case "axe":
			case "hoe":
			case "helmet":
			case "chestplate":
			case "leggings":
			case "boots":
			case "apple":
			case "carrot":
			case "horse_armor":
				return this.getNameLowercase() + (this.getNameLowercase().contains("gold") ? "en" : "");
			default:
				return this.getNameLowercase();
		}

	}

	public String getVanillaNameUppercase(final String suffix) {

		return this.getVanillaNameLowercase(suffix).toUpperCase();
	}

	public String getVanillaNameFormatted(final String suffix) {

		return StringUtils.capitalize(this.getVanillaNameLowercase(suffix));
	}

	public String getNameFormatted() {

		return String.join(" ", Arrays.asList(this.getNameLowercase().split("_")).stream().map(String::toUpperCase).collect(Collectors.toList()));
	}

	public class ModMaterialEventSubscriber {

		@SubscribeEvent(priority = EventPriority.NORMAL)
		public void registerBlocks(final RegistryEvent.Register<Block> event) {

			final IForgeRegistry<Block> registry = event.getRegistry();

			if (ModMaterial.this.getProperties().hasOre()) {
				ModMaterial.this.ore = new BlockModMaterialOre(ModMaterial.this);
				registry.register(ModMaterial.this.ore);
			}

			if (ModMaterial.this.getProperties().hasBlock()) {
				ModMaterial.this.block = new BlockModMaterialResource(ModMaterial.this);
				registry.register(ModMaterial.this.block);
			}

			ModMaterialAPI.debug("Registered blocks for " + ModMaterial.this.getNameFormatted());
		}

		@SubscribeEvent(priority = EventPriority.LOWEST)
		public void syncBlocks(final RegistryEvent.Register<Block> event) {

		}

		@SubscribeEvent(priority = EventPriority.NORMAL)
		public void registerItems(final RegistryEvent.Register<Item> event) {

		}

		@SubscribeEvent(priority = EventPriority.LOWEST)
		public void syncItems(final RegistryEvent.Register<Item> event) {

		}

	}

	private final ModMaterialEventSubscriber eventSubscriber = new ModMaterialEventSubscriber();

	public ModMaterialEventSubscriber getEventSubscriber() {

		return this.eventSubscriber;
	}

}
