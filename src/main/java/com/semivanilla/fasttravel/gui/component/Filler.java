package com.semivanilla.fasttravel.gui.component;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bukkit.Material;

import java.util.List;
import java.util.stream.Collectors;

public class Filler {

    private final Material material;
    private final List<Integer> slots;

    public Filler(Material material, List<String> slots) {
        this.material = material;
        this.slots = slots.stream().map(Integer::parseInt).collect(Collectors.toList());
    }

    public GuiItem asItem() {
        return ItemBuilder.from(material).name(Component.empty()).asGuiItem();
    }

    public List<Integer> getSlots() {
        return slots;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Filler)) return false;

        Filler filler = (Filler) o;

        if (material != filler.material) return false;
        return slots != null ? slots.equals(filler.slots) : filler.slots == null;
    }

    @Override
    public int hashCode() {
        int result = material != null ? material.hashCode() : 0;
        result = 31 * result + (slots != null ? slots.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("material", material)
                .append("slots", slots)
                .toString();
    }
}
