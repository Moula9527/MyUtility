package moula.myutility.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class Time_Cloth extends Item {

    public Time_Cloth(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(Hand.OFF_HAND);
        if(stack.getItem().isDamageable()&& stack.isDamaged()){
            stack.setDamage(0);
            return TypedActionResult.success(user.getStackInHand(hand));
        }


        return super.use(world, user, hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos blockPos = context.getBlockPos();
        World world = context.getWorld();
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.hasRandomTicks()&&world instanceof ServerWorld){
            Random random = new Random();
            int[] var = {-1,0,1};
            for (int x:var){
                for (int y:var){
                    for(int z:var){
                        BlockPos newpos = blockPos.add(x,y,z);
                        BlockState newstate = world.getBlockState(newpos);
                        if (random.nextInt(25) == 0&&newstate.hasRandomTicks()){
                            newstate.getBlock().randomTick(newstate, (ServerWorld) world,newpos,random);
                        }
                    }
                }
            }

            return ActionResult.SUCCESS;
        }
        return super.useOnBlock(context);
    }
}
