package com.example.englishwordstest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
	private ArrayList<EnglishPhrasesTest.Phrase> passPhrases;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		Intent intent = getIntent();
		EnglishPhrasesTest phrasesTest = (EnglishPhrasesTest) intent.getSerializableExtra("object");
		passPhrases = phrasesTest.getPassPhraseList();
		ScrollView scrollView = findViewById(R.id.passPhraseScrollView);
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		if(phrasesTest.getPassPhraseList().size() != 0) {
			for(int i = 0; i < phrasesTest.getPassPhraseList().size(); i++) {
				TextView textView = new TextView(this);
				textView.setText("日本語:" + passPhrases.get(i).getJapanese() + "|英語:" + passPhrases.get(i).getEnglish());
				linearLayout.addView(textView);
			}
		} else {
			TextView textView = new TextView(this);
			textView.setText("ありません");
			linearLayout.addView(textView);
		}
		scrollView.addView(linearLayout);
		setButton();
	}

	private void setButton() {
		Button againButton = findViewById(R.id.againButton);
		againButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {

			}
		});
		Button passButton = findViewById(R.id.passButton);
		passButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				Intent intent = getIntent();
				startActivity(new Intent(getApplicationContext(), PhraseTestActivity.class)
						.putExtra("phraseList", passPhrases)
						.putExtra("mode", intent.getStringExtra("mode")));
			}
		});
		Button homeButton = findViewById(R.id.homeButton);
		homeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				startActivity(new Intent(getApplicationContext(),MainActivity.class));
			}
		});
	}
}
