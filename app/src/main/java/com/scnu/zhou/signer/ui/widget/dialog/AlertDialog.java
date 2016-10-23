package com.scnu.zhou.signer.ui.widget.dialog;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.scnu.zhou.signer.R;


public class AlertDialog {

	private Context context;
	private android.app.AlertDialog dialog;
	private TextView titleView;
	private TextView messageView;
	private LinearLayout buttonLayout;

	public static final int BUTTON_LEFT = 0x001;
	public static final int BUTTON_RIGHT = 0x002;

	public AlertDialog(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		dialog = new android.app.AlertDialog.Builder(context).create();
		// 关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.dialog_view);
		titleView = (TextView) window.findViewById(R.id.tv_title);
		messageView = (TextView) window.findViewById(R.id.tv_message);
		buttonLayout = (LinearLayout) window.findViewById(R.id.ll_button);
	}

	public void setTitle(int resId) {
		titleView.setText(resId);
	}

	public void setTitle(String title) {
		titleView.setText(title);
	}

	public void setMessage(int resId) {
		messageView.setText(resId);
	}

	public void setMessage(String message) {
		messageView.setText(message);
	}

	/**
	 * 设置按钮
	 * 
	 * @param text
	 * @param listener
	 */
	public void setPositiveButton(String text, int pos,
			final View.OnClickListener listener) {

		Button button = new Button(context);
		LayoutParams params = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
		button.setLayoutParams(params);
		if (pos == BUTTON_LEFT) {
			button.setBackgroundResource(R.drawable.btn_dialog_left_selector);
		}
		else{
			button.setBackgroundResource(R.drawable.btn_dialog_right_selector);
		}
		button.setText(text);
		button.setTextColor(Color.parseColor("#97CC00"));
		button.setTextSize(15);
		button.setOnClickListener(listener);
		buttonLayout.addView(button);
	}

	/**
	 * 设置按钮
	 * 
	 * @param text
	 * @param listener
	 */
	public void setNegativeButton(String text, int pos,
			final View.OnClickListener listener) {

		Button button = new Button(context);
		LayoutParams params = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
		button.setLayoutParams(params);
		if (pos == BUTTON_LEFT) {
			button.setBackgroundResource(R.drawable.btn_dialog_left_selector);
		}
		else{
			button.setBackgroundResource(R.drawable.btn_dialog_right_selector);
		}
		button.setText(text);
		button.setTextColor(Color.parseColor("#555555"));
		button.setTextSize(15);
		button.setOnClickListener(listener);
		if (buttonLayout.getChildCount() > 0) {
			params.setMargins(20, 0, 0, 0);
			button.setLayoutParams(params);
			buttonLayout.addView(button, 1);
		} else {
			button.setLayoutParams(params);
			buttonLayout.addView(button);
		}
	}


	/**
	 * 显示对话框
	 */
	public void show(){
		dialog.show();
	}

	/**
	 * 关闭对话框
	 */
	public void dismiss() {
		dialog.dismiss();
	}

}
