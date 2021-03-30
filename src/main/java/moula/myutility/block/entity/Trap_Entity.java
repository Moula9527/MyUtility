package moula.myutility.block.entity;

import moula.myutility.MyUtility;
import moula.myutility.inventory.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

public class Trap_Entity extends BlockEntity implements ImplementedInventory {
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(1,ItemStack.EMPTY);

    public Trap_Entity() {
        super(MyUtility.TrapEntity);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        Inventories.fromTag(tag,items);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        Inventories.toTag(tag,items);
        super.toTag(tag);
        return tag;
    }

    public ItemStack onCapture(Entity entity){
        if (items.get(0)!=ItemStack.EMPTY){
            CompoundTag entitytag = entity.toTag(new CompoundTag());
            String id = Registry.ENTITY_TYPE.getId(entity.getType()).toString();
            entitytag.putString("id",id);
            items.get(0).getOrCreateTag().put("EntityTag",entitytag);
            ItemStack stack = items.get(0).copy();
            items.set(0,ItemStack.EMPTY);
            entity.remove();
            return stack;
        }
        return null;
    }
}
