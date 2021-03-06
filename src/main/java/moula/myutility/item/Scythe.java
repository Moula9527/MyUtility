package moula.myutility.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class Scythe extends SwordItem {

    int area;

    public Scythe(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings,int area) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.area = area;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        boolean spawnpartical = false;
        BlockPos blockPos = context.getBlockPos();
        World world = context.getWorld();
        PlayerEntity user = context.getPlayer();
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.getBlock() instanceof CropBlock){
            int offset = area /2;
            for (int x=0;x<area;x++){
                for (int z=0;z<area;z++){
                    BlockPos newpos = blockPos.add(x-offset,0,z-offset);
                    BlockState newstate = world.getBlockState(newpos);
                    if (newstate.getBlock() instanceof CropBlock&&((CropBlock) newstate.getBlock()).isMature(newstate)){
                        world.setBlockState(newpos, ((CropBlock) newstate.getBlock()).withAge(0),2);
                        Block.dropStacks(newstate,world,blockPos);
                        if (!user.isCreative())
                        context.getStack().damage(1,new Random(),null);
                        spawnpartical = true;
                    }
                }
            }
            if (spawnpartical){
                user.spawnSweepAttackParticles();
            }
        }
        return super.useOnBlock(context);
    }
}
