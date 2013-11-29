package com.baturman.nlp.lemmatizers.turkish;

/**
 * 
 * This class defines 29 letter Turkish Latin alphabet. Use {@link TurkishAlphabet}.display() to display letters. 
 * 
 * @see http://en.wikipedia.org/wiki/Turkish_alphabet
 * @author Baturman ŞEN
 *
 */
public class TurkishAlphabet {
	
	public final static int [] ALPHABET = {
		97,98,99,231,100,101,102,103,287,104,305,105,106,107,108,109,110,111,246,112,114,115,351,116,117,252,118,121,122
	};

	/**
	 * Displays turkish alphabet.
	 */
	public static void display(){
		for (int i = 0; i < ALPHABET.length; i++) {
			char t = (char)ALPHABET[i];
			System.out.println(ALPHABET[i] + " " + t);
			
		}
	}
	
	/**
	 * @param pos Position
	 * @return the character at given position
	 */
	public static char getLetter(int pos){
		return (char)ALPHABET[pos];
	}
	
	/**
	 * @param letter
	 * @return the position of given character. If return value is equal to <code>-1</code> that means, given character is not in Turkish Latin Alphabet
	 */
	public static int getPosition(char letter){
		int pos = -1;
		for (int i = 0; i < ALPHABET.length; i++) {
			if (ALPHABET[i] == (int)letter){
				pos = i;
				break;
			}
		}
		return pos;
	}
	
}