package moula.myutility;

import moula.myutility.block.Pedestal;
import moula.myutility.block.Trap;
import moula.myutility.block.entity.Pedestal_Entity;
import moula.myutility.block.entity.Trap_Entity;
import moula.myutility.item.Animal_sack;
import moula.myutility.item.Minning_Hammer;
import moula.myutility.item.Scythe;
import moula.myutility.item.Time_Cloth;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class MyUtility implements ModInitializer {


    public static final Item Animal_sack = new Animal_sack(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
    public static final Item Time_cloth = new Time_Cloth(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
    public static final Item Scythe_Iron = new Scythe(ToolMaterials.IRON, 3, -2.4F, new FabricItemSettings().group(ItemGroup.MISC),5);
    public static final Item Scythe_Stone = new Scythe(ToolMaterials.STONE, 3, -2.4F, new FabricItemSettings().group(ItemGroup.MISC),3);
    public static final Item Minning_Hammer = new Minning_Hammer(ToolMaterials.DIAMOND,12,-3.5F,new FabricItemSettings().group(ItemGroup.MISC).maxDamage(4096));
    public static final Block Pedestal = new Pedestal(FabricBlockSettings.of(Material.METAL).strength(4.0f));
    public static final Block Trap = new Trap(FabricBlockSettings.of(Material.WOOD).strength(4.0f));
    public static BlockEntityType<Pedestal_Entity> PedestalEntity;
    public static BlockEntityType<Trap_Entity> TrapEntity;

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier("myutility","animal_sack"), Animal_sack);
        Registry.register(Registry.ITEM,new Identifier("myutility","time_cloth"),Time_cloth);
        Registry.register(Registry.ITEM,new Identifier("myutility","ironscythe"),Scythe_Iron);
        Registry.register(Registry.ITEM,new Identifier("myutility","stonescythe"),Scythe_Stone);
        Registry.register(Registry.ITEM,new Identifier("myutility","minninghammer"),Minning_Hammer);
        Registry.register(Registry.BLOCK,new Identifier("myutility","pedestal"),Pedestal);
        Registry.register(Registry.BLOCK,new Identifier("myutility","trap"),Trap);
        Registry.register(Registry.ITEM,new Identifier("myutility","trap"),new BlockItem(Trap,new FabricItemSettings().group(ItemGroup.MISC)));
        Registry.register(Registry.ITEM,new Identifier("myutility","pedestal"),new BlockItem(Pedestal,new FabricItemSettings().group(ItemGroup.MISC)));
        PedestalEntity = Registry.register(Registry.BLOCK_ENTITY_TYPE,"myutility:pedestalentity",BlockEntityType.Builder.create(Pedestal_Entity::new, Pedestal).build(null));
        TrapEntity = Registry.register(Registry.BLOCK_ENTITY_TYPE,"myutility:trapentity",BlockEntityType.Builder.create(Trap_Entity::new,Trap).build(null));
    }
}
