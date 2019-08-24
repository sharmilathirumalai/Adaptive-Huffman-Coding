package filecompression;

public class HuffmanNode {
	public int frequency;
	public String data;
	
	// pointers to left and right child of the node
	public HuffmanNode right;
	public HuffmanNode left;

	public HuffmanNode(String data, int frequency) {
		this.frequency = frequency;
		this.data = data;
		this.left = null;
		this.right = null;
	}
	
	// Constructor used to create Huffman Tree by combining the char in frequency map.
	public HuffmanNode(HuffmanNode left, HuffmanNode right) {
		this.frequency = left.frequency + right.frequency;
		this.data = "ROOT";
		this.left = left;
		this.right = right;
	}

	public HuffmanNode() {
		//Default Constructor
	}
}
