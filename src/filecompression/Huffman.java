package filecompression;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import util.Heap;

public class Huffman implements FileCompressor {
	private static String newLineChar = "#";
	Map<Character, Integer> frequencyMap = new HashMap<Character, Integer>();
	static HuffmanNode tree = new HuffmanNode();

	// codebook returns the <character, bitvalue>Map
	public Map<Character, String> codebook() {
		// private inner class to construct codeMap
		class BinaryTree {   
			private Map<Character, String> codeMap = new HashMap<Character, String>();

			void traverse(HuffmanNode node, String code) {
				if(node == null) { return; }

				if(node.left  == null && node.right == null) {
					// Gets the character form leaf node and assigns the code.
					codeMap.put(node.data.charAt(0), code);
					return;
				}

				traverse(node.left, code + "0");
				traverse(node.right, code + "1");
			}

			public Map<Character, String> constructMap() {
				traverse(tree, "");
				return codeMap;
			}
		}; 

		return new BinaryTree().constructMap(); 
	}

	protected void constructTree(Heap heap) {
		while(heap.getSize() > 0) {
			/*
			 *  get the two smallest element and construct the tree
			 *  Repeat the process till we get single tree
			 */
			HuffmanNode left = heap.pop();
			HuffmanNode right = heap.pop();
			HuffmanNode root = new HuffmanNode(left, right);
			heap.insertNode(root);
			tree = root;
		}
	}

	// @function - Encodes the input_file and prints the bits output_file
	public boolean encode(String input_filename,  int level, boolean reset, String output_filename) {
		try {

			if(new File(input_filename).length() == 0) {
				PrintWriter writer = new PrintWriter(output_filename, "UTF-8");
				writer.close();
				return true;
			} 

			findFrequencies(input_filename);

		} catch (IOException e) {
			e.printStackTrace();
		}

		Heap heap = new Heap();
		heap.constructMinHeap(frequencyMap);
		constructTree(heap);
		Map<Character, String> codeMap = codebook();
		String metaInfo="";

		for(char c: frequencyMap.keySet()) { metaInfo += c + "|" + frequencyMap.get(c) + "|"; }

		try {
			PrintWriter writer = new PrintWriter(output_filename, "UTF-8");
			BufferedReader br = new BufferedReader(new FileReader(input_filename));
			String currentLine;

			writer.println(metaInfo.substring(0, metaInfo.length()-1));

			while((currentLine = br.readLine()) != null) {
				char[] line = currentLine.toCharArray();
				String code="";
				for(char c:line) { code += codeMap.get(c); }
				code += codeMap.get(newLineChar.charAt(0));
				writer.print(code);
			}
			br.close();
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 	

		return true;
	}

	// first pass implementation - reads and counts the number of occurrences
	private  Map<Character, Integer> findFrequencies(String file) throws IOException {
		BufferedReader br =new BufferedReader(new FileReader(file));
		String currentLine;

		while((currentLine = br.readLine()) != null) {
			currentLine += newLineChar;
			char[] characters = currentLine.toCharArray();
			for(char key:characters) {
				frequencyMap.put(key, frequencyMap.getOrDefault(key, 0) + 1);
			}
		}

		br.close();
		return frequencyMap;
	}

	//@function - Decodes the input_file and prints in output_file
	public boolean decode(String input_filename, String output_filename) {
		try {

			if(new File(input_filename).length() == 0) {
				PrintWriter writer = new PrintWriter(output_filename, "UTF-8");
				writer.close();
				return true;
			}

			BufferedReader br =new BufferedReader(new FileReader(input_filename));
			String metaInfo = br.readLine();

		
			String[] chunks = metaInfo.split("\\|");
			
		
			if(chunks.length % 2 != 0) { br.close(); throw new Exception("Corrupted File") ;}

			for(int i=0 ; i<chunks.length; i+=2) {
				frequencyMap.put(chunks[i].charAt(0), Integer.parseInt(chunks[i+1]));
			}

			Heap heap = new Heap();
			heap.constructMinHeap(frequencyMap);

			constructTree(heap);
			String encodedValue = br.readLine();
			br.close();


			PrintWriter writer = new PrintWriter(output_filename, "UTF-8");
			// Prints the decoded the value that is been computed by traversing the Huffman Tree
			writer.print(traverse(encodedValue));
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 	

		return true;
	}

	// Traverses and returns the char for the respective bits
	String traverse(String encoded_value) {
		HuffmanNode root = tree;
		String decoded_value= "";
		for(int i=0; i<encoded_value.length(); i++) {
			if(root.left == null && root.right == null) {
				if(root.data.equalsIgnoreCase(newLineChar)) {
					decoded_value += "\r\n";
				} else {
					decoded_value += root.data;

				}
				root = tree;
			}

			if(encoded_value.charAt(i) == '0') {
				root = root.left;
			} else if(encoded_value.charAt(i) == '1') {
				root = root.right;
			}
		}

		return decoded_value;	

	}
}

