package com.example.englishwordstest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class PhraseTestActivity extends AppCompatActivity {
	private EnglishPhrasesTest phrasesTest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phrase_test);

		final Intent intent = getIntent();
		ArrayList<EnglishPhrasesTest.Phrase> phraseList = (ArrayList<EnglishPhrasesTest.Phrase>) intent.getSerializableExtra("phraseList");
		if(phraseList != null) {
			final int mode;
			if(intent.getStringExtra("mode").equals("日本語を英語に")) {
				mode = EnglishPhrasesTest.MODE_JA_TO_EN;
			} else {
				mode = EnglishPhrasesTest.MODE_EN_TO_JA;
			}
			phrasesTest = new EnglishPhrasesTest(phraseList, mode);
			setIssueTextView();
			setCompleteIssueTextView();
			final EditText answerEditTextView = findViewById(R.id.answerEditText);
			final TextView resultTextView = findViewById(R.id.resultTextView);
			answerEditTextView.requestFocus();
			answerEditTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(final TextView v, final int actionId, final KeyEvent event) {
					if(phrasesTest.getRemainingPhrases() != 0) {
						if((event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) || (actionId == EditorInfo.IME_ACTION_DONE)) {
							int result = phrasesTest.play(answerEditTextView.getText().toString().replaceAll("[\r\n]", ""));
							if(result == EnglishPhrasesTest.CORRECT) {
								resultTextView.setText("正解");
								setIssueTextView();
								setCompleteIssueTextView();
								if(phrasesTest.getRemainingPhrases() == 0)
									startActivity(new Intent(getApplicationContext(), ResultActivity.class)
											.putExtra("object", phrasesTest)
											.putExtra("mode", intent.getStringExtra("mode")));
							} else {
								resultTextView.setText("不正解");
							}
							answerEditTextView.getEditableText().clear();
							answerEditTextView.requestFocus();
						}
						return false;
					} else {
						startActivity(new Intent(getApplicationContext(), ResultActivity.class).putExtra("object", phrasesTest));
					}
					return false;
				}
			});
		}
		Button passButton = findViewById(R.id.passButton);
		passButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				phrasesTest.passPhrase();
				if(phrasesTest.getRemainingPhrases() != 0) {
					setIssueTextView();
					setCompleteIssueTextView();
				} else
					startActivity(new Intent(getApplicationContext(), ResultActivity.class)
							.putExtra("object", phrasesTest)
							.putExtra("mode", intent.getStringExtra("mode")));
			}
		});
	}

	private void setIssueTextView() {
		String issue = phrasesTest.getIssue();
		if(issue != null) {
			TextView issueTextView = findViewById(R.id.issueTextView);
			issueTextView.setText(issue);
		}
	}

	private void setCompleteIssueTextView() {
		String issue = phrasesTest.getCompleteIssue();
		if(issue != null) {
			TextView completeIssueTextView = findViewById(R.id.completeIssueTextView);
			completeIssueTextView.setText(issue);
		}
	}
}
