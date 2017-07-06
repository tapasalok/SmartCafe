package com.jpmorgan.autocafe.bean;

/**
 * Created by tapas on 6/22/2017.
 */

public class MenuItem {
    String price = null;
    String venderName = null;
    String name = null;
    boolean selected = false;

    public MenuItem(String venderName, String price, String name, boolean selected) {
        super();
        this.price = price;
        this.name = name;
        this.venderName = venderName;
        this.selected = selected;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getVenderName() {
        return venderName;
    }

    public void setVenderName(String venderName) {
        this.venderName = venderName;
    }
}
