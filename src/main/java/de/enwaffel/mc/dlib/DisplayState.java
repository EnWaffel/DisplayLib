package de.enwaffel.mc.dlib;

import de.enwaffel.mc.dlib.util.ImplClasses;
import org.bukkit.Material;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface DisplayState {

    static DisplayState newState(String id, String title, int size) {
        try {
            return ImplClasses.DISPLAY_STATE_CLASS.getConstructor(String.class, String.class, Integer.class).newInstance(id, title, size);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    void addItem(DisplayItem item);
    void removeItem(DisplayItem item);
    void addPlaceableSlot(int slot);
    void removePlaceableSlot(int slot);
    void addEmptySlot(int slot);
    void removeEmptySlot(int slot);
    void setFillStyle(FillStyle fillStyle);
    void setTitle(String title);
    void setSize(int size);
    void addFillItem(DisplayItem item);
    void addFillItem(Material material);
    void setInteractionType(InteractionType interactionType);
    void setCloseable(boolean closeable);
    void setPattern(FillStylePattern pattern);

    void updateInventory();

    List<DisplayItem> getItems();
    boolean isActive();
    String getId();
    String getTitle();
    int getSize();
    List<Integer> getPlaceableSlots();
    List<Integer> getEmptySlots();
    FillStyle getFillStyle();
    InteractionType getInteractionType();
    boolean isCloseable();
    FillStylePattern getPattern();

}
