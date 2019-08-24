package filecompression;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import util.Heap;

public class AdaptiveHuffman extends Huffman {
	Map<Character, String> codeMap = new HashMap<Character, String>();
	Heap heap = new Heap();

	public boolean encode(String input_filename, int level, boolean reset, String output_filename) {
		BufferedReader br;
		String currentLine;
		String encodedValue = String.valueOf(level) + (reset ? "1" : "0");
		
		try {
			br = new BufferedReader(new FileReader(input_filename));

			frequencyMap.put('~', 0);
			frequencyMap.put('|', 0);
			
			heap.constructMinHeap(frequencyMap);
			constructTree(heap);
			codeMap = codebook();

			while((currentLine = br.readLine()) != null) {
				int start_cursor = 0;
				double pow = 1;
				int end_cursor = (int) Math.pow(2, pow) - 1;
				while( end_cursor < currentLine.length()) {
					String substr = currentLine.substring(start_cursor, end_cursor);
					String newChar = codeMap.get('|');

					for(int i=0; i<substr.length(); i++) {
						encodedValue += insertNode(substr.charAt(i), newChar);
					}
					
					heap.constructMinHeap(frequencyMap);
					constructTree(heap);
					codeMap = codebook();
					if(pow  <= level) { pow++;}
					if( end_cursor == currentLine.length() - 1) { break;}
					int newLevel = (int) Math.pow(2, pow);
					int remainingStrLen = currentLine.substring(end_cursor).length()-1;
					start_cursor = end_cursor + 1;
					end_cursor= newLevel > remainingStrLen ? end_cursor + remainingStrLen : end_cursor + newLevel;
				}
				
				// # used as indication of newline
				frequencyMap.put('#', frequencyMap.getOrDefault('#', 0) + 1);

				heap.constructMinHeap(frequencyMap);
				constructTree(heap);
				codeMap = codebook();
			}
			
			br.close();
			PrintWriter writer = new PrintWriter(output_filename, "UTF-8");
			writer.println(encodedValue);
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	// Constructs the code and returns
	private String insertNode(char data, String newChar) {
		if(frequencyMap.containsKey(data)) {
			frequencyMap.put(data, frequencyMap.get(data)+1);
			String code = codeMap.get(data);
			incrementfrequency(data, code);
			return code;
			} else {
			HuffmanNode currNode = tree;
			while(currNode.right != null && currNode.left != null) {
				currNode = currNode.right;
			}
			
			currNode.left = new HuffmanNode(currNode.data, currNode.frequency);
			currNode.right = new HuffmanNode(Character.toString(data), 1);
			currNode = new HuffmanNode(currNode.left, currNode.right);
			frequencyMap.put(data, 1);

			codeMap = codebook();
			return newChar + data;
		}

	}

	private void incrementfrequency(char data, String code) {
		HuffmanNode currentRoot = tree;
		for(int i=0; i < code.length(); i++) {
			char currchar = code.charAt(i);
			if(currchar == '0') {
				currentRoot = currentRoot.left;
			} else if(currchar == '1') {
				currentRoot = currentRoot.right;
			}
		}
		
		if(data == currentRoot.data.charAt(0)) {
			currentRoot.frequency++;
		}
	}
}
