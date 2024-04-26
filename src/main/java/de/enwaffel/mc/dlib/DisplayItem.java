package de.enwaffel.mc.dlib;

import de.enwaffel.mc.dlib.util.ImplClasses;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface DisplayItem {

    static DisplayItem fromStack(ItemStack itemStack, int slot, String id) {
        try {
            return ImplClasses.DISPLAY_ITEM_CLASS.getConstructor(ItemStack.class, Integer.class, String.class).newInstance(itemStack, slot, id);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    void remove();
    void setItemName(String name);
    void setLore(List<String> lore);
    void setSlot(int slot);
    void setVisible(boolean visible);

    ItemStack getItem();
    int getSlot();
    String getId();
    boolean isFillItem();
    boolean isVisible();

}
