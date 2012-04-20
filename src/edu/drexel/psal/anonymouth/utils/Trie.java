package edu.drexel.psal.anonymouth.utils;

public class Trie{
	
	TrieNode trie=new TrieNode();
	private final char lastChar='{';
	private final int OFFSET = 96;
	
	public void addWord(String word){
		char[] theWord = word.toLowerCase().toCharArray();
		TrieNode t = this.trie;
		for(char c:theWord){
			t = addLetter(t,c);
		}
		addLetter(t,lastChar);
	}
	
	public TrieNode addLetter(TrieNode t,char c){
		if(c=='\'')
			c='`';
		if(t.children[c-OFFSET] == null)
			t.children[c - OFFSET] = new TrieNode();
		return t.children[c - OFFSET];
	}
	
		
	public boolean find(String word){
		char[] chars = word.toLowerCase().toCharArray();
		TrieNode t = this.trie;
		for(char c:chars){
			if(c=='\'')
				c='`';
			if(t.children[c-OFFSET] == null)
					return false;
			t = t.children[c-OFFSET];
		}
		if(t.children[27]==null)
			return false;
		return true;
	}
	
	public void addWords(String[] words){
		for(String word:words){
			addWord(word);
		}
	}
	
	public static void main(String[] args){
		Trie tt = new Trie();
		tt.trie = new TrieNode();
		tt.addWords(new String[]{"and","soup","coffee","basketball"});
		System.out.println(tt.find("and"));
		System.out.println(tt.find("was"));
	}
}

class TrieNode {
	protected TrieNode[] children = new TrieNode[28];

}
