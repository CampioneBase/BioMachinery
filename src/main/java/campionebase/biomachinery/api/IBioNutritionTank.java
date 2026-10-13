package campionebase.biomachinery.api;

import campionebase.biomachinery.registry.BioMachineTypes;
import org.jetbrains.annotations.NotNull;

public interface IBioNutritionTank extends IBioMachine {
    @Override
    default @NotNull BioMachineType<? extends IBioMachine> getMachineType(){
        return BioMachineTypes.NUTRITION_TANK.get();
    }
}
