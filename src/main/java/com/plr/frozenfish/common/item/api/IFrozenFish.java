package com.plr.frozenfish.common.item.api;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

public interface IFrozenFish {
    ItemStack getOriginalFishStack();

    default boolean willConsumeNaturally(Biome biome, BlockPos pos) {
        return biome.shouldSnowGolemBurn(pos);
    }

    default boolean willRecoverNaturally(Biome biome, BlockPos pos) {
        return biome.coldEnoughToSnow(pos);
    }

    default boolean attack(ItemStack stack, LivingEntity attacker) {
        stack.hurtAndBreak(2, attacker, this::dropDefrosted);
        return true;
    }

    default boolean mine(Level level, BlockState state, BlockPos pos, ItemStack stack, LivingEntity user) {
        if (level.isClientSide || state.getDestroySpeed(level, pos) == 0.0F) return true;
        if (!state.is(BlockTags.ICE)) stack.hurtAndBreak(3, user, this::dropDefrosted);
        else {
            stack.setDamageValue(0);
            level.playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.BLOCKS, 1.0F, .6F + user.getRandom().nextFloat(.6F));
        }
        return true;
    }

    default void dropDefrosted(LivingEntity e) {
        final ItemEntity ie = new ItemEntity(e.level, e.getX(), e.getY(), e.getZ(), getOriginalFishStack());
        e.level.playSound(null, e.getX(), e.getY(), e.getZ(),
                SoundEvents.GLASS_BREAK, SoundSource.NEUTRAL, 1.0F, .6F + e.getRandom().nextFloat(.6F));
        e.level.addFreshEntity(ie);
    }

    default void tick(Level level, Entity entity, ItemStack stack) {
        if (level.isClientSide) return;
        if (!(entity instanceof LivingEntity l)) return;
        if (level.getGameTime() % 100 != 0) return;
        final BlockPos pos = l.blockPosition();
        final Biome biome = level.getBiome(pos).value();
        if (entity.isEyeInFluid(FluidTags.LAVA)) stack.hurtAndBreak(50, l, this::dropDefrosted);
        else if (entity.isEyeInFluid(FluidTags.WATER)) stack.hurtAndBreak(10, l, this::dropDefrosted);
        else if (willConsumeNaturally(biome, pos)) stack.hurtAndBreak(5, l, this::dropDefrosted);
        else if (willRecoverNaturally(biome, pos))
            stack.hurt(-1, l.getRandom(), (l instanceof ServerPlayer s) ? s : null);
    }

    default boolean repair(Level level, Player player, ItemStack stack, BlockPos pos) {
        if (!stack.isDamaged()) return false;
        if (!level.getBlockState(pos).is(BlockTags.ICE)) return false;
        stack.setDamageValue(0);
        if (player != null) level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.BLOCKS, 1.0F, .6F + player.getRandom().nextFloat(.6F));
        return true;
    }
}
