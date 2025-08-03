package net.sen.yggdrasil.common.registries;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sen.yggdrasil.common.item.PortalActivatorItem;
import net.sen.yggdrasil.common.utils.ModUtils;

import java.util.Locale;
import java.util.function.Supplier;

public class YggdrasilRealmsBeyondItems {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ModUtils.getModId());

    public static final Supplier<Item> BASIC_PORTAL_ACTIVATOR = createItem("basic_portal_activator", () -> new PortalActivatorItem(0));

    private static DeferredItem<Item> createItem(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties()));
    }

    private static DeferredItem<Item> createFood(String name, FoodProperties foodProperties) {
        return ITEMS.register(name, () -> new Item(new Item.Properties().food(foodProperties)));
    }

    private static <T extends Item> DeferredItem<T> createItem(String name, Supplier<T> item) {
        return ITEMS.register(name, item);
    }

    private static Supplier<Item> createSignItem(String name, Supplier<StandingSignBlock> sign, Supplier<WallSignBlock> wallSign) {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), () -> new SignItem(new Item.Properties().stacksTo(16), sign.get(), wallSign.get()));
    }

    private static Supplier<Item> createHangingSignItem(String name, Supplier<CeilingHangingSignBlock> sign, Supplier<WallHangingSignBlock> wallSign) {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), () -> new HangingSignItem(sign.get(), wallSign.get(),new Item.Properties().stacksTo(16)));
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
