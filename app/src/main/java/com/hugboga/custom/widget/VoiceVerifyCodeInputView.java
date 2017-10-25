package com.hugboga.custom.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.CommonUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by on 17/10/20.
 *
 * 参考加拷贝 http://www.jianshu.com/p/88d30b1d85df
 */

public class VoiceVerifyCodeInputView extends LinearLayout {

    @Bind(R.id.vvc_input_phone_tv)
    TextView phoneTV;
    @Bind(R.id.vvc_input_code_et)
    EditText codeET;
    @Bind(R.id.vvc_input_countdown_tv)
    TextView countdownTV;

    @Bind(R.id.vvc_input_code_tv1)
    TextView codeTV1;
    @Bind(R.id.vvc_input_code_stroke1)
    View strokeView1;
    @Bind(R.id.vvc_input_code_tv2)
    TextView codeTV2;
    @Bind(R.id.vvc_input_code_stroke2)
    View strokeView2;
    @Bind(R.id.vvc_input_code_tv3)
    TextView codeTV3;
    @Bind(R.id.vvc_input_code_stroke3)
    View strokeView3;
    @Bind(R.id.vvc_input_code_tv4)
    TextView codeTV4;
    @Bind(R.id.vvc_input_code_stroke4)
    View strokeView4;

    private TextView[] codeTestViews;
    private View[] strokeViews;
    private StringBuffer stringBuffer = new StringBuffer();
    private int count = 4;
    private String inputContent;

    private final int BREATH_INTERVAL_TIME = 1000; //设置呼吸灯时间间隔
    private Timer timer;
    private boolean isOpen = true;
    private int index;
    private View nextStrokeView;

    private String code;
    private String phone;
    private int surplusTime;

    public VoiceVerifyCodeInputView(Context context) {
        this(context, null);
    }

    public VoiceVerifyCodeInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_vvc_input, this);
        ButterKnife.bind(view);

        codeTestViews = new TextView[4];
        codeTestViews[0] = codeTV1;
        codeTestViews[1] = codeTV2;
        codeTestViews[2] = codeTV3;
        codeTestViews[3] = codeTV4;

        strokeViews = new View[4];
        strokeViews[0] = strokeView1;
        strokeViews[1] = strokeView2;
        strokeViews[2] = strokeView3;
        strokeViews[3] = strokeView4;

        codeET.setCursorVisible(false);
        setListener();
        nextStrokeView = strokeView1;
        strokeView1.setSelected(true);
        startTimer();
    }

    public void setData(String _code, String _phone, long time) {
        this.code = _code;
        this.phone = _phone;
        phoneTV.setText(CommonUtils.addPhoneCodeSign(code) + " " + phone);
        setVisibility(VISIBLE);
        surplusTime = (int)(time / 1000);
        timeHandler.sendEmptyMessage(surplusTime);
    }

    @OnClick(R.id.vvc_input_countdown_tv)
    public void againRequest() {
        if (inputCompleteListener != null) {
            inputCompleteListener.againRequest();
        }
    }

    private void setListener() {
        codeET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 如果字符不为""时才进行操作
                if (!editable.toString().equals("")) {
                    if (stringBuffer.length() > 3) {
                        //当文本长度大于3位时edittext置空
                        codeET.setText("");
                        return;
                    } else {
                        //将文字添加到StringBuffer中
                        stringBuffer.append(editable);
                        codeET.setText("");//添加后将EditText置空
                        count = stringBuffer.length();//记录stringbuffer的长度
                        inputContent = stringBuffer.toString();
                        if (stringBuffer.length() == 4) {
                            //文字长度位4  则调用完成输入的监听
                            if (inputCompleteListener != null) {
                                inputCompleteListener.inputComplete();
                            }
                        }
                    }

                    for (int i = 0; i < stringBuffer.length(); i++) {
                        codeTestViews[i].setText(String.valueOf(inputContent.charAt(i)));
                    }
                    for (int i = 0; i < 4; i++) {
                        strokeViews[i].clearAnimation();
                        boolean isSelected = false;
                        if (i == 3) {
                            nextStrokeView = null;
                        } if (i == stringBuffer.length()) {
                            isSelected = true;
                            nextStrokeView = strokeViews[i];
                            index = 1;
                        }
                        strokeViews[i].setSelected(isSelected);
                    }
                }
            }
        });

        codeET.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (onKeyDelete()) return true;
                    return true;
                }
                return false;
            }
        });
    }

    public boolean onKeyDelete() {
        if (count == 0) {
            count = 4;
            return true;
        }
        if (stringBuffer.length() > 0) {
            stringBuffer.delete((count - 1), count);
            count--;
            inputContent = stringBuffer.toString();
            codeTestViews[stringBuffer.length()].setText("");
            for (int i = 0; i < 4; i++) {
                strokeViews[i].clearAnimation();
                boolean isSelected = false;
                if (i == stringBuffer.length()) {
                    isSelected = true;
                    nextStrokeView = strokeViews[i];
                    index = 1;
                }
                strokeViews[i].setSelected(isSelected);
            }
            if (inputCompleteListener != null) {
                inputCompleteListener.deleteContent(true);
            }
        }
        return false;
    }

    public void clearEditText() {
        stringBuffer.delete(0, stringBuffer.length());
        inputContent = stringBuffer.toString();
        for (int i = 0; i < codeTestViews.length; i++) {
            codeTestViews[i].setText("");
            strokeViews[stringBuffer.length()].setSelected(false);
        }
    }

    private InputCompleteListener inputCompleteListener;

    public void setInputCompleteListener(InputCompleteListener inputCompleteListener) {
        this.inputCompleteListener = inputCompleteListener;
    }

    public interface InputCompleteListener {
        void inputComplete();

        void deleteContent(boolean isDelete);

        void againRequest();
    }

    private Animation getFadeIn() {
        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.captcha_fade_in);
        fadeIn.setDuration(BREATH_INTERVAL_TIME);
        fadeIn.setStartOffset(100);
        return fadeIn;
    }

    private Animation getFadeOut() {
        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.captcha_fade_out);
        fadeOut.setDuration(BREATH_INTERVAL_TIME);
        fadeOut.setStartOffset(100);
        return fadeOut;
    }

    public Handler timeHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (!isOpen) {
                return;
            }
            if (msg.what <= 0) {
                countdownTV.setText("重新获取语音验证码");
                countdownTV.setTextColor(getContext().getResources().getColor(R.color.default_highlight_blue));
                countdownTV.setEnabled(true);
                surplusTime = 0;
            } else {
                countdownTV.setText(msg.what + "s后重新获取");
                countdownTV.setTextColor(0xFF929292);
                countdownTV.setEnabled(false);
                surplusTime = --msg.what;
                timeHandler.sendEmptyMessageDelayed(surplusTime, 1000);
            }
        }
    };

    public Handler animationHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (nextStrokeView == null) {
                return;
            }
            switch (msg.what) {
                case 1:
                    nextStrokeView.clearAnimation();
                    nextStrokeView.setAnimation(getFadeIn());
                    break;
                case 2:
                    nextStrokeView.clearAnimation();
                    nextStrokeView.setAnimation(getFadeOut());
                    break;
            }
        }
    };

    private void startTimer() {
        timer = new Timer(true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (isOpen) {
                    if (index == 2) {
                        index = 0;
                    }
                    index++;
                    Message message = new Message();
                    message.what = index;
                    animationHandler.sendMessage(message);
                }
            }
        };
        timer.schedule(task, 0, BREATH_INTERVAL_TIME);
    }

    public void onDestroy() {
        isOpen = false;
        if (timer != null) {
            timer.cancel();
        }
        timer = null;
        Log.i("aa", "停止 倒计时 " + surplusTime);
    }
}
