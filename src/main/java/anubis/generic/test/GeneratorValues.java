package anubis.generic.test;

import org.springframework.stereotype.Component;

@Component("generatorValues")
public class GeneratorValues {

	public String generatorWordNumberLetters(int min, int max, boolean isUpperCase) {
		StringBuilder word = new StringBuilder();
		for(int i=0; i<generatorIntegerValue(min, max); i++) {
			if(generatorBoolean()) {
				word.append(generatorLetter(isUpperCase));
			} else {
				word.append(generatorNumber());
			}
		}
		return word.toString();
	}
	
	public char generatorLetter(Boolean isUpperCase) {
		char letter = (char) generatorIntegerValue(97, 122).intValue();
		if(isUpperCase) {
			letter = Character.toUpperCase(letter);
		} 
		return letter;
	}
	
	public char generatorNumber() {
		return (char) generatorIntegerValue(48, 57).intValue();
	}
	
	public boolean generatorBoolean() {
		return generatorIntegerValue(0, 1)==1 ? true : false;
	}
	
	public Integer generatorBooleanInt() {
		return generatorIntegerValue(0, 1);
	}
	
	public Integer generatorIntegerValue() {
		return new Double(Math.random() * 1000).intValue();
	}
	
	public Integer generatorIntegerValue(int inicio, int fim) {
		Double random = Math.floor(Math.random() * (fim - inicio + 1)) + inicio;
		return random.intValue();
	}
	
	public Double generatorDoubleValue() {
		return Math.random() * 1001;
	}
	
	public Double generatorDoubleValue(double inicio, double fim) {
		return (Math.random() * (fim - inicio + 1)) + inicio;
	}
	
	public String generatorJson(int quantidadeChaves) {
		StringBuilder json = new StringBuilder("{");
		for(int i=1; i<=quantidadeChaves; i++) {
			json.append("\"");
			json.append(generatorWordNumberLetters(1, 10, false));
			json.append("\"");
			json.append(":");
			json.append("\"");
			json.append(generatorWordNumberLetters(1, 10, false));
			json.append("\"");
			if(i != quantidadeChaves) {
				json.append(",");
			}
			
		}
		json.append("}");
		return json.toString();
	}
	
}
