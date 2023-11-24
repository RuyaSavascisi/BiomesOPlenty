/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package biomesoplenty.common.worldgen.feature.misc;

import biomesoplenty.common.util.SimpleBlockPredicate;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BambooLeaves;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class ThinBambooFeature extends Feature<NoneFeatureConfiguration>
{
    protected SimpleBlockPredicate placeOn = (world, pos) -> world.getBlockState(pos).getBlock() == Blocks.COARSE_DIRT;
    protected SimpleBlockPredicate replace = (world, pos) -> TreeFeature.isAirOrLeaves(world, pos);
    private static final BlockState BAMBOO_TRUNK = Blocks.BAMBOO.defaultBlockState().setValue(BambooStalkBlock.AGE, Integer.valueOf(0)).setValue(BambooStalkBlock.LEAVES, BambooLeaves.NONE).setValue(BambooStalkBlock.STAGE, Integer.valueOf(1));
    private static final BlockState BAMBOO_TOP = BAMBOO_TRUNK.setValue(BambooStalkBlock.LEAVES, BambooLeaves.SMALL);

    public ThinBambooFeature(Codec<NoneFeatureConfiguration> deserializer)
    {
        super(deserializer);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
    {
        WorldGenLevel world = context.level();
        BlockPos pos = context.origin();
        RandomSource rand = context.random();

        while (pos.getY() >= world.getMinBuildHeight()+1 && this.replace.matches(world, pos)) {pos = pos.below();}

        if (!this.placeOn.matches(world, pos))
        {
            // Abandon if we can't place the tree on this block
            return false;
        }

        pos = pos.above();

        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());
        if (world.isEmptyBlock(blockpos$mutableblockpos))
        {
            if (Blocks.BAMBOO.defaultBlockState().canSurvive(world, blockpos$mutableblockpos))
            {
                int j = rand.nextInt(3) + 0;

                for(int l1 = 0; l1 < j && world.isEmptyBlock(blockpos$mutableblockpos); ++l1)
                {
                    world.setBlock(blockpos$mutableblockpos, BAMBOO_TRUNK, 2);
                    blockpos$mutableblockpos.move(Direction.UP, 1);
                }

                world.setBlock(blockpos$mutableblockpos, BAMBOO_TOP, 2);
            }
        }

        return true;
    }
}