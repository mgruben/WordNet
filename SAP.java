
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
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
public class SAP {

    /**
     * Constructor takes a digraph (not necessarily a DAG).
     * 
     * @param G 
     * @throws NullPointerException if {@code G == null}
     */
    public SAP(Digraph G) {
        if (G == null) throw new java.lang.NullPointerException();
    }

    /**
     * Length of shortest ancestral path between v and w; -1 if no such path.
     * 
     * @param v
     * @param w
     * @return the length of the shortest ancestral path between <em>v</em> and
     *         <em>w</em>; {@code -1} if no such path exists
     */
    public int length(int v, int w) {

    }

    /**
     * A common ancestor of v and w that participates in a shortest ancestral
     * path; -1 if no such path.
     * 
     * @param v
     * @param w
     * @return the synset ID of the common ancestor of <em>v</em> and <em>w</em>
     *         that participates in a shortest ancestral path; {@code -1} if no
     *         such path
     */
    public int ancestor(int v, int w) {

    }

    /**
     * Length of shortest ancestral path between any vertex in v and any vertex
     * in w; -1 if no such path.
     * @param v
     * @param w
     * @throws NullPointerException if {@code v == null}
     * @throws NullPointerException if {@code w == null}
     * @return the length of the shortest ancestral path between any vertex in
     *         <em>v</em> and any vertex in <em>w</em>; {@code -1} if no
     *         such path
     */
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null | w == null) throw new java.lang.NullPointerException();
    }

    /**
     * A common ancestor that participates in shortest ancestral path; -1 if no
     * such path.
     * @param v
     * @param w
     * @throws NullPointerException if {@code v == null}
     * @throws NullPointerException if {@code w == null}
     * @return the synset ID of a common ancestor that participates in a 
     *         shortest ancestral path between any vertex in <em>v</em> and
     *         any vertex in <em>w</em>; {@code -1} if no such path
     */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null | w == null) throw new java.lang.NullPointerException();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}

