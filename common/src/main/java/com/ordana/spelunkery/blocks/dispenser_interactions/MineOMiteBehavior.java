package com.ordana.spelunkery.blocks.dispenser_interactions;

import com.ordana.spelunkery.entities.ThrownMineomiteEntity;
import com.ordana.spelunkery.entities.ThrownPebbleEntity;
import com.ordana.spelunkery.reg.ModItems;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Position;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MineOMiteBehavior extends ProjectileBehavior {

    protected MineOMiteBehavior() {
        super(ModItems.MINEOMITE.get());
    }

    @Override
    protected Projectile getProjectileEntity(BlockSource source, Position position, ItemStack stackIn) {
        var entity = new ThrownMineomiteEntity(source.getLevel(), position.x(), position.y(), position.z());
        entity.setItem(stackIn.copyWithCount(1));
        return entity;
    }
}