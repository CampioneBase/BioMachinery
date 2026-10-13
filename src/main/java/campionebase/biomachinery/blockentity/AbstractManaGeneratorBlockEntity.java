package campionebase.biomachinery.blockentity;

import campionebase.biomachinery.api.IBioNutritionTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AbstractManaGeneratorBlockEntity extends AbstractBioMachineBlockEntity implements IBioNutritionTank {
    protected AbstractManaGeneratorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }
}
