package ro.limbalatina.dictionarlatinroman;

import android.content.Context;
import android.content.res.Resources;

/*
 * Class started on 27 June 2015 by Manu.
 */

public class Noun {

	private String noun = null;
	private String nominativeNoun = null;
	private final Context context;
	private int declension = 0; // 0 will be indeterminable.
	private int typeOfDeclension = 0; // 0 will be indeterminable in a
										// declension.
	private String[] aCases; // form strings the cases.

	// The constructor:
	public Noun(Context context, String noun) {
		this.context = context;
		this.noun = noun;

		// Fill the array aCases from strings:
		Resources res = context.getResources();
		aCases = res.getStringArray(R.array.paradigm_noun_cases_array);
		this.declension = determineDeclensionAsInteger();
	} // end constructor.

	// A method to determine the declension of current noun.
	// This method return the number of declension and also attribute to
	// typeOfDeclension:
	private int determineDeclensionAsInteger() {
		int tempDec = 0; // it is indeterminable as default.

		// We split the noun by ", -", this way at the left we have the
		// nominative form, at right the genitive termination:
		String[] aDictionaryForm = noun.split("\\, -");

		// Check if there are at least two indexes of aDictionaryForm:
		if (aDictionaryForm.length >= 2) {

			// We must check if there is a number after nominative, the number
			// for order:
			// Here method to cut 1, 2, 3 or 4 at the finish of the nominative.
			aDictionaryForm[0] = aDictionaryForm[0].replaceAll("[1-4]", "");
			nominativeNoun = aDictionaryForm[0];

			// Lets go step by step to see which declension is this noun:
			// First declension:
			// Common nouns or Proper nouns, capital letter, only singular:
			if (aDictionaryForm[1].equals("ae")) {
				tempDec = 1;
				if (Character.isUpperCase(aDictionaryForm[0].charAt(0))) {
					typeOfDeclension = 2;
				} else {
					typeOfDeclension = 1;
				}

			} // end if is first declension, type 1 or 2.
			else if (aDictionaryForm[1].equals("arum")) {
				tempDec = 1;
				typeOfDeclension = 3;
			} // end if declension is 1, type 3, only plural.

			// Check now if declension is 2, in the second index of the array is
			// an i or ii:
			else if (aDictionaryForm[1].equals("i")
					|| aDictionaryForm[1].equals("ii")) {
				tempDec = 2;
				// Now some nested IFs to see which kind of declension is this:
				int firstFormLength = aDictionaryForm[0].length();
				String last2Chars = aDictionaryForm[0]
						.substring(firstFormLength - 2);
				// Check if is neuter, UM at the finish of the first form:
				if (last2Chars.equals("um")) {
					typeOfDeclension = 1;
				} // end if is neuter, finished in UM.
					// Now we check if is in us and there some variations here:
				if (last2Chars.equals("us")) {
					// Some variations:

					typeOfDeclension = 2;
				} // end if is masculine or feminine, finished in US.

				// GUITools.alert(context, "", last2Chars, "Close");
			} // end if declension is 2.

		} // end if there are at least two indexes in the array of dictionary
			// form after splitting by ", -".
		else {
			// Something is wrong, there is no a delimiter ", -":
			// Do nothing, the tempDec will remain 0.
		}
		return tempDec;
	} // end determine declension.

	// A method to decline the noun:
	private String decline1() {
		String temp = "";
		String template = ""; // extracted from strings resources.
		String theme = ""; // we determine it here.
		String[] aTerminationsSingular = { "a", "ae", "ae", "am", "a", "a" };
		String[] aTerminationsPlural;
		// For exceptions:
		if (nominativeNoun.equals("dea") || nominativeNoun.equals("filia")) {
			aTerminationsPlural = new String[] { "ae", "arum", "abus", "as",
					"ae", "abus" };
		} else {
			aTerminationsPlural = new String[] { "ae", "arum", "is", "as",
					"ae", "is" };
		}

		// Get the template1 for nouns with singular and plural:
		if (typeOfDeclension == 1) {
			template = context
					.getString(R.string.paradigm_declension_template1);
			theme = nominativeNoun.substring(0, nominativeNoun.length() - 1);
		} else if (typeOfDeclension == 2) {
			template = context
					.getString(R.string.paradigm_declension_template2);
			theme = nominativeNoun.substring(0, nominativeNoun.length() - 1);
		} else if (typeOfDeclension == 3) {
			template = context
					.getString(R.string.paradigm_declension_template3);
			theme = nominativeNoun.substring(0, nominativeNoun.length() - 2);
		} // end if typeOfDeclension is 3, only plural.
		else {
			template = context
					.getString(R.string.paradigm_declension_cannot_be_created);
		} // end if typeOfDeclension cannot be determined.

		// Now we process the place holders for template:
		StringBuilder singular = new StringBuilder("");
		for (int i = 0; i < aCases.length; i++) {
			singular.append(aCases[i] + " – &lt;lt>" + theme
					+ aTerminationsSingular[i] + "&lt;/i>&lt;br>");
		} // end for singular.

		StringBuilder plural = new StringBuilder("");
		for (int i = 0; i < aCases.length; i++) {
			plural.append(aCases[i] + " – &lt;i>" + theme
					+ aTerminationsPlural[i] + "&lt;/i>&lt;br>");
		} // end for plural.

		// Now we format the string including the singular and the plural if
		// needed:
		if (typeOfDeclension == 1) {
			temp = String.format(template, "I",
					MyHtml.fromHtml(singular.toString()).toString(), MyHtml
							.fromHtml(plural.toString()).toString());
		} else if (typeOfDeclension == 2) {
			temp = String.format(template, "I",
					MyHtml.fromHtml(singular.toString()).toString());
		} else if (typeOfDeclension == 3) {
			temp = String.format(template, "I",
					MyHtml.fromHtml(plural.toString()).toString());
		} else {
			// The declension cannot be created:
			temp = String.format(template, "I");
		}

		return MyHtml.fromHtml(temp).toString();
	} // end decline1 method.

	// A public method which makes the paradigm, decline the noun in other
	// classes, mostly in Paradigm class:
	public void makeParadigm() {
		// A switch on declension:
		switch (declension) {

		// First declension:
		case 1:
			showParadigm(decline1());
			break;

		default:
			showParadigm(MyHtml
					.fromHtml(
							context.getString(R.string.paradigm_declension_cannot_be_created))
					.toString());
			break;
		} // end switch on declension.
	} // end makeParadigm() method.

	// A method to show the paradigm effectively:
	private void showParadigm(String text) {
		// Play a sound if paradigm is created, another one if not:
		if (declension > 0 && typeOfDeclension > 0) {
			SoundPlayer.playSimple(context, "paradigm_shown");
		} else {
			SoundPlayer.playSimple(context, "paradigm_not_available");
		} // end play sounds.

		String title = MyHtml.fromHtml(
				String.format(
						context.getString(R.string.paradigm_declension_title),
						noun)).toString();
		// Show now the alert:
		GUITools.alert(context, title, text,
				context.getString(R.string.bt_close));
	} // end show paradigm.

} // end class Noun.
