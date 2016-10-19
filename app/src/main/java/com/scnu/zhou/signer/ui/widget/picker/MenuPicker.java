package com.scnu.zhou.signer.ui.widget.picker;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.adapter.MenuPickerAdapter;

import java.util.List;

/**
 * Created by zhou on 16/9/7.
 */
public abstract class MenuPicker {

    private String select;
    private Dialog dialog;

    public MenuPicker(){
        select = "取消";
    }

    public void show(Activity activity, final List<String> items){

        View view = activity.getLayoutInflater().inflate(R.layout.layout_menu_picker,
                null);

        // 初始化item项
        LinearLayout ll_menu_item = (LinearLayout) view.findViewById(R.id.ll_menu_item);
        MenuPickerAdapter adapter = new MenuPickerAdapter(activity, items);
        for (int i=0; i<adapter.getSize(); i++){
            View child = adapter.getView(i);
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select = ((Button) v).getText().toString();
                    execute();
                }
            });
            ll_menu_item.addView(child);
        }

        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog = new Dialog(activity,
                R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = activity.getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }

    public void dismiss(){
        dialog.dismiss();
    }

    public String getSelect(){
        return select;
    }

    public abstract void execute();
}
