package com.annasizova.loftmoney;

public enum FragmentType {

    expense(R.color.dark_sky_blue),
    income(R.color.add_button_text_color);

    private int priceColor;

    FragmentType(int priceColor) {
        this.priceColor = priceColor;
    }

    public int getPriceColor() {
        return priceColor;
    }
}
