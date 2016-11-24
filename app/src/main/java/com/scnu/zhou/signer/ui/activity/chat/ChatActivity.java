package com.scnu.zhou.signer.ui.activity.chat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.adapter.listview.ChatMessageAdapter;
import com.scnu.zhou.signer.component.bean.chat.ChatMessage;
import com.scnu.zhou.signer.component.bean.chat.Emotion;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.cache.TimeCache;
import com.scnu.zhou.signer.component.cache.UserCache;
import com.scnu.zhou.signer.component.util.density.DensityUtil;
import com.scnu.zhou.signer.component.util.emotion.ExpressionUtil;
import com.scnu.zhou.signer.component.util.emotion.XmlUtil;
import com.scnu.zhou.signer.presenter.chat.ChatPresenter;
import com.scnu.zhou.signer.presenter.chat.IChatPresenter;
import com.scnu.zhou.signer.ui.activity.base.BaseSlideActivity;
import com.scnu.zhou.signer.ui.widget.scrollview.IMMListenerScrollView;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;
import com.scnu.zhou.signer.view.chat.IChatView;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by zhou on 16/11/7.
 */
public class ChatActivity extends BaseSlideActivity implements IChatView, AbsListView.OnScrollListener,
        View.OnKeyListener, OnItemClickListener, TextWatcher{

    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.ll_return) LinearLayout ll_return;

    @Bind(R.id.lv_chat) ListView lv_chat;
    @Bind(R.id.ll_no_message) LinearLayout ll_no_message;

    @Bind(R.id.et_content) EditText et_content;
    @Bind(R.id.scrollContent) IMMListenerScrollView scrollContent;

    @Bind(R.id.ll_emotion) LinearLayout ll_emotion;
    @Bind(R.id.btn_emotion) Button btn_emotion;
    @Bind(R.id.btn_send) Button btn_send;
    @Bind(R.id.gv_emotions) GridView gv_emotions;

    private List<ChatMessage> mData;
    private ChatMessageAdapter adapter;

    private IChatPresenter presenter;

    private String courseId;
    private String role;
    private int page = 0;
    private boolean isRefresh = true;
    private boolean isCompleted = false;

    private View headerView = null;
    private View footerView = null;

    private int headerHeight = 0;

    private static final int STATE_EMOTION = 0x001;
    private static final int STATE_KEYBOARD = 0x002;
    private int input_state = STATE_KEYBOARD;


    private List<Emotion> emotions = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);

        initView();
        initData();
    }


    @Override
    public void initView(){

        tv_title.setText(getIntent().getStringExtra("courseName"));
        ll_return.setVisibility(View.VISIBLE);

        lv_chat.setOnScrollListener(this);
        //et_content.setOnKeyListener(this);
        et_content.addTextChangedListener(this);

        // 添加头布局
        headerView = LayoutInflater.from(this).inflate(R.layout.listview_loadingview, null);
        headerView.measure(0, 0);
        headerHeight = headerView.getMeasuredHeight();
        headerView.setPadding(0, - headerHeight, 0, 0);
        lv_chat.addHeaderView(headerView);

        // 添加底部布局
        footerView = new View(this);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, 40);
        footerView.setLayoutParams(params);
        lv_chat.addFooterView(footerView);

        // 监听软键盘弹出事件
        scrollContent.setListener(new IMMListenerScrollView.InputWindowListener() {
            @Override
            public void show() {
                Log.e("chat", "input window show");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn_emotion.setBackgroundResource(R.drawable.icon_emotion);
                        ll_emotion.setVisibility(View.GONE);
                        input_state = STATE_KEYBOARD;
                        lv_chat.setSelection(lv_chat.getBottom());
                    }
                }, 10);

            }

            @Override
            public void hidden() {

            }
        });


        // 初始化表情布局
        initEmotionView();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
    }

    @Override
    public void initData(){

        mData = new ArrayList<>();

        adapter = new ChatMessageAdapter(this, mData, role);
        lv_chat.setAdapter(adapter);

        courseId = getIntent().getStringExtra("courseId");
        role = UserCache.getInstance().getRole(this);

        presenter = new ChatPresenter(this);
        presenter.sendMessageListRequest(courseId, page);
    }


    // 发送消息
    public void send(){

        if (!TextUtils.isEmpty(et_content.getText().toString().trim())) {
            ChatMessage message = new ChatMessage();
            message.setAvatar(UserCache.getInstance().getAvatar(this));
            if (role.equals("0")) {
                message.setStudentId(UserCache.getInstance().getId(this));
            }
            else if (role.equals("1")) {
                message.setTeacherId(UserCache.getInstance().getId(this));
            }
            message.setCourseId(courseId);
            message.setContent(et_content.getText().toString());

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String time = format.format(curDate);

            message.setCreatedAt(time);
            mData.add(message);
            adapter.notifyDataSetChanged();
            lv_chat.setSelection(lv_chat.getBottom());

            presenter.sendMessageAction(courseId, UserCache.getInstance().getId(this), role,
                    et_content.getText().toString().trim());

            et_content.setText("");
        }
    }


    /**
     * Implementation for IChatView
     * @param response
     */
    @Override
    public void onGetMessageListSuccess(final ResultResponse<List<ChatMessage>> response) {

        Log.e("response", "msg-list");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (response.getCode().equals("200")){

                    //dismissLoadingDialog();
                    if (response.getData() == null || response.getData().size() < 18){  // 每次返回18条信息
                        Log.e("update", "completed");
                        isCompleted = true;
                    }

                    if (page == 0) {
                        Collections.reverse(response.getData());
                        mData = response.getData();
                        adapter = new ChatMessageAdapter(ChatActivity.this, mData, role);
                        lv_chat.setAdapter(adapter);

                        lv_chat.setSelection(lv_chat.getBottom());
                    }
                    else{

                        headerView.setPadding(0, - headerHeight, 0, 0);

                        int size = response.getData().size();
                        Collections.reverse(mData);
                        mData.addAll(response.getData());
                        Collections.reverse(mData);
                        adapter = new ChatMessageAdapter(ChatActivity.this, mData, role);
                        lv_chat.setAdapter(adapter);

                        lv_chat.setSelection(size);
                    }

                    isRefresh = false;
                    page++;
                }
                else{
                    //dismissLoadingDialog();
                    String msg = response.getMsg();
                    ToastView toastView = new ToastView(ChatActivity.this, msg);
                    toastView.setGravity(Gravity.CENTER, 0, 0);
                    toastView.show();
                }

                if (mData.size() == 0){
                    ll_no_message.setVisibility(View.VISIBLE);
                }
                else{
                    ll_no_message.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onReceiveNewMessage(final ResultResponse<ChatMessage> response) {

        Log.e("response", "receive new message");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (response.getCode().equals("200")){

                    //dismissLoadingDialog();

                    //et_content.setText("");

                    ChatMessage message = response.getData();
                    mData.add(message);
                    adapter.notifyDataSetChanged();
                    lv_chat.setSelection(lv_chat.getBottom());
                    //refresh();
                }
                else{
                    //dismissLoadingDialog();
                    String msg = response.getMsg();
                    ToastView toastView = new ToastView(ChatActivity.this, msg);
                    toastView.setGravity(Gravity.CENTER, 0, 0);
                    toastView.show();
                }
            }
        });
    }


    /**
     * implementation for Scrolllistener
     * @param view
     * @param scrollState
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (firstVisibleItem == 0 && !isRefresh && !isCompleted){
            View firstVisibleItemView = lv_chat.getChildAt(0);
            if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                Log.e("ListView", "##### 滚动到顶部 #####");
                isRefresh = true;
                headerView.setPadding(0, 0, 0, 0);
                presenter.sendMessageListRequest(courseId, page);
            }
        }
    }


    /**
     * implementation for Keylistener
     * @param v
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (keyCode == event.KEYCODE_ENTER) {
            // 执行发送动作
            Log.e("action", "send >>>>");

            send();
        }
        return false;
    }


    @Override
    public void finish() {

        // 保存最近访问时间戳
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String time = format.format(curDate);

        TimeCache.getInstance().setTime(this, courseId, time);

        super.finish();
    }


    @OnClick(R.id.btn_emotion)
    public void switchInput(){

        if (input_state == STATE_EMOTION){
            // 切换输入法
            btn_emotion.setBackgroundResource(R.drawable.icon_emotion);
            showKeyBoard();
            ll_emotion.setVisibility(View.GONE);

            input_state = STATE_KEYBOARD;
        }
        else{
            // 切换表情
            btn_emotion.setBackgroundResource(R.drawable.icon_keyboard);
            dismissKeyBoard();
            ll_emotion.setVisibility(View.VISIBLE);

            input_state = STATE_EMOTION;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lv_chat.setSelection(lv_chat.getBottom());
            }
        }, 10);
    }


    @OnItemClick(R.id.lv_chat)
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        dismissKeyBoard();
    }

    // 返回上一页面
    @OnClick(R.id.ll_return)
    public void back(){
        finish();
    }


    // 打开软键盘
    public void showKeyBoard(){

        InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {

            imm.showSoftInput(et_content,InputMethodManager.SHOW_FORCED);
        }
    }


    // 取消软键盘
    public void dismissKeyBoard(){

        InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {

            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }


    /**
     * 初始化表情区
     */
    public void initEmotionView(){

        try {
            InputStream inputStream = this.getResources().getAssets()
                    .open("emotions.xml");
            emotions = XmlUtil.getEmotions(inputStream);

            ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
            for (int i = 0; i < emotions.size(); i++) {
                Emotion emotion = emotions.get(i);
                if (emotion != null) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    Field f = (Field) R.drawable.class.getDeclaredField(emotion
                            .getName());
                    int j = f.getInt(R.drawable.class);
                    map.put("itemImage", j);
                    items.add(map);
                }
            }
            items.remove(items.size() - 1);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemImage", R.drawable.img_delete);
            items.add(map);

            SimpleAdapter saImageItems = new SimpleAdapter(this, items,
                    R.layout.emotion_item, new String[] { "itemImage" },
                    new int[] { R.id.iv_emotion });
            gv_emotions.setSelector(new ColorDrawable(Color.TRANSPARENT));
            gv_emotions.setAdapter(saImageItems);

            gv_emotions.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(position == emotions.size() - 1) {
                        int cursorPosition = et_content.getSelectionStart();
                        if(cursorPosition > 0) {
                            String str = et_content.getText().subSequence(0, cursorPosition).toString();
                            int lastIndex = str.lastIndexOf("f");
                            if(lastIndex >= 0 && lastIndex < cursorPosition) {
                                str = str.substring(lastIndex, cursorPosition);
                                boolean match = ExpressionUtil.matchEmotion(str);
                                if(match) {
                                    et_content.getEditableText().delete(lastIndex, cursorPosition);
                                } else {
                                    et_content.getEditableText().delete(cursorPosition - 1, cursorPosition);
                                }
                            } else {
                                et_content.getEditableText().delete(cursorPosition - 1, cursorPosition);
                            }
                        }
                    } else {
                        Emotion emotion = emotions.get(position);
                        int cursor = et_content.getSelectionStart();
                        Field f;
                        try {
                            f = (Field) R.drawable.class.getDeclaredField(emotion.getName());
                            int j = f.getInt(R.drawable.class);
                            Drawable d = getResources().getDrawable(j);
                            int textSize = (int) et_content.getTextSize();
                            d.setBounds(0, 0, textSize + 10, textSize + 10);

                            String str = null;
                            int pos = position + 1;
                            if (pos < 10) {
                                str = "f00" + pos;
                            } else if (pos < 100) {
                                str = "f0" + pos;
                            } else {
                                str = "f" + pos;
                            }
                            SpannableString ss = new SpannableString(str);
                            ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
                            ss.setSpan(span, 0, str.length(),
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            et_content.getText().insert(cursor, ss);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * implementation for TextWatacher
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (TextUtils.isEmpty(et_content.getText().toString().trim())){
            btn_send.setVisibility(View.GONE);

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) btn_emotion.getLayoutParams();
            layoutParams.setMargins(0, 0, DensityUtil.dip2px(this, 8), 0);
            btn_emotion.setLayoutParams(layoutParams);
        }
        else{
            btn_send.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) btn_emotion.getLayoutParams();
            layoutParams.setMargins(0, 0, DensityUtil.dip2px(this, 64), 0);
            btn_emotion.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
