package org.telegram.ui;

import android.content.Context;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.LayoutHelper;

/**
 * Created by khanhnq2 on 04/05/2016.
 */
public class QuestionActivity extends BaseFragment {

    public interface QuestionActivityDelegate {
        public void onDidComposeQuestion(String question, String answer, int time);
    }


    QuestionActivityDelegate delegate;

    public QuestionActivity(QuestionActivityDelegate delegate){
        this.delegate = delegate;
    }

    private EditText questionText;
    private EditText answerText;
    private View doneButton;

    private final static int done_button = 1;

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle(LocaleController.getString("ChatQuestion", R.string.ChatQuestion));
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                } else if (id == done_button) {
                    String question = questionText.getText().toString();
                    String answer = answerText.getText().toString();
                    if (question.length() > 0 && answer.length() > 0){
                        delegate.onDidComposeQuestion(question, answer, 5);
                        finishFragment();
                    }
                }
            }
        });

        ActionBarMenu menu = actionBar.createMenu();
        doneButton = menu.addItemWithWidth(done_button, R.drawable.ic_done, AndroidUtilities.dp(56));

        LinearLayout linearLayout = new LinearLayout(context);
        fragmentView = linearLayout;
        fragmentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((LinearLayout) fragmentView).setOrientation(LinearLayout.VERTICAL);
        fragmentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        questionText = new EditText(context);
        questionText.setHint("Question...");
        questionText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        questionText.setHintTextColor(0xff979797);
        questionText.setTextColor(0xff212121);
        questionText.setLines(3);
        questionText.setMaxLines(6);
        questionText.setPadding(0, 0, 0, 0);
        questionText.setGravity(LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT);
        questionText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        questionText.setGravity(LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT);
        AndroidUtilities.clearCursorDrawable(questionText);


        answerText = new EditText(context);
        answerText.setHint("Answer...");
        answerText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        answerText.setHintTextColor(0xff979797);
        answerText.setTextColor(0xff212121);
        answerText.setSingleLine();
        answerText.setPadding(0, 0, 0, 0);
        answerText.setGravity(LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT);
        answerText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        answerText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        answerText.setGravity(LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT);
        AndroidUtilities.clearCursorDrawable(questionText);
        answerText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE && doneButton != null) {
                    AndroidUtilities.hideKeyboard(answerText);
                    return true;
                }
                return false;
            }
        });

        linearLayout.addView(questionText, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 24, 24, 24, 0));
        linearLayout.addView(answerText, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 24, 24, 24, 0));
        return fragmentView;
        
    }

    @Override
    public void onTransitionAnimationEnd(boolean isOpen, boolean backward) {
        if (isOpen) {
            AndroidUtilities.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (questionText != null) {
                        questionText.requestFocus();
                        AndroidUtilities.showKeyboard(questionText);
                    }
                }
            }, 100);
        }
    }
    
    
}
