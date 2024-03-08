package com.roseno.curbcrime.preference;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;

import java.util.regex.Pattern;

public class MessageTargetPreference extends EditTextPreference implements DialogInterface.OnShowListener, View.OnClickListener {

    AlertDialog mDialog;
    EditText mTargetEditText;

    public MessageTargetPreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getDialogTitle())
                .setMessage(getDialogMessage())
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

        FrameLayout container = new FrameLayout(getContext());
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        params.leftMargin = 50;
        params.rightMargin = 50;

        mTargetEditText = new EditText(getContext());
        mTargetEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        mTargetEditText.setText(getText());
        mTargetEditText.requestFocus();
        mTargetEditText.setShowSoftInputOnFocus(true);
        mTargetEditText.setLayoutParams(params);

        container.addView(mTargetEditText);
        builder.setView(container);

        mDialog = builder.create();
        mDialog.setOnShowListener(this);

        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mDialog.show();
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(this);
        mDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(this);
    }

    /**
     * 버튼 클릭 이벤트 (확인 버튼에서 다이얼로그가 꺼지는 것을 방지하기 위해 다시 만듬)
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int buttonId = v.getId();

        if (buttonId == mDialog.getButton(AlertDialog.BUTTON_POSITIVE).getId()) {
            Pattern phonePattern = Pattern.compile("\\d{10,11}");

            String phoneNumber = mTargetEditText.getText().toString();
            if (phonePattern.matcher(phoneNumber).matches()) {
                setText(phoneNumber);

                mDialog.dismiss();
            } else {
                Toast.makeText(getContext(), "올바른 핸드폰 번호 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
            }
        } else if (buttonId == mDialog.getButton(AlertDialog.BUTTON_NEGATIVE).getId()) {
            mDialog.cancel();
        }
    }
}
