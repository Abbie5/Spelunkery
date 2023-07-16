package com.ordana.spelunkery;

import com.ordana.spelunkery.entities.PrimedMineomiteEntityRenderer;
import com.ordana.spelunkery.items.HandheldCompactorItem;
import com.ordana.spelunkery.items.MagneticCompassItem;
import com.ordana.spelunkery.items.magnetic_compass.MagneticCompassItemPropertyFunction;
import com.ordana.spelunkery.reg.ModBlocks;
import com.ordana.spelunkery.reg.ModEntities;
import com.ordana.spelunkery.reg.ModItems;
import net.mehvahdjukaar.moonlight.api.platform.ClientPlatformHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

import java.util.Locale;

public class SpelunkeryClient {

    public static void init() {
        ClientPlatformHelper.addEntityRenderersRegistration(SpelunkeryClient::registerEntityRenderers);
    }

    public static void setup() {

        ClientPlatformHelper.registerRenderType(ModBlocks.PORTAL_FLUID.get(), RenderType.translucent());
        ClientPlatformHelper.registerRenderType(ModBlocks.PORTAL_CAULDRON.get(), RenderType.translucent());
        ClientPlatformHelper.registerRenderType(ModBlocks.MINEOMITE.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.SALT_LAMP.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.SALT.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.WOODEN_RAIL.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.ROPE_LADDER.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.TANGLE_ROOTS.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.TANGLE_ROOTS_PLANT.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.SPOROPHYTE.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.TALL_SPOROPHYTE.get(), RenderType.cutout());

        ClientPlatformHelper.registerRenderType(ModBlocks.CONK_FUNGUS.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.INKCAP_MUSHROOM.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.WHITE_INKCAP_MUSHROOM.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.PHOSPHOR_FUNGUS.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.MUSHGLOOM.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.MILLY_BUBCAP.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.PORTABELLA.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.CRIMINI.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.BUTTON_MUSHROOM.get(), RenderType.cutout());

        ClientPlatformHelper.registerRenderType(ModBlocks.POTTED_INKCAP_MUSHROOM.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.POTTED_WHITE_INKCAP_MUSHROOM.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.POTTED_PHOSPHOR_FUNGUS.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.POTTED_MUSHGLOOM.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.POTTED_MILLY_BUBCAP.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.POTTED_PORTABELLA.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.POTTED_CRIMINI.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.POTTED_BUTTON_MUSHROOM.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(ModBlocks.POTTED_SPOROPHYTE.get(), RenderType.cutout());

        ClientPlatformHelper.registerRenderType(ModBlocks.NEPHRITE_SPOUT.get(), RenderType.cutout());

        ClientPlatformHelper.registerItemProperty(ModItems.DEPTH_GAUGE.get(), Spelunkery.res("depth"),
                (stack, world, entity, seed) -> entity != null ? (((float) entity.getBlockY() + 64) / 384) : 0);

        ClientPlatformHelper.registerItemProperty(ModItems.NEPHRITE_CHARM.get(), Spelunkery.res("charge"),
                (stack, world, entity, seed) -> stack.getTag() != null ? (stack.getTag().getInt("xp") / 1395f) : 0);

        ClientPlatformHelper.registerItemProperty(ModItems.ITEM_MAGNET.get(), Spelunkery.res("active"),
                (stack, world, entity, seed) -> stack.getTag() != null && stack.getTag().getBoolean("active") ? 0.5f : 0);

        ClientPlatformHelper.registerItemProperty(ModItems.HANDHELD_COMPACTOR.get(), Spelunkery.res("Mode"),
                (stack, world, entity, seed) -> stack.getTag() != null ? (HandheldCompactorItem.CompressionMode.VALUES[(HandheldCompactorItem.getMode(stack).ordinal())].ordinal() / 4f) : 0f);

        ClientPlatformHelper.registerItemProperty(ModItems.MAGNETIC_COMPASS.get(), Spelunkery.res("angle"),
                new MagneticCompassItemPropertyFunction(((clientLevel, itemStack, entity) -> MagneticCompassItem.isMagnetiteNearby(itemStack) ? MagneticCompassItem.getMagnetitePos(itemStack.getOrCreateTag()) : MagneticCompassItem.getNorthPosition(clientLevel))));

        ClientPlatformHelper.registerItemProperty(ModItems.SALT_BUCKET.get(), Spelunkery.res("salt"),
                (stack, world, entity, seed) -> stack.getTag() != null ? (stack.getTag().getInt("salt") / 8f) : 0);

        ClientPlatformHelper.registerItemProperty(ModItems.PARACHUTE.get(), Spelunkery.res("active"),
                (stack, world, entity, seed) -> stack.getTag() != null ? (stack.getTag().getBoolean("active") ? 0.5f : 0) : 0);

        ClientPlatformHelper.registerItemProperty(ModItems.PARACHUTE.get(), Spelunkery.res("used"),
                (stack, world, entity, seed) -> stack.getTag() != null ? (stack.getTag().getBoolean("used") ? 0.5f : 0) : 0);

    }

    private static void registerEntityRenderers(ClientPlatformHelper.EntityRendererEvent event) {
        event.register(ModEntities.GLOWSTICK.get(), context -> new ThrownItemRenderer<>(context, 1, false));
        event.register(ModEntities.MINEOMITE.get(), context -> new ThrownItemRenderer<>(context, 1, false));
        event.register(ModEntities.THROWN_PRIMED_MINEOMITE.get(), context -> new ThrownItemRenderer<>(context, 1, false));
        event.register(ModEntities.PRIMED_MINEOMITE.get(), PrimedMineomiteEntityRenderer::new);
        event.register(ModEntities.EGGPLANT.get(), context -> new ThrownItemRenderer<>(context, 1, false));
    }

}