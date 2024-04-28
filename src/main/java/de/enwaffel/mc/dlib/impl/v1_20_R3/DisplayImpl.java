package de.enwaffel.mc.dlib.impl.v1_20_R3;

import de.enwaffel.mc.dlib.Display;
import de.enwaffel.mc.dlib.DisplayItem;
import de.enwaffel.mc.dlib.DisplayState;
import de.enwaffel.mc.dlib.InteractionType;
import de.enwaffel.mc.dlib.impl.DLib;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DisplayImpl implements Display {

    private final List<DisplayState> states;
    private final List<Player> players = new ArrayList<>();
    private Inventory inv;
    private DisplayState curState;
    private DisplayState _lastState;
    private ItemInteractionCallback itemCallback;
    private ItemPlaceCallback placeCallback;
    private ItemTakeCallback takeCallback;
    private boolean dumpOnClose = false;
    private final List<Player> updatingPlayers = new ArrayList<>();
    private boolean dumped = false;

    public DisplayImpl(List<DisplayState> states) {
        this.states = states;
        if (states.isEmpty()) throw new IllegalArgumentException("At least one state must exist");
        try {
            DLib.DISPLAYS.put(this,
                    new DLib.DisplayImplMethods(
                            getClass().getDeclaredMethod("_onInvClick", InventoryClickEvent.class),
                            getClass().getDeclaredMethod("_onInvClose", InventoryCloseEvent.class)
                    )
            );
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        for (DisplayState state : states) {
            ((DisplayStateImpl)state)._DISPLAY = this;
        }

        curState = states.get(0);
    }

    @Override
    public void show(Player player) {
        players.add(player);
        updateInventory();
    }

    @Override
    public void hide(Player player) {
        players.remove(player);
        player.closeInventory();
    }

    @Override
    public void updateInventory() {
        boolean invChanged = false;

        if (inv == null || _lastState != curState) {
            _lastState = curState;
            invChanged = true;

            inv = Bukkit.createInventory(null, curState.getSize(), curState.getTitle());
        }

        List<Integer> usedSlots = new ArrayList<>();
        List<DisplayItem> fillItems = new ArrayList<>();

        usedSlots.addAll(curState.getPlaceableSlots());
        usedSlots.addAll(curState.getEmptySlots());

        for (DisplayItem item : curState.getItems()) {
            if (item.isFillItem()) {
                fillItems.add(item);
                continue;
            }
            usedSlots.add(item.getSlot());

            inv.setItem(item.getSlot(), NBTEditor.set(item.getItem().clone(), item.getSlot(), "display_data_slot"));
        }

        curState.getPattern().reset();
        curState.getPattern().begin(fillItems);

        curState.getFillStyle().fill(inv, curState.getSize(), usedSlots, curState.getPattern());

        if (!invChanged) return;
        for (Player player : players) {
            updatingPlayers.add(player);
            player.openInventory(inv);
            updatingPlayers.remove(player);
        }
    }

    @Override
    public void setDumpOnClose(boolean dumpOnClose) {
        this.dumpOnClose = dumpOnClose;
    }

    @Override
    public void setItemClickedCallback(ItemInteractionCallback callback) {
        itemCallback = callback;
    }

    @Override
    public void setItemPlaceCallback(ItemPlaceCallback callback) {
        placeCallback = callback;
    }

    @Override
    public void setItemTakeCallback(ItemTakeCallback callback) {
        takeCallback = callback;
    }

    @Override
    public void changeState(String id) {
        DisplayState s = null;
        for (DisplayState state : states) {
            if (state.getId().equals(id)) {
                s = state;
            }
        }
        if (s == null) return;

        curState = s;
        updateInventory();
    }

    @Override
    public void dump() {
        dumped = true;
        DLib.DISPLAYS.remove(this);
        for (Player player : players) {
            player.closeInventory();
        }
    }

    @Override
    public DisplayState getCurrentState() {
        return curState;
    }

    @Override
    public List<DisplayState> getStates() {
        return states;
    }

    public void _onInvClick(InventoryClickEvent event) {
        if (dumped) return;

        if (!event.getInventory().equals(inv)) return;
        Player player = (Player) event.getWhoClicked();
        if (!players.contains(player)) return;

        boolean isPlayerInventory = false;
        if (event.getClickedInventory() == null) {
            isPlayerInventory = false;
        } else if (!event.getClickedInventory().equals(inv)) {
            isPlayerInventory = true;
        }

        int slot = event.getSlot();

        if (curState.getPlaceableSlots().contains(slot)) {
            if (event.getCurrentItem() == null) {
                placeCallback.call(player, curState, event.getCurrentItem(), slot, event.getClick());
            } else {
                takeCallback.call(player, curState, slot, event.getClick());
            }
            return;
        }

        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null) {
            event.setCancelled(!isPlayerInventory || !curState.getInteractionType().equals(InteractionType.PLAYER_INVENTORY));
            return;
        }

        DisplayItem item = getItemFromSlot(slot);
        if (item == null) {
            event.setCancelled(!isPlayerInventory || !curState.getInteractionType().equals(InteractionType.PLAYER_INVENTORY));
            return;
        }

        event.setCancelled(true);

        if (itemCallback != null) {
            itemCallback.call(player, curState, item, event.getClick());
        }
    }

    public void _onInvClose(InventoryCloseEvent event) {
        if (dumped) return;

        if (!event.getInventory().equals(inv)) return;
        Player player = (Player) event.getPlayer();
        if (!players.contains(player)) return;
        if (updatingPlayers.contains(player)) return;
        if (!curState.isCloseable()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(DLib.PLUGIN, () -> {
                updatingPlayers.add(player);
                player.openInventory(inv);
                updatingPlayers.remove(player);
            }, 1);
            return;
        }

        players.remove(player);

        if (players.isEmpty()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(DLib.PLUGIN, this::dump, 1);
        }
    }

    private DisplayItem getItemFromSlot(int slot) {
        for (DisplayItem item : curState.getItems()) {
            if (item.isFillItem()) continue;
            if (item.getSlot() == slot) return item;
        }
        return null;
    }

}
