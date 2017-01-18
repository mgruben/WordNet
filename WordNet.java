
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;
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
    RedBlackBST<String, Bag<Integer>> nouns;
    Set<String> allNouns;
    Digraph G;
    SAP sap;
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
        if (synsets == null | hypernyms == null)
            throw new java.lang.NullPointerException();
        
        // Initialize instance variables
        nouns = new RedBlackBST<>();
        allNouns = new TreeSet<>();
        
        // Handle the given files
        In synIn = new In(synsets);
        In hypIn = new In(hypernyms);
        
        // Keep count of how many vertices there are
        int verts = 0;
        
        // Parse the synsets file
        while (synIn.hasNextLine()) {
            verts++;
            /** 
             * l[0] = id
             * l[1] = all the nouns separated by spaces
             * l[2] = gloss
             */
            String[] l = synIn.readLine().split(",");
            int id = Integer.parseInt(l[0]);
            for (String noun: l[1].split(" ")) {
                boolean seen = allNouns.add(noun);
                if (seen) {
                    nouns.get(noun).add(id);
                }
                else {
                    Bag<Integer> b = new Bag<>();
                    b.add(id);
                    nouns.put(noun, b);
                }
            }
        }
        
        // Initialize our vertex-indexed digraph
        G = new Digraph(verts);
        
        // Parse the hypernyms file
        while (hypIn.hasNextLine()) {
            String[] e = hypIn.readLine().split(",");
            int v = Integer.parseInt(e[0]);
            for (int i = 1; i < e.length; i++) {
                G.addEdge(v, Integer.parseInt(e[i]));
            }
        }
        // TODO check for rooted DAG and throw IllegalArgumentException
        
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
        if (nounA == null | nounB == null) 
            throw new java.lang.NullPointerException();
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
        if (nounA == null | nounB == null) 
            throw new java.lang.NullPointerException();
        return sap.ancestor(nouns.get(nounA), nouns.get(nounB));
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet("testing/synsets.txt", "testing/hypernyms.txt");
        System.out.println(wn.isNoun("nin-sin"));
        System.out.println(wn.isNoun("World of Warcraft"));
    }
}

