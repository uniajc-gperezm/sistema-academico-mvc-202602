package com.uniajc.vista;

/**
 * Representa una opción de ComboBox con ID y etiqueta.
 * Permite guardar la clave foránea y mostrar un texto entendible al usuario.
 */
public class ItemCombo {
    public final int id;
    public final String label;

    public ItemCombo(int id, String label) {
        this.id = id;
        this.label = label;
    }

    @Override
    public String toString() {
        return label + " (ID: " + id + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemCombo)) return false;
        return id == ((ItemCombo) o).id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}