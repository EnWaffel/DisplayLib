package de.enwaffel.mc.dlib;

import org.bukkit.inventory.Inventory;

import java.util.List;

public interface FillStyle {

    void fill(Inventory inv, int size, List<Integer> usedSlots, FillStylePattern pattern);

    class Full implements FillStyle {

        @Override
        public void fill(Inventory inv, int size, List<Integer> usedSlots, FillStylePattern pattern) {
            for (int i = 0; i < size; i++) {
                if (!usedSlots.contains(i)) {
                    inv.setItem(i, pattern.next(i).getItem());
                }
            }
        }

    }

    class Border implements FillStyle {

        @Override
        public void fill(Inventory inv, int size, List<Integer> usedSlots, FillStylePattern pattern) {
            int rows = size / 9;

            // Top
            for (int i = 0; i < 9; i++) {
                DisplayItem item = pattern.next(i);
                if (usedSlots.contains(i)) continue;
                inv.setItem(i, item.getItem());
            }

            if (rows < 2) return;

            // Right
            for (int i = 1; i < rows; i++) {
                int slot = (9 * i) + 8;
                DisplayItem item = pattern.next(slot);
                if (usedSlots.contains(slot)) continue;
                inv.setItem(slot, item.getItem());
            }

            // Bottom
            for (int i = (rows * 9) - 2; i > (rows * 9) - 9; i--) {
                DisplayItem item = pattern.next(i);
                if (usedSlots.contains(i)) continue;
                inv.setItem(i, item.getItem());
            }

            // Left
            for (int i = rows - 1; i > 0; i--) {
                int slot = i * 9;
                DisplayItem item = pattern.next(slot);
                if (usedSlots.contains(slot)) continue;
                inv.setItem(slot, item.getItem());
            }
        }

    }

}
