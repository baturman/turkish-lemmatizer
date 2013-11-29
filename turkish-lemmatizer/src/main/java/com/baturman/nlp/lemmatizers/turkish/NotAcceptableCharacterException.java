package com.baturman.nlp.lemmatizers.turkish;

/**
 * 
 * Definition for {@link NotAcceptableCharacterException}.
 * 
 * @author Baturman SEN
 *
 */
public class NotAcceptableCharacterException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public NotAcceptableCharacterException() {

	}
	
	public NotAcceptableCharacterException(char c, String stem, int pos) {
		super("Character '" + c + "' at position " + (pos+1) + " \"" + stem + "\" is not an acceptable letter for Turkish Lemmatizer.");
	}
	
}
