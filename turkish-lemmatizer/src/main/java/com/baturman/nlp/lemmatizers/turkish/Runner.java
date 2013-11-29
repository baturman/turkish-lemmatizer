package com.baturman.nlp.lemmatizers.turkish;

/**
 * Test class.
 * 
 * @author Baturman SEN
 *
 */
public class Runner {
	public static void main(String[] args) {

		TurkishLemmatizer tl = new TurkishLemmatizer();
		try {
			tl.addStem("kitap");
			tl.addStem("kulak");
			tl.addStem("ağaç");
			tl.addStem("yemek");
			tl.addStem("kalp");
			tl.addStem("çelenk");
			tl.addStem("metot");
			tl.addStem("hukuk");
			tl.addStem("genç");
			tl.addStem("et");
			tl.addStem("bekle");
			tl.addStem("kal");
			tl.addStem("özle");
			tl.addStem("solla");
			tl.addStem("de");
			tl.addStem("ye");
			tl.addStem("oğul");
			tl.addStem("gönül");
			tl.addStem("küçük");
			tl.addStem("nesil");
			tl.addStem("resim");
			tl.addStem("resmi");
			tl.addStem("alçak");
			tl.addStem("yüksek");
			tl.addStem("seyrek");
			tl.addStem("seyir");
			tl.addStem("af");
			tl.addStem("zan");
			tl.addStem("sağlam");
			tl.addStem("kırmızı");
			tl.addStem("yeşil");
			tl.addStem("sarı");
			tl.addStem("karın");
			tl.addStem("hastane");
			tl.addStem("baş");
			tl.addStem("başbakan");
			tl.addStem("elazığ");
			tl.addStem("koğuş");
			tl.addStem("sabır");
			tl.addStem("kork");
			tl.addStem("sen");

			String [] word = {"kitabım", "kulağım", "ağacımız", "yemeğe", "kalbim", "çelengi", "metodumuzu", "hukukun", "hukuğun", "gencecik", "ediliyor",
					"bekliyor", "kalmıyor", "özlüyorum", "solluyorum", "solladım", "diyorum", "diyerek", "deyince", "yiyerek", "yedirdi", "oğlum", "gönlüm",
					"küçüğüm", "neslimiz", "resminde", "resmi", "küçücük", "alçalmak", "yükselmek", "seyreldi", "seyrettim", "affetmek", "zannedersen", "sapasağlam",
					"kıpkırmızı", "yemyeşil", "sarardım", "sapsarı", "karnından", "karındaş", "hastanelik", "başından", "başbakanın", "elazığlı", "koğuşunda", "sabrım",
					"sabreden", "korktu", "sana"
			};
			
			for (int i = 0; i < word.length; i++) {
				String message = word[i] + " = ";
				String defaultStem = tl.lemmatize(word[i]);
				message += defaultStem;
				String [] list = tl.getAllCandidates();
				if (list.length > 1){
					message += " -> ";
					for (int j = 0; j < list.length; j++) {
						message += list[j] + " ";
					}
				}
				//message +="\n";
				//message += tl.getTraceExplanation();
				System.out.println(message);
				//System.out.println();
			}

		} catch (NotAcceptableCharacterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
