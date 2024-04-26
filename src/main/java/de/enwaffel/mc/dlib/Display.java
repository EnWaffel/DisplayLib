package de.enwaffel.mc.dlib;

import de.enwaffel.mc.dlib.util.ImplClasses;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public interface Display {

    static Builder newBuilder() {
        return new Builder();
    }

    void setDumpOnClose(boolean dumpOnClose);
    void setItemClickedCallback(ItemInteractionCallback callback);
    void setItemPlaceCallback(ItemPlaceCallback callback);
    void setItemTakeCallback(ItemTakeCallback callback);
    void show(Player player);

    void hide(Player player);
    void updateInventory();
    void changeState(String id);
    void dump();

    DisplayState getCurrentState();
    List<DisplayState> getStates();

    class Builder {

        private final List<DisplayState> states = new ArrayList<>();

        public Builder states(DisplayState... states) {
            this.states.addAll(List.of(states));
            return this;
        }

        public Display build() {
            try {
                return ImplClasses.DISPLAY_CLASS.getConstructor(List.class).newInstance(states);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

    }

    interface ItemInteractionCallback {
        void call(Player player, DisplayState state, DisplayItem item, ClickType clickType);
    }

    interface ItemPlaceCallback {
        void call(Player player, DisplayState state, ItemStack item, int slot, ClickType clickType);
    }

    interface ItemTakeCallback {
        void call(Player player, DisplayState state, int slot, ClickType clickType);
    }

}
