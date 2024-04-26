package de.enwaffel.mc.dlib;

import java.util.List;

public interface FillStylePattern {
    void begin(List<DisplayItem> items);
    DisplayItem next(int slot);
    void reset();

    class Solid implements FillStylePattern {

        private List<DisplayItem> items;

        @Override
        public void begin(List<DisplayItem> items) {
            this.items = items;
        }

        @Override
        public DisplayItem next(int slot) {
            if (!items.isEmpty()) {
                return items.get(0);
            }
            return null;
        }

        @Override
        public void reset() {
            items = null;
        }

    }
}
