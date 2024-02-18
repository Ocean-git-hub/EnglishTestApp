package com.example.englishwordstest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
	@SuppressLint("SetTextI18n")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setSpinner();
		Button editButton = findViewById(R.id.editButton);
		editButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				startActivity(new Intent(getApplicationContext(), EditPhraseActivity.class));
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		setSpinner();
	}

	private void setSpinner() {
		final String filePath = Environment.getExternalStorageDirectory().getPath() + "/EnglishPhrase/";
		final List<String> filePathList = new ArrayList<>(getFilePathList(filePath));
		final String defaultSpinnerElement = "No select";
		filePathList.add(0, defaultSpinnerElement);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(
				this,
				android.R.layout.simple_spinner_item,
				filePathList
		);

		final Spinner chooseSpinner = findViewById(R.id.chooseSpinner);
		chooseSpinner.setFocusable(false);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		chooseSpinner.setAdapter(adapter);

		chooseSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
				if(!chooseSpinner.isFocusable()) {
					chooseSpinner.setFocusable(true);
					return;
				}
				RadioGroup radioGroup = findViewById(R.id.radioGroup);
				RadioButton selectButton = findViewById(radioGroup.getCheckedRadioButtonId());
				if(!parent.getSelectedItem().toString().equals(defaultSpinnerElement))
					startActivity(new Intent(getApplicationContext(), PhraseTestActivity.class)
							.putExtra("phraseList", getPhraseList(filePath + parent.getSelectedItem()))
							.putExtra("mode", selectButton.getText()));
			}

			@Override
			public void onNothingSelected(final AdapterView<?> parent) {

			}
		});
	}

	private ArrayList<EnglishPhrasesTest.Phrase> getPhraseList(String filePath) {
		if(isExternalStorageReadable()) {
			ArrayList<EnglishPhrasesTest.Phrase> phrases = new ArrayList<>();
			try {
				FileInputStream fileInputStream = new FileInputStream(filePath);
				InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF8");
				BufferedReader reader = new BufferedReader(inputStreamReader);
				String lineBuffer;

				while((lineBuffer = reader.readLine()) != null) {
					phrases.add(new EnglishPhrasesTest.Phrase(lineBuffer, reader.readLine()));
				}
				return phrases;
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	private boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state) ||
				Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
	}

	private List<String> getFilePathList(String filePath) {
		File directory = new File(filePath);
		if(!directory.exists()) {
			directory.mkdirs();
		}
		directory = new File(filePath);
		return Arrays.asList(directory.list());
	}

}
