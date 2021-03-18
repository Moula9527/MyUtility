package moula.myutility.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class Minning_Hammer extends PickaxeItem {

    public Minning_Hammer(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        world.setBlockState(pos, state);
        HitResult hitResult = raycast(world,(PlayerEntity) miner, RaycastContext.FluidHandling.NONE);
        world.removeBlock(pos,false);
        Direction direction;
        direction = ((BlockHitResult)hitResult).getSide();
        int mx = 1;
        int my = 1;
        int mz = 1;
        switch (direction.getAxis()){
            case X:
                mx = 0;
                break;
            case Y:
                my = 0;
                break;
            case Z:
                mz = 0;
                break;
        }
        int[] var = {-1,0,1};
        for (int x:var){
            for (int y:var){
                for (int z:var){
                    BlockPos newpos = pos.add((x*mx),(y*my),(z*mz));
                    BlockState blockState = world.getBlockState(newpos);
                    if (this.isEffectiveOn(blockState)){
                        blockState.getBlock().onBreak(world, newpos, blockState, (PlayerEntity) miner);
                        boolean bl = world.removeBlock(newpos, false);
                        if (bl) {
                            blockState.getBlock().onBroken(world, newpos, blockState);
                            blockState.getBlock().afterBreak(world, (PlayerEntity) miner, newpos, blockState, null, stack.copy());
                        }
                    }
                }
            }
        }
        return super.postMine(stack, world, state, pos, miner);
    }
}
