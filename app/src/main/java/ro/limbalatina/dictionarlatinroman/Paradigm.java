package ro.limbalatina.dictionarlatinroman;

import java.util.Locale;

import android.content.Context;
import android.content.res.Resources;

/*
 * Class started on 21 June 2015 by Manu
 * This class tries to make paradigms for Latin words.
 */

public class Paradigm {

	private final Context context;
	private String latinWord;
	private String romanianExplanation;
	private int iPartOfSpeech = 0; // as integer, from 1 to 9, 0 means
									// indeterminable.
	private String sPartOfSpeech = null; // default is indeterminable.
	private String[] aPartsOfSpeech; // an array which extract from resources
										// the parts of speech as strings.
	private String[] aPartsOfSpeechArticled; // an array which extract from
												// resources the articled
												// definitely parts of speech as
												// strings.

	// The constructor:
	public Paradigm(Context context, String latinWord,
			String romanianExplanation) {
		this.context = context;
		this.latinWord = latinWord;
		this.romanianExplanation = romanianExplanation;

		// Fill the arrays with parts of speech as strings:
		Resources res = context.getResources();
		aPartsOfSpeech = res.getStringArray(R.array.parts_of_speech_array);
		aPartsOfSpeechArticled = res
				.getStringArray(R.array.parts_of_speech_articled_array);
		res = null;

		initialThingsOfParadigm();
	} // end constructor.

	// A method called in constructor to attribute values for iPartOfSpeech and
	// sPartOfSpeech:
	private void initialThingsOfParadigm() {
		iPartOfSpeech = determinePartOfSpeechAsInteger();
		sPartOfSpeech = determinePartOfSpeechAsString();
	} // end initialThingsOfParadigm() method.

	// Determine the part of speech as integer, this is on of the methods where
	// the process of paradigm is started:
	private int determinePartOfSpeechAsInteger() {
		int tempPartOfSpeech = 0;
		// An array of strings containing the start of explanation for each part
		// of speech:
		String[] clues = { "", "s.", "pron.", "num.", "adj.", "vb.", "prep.",
				"conj.", "adv.", "interj." };
		for (int i = 1; i < clues.length; i++) {
			// Check if the substring at the start of romanianExplanation is the
			// same with one in the array above:
			if (clues[i].equals(romanianExplanation.substring(0,
					clues[i].length()))) {
				tempPartOfSpeech = i;
				break;
			} // end if there is one of the clues at start of the explanation.
		} // end for.

		return tempPartOfSpeech;
	} // end determine partOfSpeechAsInteger.

	// A method to determine the part of speech as string:
	private String determinePartOfSpeechAsString() {
		return aPartsOfSpeech[iPartOfSpeech];
	} // end determinePartOfSpeechAsString method.

	// A method to show part of speech of current latinWord:
	public void showPartOfSpeech() {
		CharSequence cs = MyHtml.fromHtml(String.format(
				context.getString(R.string.body_part_of_speech), latinWord,
				sPartOfSpeech));
		GUITools.alert(context,
				context.getString(R.string.title_part_of_speech),
				cs.toString(), context.getString(R.string.bt_close));
	} // end showPartOfSpeech() method.

	// A method to make the paradigm for current latinWord:
	public void makeParadigm() {
		// A switch from 0 to 9 for each part of speech:
		switch (iPartOfSpeech) {
		// Noun:
		case 1:
			Noun n = new Noun(context, latinWord);
			n.makeParadigm();
			break;
		// Pronoun:
		case 2:

			break;
		// Numeral:
		case 3:

			break;
		// Adjective:
		case 4:

			break;
		// Verb:
		case 5:

			break;
		// Preposition, conjunction, adverb and interjection:
		case 6:
		case 7:
		case 8:
		case 9:
			showInflexibleInformation();
			break;
		// If is indeterminable, it is case 0 in this class:
		default:
			GUITools.alert(context, getPartOfSpeechInCapitalLetter(), context
					.getString(R.string.paradigm_indeterminable_information),
					context.getString(R.string.bt_ok));
			break;
		} // end of switch.
	} // end makeTheParadigm.

	// A method to show information about inflexible parts of speech:
	private void showInflexibleInformation() {
		CharSequence cs = MyHtml.fromHtml(String.format(
				context.getString(R.string.paradigm_inflexible_information),
				aPartsOfSpeechArticled[iPartOfSpeech]));
		GUITools.alert(context, getPartOfSpeechInCapitalLetter(),
				cs.toString(), context.getString(R.string.bt_close));
	} // end showInflexibleInformation.

	// A method to get title for makeParadigm in capital letter from array not
	// articled:
	private String getPartOfSpeechInCapitalLetter() {
		return aPartsOfSpeech[iPartOfSpeech].substring(0, 1).toUpperCase(
				Locale.getDefault())
				+ aPartsOfSpeech[iPartOfSpeech].substring(1);
	} // end getPartOfSpeechInCapitalLetter() method.

} // end Paradigm class.
