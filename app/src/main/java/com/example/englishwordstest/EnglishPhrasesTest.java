package com.example.englishwordstest;

import java.io.Serializable;
import java.util.ArrayList;

class EnglishPhrasesTest implements Serializable{
	static int CORRECT = 0;
	static int INCORRECT = 1;
	static int MODE_JA_TO_EN = 0;
	static int MODE_EN_TO_JA = 1;
	static int ERROR = 3;
	private ArrayList<Phrase> phraseArrayList;
	private int mode;
	private int pointer;
	private ArrayList<Phrase> passPhraseList;

	EnglishPhrasesTest(final ArrayList<Phrase> phraseArrayList, final int MODE) {
		this.phraseArrayList = phraseArrayList;
		this.mode = MODE;
		passPhraseList = new ArrayList<>();
	}

	int play(String answer) {
		if(phraseArrayList != null && phraseArrayList.size() != pointer) {
			if(mode == MODE_EN_TO_JA) {
				if(phraseArrayList.get(pointer).getJaAsw().equals(answer)) {
					pointer++;
					return CORRECT;
				} else return INCORRECT;
			} else {
				if(phraseArrayList.get(pointer).getEngAsw().equals(answer)) {
					pointer++;
					return CORRECT;
				} else return INCORRECT;
			}
		} else {
			return ERROR;
		}
	}

	void passPhrase() {
		if(phraseArrayList.size() != pointer) {
			passPhraseList.add(phraseArrayList.get(pointer));
			pointer++;
		}
	}

	String getIssue() {
		if(mode == MODE_JA_TO_EN && phraseArrayList.size() != pointer)
			return phraseArrayList.get(pointer).getEngIssue();
		else if(mode == MODE_EN_TO_JA && phraseArrayList.size() != pointer)
			return phraseArrayList.get(pointer).getJaIssue();
		else
			return null;
	}

	String getCompleteIssue(){
		if(mode == MODE_JA_TO_EN && phraseArrayList.size() != pointer)
			return phraseArrayList.get(pointer).getJapanese();
		else if(mode == MODE_EN_TO_JA && phraseArrayList.size() != pointer)
			return phraseArrayList.get(pointer).getEnglish();
		else
			return null;
	}

	int getRemainingPhrases(){
		return phraseArrayList.size() - pointer;
	}

	ArrayList<Phrase> getPassPhraseList() {
		return passPhraseList;
	}

	static class Phrase implements Serializable {
		private final String japanese;
		private final String english;
		private final String[] japaneseIssue;
		private final String[] englishIssue;
		private final String japaneseAsw;
		private final String englishAsw;

		Phrase(final String japanese, final String english) {
			this.japanese = japanese;
			this.english = english;
			japaneseIssue = new String[2];
			String split = "/";
			japaneseIssue[0] = japanese.split(split)[0];
			japaneseIssue[1] = japanese.split(split)[2];
			japaneseAsw = japanese.split(split)[1];
			englishIssue = new String[2];
			englishIssue[0] = english.split(split)[0];
			englishIssue[1] = english.split(split)[2];
			englishAsw = english.split(split)[1];
		}

		String getJaIssue() {
			return japaneseIssue[0] + "(    )" + japaneseIssue[1];
		}

		String getEngIssue() {
			return englishIssue[0] + "(    )" + englishIssue[1];
		}

		String getJaAsw() {
			return japaneseAsw;
		}

		String getEngAsw() {
			return englishAsw;
		}

		String getJapanese() {
			return japanese;
		}

		String getEnglish() {
			return english;
		}
	}
}
