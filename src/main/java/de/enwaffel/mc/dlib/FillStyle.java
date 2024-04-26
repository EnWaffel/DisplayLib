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

        }

    }

}
