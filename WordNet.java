
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Set;
import java.util.TreeSet;

/*
 * Copyright (C) 2017 Michael <GrubenM@GMail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 *
 * @author Michael <GrubenM@GMail.com>
 */
public class WordNet {
    
    // A lookup for nouns, both to check existence and to get their synset ids
    private RedBlackBST<String, Bag<Integer>> nouns;
    
    // An iterable of all nouns seen; also helps to correctly handle RedBlackBST
    private Set<String> allNouns;
    
    // The digraph for use in SAP
    private Digraph G;
    
    // Client program to check for cycles
    private DirectedCycle dc;
    
    // The SAP for running shortest-ancestral path queries
    private SAP sap;
    
    // The synset lookup table
    private ST<Integer, String> synMap;
    
    /**
     * Takes the name of the two input files, and constructs a WordNet.
     * 
     * A WordNet is a rooted Directed Acyclic Graph, in which each vertex is an
     * integer that represents a synset, and each edge denotes a hypernym
     * relationship.
     * 
     * Specifically, v -> w means that:
     * (1) v is a hyponym of w, and
     * (2) w is a hypernym of v
     * 
     * @param synsets The name of the input file containing synsets
     * @param hypernyms The name of the input file containing hypernyms
     * @throws NullPointerException if {@code synsets == null}
     * @throws NullPointerException if {@code hypernyms == null}
     * @throws IllegalArgumentException if the input does not correspond to a
     *         rooted DAG
     */
    public WordNet(String synsets, String hypernyms) {
        
        // Check for invalid input
        if (synsets == null || hypernyms == null)
            throw new java.lang.NullPointerException();
        
        // Initialize instance variables
        nouns = new RedBlackBST<>();
        allNouns = new TreeSet<>();
        synMap = new ST<>();
        
        // Handle the given files
        In synIn = new In(synsets);
        In hypIn = new In(hypernyms);
        
        // Record the number of synsets for use in declaring the digraph.
        // id will increase monotonically until we run out of synsets, at which
        // point it represents the number of synsets seen (minus 1).
        int id = 0;
        
        // Parse the synsets file
        while (synIn.hasNextLine()) {
            /** 
             * l[0] = id
             * l[1] = all the nouns separated by spaces
             * l[2] = gloss
             */
            String[] l = synIn.readLine().split(",");
            id = Integer.parseInt(l[0]);
            synMap.put(id, l[1]);
            for (String noun: l[1].split(" ")) {
                boolean isNewAdd = allNouns.add(noun);
                if (isNewAdd) {
                    Bag<Integer> b = new Bag<>();
                    b.add(id);
                    nouns.put(noun, b);
                }
                else {
                    nouns.get(noun).add(id);
                }
            }
        }
        
        // Initialize our vertex-indexed digraph
        G = new Digraph(id+1);
        
        // Parse the hypernyms file
        while (hypIn.hasNextLine()) {
            String[] e = hypIn.readLine().split(",");
            int v = Integer.parseInt(e[0]);
            for (int i = 1; i < e.length; i++) {
                G.addEdge(v, Integer.parseInt(e[i]));
            }
        }
        
        // Check for a cycle (i.e., input is not a DAG)
        dc = new DirectedCycle(G);
        if (dc.hasCycle()) throw new IllegalArgumentException();
        
        // Check for multiple roots
        int root = 0;
        for (String noun: allNouns) {
            for (int syn: nouns.get(noun)) {
                int c = 0;
                for (int adj: G.adj(syn)) {
                    c++;
                }
                if (c == 0) {
                    root++;
                    if (root > 1) throw new IllegalArgumentException();
                }
            }
        }
        
        // Initialize the SAP data structure
        sap = new SAP(G);
    }

    /**
     * Returns all WordNet nouns.
     * 
     * @return all WordNet nouns
     */
    public Iterable<String> nouns() {
        return allNouns;
    }

    /**
     * Is the word a WordNet noun?
     * 
     * @param word
     * @throws NullPointerException if {@code word == null}
     * @return {@code true} if <em>word</em> is a WordNet noun,
     *         {@code false} otherwise
     */
    public boolean isNoun(String word) {
        if (word == null) throw new java.lang.NullPointerException();
        return nouns.contains(word);
    }

    /**
     * Distance between nounA and nounB.
     * 
     * Distance is defined as the minimum length of any ancestral path between
     * any synset v of nounA and any synset w of nounB.
     * 
     * @param nounA 
     * @param nounB
     * @throws NullPointerException if {@code nounA == null}
     * @throws NullPointerException if {@code nounB == null}
     * @throws IllegalArgumentException if either <em>nounA</em> or
     *         <em>nounB</em> are not WordNet nouns
     * @return the distance between <em>nounA</em> and <em>nounB</em>
     */
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) 
            throw new java.lang.NullPointerException();
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
        return sap.length(nouns.get(nounA), nouns.get(nounB));
    }

    /**
     * A synset (second field of synsets.txt) that is the common ancestor of
     * nounA and nounB in a shortest ancestral path.
     * 
     * A shortest ancestral path is an ancestral path of minimum total length,
     * where length is a measure of the number of hops to get from a synset to
     * a hypernym of that synset.
     * 
     * @param nounA
     * @param nounB
     * @throws NullPointerException if {@code nounA == null}
     * @throws NullPointerException if {@code nounB == null}
     * @throws IllegalArgumentException if either <em>nounA</em> or
     *         <em>nounB</em> are not WordNet nouns
     * @return a synset that is the common ancestor of <em>nounA</em> and
     *         <em>nounB</em> in a shortest ancestral path
     */
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) 
            throw new java.lang.NullPointerException();
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
        return synMap.get(sap.ancestor(nouns.get(nounA), nouns.get(nounB)));
    }
            
    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet(args[0], args[1]);
        
//        while (!StdIn.isEmpty()) {
//            Iterable<Integer> b = wn.getSynsetIDs(StdIn.readString());
//            for (int s: b) {
//                StdOut.println(s + ": " + wn.getSynsetNames(s));
//            }
//        }
        
        while (!StdIn.isEmpty()) {
            String nounA = StdIn.readString();
            String nounB = StdIn.readString();
            StdOut.println("sap: " + wn.sap(nounA, nounB) +
                    " (" + wn.distance(nounA, nounB) + ")");
        }

    }
}

