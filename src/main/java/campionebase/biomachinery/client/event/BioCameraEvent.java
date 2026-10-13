package campionebase.biomachinery.client.event;

import campionebase.biomachinery.BioMachinery;
import campionebase.biomachinery.api.IBioCamera;
import campionebase.biomachinery.inventory.BioControllerMenu;
import campionebase.biomachinery.mixin.CameraAccessor;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = BioMachinery.MODID,
        value = Dist.CLIENT
)
public class BioCameraEvent {
    // 核心：设置摄像机位置与视角
    @SubscribeEvent
    public static void setCameraPositionAndAngles(ViewportEvent.ComputeCameraAngles event){
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;
        if (!(player.containerMenu instanceof BioControllerMenu menu)) return;

        IBioCamera bioCamera = menu.getMachine();
        if (bioCamera == null) return;

        Camera camera = event.getCamera();
        float yaw = menu.cameraYaw;
        float pitch = menu.cameraPitch;
        ((CameraAccessor) camera).invokeSetPosition(bioCamera.getCameraPosition(yaw, pitch));
        event.setYaw(yaw);
        event.setPitch(pitch);
    }

    // 取消手臂渲染
    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;
        if (player.containerMenu instanceof BioControllerMenu menu && menu.getMachine() != null) {
            event.setCanceled(true);
        }
    }

    // 处理铭牌绘制
    @SubscribeEvent
    public static void onRenderNameTag(RenderNameTagEvent event){
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        if (!(player.containerMenu instanceof BioControllerMenu menu)) return;
        IBioCamera bioCamera = menu.getMachine();
        if (bioCamera == null) return;

        Camera camera = mc.gameRenderer.getMainCamera();
        ((CameraAccessor) camera).invokeSetRotation(menu.cameraYaw, menu.cameraPitch);
    }
}
