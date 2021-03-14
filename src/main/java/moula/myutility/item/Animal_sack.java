package moula.myutility.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.Optional;

public class Animal_sack extends Item {

    public Animal_sack(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (entity instanceof AnimalEntity){
            CompoundTag entitytag = entity.toTag(new CompoundTag());
            String id = Registry.ENTITY_TYPE.getId(entity.getType()).toString();
            entitytag.putString("id",id);
            user.getStackInHand(hand).getOrCreateTag().put("EntityTag",entitytag);
            entity.remove();
            return ActionResult.SUCCESS;
        }else{
            return ActionResult.PASS;
        }

    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stackinhand = user.getStackInHand(hand);
        CompoundTag Tag = stackinhand.getTag();
        if(Tag == null || !Tag.contains("EntityTag")){
            return TypedActionResult.fail(stackinhand);
        }
        HitResult hitResult = raycast(world,user, RaycastContext.FluidHandling.SOURCE_ONLY);
        if(hitResult.getType()!=HitResult.Type.BLOCK){
            return TypedActionResult.pass(stackinhand);
        }else if(!(world instanceof ServerWorld)){
            return TypedActionResult.success(stackinhand);
        }else{
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            BlockPos blockPos = blockHitResult.getBlockPos().offset(((BlockHitResult) hitResult).getSide(),1);
            if(world.canPlayerModifyAt(user,blockPos) && user.canPlaceOn(blockPos,blockHitResult.getSide(),stackinhand)){
                CompoundTag entityTag = postrans(Tag.getCompound("EntityTag"), blockPos.getX(), blockPos.getY(), blockPos.getZ());
                EntityType entityType = Registry.ENTITY_TYPE.getOrEmpty(new Identifier(entityTag.getString("id"))).get();
                Entity entity = entityType.create((ServerWorld) world,null,null,user,blockPos,SpawnReason.SPAWN_EGG,true,false);
                entity.fromTag(entityTag);
                entity.updatePosition(blockPos.getX()+0.5D,blockPos.getY(),blockPos.getZ()+0.5D);
                ((ServerWorld) world).spawnEntityAndPassengers(entity);
                stackinhand.removeSubTag("EntityTag");
            }
            return TypedActionResult.success(stackinhand);
        }
    }

    private CompoundTag postrans(CompoundTag tag, int x, int y,int z){
        ListTag newpos = new ListTag();
        newpos.add(DoubleTag.of(x));
        newpos.add(DoubleTag.of(y));
        newpos.add(DoubleTag.of(z));
        tag.remove("pos");
        tag.put("pos",newpos);
        return tag;
    }
}
