package experiment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HuffmanCode {
	private Map<Character, String> codeTable;
	
	// constructor
	public HuffmanCode(){
		this.codeTable = new HashMap<Character, String>();
	}
	
	// Definition of Node
	class Node{
		public int times;
		public char c;
		public Node left;
		public Node right;
		public Node(char c, int times) {
			this.c = c;
			this.times = times;
			this.left = null;
			this.right = null;
		}
	}
	
	// count char frequency
	private Map<Character, Integer> getCharCount(String s){
		Map<Character, Integer> resultMap = new HashMap<Character, Integer>();
		if(s == null || s.length() == 0)
			return resultMap;
		for(int i = 0 ; i < s.length(); i++) {
			int count = resultMap.getOrDefault(s.charAt(i), 0);
			resultMap.put(s.charAt(i), count + 1);
		}
		return resultMap;
	}
	
	// get List of Node
	private List<Node> getNodeList(Map<Character, Integer> param){
		List<Node> nodeList = new ArrayList<HuffmanCode.Node>();
		if(param == null || param.size() == 0)
			return nodeList;
		for(Map.Entry<Character, Integer> entry : param.entrySet()) {
			Node node = new Node(entry.getKey().charValue(), entry.getValue().intValue());
			nodeList.add(node);
		}
		Collections.sort(nodeList, new Comparator<Node>() {
			@Override
			public int compare(Node node1, Node node2) {
				return node1.times - node2.times;
			}
		});
		return nodeList;
	}
	
	// construct Huffman tree
	private Node constructHuffTree(List<Node> nodeList) {
		if(nodeList == null || nodeList.size() == 0)
			return null;
		if(nodeList.size() == 1)
			return nodeList.get(0);
		Node node1 = nodeList.get(0);
		Node node2 = nodeList.get(1);
		Node node3 = new Node('#', node1.times + node2.times);
		node3.left = node1;
		node3.right = node2;
		
		nodeList.remove(0);
		nodeList.remove(0);
		int i = 0;
		while(i < nodeList.size() && node3.times > nodeList.get(i).times) {
			i++;
		}
		nodeList.add(i, node3);
		return constructHuffTree(nodeList);
	}
	
	// encode every char according to Huffman definition by dfs + backtracking 
	private void encodeTree(Node root, List<Integer> record) {
		if(root == null)
			return;
		if(root.left == null && root.right == null) {
			StringBuilder sb = new StringBuilder();
			for(Integer integer : record) {
				sb.append(integer);
			}
			this.codeTable.put(root.c, sb.toString());
			return;
		}
		record.add(0);
		encodeTree(root.left, record);
		
		record.remove(record.size() - 1);
		record.add(1);
		encodeTree(root.right, record);
		record.remove(record.size() - 1);
	}
	
	private void doPreliminary(String str) {
		Map<Character, Integer> charMap = this.getCharCount(str);
		List<Node> nodeList = this.getNodeList(charMap);
		Node rootNode = this.constructHuffTree(nodeList);
		encodeTree(rootNode, new ArrayList<Integer>());
	}
	
	// encode str;
	public String encode(String str) {
		if(str == null || str.length() == 0)
			return str;
		doPreliminary(str);
		StringBuilder sb = new StringBuilder();
		for(char c : str.toCharArray()) {
			sb.append(this.codeTable.get(c));
		}
		return sb.toString();
	}
	
	public Map<Character, String> getCodeTable() {
		return new HashMap<Character, String>(this.codeTable);
	}
	
	// unit test
	public static void main(String args[]) {
		HuffmanCode code = new HuffmanCode();
		String test = "ABBACCCDDDDE";
		String string = code.encode(test);
		System.out.println(string);
	}
	
}
