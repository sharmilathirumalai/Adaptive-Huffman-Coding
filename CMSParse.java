import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class CMSParse {

	static String readNextValidLine(BufferedReader br) throws IOException {
		String currentLine;
		while ((currentLine = br.readLine()) != null) {
			currentLine = currentLine.trim();
			if(!currentLine.isEmpty()) {
				break;
			}
		}
		return currentLine;
	}

	/* Main method
	 * 
	 * Use {@link #checkForAnnotation(String str)} to replace the notations.
	 * */
	public static void main(String... args) throws IOException {
		System.out.println("filename?");
		BufferedReader brFileName = new BufferedReader(new InputStreamReader(System.in));
		final String file = brFileName.readLine();	
		System.out.flush();

		if(!new File(file).exists()) {
			System.out.println("File not found");
			return;
		}

		String currentLine;
		Stack<String> stack = new Stack<String>();
		BufferedReader br =new BufferedReader(new FileReader(file));
		HTMLTranslator translator = new HTMLTranslator();

		stack.push("html");
		System.out.println("<html>");

		while((currentLine = br.readLine()) != null) {
			currentLine = currentLine.trim();
			if(!currentLine.isEmpty()) {
				if(currentLine.matches("(title: ).*")) {
					System.out.println("<head>");
					System.out.println(currentLine.replace("title: ", "<title>") + "</title>\n</head>");
					currentLine = readNextValidLine(br);		
				}
				break;

			}
		}

		if(currentLine != null) {
			stack.push("body");
			System.out.println("<body>");
			String htmlLine = translator.checkForAnnotation(currentLine.trim());
			Boolean isList = htmlLine.matches("(<li>).*");
			String enclosingTag = isList ? "ul" : "p";
			stack.push(enclosingTag);
			System.out.println("<"+enclosingTag+">");
			System.out.println(htmlLine);
		}

		while ((currentLine = br.readLine()) != null) {
			currentLine = currentLine.trim();
			if(currentLine.isEmpty()) {
				System.out.println("</"+stack.pop()+">");
				currentLine = readNextValidLine(br);		

				if(currentLine != null) {
					String htmlLine = translator.checkForAnnotation(currentLine);
					Boolean isList = htmlLine.matches("(<li>).*");
					String enclosingTag = isList ? "ul" : "p";
					stack.push(enclosingTag);
					System.out.println("<"+enclosingTag+">");
					System.out.println(htmlLine);
				} else {
					break;
				}
			} else {
				System.out.println(translator.checkForAnnotation(currentLine));
			}
		}

		int sLength = stack.size();		
		for(int i=1; i<= sLength; i++) {
			System.out.println("</"+stack.pop()+">");
		}

		br.close();
	}
}

class HTMLTranslator {
	private static Map<Character, String> tagMap =  new HashMap<Character, String>();
	private static char[] blocknotations= {'*','_','%'};
	private static char[] wordnotations = {'!'};
	private static char[] linenotations = {'-'};
	private static char[] notations;

	/*Constructor to initializes tag Map and notations */
	HTMLTranslator() {
		notations= new char[ blocknotations.length + linenotations.length + wordnotations.length ];
		System.arraycopy(blocknotations, 0, notations, 0, blocknotations.length);
		System.arraycopy(linenotations, 0, notations, blocknotations.length, linenotations.length);
		System.arraycopy(wordnotations, 0, notations, (blocknotations.length + linenotations.length), wordnotations.length);

		tagMap.put('*', "b");
		tagMap.put('_',"i");
		tagMap.put('%',"u");
		tagMap.put('!', "b");
		tagMap.put('-', "li");	
	}

	/* Finds the closing annotation and replaces with Appropriate HTML Tag 
	 * 
	 * @param substr  String to be parsed
	 * @param symbol  Symbol that need to be replaced
	 * @param isNested indicates nested tags
	 * @return Returns the parsed string
	 */
	private String replaceWithTag(String substr, char symbol, Boolean isNested) {

		// Checks whether string  preceding the symbol is not empty; if so returns the string as such
		if(substr.isEmpty()) {
			return substr;
		}

		if(contains(wordnotations, symbol)) {

			String htmlTag = tagMap.get(symbol);
			int indexOf = (substr.indexOf(" ") != -1) ? substr.indexOf(" ") : substr.length();
			Boolean isAnnotation = Character.toString(substr.charAt(0)).matches("^[a-zA-Z0-9]*$");
			String processedStr =  isAnnotation ? "<"+ htmlTag+ ">" + substr.substring(0, indexOf) + "</"+ htmlTag+ ">" : symbol + substr.substring(0, indexOf); 
			return processedStr + checkForAnnotation(substr.substring(indexOf));
		} 

		if (contains(linenotations, symbol)) {

			String htmlTag = tagMap.get(symbol);
			Boolean isAnnotation = substr.substring(0,1).matches("^\\s+[a-zA-Z0-9]*$");
			return isAnnotation ? "<"+ htmlTag+ ">" + checkForAnnotation(substr.substring(1, substr.length())) + "</"+ htmlTag+ ">" : symbol + checkForAnnotation(substr.substring(0, substr.length()));
		}

		/* Checks for Block notations in between line and calls recursively if notations
		 * are been nested
		 */
		for (int i=0; i< substr.length(); i++) {

			char currentChar = substr.charAt(i);

			if(contains(blocknotations, currentChar)) {

				if(currentChar == symbol) {
					String htmlTag = tagMap.get(symbol);
					if(isNested) {
						return "<"+ htmlTag+ ">" + substr.substring(0, i) + "</"+ htmlTag+ ">" + substr.substring(i+1);
					}
					return "<"+ htmlTag+ ">" + substr.substring(0, i) + "</"+ htmlTag+ ">" + checkForAnnotation(substr.substring(i+1));

				} else {

					substr = substr.substring(0, i)+ replaceWithTag(substr.substring(i+1), currentChar, true);
				}
			}
		}

		return symbol + substr;
	}

	/* Utility function to check whether an given element is present in the array*/
	boolean contains(char[] array, char element) {
		boolean result = false;
		for(char ch : array){
			if(ch == element){
				result = true;
				break;
			}
		}

		return result;
	}

	/* Main invocation function;
	 *
	 * @param str Gets the line to be parsed
	 * @return Returns the processed line.
	 */

	public String checkForAnnotation(String str) {

		for(int index=0; index<str.length(); index++) {
			char currentChar = str.charAt(index);

			if(contains(notations, currentChar)) {
				return str.substring(0, index) + replaceWithTag(str.substring(index+1), currentChar, false);
			}
		}

		return str;
	}
}
