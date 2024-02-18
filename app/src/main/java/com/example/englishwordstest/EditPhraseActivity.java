package com.example.englishwordstest;

import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditPhraseActivity extends AppCompatActivity {
	private String filePathName;
	final String filePath = Environment.getExternalStorageDirectory().getPath() + "/EnglishPhrase/";
	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_phrase);


		setSpinner();
		setButton();
	}

	private void setButton() {
		Button addButton = findViewById(R.id.editButton);
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				EditText japaneseET = findViewById(R.id.japaneseEditText);
				EditText englishET = findViewById(R.id.englishEditText);

				if(!japaneseET.getText().toString().equals("") &&
						japaneseET.getText() != null &&
						!englishET.getText().toString().equals("") &&
						englishET.getText() != null
						&& filePathName != null) {
					if(isExternalStorageWritable()) {
						try {
							FileWriter fileWriter = new FileWriter(filePathName, true);
							PrintWriter printWriter = new PrintWriter(new BufferedWriter(fileWriter));
							printWriter.println(japaneseET.getText().toString());
							printWriter.println(englishET.getText().toString());
							printWriter.close();
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
				}
				japaneseET.getEditableText().clear();
				englishET.getEditableText().clear();
				setScrollView();
			}
		});

		Button makeFileButton = findViewById(R.id.makeFileButton);
		makeFileButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				EditText fileNameET = findViewById(R.id.fileNameEditText);
				if(!fileNameET.getText().toString().equals("") && fileNameET.getText() != null) {
					try {
						new FileOutputStream(filePath + fileNameET.getText().toString());
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
				fileNameET.getEditableText().clear();
				setSpinner();
			}
		});


		Button deleteButton = findViewById(R.id.deletebutton);
		deleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				String fileName = filePathName.replaceAll(filePath, "");
				new AlertDialog.Builder(EditPhraseActivity.this)
						.setMessage(fileName + "を削除しますか？")
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(final DialogInterface dialog, final int which) {
								File file = new File(filePathName);
								file.delete();
								setSpinner();
								setScrollView();
							}
						}).setNegativeButton("Cancel", null).show();
			}
		});
	}

	private void setSpinner() {
		final List<String> filePathList = getFilePathList(filePath);
		if(filePathList != null) {
			adapter = new ArrayAdapter<>(
					this,
					android.R.layout.simple_spinner_item,
					filePathList
			);

			Spinner fileSpinner = findViewById(R.id.fileSpinner);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			fileSpinner.setAdapter(adapter);

			fileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
					filePathName = filePath + parent.getSelectedItem();
					setScrollView();
				}

				@Override
				public void onNothingSelected(final AdapterView<?> parent) {

				}
			});
		}
	}

	private List<String> getFilePathList(String filePath) {
		File directory = new File(filePath);
		return Arrays.asList(directory.list());
	}

	private boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}

	private boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state) ||
				Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
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

	private void setScrollView() {
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		ScrollView scrollView = findViewById(R.id.phraseScrollView);
		scrollView.removeAllViews();

		final ArrayList<EnglishPhrasesTest.Phrase> phrase = getPhraseList(filePathName);
		if(phrase != null) {
			for(int i = 0; i < phrase.size(); i++) {
				final TextView textView = new TextView(this);
				textView.setTextSize(18);
				textView.setText(phrase.get(i).getJapanese() + "|" + phrase.get(i).getEnglish());
				linearLayout.addView(textView);
			}
			scrollView.addView(linearLayout);
		}
	}

}
