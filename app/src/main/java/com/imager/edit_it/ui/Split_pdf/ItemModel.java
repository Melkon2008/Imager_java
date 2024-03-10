package com.imager.edit_it.ui.Split_pdf;

public class ItemModel {
    private String itemName;
    private String itemDetails;

    public ItemModel(String itemName, String itemDetails) {
        this.itemName = itemName;
        this.itemDetails = itemDetails;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(String itemDetails) {
        this.itemDetails = itemDetails;
    }
}
