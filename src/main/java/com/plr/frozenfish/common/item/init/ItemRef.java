package com.plr.frozenfish.common.item.init;

import com.plr.frozenfish.FrozenFish;
import com.plr.frozenfish.common.item.impl.BigFrozenFishItem;
import com.plr.frozenfish.common.item.impl.SmallFrozenFishItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;
import java.util.function.Supplier;

public enum ItemRef implements Supplier<Item> {
    FROZEN_COD(Items.COD.getDefaultInstance(), false),
    FROZEN_SALMON(Items.SALMON.getDefaultInstance(), false),
    FROZEN_TROPICAL_FISH(Items.TROPICAL_FISH.getDefaultInstance(), false),
    FROZEN_PUFFERFISH(Items.PUFFERFISH.getDefaultInstance(), true);

    ItemRef(ItemStack original, boolean big) {
        this.reg = addFrozen(this.name().toLowerCase(Locale.ROOT), original, big);
    }

    private final RegistryObject<Item> reg;

    @Override
    public Item get() {
        return reg.get();
    }

    public static void init() {
        FrozenFish.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static RegistryObject<Item> addFrozen(String name, ItemStack original, boolean big) {
        final RegistryObject<Item> obj = FrozenFish.ITEMS.register(name, () -> big ? new BigFrozenFishItem(original) : new SmallFrozenFishItem(original));
        FrozenFish.addFrozenRef(original.getItem(), () -> obj.get().getDefaultInstance());
        return obj;
    }

}
