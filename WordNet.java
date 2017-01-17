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
        if (synsets == null | hypernyms == null)
            throw new java.lang.NullPointerException();
    }

    /**
     * Returns all WordNet nouns.
     * 
     * @return all WordNet nouns
     */
    public Iterable<String> nouns() {

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
    }

    // do unit testing of this class
    public static void main(String[] args) {
        
    }
}

