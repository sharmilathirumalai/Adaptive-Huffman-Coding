import java.io.File;
import java.util.Scanner;

import filecompression.AdaptiveHuffman;
import filecompression.Huffman;

public class Main {
	// Main method - Gets the input filename, output filename and the operation to be performed
	public static void main( String[] args ) throws Exception {
		Scanner in = new Scanner( System.in );
		System.out.println("Input filename ?");
		String input = in.next();
		System.out.println("Output filename ?");
		String output = in.next();
		System.out.println("Input 'E' for encoding, 'D' for decoding and A for adaptive encoding....");
		char option = in.next().charAt(0);

		if(!new File(input).exists()) {
			System.out.println("File not found");
			in.close();
			return;
		}
		
		// Invokes the respective method according to the inputed option.
		switch(option) {
		case 'E' :
			encode(input, output);
			break;
		case 'D' :
			decode(input, output);
			break;
		case 'A' :
			adaptiveEncoding(input, output);
			break;

		default:
			System.out.println("Invalid Option");
		}
		in.close();
	}

	static void encode(String input, String output) throws Exception {
		Huffman filecompressor= new Huffman();
		if(filecompressor.encode(input, 0 , false, output)) {
			System.out.println("Given file has been encoded Successfully");
		}

	}

	static void decode(String input, String output) throws Exception {	
		Huffman filecompressor= new Huffman();
		if(filecompressor.decode(input, output)) {
			System.out.println("Given file has been decoded Successfully");
		}
	}
	
	static void adaptiveEncoding(String input, String output) throws Exception {
		AdaptiveHuffman filecompressor= new AdaptiveHuffman();
		Scanner scan = new Scanner( System.in );
		System.out.println("Level (0-9)?");
		int level = scan.nextInt();
		scan.close();
		if(filecompressor.encode(input, level, false,  output)) {
			System.out.println("Given file has been encoded Successfully");
		}
	}

}
