package de.enwaffel.mc.dlib.util;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ItemBuilder {

    public static ItemBuilder newBuilder(Material material) {
        return new ItemBuilder().material(material);
    }

    private Material material;
    private String displayName;
    private boolean unbreakable;
    private String[] lore = new String[]{};
    private final HashMap<Enchantment, Integer> enchantments = new HashMap<>();
    private int customModelData;
    private final HashMap<Attribute, AttributeModifier> attributes = new HashMap<>();
    private final List<ItemFlag> flags = new ArrayList<>();
    private short durability;

    public ItemBuilder() {
    }

    public ItemBuilder(Material material) {
        this.material = material;
    }

    public ItemBuilder material(Material material) {
        this.material = material;
        return this;
    }

    public ItemBuilder displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public ItemBuilder lore(String... lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        this.enchantments.put(enchantment, level);
        return this;
    }

    public ItemBuilder model(int customModelData) {
        this.customModelData = customModelData;
        return this;
    }

    public ItemBuilder attribute(Attribute attribute, AttributeModifier modifier) {
        attributes.put(attribute, modifier);
        return this;
    }

    public ItemBuilder flags(ItemFlag... flag) {
        flags.addAll(Arrays.asList(flag));
        return this;
    }

    public ItemBuilder durability(short durability) {
        this.durability = durability;
        return this;
    }

    public ItemStack build() {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        attributes.forEach(meta::addAttributeModifier);
        meta.setDisplayName(displayName);
        meta.addItemFlags(flags.toArray(new ItemFlag[0]));
        meta.setUnbreakable(unbreakable);
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(customModelData);
        stack.setItemMeta(meta);
        stack.setDurability(durability);
        stack.addUnsafeEnchantments(enchantments);
        return stack;
    }

}

