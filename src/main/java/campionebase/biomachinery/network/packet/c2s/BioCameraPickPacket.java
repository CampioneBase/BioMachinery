package campionebase.biomachinery.network.packet.c2s;

import campionebase.biomachinery.api.IBioCamera;
import campionebase.biomachinery.api.IBioController;
import campionebase.biomachinery.inventory.BioControllerMenu;
import campionebase.biomachinery.network.BioCameraHelper;
import campionebase.biomachinery.network.BioCameraManager;
import campionebase.biomachinery.network.BioMachineryNetwork;
import campionebase.biomachinery.network.BioMachineryService;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record BioCameraPickPacket(BlockPos camera, float yaw, float pitch, boolean clear) {
    public static void encode(BioCameraPickPacket packet, FriendlyByteBuf buf){
        buf.writeBlockPos(packet.camera);
        buf.writeFloat(packet.yaw);
        buf.writeFloat(packet.pitch);
        buf.writeBoolean(packet.clear);
    }

    public static BioCameraPickPacket decode(FriendlyByteBuf buf){
        return new BioCameraPickPacket(
                buf.readBlockPos(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readBoolean()
        );
    }

    public static void handle(BioCameraPickPacket packet, Supplier<NetworkEvent.Context> contextSupplier){
        NetworkEvent.Context context = contextSupplier.get();
        ServerPlayer sender = context.getSender();
        if (sender == null) return;
        context.enqueueWork(() -> {
            ServerLevel level = sender.serverLevel();
            if (!(sender.containerMenu instanceof BioControllerMenu menu) ||
                    !(level.getBlockEntity(packet.camera) instanceof IBioCamera camera) ||
                    !BioCameraManager.get(level).tryRenewCamera(packet.camera, sender.getUUID(), packet.yaw, packet.pitch)
            ) {
                BioMachineryNetwork.LOGGER.warn(
                        "Preventing {} from picking via bio-camera[{}]: bio-camera or menu invalid.",
                        sender.getName().getString(), packet.camera.toShortString()
                );
                return;
            }

            BioCameraManager.CameraOccupation occupation = BioCameraManager.get(level).getCameraOwner(packet.camera);
            if (occupation == null || !occupation.getPlayerUUID().equals(sender.getUUID())) {
                BioMachineryNetwork.LOGGER.warn(
                        "Preventing {} from picking via bio-camera[{}]: player is not current user.",
                        sender.getName().getString(), packet.camera.toShortString()
                );
                return;
            }
            if (!menu.getBlockPos().equals(occupation.getController())) {
                BioMachineryNetwork.LOGGER.warn(
                        "Preventing {} from picking via bio-camera[{}]: controller mismatch.",
                        sender.getName().getString(), packet.camera.toShortString()
                );
                return;
            }
            if (!(level.getBlockEntity(occupation.getController()) instanceof IBioController controller) ||
                    controller.getUser() == null ||
                    !controller.getUser().getUUID().equals(sender.getUUID()) ||
                    !BioMachineryService.isConnected(level, controller.getBlockPos(), packet.camera)
            ) {
                BioMachineryNetwork.LOGGER.warn(
                        "Preventing {} from picking via bio-camera[{}]: controller disconnected or invalid.",
                        sender.getName().getString(), packet.camera.toShortString()
                );
                return;
            }

            if (packet.clear) {
                controller.setTarget(null);
                return;
            }

            HitResult result = BioCameraHelper.pick(level, camera, packet.yaw, packet.pitch);
            if (result.getType() == HitResult.Type.ENTITY && result instanceof EntityHitResult entityHit) {
                Entity entity = entityHit.getEntity();
                if (entity.isAlive() && entity.isPickable() && !entity.isSpectator()) {
                    controller.setTarget(entity);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
