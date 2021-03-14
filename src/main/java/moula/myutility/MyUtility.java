package moula.myutility;

import moula.myutility.block.Pedestal;
import moula.myutility.block.entity.Pedestal_Entity;
import moula.myutility.item.Animal_sack;
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
    public static final Item Scythe = new Scythe(ToolMaterials.IRON, 3, -2.4F, new FabricItemSettings().group(ItemGroup.MISC),3);
    public static final Block Pedestal = new Pedestal(FabricBlockSettings.of(Material.METAL).strength(4.0f));
    public static BlockEntityType<Pedestal_Entity> PedestalEntity;

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier("myutility","animal_sack"), Animal_sack);
        Registry.register(Registry.ITEM,new Identifier("myutility","time_cloth"),Time_cloth);
        Registry.register(Registry.ITEM,new Identifier("myutility","scythe"),Scythe);
        Registry.register(Registry.BLOCK,new Identifier("myutility","pedestal"),Pedestal);
        Registry.register(Registry.ITEM,new Identifier("myutility","pedestal"),new BlockItem(Pedestal,new FabricItemSettings().group(ItemGroup.MISC)));
        PedestalEntity = Registry.register(Registry.BLOCK_ENTITY_TYPE,"myutility:pedestalentity",BlockEntityType.Builder.create(Pedestal_Entity::new, Pedestal).build(null));
    }
}
