package moula.myutility.block.entity;

import moula.myutility.MyUtility;
import moula.myutility.inventory.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import java.util.Random;


public class Pedestal_Entity extends BlockEntity implements ImplementedInventory, Tickable {
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(1,ItemStack.EMPTY);


    public Pedestal_Entity() {
        super(MyUtility.PedestalEntity);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        Inventories.toTag(tag,items);
        return tag;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        Inventories.fromTag(tag,items);
        super.fromTag(state, tag);
    }

    @Override
    public void tick() {
        if (this.getStack(0).getItem()==MyUtility.Time_cloth){
            for (int x=0;x<9;x++){
                for (int z=0;z<9;z++){
                    for (int y=0;y<3;y++){
                        BlockPos pos = this.pos.add(x-4,y-1,z-4);
                        BlockState blockState = this.world.getBlockState(pos);
                        Random random = new Random();
                        if (blockState.hasRandomTicks()&&random.nextInt(50)==0)
                            blockState.getBlock().randomTick(blockState,(ServerWorld)this.world,pos,random);
                        if (blockState.getBlock()== Blocks.FURNACE){
                            BlockEntity furnaceEntity = this.world.getBlockEntity(pos);
                            for (int q=0;q<4;q++){
                                ((AbstractFurnaceBlockEntity)furnaceEntity).tick();
                            }
                        }
                    }
                }
            }
        }
    }
}
