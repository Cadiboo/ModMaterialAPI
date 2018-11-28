package cadiboo.modmaterialapi.block;

import cadiboo.modmaterialapi.modmaterial.ModMaterial;
import cadiboo.modmaterialapi.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.Random;

/**
 * Custom ore block for materials
 *
 * @author Cadiboo
 */
public class BlockModMaterialOre extends Block implements IBlockModMaterial {

	public static final String SUFFIX = "ore";

	protected final ModMaterial material;

	public BlockModMaterialOre(final ModMaterial material) {

		super(Material.ROCK);
		ModUtil.setRegistryNames(this, material, SUFFIX);
		this.material = material;

	}

	@Override
	public final ModMaterial getModMaterial() {

		return this.material;

	}

	@Override
	public BlockRenderLayer getRenderLayer() {

		return this.material.getProperties().getBlockRenderLayers().get(0);

	}

	@Override
	public boolean canRenderInLayer(final IBlockState state, final BlockRenderLayer layer) {

		return this.material.getProperties().getBlockRenderLayers().contains(layer);

	}

	@Override
	public int getLightValue(final IBlockState state, final IBlockAccess world, final BlockPos pos) {

		return this.material.getProperties().getLightValue(state, world, pos);

	}

	@Override
	public int getLightOpacity(final IBlockState state, final IBlockAccess world, final BlockPos pos) {

		return this.material.getProperties().getLightOpacity(state, world, pos);

	}

	@Override
	public boolean isFullCube(final IBlockState state) {

		return true;

	}

	@Override
	public boolean isOpaqueCube(final IBlockState state) {

		return true;

	}

	@Override
	public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {

		final Item item = this.material.getProperties().getOreDrop();
		if (item == null) {
			return Item.getItemFromBlock(this);
		}
		return item;

	}

	@Override
	public int quantityDroppedWithBonus(final int fortune, final Random random) {

		return this.material.getProperties().getQuantityDroppedWithBonus(fortune, random);

	}

}