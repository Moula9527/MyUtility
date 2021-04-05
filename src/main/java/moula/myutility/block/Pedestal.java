package moula.myutility.block;

import moula.myutility.block.entity.Pedestal_Entity;
import moula.myutility.item.Time_Cloth;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class Pedestal extends Block implements BlockEntityProvider {
    public static final BooleanProperty ItemInside = BooleanProperty.of("iteminside");

    public Pedestal(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(ItemInside,false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ItemInside);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(3d,0d,3d,13d,11d,13d);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new Pedestal_Entity();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;
        Inventory blockentity = (Inventory) world.getBlockEntity(pos);

        if (blockentity.getStack(0).isEmpty()&&player.getStackInHand(hand).getItem() instanceof Time_Cloth){
            blockentity.setStack(0,player.getStackInHand(hand).copy());
            player.getStackInHand(hand).setCount(0);
            world.setBlockState(pos,state.with(ItemInside,true));
            return ActionResult.SUCCESS;
        } else if (player.getStackInHand(hand).isEmpty()&&!blockentity.getStack(0).isEmpty()){
            player.inventory.offerOrDrop(world,blockentity.getStack(0));
            blockentity.removeStack(0);
            world.setBlockState(pos,state.with(ItemInside,false));
            return ActionResult.SUCCESS;
        }

        return ActionResult.FAIL;
    }
}
