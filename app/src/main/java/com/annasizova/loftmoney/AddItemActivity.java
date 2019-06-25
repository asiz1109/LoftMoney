package com.annasizova.loftmoney;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddItemActivity extends AppCompatActivity {

    private EditText titleEdit;
    private EditText priceEdit;
    private Button addButton;

    private String title;
    private String price;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        titleEdit = findViewById(R.id.title_edittext);
        priceEdit = findViewById(R.id.price_edittext);
        addButton = findViewById(R.id.add_button);

        titleEdit.addTextChangedListener(new MyTextWatcher(titleEdit));
        priceEdit.addTextChangedListener(new MyTextWatcher(priceEdit));
    }

    private void changeButtonTextColor (){
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(price)) {
            addButton.setTextColor(ContextCompat.getColor(this, R.color.add_button_text_color));
        } else {
            addButton.setTextColor(ContextCompat.getColor(this, R.color.add_button_color_inactive));
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher (View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.title_edittext:
                    title = s.toString();
                    changeButtonTextColor();
                    break;
                case R.id.price_edittext:
                    price = s.toString();
                    changeButtonTextColor();
                    break;
            }

        }
    }

}
