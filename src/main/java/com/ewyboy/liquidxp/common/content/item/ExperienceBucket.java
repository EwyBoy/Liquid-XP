package com.ewyboy.liquidxp.common.content.item;

import com.ewyboy.liquidxp.LiquidXP;
import com.ewyboy.liquidxp.common.register.Register;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ExperienceBucket extends BucketItem {

    public ExperienceBucket() {
        super(() -> Register.FLUID.XP, new Item.Properties().group(LiquidXP.itemGroup).maxStackSize(1).containerItem(Items.BUCKET));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public boolean tryPlaceContainedLiquid(@Nullable PlayerEntity player, World worldIn, BlockPos posIn, @Nullable BlockRayTraceResult rayTrace) {
        return false;
    }

}
