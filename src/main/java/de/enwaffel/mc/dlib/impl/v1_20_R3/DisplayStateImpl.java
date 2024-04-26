package de.enwaffel.mc.dlib.impl.v1_20_R3;

import de.enwaffel.mc.dlib.*;
import de.enwaffel.mc.dlib.util.ItemBuilder;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class DisplayStateImpl implements DisplayState {

    protected DisplayImpl _DISPLAY = null;

    private final String id;
    private String title;
    private int size;
    private final List<DisplayItem> items = new ArrayList<>();
    private final List<Integer> placeableSlots = new ArrayList<>();
    private final List<Integer> emptySlots = new ArrayList<>();
    private FillStyle fillStyle = new FillStyle.Full();
    private InteractionType interactionType = InteractionType.NONE;
    private boolean closeable = true;
    private FillStylePattern pattern = new FillStylePattern.Solid();

    public DisplayStateImpl(String id, String title, Integer size) {
        this.id = id;
        this.title = title;
        this.size = size;
    }

    @Override
    public void addItem(DisplayItem item) {
        ((DisplayItemImpl)item)._STATE = this;
        items.add(item);
    }

    @Override
    public void removeItem(DisplayItem item) {
        items.remove(item);
    }

    @Override
    public void addPlaceableSlot(int slot) {
        interactionType = InteractionType.PLAYER_INVENTORY;
        placeableSlots.add(slot);
    }

    @Override
    public void removePlaceableSlot(int slot) {
        placeableSlots.remove((Object)slot);
    }

    @Override
    public void addEmptySlot(int slot) {
        emptySlots.add(slot);
    }

    @Override
    public void removeEmptySlot(int slot) {
        emptySlots.remove((Object)slot);
    }

    @Override
    public void setFillStyle(FillStyle fillStyle) {
        this.fillStyle = fillStyle;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public void addFillItem(DisplayItem item) {
        ((DisplayItemImpl)item).fill = true;
        addItem(item);
    }

    @Override
    public void addFillItem(Material material) {
        addFillItem(DisplayItem.fromStack(ItemBuilder.newBuilder(material).displayName("§r").build(), -1, "__fill"));
    }

    @Override
    public void setInteractionType(InteractionType interactionType) {
        this.interactionType = interactionType;
    }

    @Override
    public void setCloseable(boolean closeable) {
        this.closeable = closeable;
    }

    @Override
    public void setPattern(FillStylePattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public void updateInventory() {
        _DISPLAY.updateInventory();
    }

    @Override
    public List<DisplayItem> getItems() {
        return items;
    }

    @Override
    public boolean isActive() {
        if (_DISPLAY == null) return false;
        return _DISPLAY.getCurrentState().getId().equals(id);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public List<Integer> getPlaceableSlots() {
        return placeableSlots;
    }

    @Override
    public List<Integer> getEmptySlots() {
        return emptySlots;
    }

    @Override
    public FillStyle getFillStyle() {
        return fillStyle;
    }

    @Override
    public InteractionType getInteractionType() {
        return interactionType;
    }

    @Override
    public boolean isCloseable() {
        return closeable;
    }

    @Override
    public FillStylePattern getPattern() {
        return pattern;
    }

}
