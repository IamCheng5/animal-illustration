package com.iamcheng5.pet;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


public class CustomDialog1 extends Dialog {
    public final static int POSITIVE_BUTTON_CLICK = 1;
    private String message, positiveBtnText;
    private DialogListener dialogListener;
    private Button btnPositive;
    private TextView tvMessage;

    public CustomDialog1(Context context, String message, String positiveBtnText, DialogListener dialogListener) {
        super(context);
        this.message = message;
        this.positiveBtnText = positiveBtnText;
        this.dialogListener = dialogListener;

    }

    public interface DialogListener {
        void onCallBackListener(int CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_style_1);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        tvMessage = findViewById(R.id.style1Dlg_tv_message);
        tvMessage.setText(message);
        btnPositive = findViewById(R.id.style1Dlg_btn_positive);
        btnPositive.setText(positiveBtnText);
        btnPositive.setOnClickListener(v -> {
            dialogListener.onCallBackListener(POSITIVE_BUTTON_CLICK);
            dismiss();
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }



}
