package edu.drexel.psal.anonymouth.utils;

public class TrieTest{
	
	TrieNode trie;
	private final int OFFSET = 97;
	
	public void addWord(String letter){
		char[] theWord = letter.toLowerCase().toCharArray();
		TrieNode t = this.trie;
		for(char c:theWord){
			t = addLetter(t,c);
		}
	}
	
	public TrieNode addLetter(TrieNode t,char c){
		if(t.children[c-OFFSET] == null)
			t.children[c - OFFSET] = new TrieNode();
		return t.children[c - OFFSET];
	}
	
		
	public boolean find(String word){
		char[] chars = word.toLowerCase().toCharArray();
		TrieNode t = this.trie;
		for(char c:chars){
			if(t.children[c-OFFSET] == null)
					return false;
			t = t.children[c-OFFSET];
		}
		return true;
	}
	
	public void addWords(String[] words){
		for(String word:words){
			addWord(word);
		}
	}
	
	public static void main(String[] args){
		TrieTest tt = new TrieTest();
		tt.trie = new TrieNode();
		tt.addWords(new String[]{"and","soup","coffee","basketball"});
		System.out.println(tt.find("and"));
		System.out.println(tt.find("was"));
	}
}

class TrieNode {
	protected TrieNode[] children = new TrieNode[26];
}
