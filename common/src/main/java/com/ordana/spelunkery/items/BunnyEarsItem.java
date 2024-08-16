package com.ordana.spelunkery.items;

import com.ordana.spelunkery.reg.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BunnyEarsItem extends BlockItem implements Equipable {

    public BunnyEarsItem(Properties properties) {
        super(ModBlocks.BUNNY_EARS.get(), properties);
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }

    public InteractionResult useOn(UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, InteractionHand hand) {
        return swapWithEquipmentSlot(this, level, player, hand);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, Entity entity, int slotId, boolean isSelected) {
        if (entity instanceof LivingEntity lEntity) {
            if (lEntity.getItemBySlot(EquipmentSlot.HEAD).getItem() == this) {
                lEntity.addEffect(new MobEffectInstance(MobEffects.JUMP, 2, 1, true, false, true));
            }
        }
    }
}