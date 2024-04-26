package de.enwaffel.mc.dlib.impl.v1_20_R3;

import de.enwaffel.mc.dlib.DisplayItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class DisplayItemImpl implements DisplayItem {

    protected DisplayStateImpl _STATE;

    private final ItemStack itemStack;
    private int slot;
    public boolean fill;
    private final String id;
    private boolean visible = true;

    public DisplayItemImpl(ItemStack itemStack, Integer slot, String id) {
        this.itemStack = itemStack;
        this.slot = slot;
        this.id = id;
    }

    @Override
    public void remove() {
        _STATE.removeItem(this);
        _STATE.updateInventory();
    }

    @Override
    public void setItemName(String name) {
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        itemStack.setItemMeta(meta);
    }

    @Override
    public void setLore(List<String> lore) {
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }

    @Override
    public void setSlot(int slot) {
        this.slot = slot;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public ItemStack getItem() {
        return itemStack;
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isFillItem() {
        return fill;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

}
