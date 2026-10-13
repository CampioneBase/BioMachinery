package campionebase.biomachinery.datagen;

import campionebase.biomachinery.BioMachinery;
import campionebase.biomachinery.datagen.blockstate.BioCameraBlockStateProvider;
import campionebase.biomachinery.datagen.blockstate.BioConnectorBlockStateProvider;
import campionebase.biomachinery.datagen.blockstate.HorizontalBlockStateProvider;
import campionebase.biomachinery.datagen.blockstate.NormalBlockStateProvider;
import campionebase.biomachinery.datagen.model.block.BedBlockModelProvider;
import campionebase.biomachinery.datagen.model.item.NormalItemModelProvider;
import campionebase.erosionera.datagen.lang.MultiLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(
        modid = BioMachinery.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class DataProviderEvent {
    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> registries = event.getLookupProvider();
        // 生成方块模型
        generator.addProvider(event.includeClient(), new BedBlockModelProvider(output, helper));
        // 生成物品模型
        generator.addProvider(event.includeClient(), new NormalItemModelProvider(output, helper));
        // 生成方块状态
        generator.addProvider(event.includeServer(), new HorizontalBlockStateProvider(output, helper));
        generator.addProvider(event.includeServer(), new NormalBlockStateProvider(output, helper));
        generator.addProvider(event.includeServer(), new BioConnectorBlockStateProvider(output, helper));
        generator.addProvider(event.includeServer(), new BioCameraBlockStateProvider(output, helper));
        // 生成翻译文件：只处理本模组自身的翻译来源
        for (String locale : MultiLanguageProvider.collectLanguages(BioMachinery.MODID)) {
            generator.addProvider(event.includeClient(),
                    new MultiLanguageProvider(output, BioMachinery.MODID, locale));
        }
    }
}
