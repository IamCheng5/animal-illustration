package com.iamcheng5.pet;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CustomDialog2 extends Dialog {
    public final static int POSITIVE_BUTTON_CLICK = 1;
    public final static int NEGATIVE_BUTTON_CLICK = 2;
    private String message, positiveBtnText, negativeBtnText;
    private DialogListener dialogListener;
    private Button btnPositive, btnNegative;
    private TextView tvMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_style_2);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        tvMessage = findViewById(R.id.style2Dlg_tv_message);
        tvMessage.setText(message);
        btnPositive = findViewById(R.id.style2Dlg_btn_positive);
        btnPositive.setText(positiveBtnText);
        btnPositive.setOnClickListener(v -> {
            dialogListener.onCallBackListener(POSITIVE_BUTTON_CLICK);
            dismiss();

        });
        btnNegative = findViewById(R.id.style2Dlg_btn_negative);
        btnNegative.setText(negativeBtnText);
        btnNegative.setOnClickListener(v -> {
            dialogListener.onCallBackListener(NEGATIVE_BUTTON_CLICK);
            dismiss();
        });
    }

    public interface DialogListener {
        void onCallBackListener(int CODE);
    }

    public CustomDialog2(@NonNull Context context, String message, String positiveBtnText, String negativeBtnText, DialogListener dialogListener) {
        super(context);
        this.message = message;
        this.negativeBtnText = negativeBtnText;
        this.positiveBtnText = positiveBtnText;
        this.dialogListener = dialogListener;
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }



}
