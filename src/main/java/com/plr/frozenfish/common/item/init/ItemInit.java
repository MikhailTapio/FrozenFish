package com.plr.frozenfish.common.item.init;

import com.plr.frozenfish.FrozenFish;
import com.plr.frozenfish.common.item.impl.BigFrozenFishItem;
import com.plr.frozenfish.common.item.impl.SmallFrozenFishItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FrozenFish.MODID);

    public static final RegistryObject<Item> FROZEN_COD;
    public static final RegistryObject<Item> FROZEN_SALMON;
    public static final RegistryObject<Item> FROZEN_TROPICAL_FISH;
    public static final RegistryObject<Item> FROZEN_PUFFERFISH;

    static {
        FROZEN_COD = addFrozen("frozen_cod", Items.COD.getDefaultInstance(), false);
        FROZEN_SALMON = addFrozen("frozen_salmon", Items.SALMON.getDefaultInstance(), false);
        FROZEN_TROPICAL_FISH = addFrozen("frozen_tropical_fish", Items.TROPICAL_FISH.getDefaultInstance(), false);
        FROZEN_PUFFERFISH = addFrozen("frozen_pufferfish", Items.PUFFERFISH.getDefaultInstance(), true);
    }

    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static RegistryObject<Item> addFrozen(String name, ItemStack original, boolean big) {
        final RegistryObject<Item> obj = ITEMS.register(name, () -> big ? new BigFrozenFishItem(original) : new SmallFrozenFishItem(original));
        FrozenFish.addFrozenRef(original.getItem(), () -> obj.get().getDefaultInstance());
        return obj;
    }
}
