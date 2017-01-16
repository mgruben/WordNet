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
     * Takes the name of the two input files.
     * 
     * 
     * @param synsets
     * @param hypernyms 
     * @throws NullPointerException if {@code synsets == null}
     * @throws NullPointerException if {@code hypernyms == null}
     */
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null | hypernyms == null)
            throw new java.lang.NullPointerException();
    }

    /**
     * Returns all WordNet nouns.
     * 
     * @return 
     */
    public Iterable<String> nouns() {

    }

    /**
     * Is the word a WordNet noun?
     * 
     * @param word
     * @throws NullPointerException if {@code word == null}
     * @return 
     */
    public boolean isNoun(String word) {
        if (word == null) throw new java.lang.NullPointerException();
    }

    /**
     * Distance between nounA and nounB (defined below)
     * 
     * @param nounA
     * @param nounB
     * @throws NullPointerException if {@code nounA == null}
     * @throws NullPointerException if {@code nounB == null}
     * @return 
     */
    public int distance(String nounA, String nounB) {
        if (nounA == null | nounB == null) 
            throw new java.lang.NullPointerException();
    }

    /**
     * A synset (second field of synsets.txt) that is the common ancestor of
     * nounA and nounB in a shortest ancestral path (defined below).
     * 
     * @param nounA
     * @param nounB
     * @throws NullPointerException if {@code nounA == null}
     * @throws NullPointerException if {@code nounB == null}
     * @return 
     */
    public String sap(String nounA, String nounB) {
        if (nounA == null | nounB == null) 
            throw new java.lang.NullPointerException();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        
    }
}

