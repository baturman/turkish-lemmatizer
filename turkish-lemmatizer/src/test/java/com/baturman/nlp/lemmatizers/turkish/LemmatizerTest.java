package com.baturman.nlp.lemmatizers.turkish;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class LemmatizerTest extends TestCase{

	TurkishLemmatizer tl;

	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public LemmatizerTest( String testName )
	{
		super( testName );
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite()
	{
		return new TestSuite( LemmatizerTest.class );
	}

	/**
	 * During Turkish word formation, the loss of a letter is possible in either root word or suffix. This loss can be a vowel or a consonant.
	 * The drop of middle vowel: The vowel of the second syllable is lost when a suffix beginning with a vowel is added. 
	 * 
	 * For example:
	 * 
	 * stem						word
	 * ------------------		--------------	
	 * oğul	(son) + u			oğlum
	 * burun (nose) + u			burnunda (on his/her nose)
	 * karın (stomach) + im		karnım (my stomach)
	 * 		
	 */
	public void testDropping(){
		String [] words = { "oğlum", "burnunda", "burnum", "karnım"};
		String [] stems = { "oğul", "burun", "burun", "karın"};
		
		try {
			tl = new TurkishLemmatizer();
			tl.addStem("oğul");
			tl.addStem("burun");
			tl.addStem("karın");
			
			for (int i = 0; i < words.length; i++) {
				assertEquals(stems[i], tl.lemmatize(words[i]));
			}
			
		} catch (StringIndexOutOfBoundsException e) {
			e.printStackTrace();
		} catch (NotAcceptableCharacterException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Character change: When a vowel is added to some nouns of one syllable and most nouns of more than one syllable, 
	 * ending with “p”, “ç”, “t”, “k”, the final consonant changes to “b”, “c”, “d”, or “ğ” respectively. So with the 
	 * addition of suffix for the third person, “-i” as shown below:
	 * 
	 * For example:
	 *
	 * stem 				word
	 * ------------ 		--------------------------
	 * kitap (book) + ı 	kitabı (his/her/its book)
	 * ağaç (tree) + ı 		ağacı (his/her/its tree)
	 * armut (pear) + ı 	armudu (his/her/its pear)
	 * ayak (foot) + ı 		ayağı (his/her/its foot)
	 * 
	 * TODO: These two conditions should be verified, within the code.
	 * 
	 * But there are nouns, whose final consonants are not subject to this change, when the letter “n” comes 
	 * before the letter “k” at the end of a word, the letter “k” becomes “g” instead of “ğ”. 
	 * 
	 * For example:
	 * 
	 * stem 				word
	 * ------------ 		--------------------------
	 * renk (color) + ı 	rengi (his/her/its color)
	 * 
	 * In foreign origin words, if the last letter of a word is “g” and combines with the suffix where first letter 
	 * is vowel, the letter “g” changes to the letter “ğ” as in the example:
	 * 
	 * stem 					word
	 * ------------ 			--------------------------
	 * monolog (monologue) + ı 	monoloğu (his/her/its monologue)
	 * 
	 * The change of “g” and “ğ” is not observed for one syllable words or words which the second letter from the 
	 * last one is “n”. For example:
	 * 
	 * stem 						word
	 * ------------ 				--------------------------
	 * şezlong (chaise longue) + ı 	şezlongu (his/her chaise longue)
	 * 
	 */
	public void testCharacterChange(){
		String [] words = { "kitabı", "ağacı", "armudu", "ayağı", "rengi", "monoloğu", "şezlongu"};
		String [] stems = { "kitap", "ağaç", "armut", "ayak", "renk", "monolog", "şezlong"};
		
		try {
			tl = new TurkishLemmatizer();
			tl.addStem("kitap");
			tl.addStem("ağaç");
			tl.addStem("armut");
			tl.addStem("ayak");
			tl.addStem("renk");
			tl.addStem("monolog");
			tl.addStem("şezlong");
			
			for (int i = 0; i < words.length; i++) {
				assertEquals(stems[i], tl.lemmatize(words[i]));
			}
			
		} catch (StringIndexOutOfBoundsException e) {
			e.printStackTrace();
		} catch (NotAcceptableCharacterException e) {
			e.printStackTrace();
		}
	}

	
	
	
	/**
	 * Test for lemmatize method
	 */
	public void testLemmatizer()
	{

		TurkishLemmatizer tl = new TurkishLemmatizer();
		try {
			tl.addStem("kitap");
			tl.addStem("kulak");
			tl.addStem("ağaç");
			tl.addStem("kalp");


			String [] word = {
					"kitabım", 
					"kulağım", 
					"ağacımız", 
					"kalbim", 
			};

			String [] expected = {
					"kitap", 
					"kulak", 
					"ağaç", 
					"kalp", 
			};

			for (int i = 0; i < word.length; i++) {
				String defaultStem = tl.lemmatize(word[i]);
				assertEquals(expected[i], defaultStem);
			}

		} catch (NotAcceptableCharacterException e) {
			e.printStackTrace();
		}
	}
}
