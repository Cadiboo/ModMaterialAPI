package cadiboo.modmaterialapi.item;

import cadiboo.modmaterialapi.block.IBlockModMaterial;
import cadiboo.modmaterialapi.modmaterial.ModMaterial;
import cadiboo.modmaterialapi.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;

public class ModMaterialItemBlock extends ItemBlock {

	protected final ModMaterial modMaterial;

	public <T extends Block & IBlockModMaterial> ModMaterialItemBlock(final T block) {

		this(block, block.getRegistryName());
	}

	public <T extends Block & IBlockModMaterial> ModMaterialItemBlock(final T block, final ResourceLocation registryName) {

		super(block);
		this.modMaterial = block.getModMaterial();
		ModUtil.setRegistryNames(this, registryName);
	}

}
