package com.iamcheng5.pet;

import android.app.Activity;
import android.content.Context;

import androidx.activity.OnBackPressedCallback;

public class Callback {
    public static OnBackPressedCallback getBackPressExitCallback(Context context, Activity activity) {
        final OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new CustomDialog2(context,
                        context.getResources().getString(R.string.dialog_message_quit_message),
                        context.getResources().getString(R.string.dialog_message_yes_text),
                        context.getResources().getString(R.string.dialog_message_no_text),
                        CODE -> {
                            if (CODE == CustomDialog2.POSITIVE_BUTTON_CLICK) {
                                activity.finish();
                                System.exit(0);
                            }

                        }).show();
            }
        };

        return callback;
    }

}
