package com.plr.frozenfish;

import com.google.common.collect.ImmutableMap;
import com.plr.frozenfish.common.item.init.ItemInit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Mod(FrozenFish.MODID)
public class FrozenFish {
    public static final String MODID = "frozenfish";

    public FrozenFish() {
        ItemInit.init();
    }

    private static final Map<Item, Supplier<ItemStack>> frozen = new HashMap<>();

    public static Map<Item, Supplier<ItemStack>> getFrozen() {
        return frozen;
    }

    public static Map<Item, Supplier<ItemStack>> getFrozenCopied() {
        return ImmutableMap.copyOf(frozen);
    }

    public static void addFrozenRef(Item original, Supplier<ItemStack> frozen$) {
        frozen.put(original, frozen$);
    }
}
