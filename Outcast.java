
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

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
public class Outcast {
    WordNet wordnet;
    
    /**
     * Constructor takes a WordNet object.
     * 
     * @param wordnet 
     * @throws NullPointerException if {@code wordnet == null}
     */
    public Outcast(WordNet wordnet) {
        if (wordnet == null) throw new java.lang.NullPointerException();
        this.wordnet = wordnet;
    }
    
    /**
     * Given an array of WordNet nouns, return an outcast.
     * 
     * @param nouns
     * @throws NullPointerException if {@code nouns == null}
     * @return an outcast
     */
    public String outcast(String[] nouns) {
       if (nouns == null) throw new java.lang.NullPointerException();
       int sum = 0;
       String out = "";
       for (int i = 0; i < nouns.length; i++) {
           int d = 0;
           for (int j = 0; j < nouns.length; j++) {
               d += wordnet.distance(nouns[i], nouns[j]);;
           }
           if (d > sum) {
               sum = d;
               out = nouns[i];
           }
       }
       return out;
    }
    
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }

}

