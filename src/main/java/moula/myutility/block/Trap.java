package moula.myutility.block;

import moula.myutility.block.entity.Trap_Entity;
import moula.myutility.item.Animal_sack;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class Trap extends Block implements BlockEntityProvider {
    public Trap(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new Trap_Entity();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;
        Inventory blockentity = (Inventory) world.getBlockEntity(pos);

        if (blockentity.getStack(0).isEmpty()&&player.getStackInHand(hand).getItem() instanceof Animal_sack){
            if (player.getStackInHand(hand).getOrCreateTag().contains("EntityTag"))
                return ActionResult.FAIL;
            blockentity.setStack(0,player.getStackInHand(hand).copy());
            player.getStackInHand(hand).setCount(0);
            return ActionResult.SUCCESS;
        } else if (player.getStackInHand(hand).isEmpty()&&!blockentity.getStack(0).isEmpty()){
            player.inventory.offerOrDrop(world,blockentity.getStack(0));
            blockentity.removeStack(0);
            return ActionResult.SUCCESS;
        }

        return ActionResult.FAIL;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        for (Direction dirs : Direction.values()){
            if (dirs.getAxis()== Direction.Axis.Y){
                if (!(world.getBlockState(pos.offset(dirs)).getBlock() == Blocks.AIR))
                    return false;
            }else {
                if (!(world.getBlockState(pos.offset(dirs)).isSideSolidFullSquare(world,pos.offset(dirs),dirs.getOpposite())))
                    return false;
            }
        }
        return true;
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, Entity entity) {
        if (entity instanceof AnimalEntity||entity instanceof VillagerEntity&&!world.isClient){
            BlockEntity trap_entity = world.getBlockEntity(pos);
            ItemStack sack_item = ((Trap_Entity)trap_entity).onCapture(entity);
            if (sack_item!=null){
                world.spawnEntity(new ItemEntity(world,pos.getX(),pos.getY(),pos.getZ(),sack_item));
            }
        }

        super.onSteppedOn(world, pos, entity);
    }
}
