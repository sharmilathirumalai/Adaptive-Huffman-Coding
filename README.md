## Adaptive Huffman
A adaptive form of Huffaman Coding algorithm that compress and decompress the file by encoding & decoding the data in binary form.

## Data Structures
* Huffman Tree - Tree constructed by [Huffman Node](https://github.com/sharmilathirumalai/Adaptive-Huffman-Coding/blob/master/src/filecompression/HuffmanNode.java)

```java
class HuffmanNode {
 public int frequency;
 public String data;
 public HuffmanNode right;
 public HuffmanNode left;
}
  ```
  
* [Custom Heap](https://github.com/sharmilathirumalai/Adaptive-Huffman-Coding/blob/master/src/util/Heap.java) - A Priority Heap for Huffman Node.
* [Hash Map](https://github.com/sharmilathirumalai/Adaptive-Huffman-Coding/blob/master/src/filecompression/Huffman.java#L15) - Symbol Table.

## Sample I/O

Input

`the time has come the walrus said to talk of many things`

Output 
* Basic Huffman ` |11|a|5|c|1|#|1|d|1|e|4|f|1|g|1|h|4|i|3|k|1|l|2|m|3|n|2|o|3|r|1|s|4|t|6|u|1|w|1|y|1
01011011100000101110101101100001101111110000010100101110110110000010110111000010011011111010110111010111110000010001111111011011000001001110001011111010110010100011110110100011011111110010011100010110111101111001001001000101000`

* Adaptive Huffman `0t011e011 101111011m1111110111111101111011h1111011a1111011s11111110111111011o111111111101111111101111111110111111101111111111101111111101111111110111111011w1111111111110111111011l111111011r111111011u1111111111111011111111101111011011100i11011100d1011100110110101110011110110111111011100k10111011011011100f10110001111011011100n11011100y101110011111011111101111111111011011100g`

Packages
--------

- Default: Has the main class
- Util: Has heap class that implement Min-Heap Data Structure
- FileCompression: Has interface and classes related to huffman coding

Classes & interface
-------------------

- Filecomperssor: interface that has the major functions of huffman coding.
- Huffman: Has the method to encode and decode the given file.
- HuffmanNode: A template of huffman node and its behaviour.
- AdaptiveHuffman : has encode method to perform adaptive huffman

Method
------

- constructMinHeap #util.Heap
                 A Min-heap implementation using HuffmanNode.

- insertNode #util.Heap
                 Inserts a new node into min-heap and rebuilds it.

- compare #util.Heap
                 Method to compare and find the node that has less value.

- encode #filecompression.Huffman
                 Reads and encodes the file content as 0's and 1's.
                 Writes the meta information - words frequency as firstline in output file.
                 Writes the encoded value to output file.

- constructTree #filecompression.Huffman
                 Constructs huffman tree from given heap set and returns it to encode function.

- decode #filecompression.Huffman
                  Constructs the frequencyMap from the meta information.
                  Rebuilds the tree with constructed frequencyMap.
                  Replaces the bit values with respective characters.

- codeBook #filecompression.Huffman
                  Constructs the map of Character and its respective encoding from the constructed huffman tree.


Assumptions & Constraints
-------------------------

- Tree has the maximum size of 256 limited to the total number of alphabets.
- "#" & "| will not be present in file content.
- The file does not contain any non-printable characters

References
----------

1) Let's Build a Min Heap – randerson112358 – Medium [https://medium.com/@randerson112358/lets-build-a-min-heap-4d863cac6521]
2) Regular-Expressions.info - Regex Tutorial, Examples and Reference - Regexp Patterns [https://www.regular-expressions.info/]
 
