/**
 * 
 * Copyright 2011, 2013 Baturman SEN
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.baturman.nlp.lemmatizers.turkish;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Stack;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Turkish Lemmatizer class. It provides users method to lemmatize given word. 
 * 
 * @author Baturman SEN
 *
 */
public class TurkishLemmatizer {

	private Vector<Vector<Vector<Stem>>> stems = new Vector<Vector<Vector<Stem>>>();
	private static Locale TR_LOCALE = new Locale("tr", "TR");
	private static final int EN_UZUN_KOK_KONTROLU = 0;
	private static final int UNSUZ_YUMUSAMA_KONTROLU = 1; // pçtk olayı
	private static final int UNLU_DARALMASI_KONTROLU = 2; // bekl-e-mek bekl-i-yor olayı
	private static final int UNLU_DUSMESI_KONTROLU = 3; // oğul oğlum olayı
	private static final int UNSUZ_DUSMESI_KONTROLU = 4; // küçücük, ufacık, yükselmek, alçalmak
	private static final int PEKISTIRME_KONTROLU = 5; // sapasağlam -> sağlam

	private ArrayList<String> candidates;
	private Stack<Tracer> traceStack;

	private boolean stemFound = false;

	
	/**
	 * Default constructor. Initilizes stem list with empty {@link Vector}s.
	 */
	public TurkishLemmatizer() {
		initializeStemList();
	}

	/**
	 * Initializes an empty two-level tree structure to save stems. 
	 */
	private void initializeStemList(){
		// Prepare an empty two-level tree structure
		for (int i = 0; i < TurkishAlphabet.ALPHABET.length; i++) {
			stems.add(i, new Vector<Vector<Stem>>()); // for first letters
			stems.elementAt(i).setSize(TurkishAlphabet.ALPHABET.length);
			for (int j = 0; j < TurkishAlphabet.ALPHABET.length; j++) {
				stems.elementAt(i).add(j, new Vector<Stem>()); // for second letters
			}
		}
	}

	/**
	 * Adds and validates stem to stem list.
	 * 
	 * @param stem {@link Stem} to add
	 * @throws NotAcceptableCharacterException if stem contains letter that does not exists in standard Turkish Latin alphabet.
	 */
	public void addStem(String stem) throws NotAcceptableCharacterException{
		for (int i = 0; i < stem.length(); i++) {
			char letter = stem.charAt(i);
			int position = TurkishAlphabet.getPosition(letter);
			if (position == -1){
				throw new NotAcceptableCharacterException(letter, stem, i);	
			}
		}

		stem = stem.toLowerCase(TR_LOCALE);
		char firstLetter = stem.charAt(0);
		char secondLetter = stem.charAt(1);

		int firstLetterIndex = TurkishAlphabet.getPosition(firstLetter);
		int secondLetterIndex = TurkishAlphabet.getPosition(secondLetter);

		Stem s = new Stem(stem);
		stems.get(firstLetterIndex).get(secondLetterIndex).add(s);
	}

	/**
	 * Finds stem of a given word. This function allows words that contains more than or equal 2 characters. Passing string that has less than
	 * 2 characters will throw {@link StringIndexOutOfBoundsException}. This function returns first found stem as a result. <b>HOWEVER</b>, stem of
	 * the word may not be the return value. User <code>getAllCandidates()</code> function to retrieve all candidate stems. The correct stem might be
	 * one of them. 
	 *  
	 * @param word String to be lemmatized.
	 * @return longest stem candidates.
	 * @throws NotAcceptableCharacterException if <code>word</code> contains non-latin Turkish characters.
	 * @throws StringIndexOutOfBoundsException if word contains less than two characters.
	 */
	@SuppressWarnings("unchecked")
	public String lemmatize(String word) throws NotAcceptableCharacterException, StringIndexOutOfBoundsException{
		
		// Handle ÜNLÜ DEĞİŞİMİ. Only seen in sana and bana
		if (word.equalsIgnoreCase("sana")){
			return "sen";
		}
		
		if (word.equalsIgnoreCase("bana")){
			return "ben";
		}
		
		// Validate word
		for (int i = 0; i < word.length(); i++) {
			char letter = word.charAt(i);
			int position = TurkishAlphabet.getPosition(letter);
			if (position == -1){
				throw new NotAcceptableCharacterException(letter, word, i);	
			}
		}

		// Initialize trace stack
		traceStack = new Stack<Tracer>();
		candidates = new ArrayList<String>();

		// Check word and load stem list depending on first two character
		char firstCharacter = word.charAt(0);
		char secondCharacter = word.charAt(1);
		int firstPos = TurkishAlphabet.getPosition(firstCharacter);
		int secondPos = TurkishAlphabet.getPosition(secondCharacter);

		Vector<Stem> list = (Vector<Stem>) stems.get(firstPos).get(secondPos).clone();

		String candidate = "";
		
		/**
		 * ÜNSÜZ YUMUŞAMA KONTROLÜ 
		 * 
		 * p,ç,t,k -> b,c,d,ğ,g
		 * 
		 * dönüşmesi
		 */

		// Check that word may have two letter stem and second character of the stem might be softened.
		// So, we should also load stems that second letter is not softened 
		// For example, word "edildi" has "et" as stem, however we are loading stems that starts with ed.
		// So lemmatizer fails to find correct stem.

		switch (secondCharacter) {
		case 'd':
			secondPos = TurkishAlphabet.getPosition('t');
			break;
		case 'c':
			secondPos = TurkishAlphabet.getPosition('ç');
			break;
		case 'g':
			secondPos = TurkishAlphabet.getPosition('k');
			break;
		case 'ğ':
			secondPos = TurkishAlphabet.getPosition('k');
			break;
		case 'b':
			secondPos = TurkishAlphabet.getPosition('p');
			break;
		default:
			break;
		}

		list.addAll(stems.get(firstPos).get(secondPos));

		candidate = findLongestMatchedStem(list, word, TurkishLemmatizer.UNSUZ_YUMUSAMA_KONTROLU);

		if (candidate.length() > 0){
			traceStack.push(new Tracer(TurkishLemmatizer.UNSUZ_YUMUSAMA_KONTROLU, true));
			candidates.add(candidate);
		}else{
			traceStack.push(new Tracer(TurkishLemmatizer.UNSUZ_YUMUSAMA_KONTROLU, false));
		}


		/**
		 * 
		 * ÜNLÜ DARALMASI KONTROLÜ
		 * 
		 * a ve e ile biten sözcüklerin -yor eki aldığında a ve e'nin i,ı,u,ü dönüşmesi
		 * bekl-e-yor -> bekl-i-yor
		 * 
		 * d-e-yorum -> d-i-yor
		 */

		// Check that word may have two letter stem for UNLU daralması
		// diyorum -> de stems that second letter e,a should also be loaded. before checking for unlu daralması

		secondPos = TurkishAlphabet.getPosition('e');
		list.addAll(stems.get(firstPos).get(secondPos));

		secondPos = TurkishAlphabet.getPosition('a');
		list.addAll(stems.get(firstPos).get(secondPos));

		candidate = findLongestMatchedStem(list, word, TurkishLemmatizer.UNLU_DARALMASI_KONTROLU);
		if (candidate.length() > 0){
			traceStack.push(new Tracer(TurkishLemmatizer.UNLU_DARALMASI_KONTROLU, true));
			candidates.add(candidate);
		}else{
			traceStack.push(new Tracer(TurkishLemmatizer.UNLU_DARALMASI_KONTROLU, false));
		}

		/**
		 * 
		 * ÜNLÜ DÜŞMESİ KONTROLÜ
		 * 
		 * İkinci hecesinde dar ünlü bulunan sözcükler ünlüyle başlayan ek aldığında ünlü düşmesi görülür.
		 * 
		 * sabır -> sabrım
		 * oğul -> oğlum
		 * gönül -> gönlüm
		 * 
		 */
		candidate = findLongestMatchedStem(list, word, TurkishLemmatizer.UNLU_DUSMESI_KONTROLU);
		if (candidate.length() > 0){
			traceStack.push(new Tracer(TurkishLemmatizer.UNLU_DUSMESI_KONTROLU, true));
			candidates.add(candidate);
		}else{
			traceStack.push(new Tracer(TurkishLemmatizer.UNLU_DUSMESI_KONTROLU, false));
		}

		/**
		 * 
		 * ÜNSÜZ DÜŞMESİ
		 * 
		 * Kimi sözcüklerde türetme ve birleştirme sırasında "ünsüz düşmesi" görülür.
		 * 
		 * küçük-cük →  küçücük
		 * ufak-cık  →  ufacık
		 * yüksek-l  →  yüksel–
		 * alçak-l   →  alçal–
		 * seyrek-l  →  seyrel–
		 * 
		 */
		candidate = findLongestMatchedStem(list, word, TurkishLemmatizer.UNSUZ_DUSMESI_KONTROLU);
		if (candidate.length() > 0){
			traceStack.push(new Tracer(TurkishLemmatizer.UNSUZ_DUSMESI_KONTROLU, true));
			candidates.add(candidate);
		}else{
			traceStack.push(new Tracer(TurkishLemmatizer.UNSUZ_DUSMESI_KONTROLU, false));
		}

		/**
		 * PEKİŞTİRME KONTROLU
		 * 
		 * Türkçede bazı kelimeler önüne ek alarak pekiştirilirler.
		 * 
		 * kırmızı -> kıpkırmızı
		 * sağlam -> sapasağlam
		 * gündüz -> güpegündüz
		 *
		 */
		candidate = findLongestMatchedStem(list, word, TurkishLemmatizer.PEKISTIRME_KONTROLU);
		if (candidate.length() > 0){
			traceStack.push(new Tracer(TurkishLemmatizer.PEKISTIRME_KONTROLU, true));
			candidates.add(candidate);
		}else{
			traceStack.push(new Tracer(TurkishLemmatizer.PEKISTIRME_KONTROLU, false));
		}
		
		
		String [] allCandidates = this.getAllCandidates();
		String longest = "";
		for (int i = 0; i < allCandidates.length; i++) {
			if (allCandidates[i].length() > longest.length()){
				longest = allCandidates[i];
			}
		}
		if (allCandidates.length < 1){
			stemFound = false;
		}else{
			stemFound = true;
		}
		
		/**
		 * EN UZUN KÖK KONTROLÜ 
		 */

		// First check if there is a longest matched stem in word

		candidate = findLongestMatchedStem(list, word, TurkishLemmatizer.EN_UZUN_KOK_KONTROLU);

		if (candidate.length() > 0){
			traceStack.push(new Tracer(TurkishLemmatizer.EN_UZUN_KOK_KONTROLU, true));
			candidates.add(candidate);
			return candidate;
		}else{
			traceStack.push(new Tracer(TurkishLemmatizer.EN_UZUN_KOK_KONTROLU, false));
		}
		
		return longest.length() < 1 ? word:longest;
	}

	/**
	 * Finds longest matched stem of <code>word</code> in given <code>stemList</code> by considering given <code>control</code> variable.
	 * 
	 * @param stemList {@link Stem} list to be scanned.
	 * @param word word to be found
	 * @param control control object.
	 * <br/>
	 * <b>Control objects</b><br/>
	 * <ol>
	 * <li>EN_UZUN_KOK_KONTROLU</li>
	 * <li>UNSUZ_YUMUSAMA_KONTROLU</li>
	 * <li>UNLU_DARALMASI_KONTROLU</li>
	 * <li>UNLU_DUSMESI_KONTROLU</li>
	 * <li>UNSUZ_DUSMESI_KONTROLU</li>
	 * <li>PEKISTIRME_KONTROLU</li>
	 * </ol> 
	 * 
	 * @return stem of given word. If stem cannot be found an empty string returns.
	 */
	private String findLongestMatchedStem(Vector<Stem> stemList, String word, int control){
		String longest = "";

		for (Stem stem : stemList) {
			Matcher m1;
			Matcher m2;

			switch (control) {
			case TurkishLemmatizer.EN_UZUN_KOK_KONTROLU:
				m1 = stem.getPattern().matcher(word);
				if (m1.find() && m1.start() == 0){
					if (longest.length() < stem.getStem().length()){
						longest = stem.getStem();
					}
				}

				break;
			case TurkishLemmatizer.UNSUZ_YUMUSAMA_KONTROLU:

				if (stem.getStem().endsWith("k")){
					// if stem ends with k softening conversion can be either g or ğ
					m1 = Pattern.compile(stem.getSoftenedConversion()[0]).matcher(word);
					m2 = Pattern.compile(stem.getSoftenedConversion()[1]).matcher(word);
					if ((m1.find() && m1.start() == 0) || (m2.find() && m2.start() == 0)){
						if (longest.length() < stem.getStem().length()){
							longest = stem.getStem();
						}
					}
				}else{
					// otherwise get softened version
					m1 = Pattern.compile(stem.getSoftenedConversion()[0]).matcher(word);
					if (m1.find() && m1.start() == 0){
						if (longest.length() < stem.getStem().length()){
							longest = stem.getStem();
						}
					}
				}


				break;
			case TurkishLemmatizer.UNLU_DARALMASI_KONTROLU:
				String s = stem.getStem();
				if (s.endsWith("e") || s.endsWith("a")){
					String [] daralmis = stem.getDaralmisHalleri();
					for (int i = 0; i < daralmis.length; i++) {
						m1 = Pattern.compile(daralmis[i]).matcher(word);
						if (m1.find() && m1.start() == 0){
							if (longest.length() < stem.getStem().length()){
								longest = stem.getStem();
								break;
							}
						}
					}

				}
				break;

			case TurkishLemmatizer.UNLU_DUSMESI_KONTROLU:
				if (stem.getStem().length() > 3){
					String ltc = stem.getStem().substring(stem.getStem().length()-3, stem.getStem().length());
					if (ltc.indexOf('i') > 0 || ltc.indexOf('ı') > 0 || ltc.indexOf('u') > 0 || ltc.indexOf('ü') > 0){
						m1 = Pattern.compile(stem.getUnluDusmusHali()).matcher(word);
						if (m1.find() && m1.start() == 0){
							if (longest.length() < stem.getStem().length()){
								longest = stem.getStem();
							}
						}
					}
				}
				break;

			case TurkishLemmatizer.UNSUZ_DUSMESI_KONTROLU:
				m1 = Pattern.compile(stem.getUnsuzDusmeliHali()).matcher(word);
				if (m1.find() && m1.start() == 0){
					if (longest.length() < stem.getStem().length()){
						longest = stem.getStem();
					}
				}
				break;

			case TurkishLemmatizer.PEKISTIRME_KONTROLU:
				m1 = stem.getPattern().matcher(word);
				if (m1.find() && (m1.end() == stem.getStem().length() + m1.start())){
					if (longest.length() < stem.getStem().length()){
						longest = stem.getStem();
					}
				}
				break;
				
			}
		}
		return longest;
	}

	/**
	 * Checks whether given word is lemmatized.
	 * 
	 * @return <code>true</code> if lemmatization process is successful.
	 */
	public boolean isLemmatizationSuccessful(){
		return this.stemFound;
	}

	/**
	 * This function returns which controls have been applied to word to obtain lemma.
	 * 
	 * @return trace stack string.
	 */
	public String getTraceExplanation(){
		String message = "Trace Stack: ";
		while(!traceStack.isEmpty()){
			Tracer t = traceStack.pop();
			message += "[" + t.getControlName() + "=" + t.getResult() + "] <- ";
		}
		return message;
	}

	/**
	 * @return list of all stems found in given controls.
	 */
	public String [] getAllCandidates(){
		Object [] stems = new HashSet<String>(this.candidates).toArray();
		String [] candidates = new String[stems.length];
		for (int i = 0; i < stems.length; i++) {
			candidates[i] = stems[i].toString();
		}

		return candidates;
	}


}
