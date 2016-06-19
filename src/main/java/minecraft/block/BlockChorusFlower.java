package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockChorusFlower extends Block
{
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 5);

    protected BlockChorusFlower()
    {
        super(Material.plants);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setTickRandomly(true);
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return null;
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!this.func_185606_b(worldIn, pos))
        {
            worldIn.destroyBlock(pos, true);
        }
        else
        {
            BlockPos blockpos = pos.up();

            if (worldIn.isAirBlock(blockpos) && blockpos.getY() < 256)
            {
                int i = ((Integer)state.getValue(AGE)).intValue();

                if (i < 5 && rand.nextInt(1) == 0)
                {
                    boolean flag = false;
                    boolean flag1 = false;
                    Block block = worldIn.getBlockState(pos.down()).getBlock();

                    if (block == Blocks.end_stone)
                    {
                        flag = true;
                    }
                    else if (block == Blocks.chorus_plant)
                    {
                        int j = 1;

                        for (int k = 0; k < 4; ++k)
                        {
                            Block block1 = worldIn.getBlockState(pos.down(j + 1)).getBlock();

                            if (block1 != Blocks.chorus_plant)
                            {
                                if (block1 == Blocks.end_stone)
                                {
                                    flag1 = true;
                                }

                                break;
                            }

                            ++j;
                        }

                        int i1 = 4;

                        if (flag1)
                        {
                            ++i1;
                        }

                        if (j < 2 || rand.nextInt(i1) >= j)
                        {
                            flag = true;
                        }
                    }
                    else if (block == Blocks.air)
                    {
                        flag = true;
                    }

                    if (flag && func_185604_a(worldIn, blockpos, (EnumFacing)null) && worldIn.isAirBlock(pos.up(2)))
                    {
                        worldIn.setBlockState(pos, Blocks.chorus_plant.getDefaultState(), 2);
                        this.func_185602_a(worldIn, blockpos, i);
                    }
                    else if (i < 4)
                    {
                        int l = rand.nextInt(4);
                        boolean flag2 = false;

                        if (flag1)
                        {
                            ++l;
                        }

                        for (int j1 = 0; j1 < l; ++j1)
                        {
                            EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(rand);
                            BlockPos blockpos1 = pos.offset(enumfacing);

                            if (worldIn.isAirBlock(blockpos1) && worldIn.isAirBlock(blockpos1.down()) && func_185604_a(worldIn, blockpos1, enumfacing.getOpposite()))
                            {
                                this.func_185602_a(worldIn, blockpos1, i + 1);
                                flag2 = true;
                            }
                        }

                        if (flag2)
                        {
                            worldIn.setBlockState(pos, Blocks.chorus_plant.getDefaultState(), 2);
                        }
                        else
                        {
                            this.func_185605_c(worldIn, pos);
                        }
                    }
                    else if (i == 4)
                    {
                        this.func_185605_c(worldIn, pos);
                    }
                }
            }
        }
    }

    private void func_185602_a(World p_185602_1_, BlockPos p_185602_2_, int p_185602_3_)
    {
        p_185602_1_.setBlockState(p_185602_2_, this.getDefaultState().withProperty(AGE, Integer.valueOf(p_185602_3_)), 2);
        p_185602_1_.playAuxSFX(1033, p_185602_2_, 0);
    }

    private void func_185605_c(World p_185605_1_, BlockPos p_185605_2_)
    {
        p_185605_1_.setBlockState(p_185605_2_, this.getDefaultState().withProperty(AGE, Integer.valueOf(5)), 2);
        p_185605_1_.playAuxSFX(1034, p_185605_2_, 0);
    }

    private static boolean func_185604_a(World p_185604_0_, BlockPos p_185604_1_, EnumFacing p_185604_2_)
    {
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
            if (enumfacing != p_185604_2_ && !p_185604_0_.isAirBlock(p_185604_1_.offset(enumfacing)))
            {
                return false;
            }
        }

        return true;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube(IBlockState p_149662_1_)
    {
        return false;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return super.canPlaceBlockAt(worldIn, pos) && this.func_185606_b(worldIn, pos);
    }

    /**
     * Called when a neighboring block changes.
     */
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (!this.func_185606_b(worldIn, pos))
        {
            worldIn.scheduleUpdate(pos, this, 1);
        }
    }

    public boolean func_185606_b(World p_185606_1_, BlockPos p_185606_2_)
    {
        Block block = p_185606_1_.getBlockState(p_185606_2_.down()).getBlock();

        if (block != Blocks.chorus_plant && block != Blocks.end_stone)
        {
            if (block == Blocks.air)
            {
                int i = 0;

                for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
                {
                    Block block1 = p_185606_1_.getBlockState(p_185606_2_.offset(enumfacing)).getBlock();

                    if (block1 == Blocks.chorus_plant)
                    {
                        ++i;
                    }
                    else if (block1 != Blocks.air)
                    {
                        return false;
                    }
                }

                return i == 1;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack p_180657_6_)
    {
        super.harvestBlock(worldIn, player, pos, state, te, p_180657_6_);
        spawnAsEntity(worldIn, pos, new ItemStack(Item.getItemFromBlock(this)));
    }

    protected ItemStack createStackedBlock(IBlockState state)
    {
        return null;
    }

    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(AGE, Integer.valueOf(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(AGE)).intValue();
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {AGE});
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(worldIn, pos, state);
    }

    public static void func_185603_a(World p_185603_0_, BlockPos p_185603_1_, Random p_185603_2_, int p_185603_3_)
    {
        p_185603_0_.setBlockState(p_185603_1_, Blocks.chorus_plant.getDefaultState(), 2);
        func_185601_a(p_185603_0_, p_185603_1_, p_185603_2_, p_185603_1_, p_185603_3_, 0);
    }

    private static void func_185601_a(World p_185601_0_, BlockPos p_185601_1_, Random p_185601_2_, BlockPos p_185601_3_, int p_185601_4_, int p_185601_5_)
    {
        int i = p_185601_2_.nextInt(4) + 1;

        if (p_185601_5_ == 0)
        {
            ++i;
        }

        for (int j = 0; j < i; ++j)
        {
            BlockPos blockpos = p_185601_1_.up(j + 1);

            if (!func_185604_a(p_185601_0_, blockpos, (EnumFacing)null))
            {
                return;
            }

            p_185601_0_.setBlockState(blockpos, Blocks.chorus_plant.getDefaultState(), 2);
        }

        boolean flag = false;

        if (p_185601_5_ < 4)
        {
            int l = p_185601_2_.nextInt(4);

            if (p_185601_5_ == 0)
            {
                ++l;
            }

            for (int k = 0; k < l; ++k)
            {
                EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(p_185601_2_);
                BlockPos blockpos1 = p_185601_1_.up(i).offset(enumfacing);

                if (Math.abs(blockpos1.getX() - p_185601_3_.getX()) < p_185601_4_ && Math.abs(blockpos1.getZ() - p_185601_3_.getZ()) < p_185601_4_ && p_185601_0_.isAirBlock(blockpos1) && p_185601_0_.isAirBlock(blockpos1.down()) && func_185604_a(p_185601_0_, blockpos1, enumfacing.getOpposite()))
                {
                    flag = true;
                    p_185601_0_.setBlockState(blockpos1, Blocks.chorus_plant.getDefaultState(), 2);
                    func_185601_a(p_185601_0_, blockpos1, p_185601_2_, p_185601_3_, p_185601_4_, p_185601_5_ + 1);
                }
            }
        }

        if (!flag)
        {
            p_185601_0_.setBlockState(p_185601_1_.up(i), Blocks.chorus_flower.getDefaultState().withProperty(AGE, Integer.valueOf(5)), 2);
        }
    }
}
