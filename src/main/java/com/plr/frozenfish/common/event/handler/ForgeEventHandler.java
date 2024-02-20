package com.plr.frozenfish.common.event.handler;

import com.plr.frozenfish.FrozenFish;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class ForgeEventHandler {
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        final Level level = event.getLevel();
        final BlockPos pos = event.getHitVec().getBlockPos();
        if (!level.getBlockState(pos).is(BlockTags.ICE)) return;
        final Player player = event.getEntity();
        final ItemStack stack = player.getItemInHand(event.getHand());
        final Supplier<ItemStack> gen = FrozenFish.getFrozen().get(stack.getItem());
        if (gen == null) return;
        stack.shrink(1);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.BLOCKS, 1.0F, .6F + ThreadLocalRandom.current().nextFloat(.6F));
        final ItemEntity i = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), gen.get());
        level.addFreshEntity(i);
        if (!(player instanceof ServerPlayer p)) return;
        final MinecraftServer server = p.getServer();
        if (server == null) return;
        final AdvancementHolder adv = server.getAdvancements().get(new ResourceLocation(FrozenFish.MODID, "a_slow_witted_fish"));
        if (adv == null) return;
        p.getAdvancements().getOrStartProgress(adv).getRemainingCriteria().forEach(c -> p.getAdvancements().award(adv, c));
    }
}
