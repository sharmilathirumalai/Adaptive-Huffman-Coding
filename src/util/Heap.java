package util;

import java.util.Iterator;
import java.util.Map;

import filecompression.HuffmanNode;

public class Heap {

	public HuffmanNode[] heapTree = new HuffmanNode[256];
	private int size = -1;

	/*
	 * @param nodeType - leftChild, rightChild and parent
	 * @param index - index for which the respective node has to be found
	 */
	private HuffmanNode get(String nodeType, int index) { 
		/* Gets the respective node by processing the index value
		 * Say 0-root, 1-left child, 2-right child
		 */
		if(nodeType == "leftChild") {
			return heapTree[2*index+1];
		} 

		if(nodeType == "rightChild") {
			return heapTree[2*index+2];
		} 

		return heapTree[index/2 - (index+1)%2];	
	}

	private boolean compare(HuffmanNode parent, HuffmanNode child) {
		/* Compares frequency to find the least node and goes 
		 * further to check characters if frequency resolves to be same 
		 * */
		if(parent.frequency > child.frequency) {
			return true;
		}

		if( parent.frequency == child.frequency && parent.data != "ROOT" && child.data !="ROOT" && parent.data.compareTo(child.data) > 0) {
			return true;
		}

		return false;
	}

	private boolean isHeapPptyViolated(int index) {
		// Checks whether the parent has the heap ppty preserved.
		HuffmanNode parent = get("parent", index);
		HuffmanNode currentnode = heapTree[index];
		return compare(parent, currentnode);
	}


	private void reBuildHeap(int index) {
		/* Rebuild the tree by pushing the element down
		 * top-bottom approach
		 */
		while(index <= size) {
			HuffmanNode leftHuffmanNode = get("leftChild", index);
			HuffmanNode rightHuffmanNode = get("rightChild", index);

			int smallest = index;

			if(leftHuffmanNode !=null && compare(heapTree[smallest], leftHuffmanNode)) {
				smallest = 2*index+1;
			}

			if(rightHuffmanNode != null && compare(heapTree[smallest], rightHuffmanNode)) {
				smallest = 2*index+2;
			}

			if(index != smallest) {
				swap(smallest);
				index = smallest;

			} else {
				break;
			}

		}

	}

	private void swap(int index) {
		HuffmanNode parent = get("parent", index);
		HuffmanNode currentnode = heapTree[index];

		heapTree[index/2 - (index+1)%2] = currentnode;
		heapTree[index]= parent;
	}

	public void insertNode(HuffmanNode node) {
		/* inserts the new element at the end and rebuilds the heap to preserve
		 * order property.
		 * bottom-top approach
		 */
		heapTree[++size] = node;
		int index = size;

		while(index != 0 && isHeapPptyViolated(index)) {
			swap(index);
			index = index/2 - (index+1)%2;
		}
	}

	public int getSize() {
		return this.size;
	}

	public HuffmanNode pop() {
		HuffmanNode root = heapTree[0];
		if(size > 0 ) {
			//gets the minimum element and rebuilds to tree
			HuffmanNode temp = heapTree[size];
			heapTree[0] = temp ;
			heapTree[size] = null;
			size--;
			reBuildHeap(0);
		} else {
			//case where there is only one element
			heapTree[0] = null;
			size--;
		}

		return root;
	}

	// Function to print elements in ascending order
	public void printHeap( ) {
		while(size >= 0 ) {
			HuffmanNode node = pop();
			System.out.println(node.data +""+ node.frequency);
		}
	}

	// Constructs a min-heap for given <char data, int frequency> map.
	public void constructMinHeap(Map<Character, Integer> frequencyMap) {
		Iterator<Map.Entry<Character, Integer>> it = frequencyMap.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry<Character, Integer> pair = it.next();
			HuffmanNode node = new HuffmanNode(Character.toString(pair.getKey()), pair.getValue());
			insertNode(node);
		}	
	}
}
