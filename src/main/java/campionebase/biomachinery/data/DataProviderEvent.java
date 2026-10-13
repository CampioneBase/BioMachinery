package campionebase.biomachinery.data;

import campionebase.biomachinery.Biomachinery;
import campionebase.biomachinery.data.blockstate.BioCameraBlockStateProvider;
import campionebase.biomachinery.data.blockstate.BioConnectorBlockStateProvider;
import campionebase.biomachinery.data.blockstate.HorizontalBlockStateProvider;
import campionebase.biomachinery.data.blockstate.NormalBlockStateProvider;
import campionebase.biomachinery.data.lang.MultiLanguageProvider;
import campionebase.biomachinery.data.model.block.BedBlockModelProvider;
import campionebase.biomachinery.data.model.item.NormalItemModelProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(
        modid = Biomachinery.MODID,
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
        // 生成翻译文件
        for (String locale : MultiLanguageProvider.collectLanguages()) {
            generator.addProvider(event.includeClient(), new MultiLanguageProvider(output, locale));
        }
    }
}
