
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

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
    Digraph G;
    private int[] distTo;
    
    /**
     * Constructor takes a digraph (not necessarily a DAG).
     * 
     * @param G 
     * @throws NullPointerException if {@code G == null}
     */
    public SAP(Digraph G) {
        // check for bad input
        if (G == null) throw new java.lang.NullPointerException();
        
        // Store the given digraph, so that we can ask it for adjacent vertices
        this.G = G;
        
        // Create a vertex-indexed array to keep track of distances and marking
        distTo = new int[this.G.V()];
                
        // Adopt the convention that -1 means that this vertex is unmarked
        Arrays.fill(distTo, -1);
    }

    /**
     * Length of shortest ancestral path between v and w; -1 if no such path.
     * 
     * @param v
     * @param w
     * @throws IndexOutOfBoundsException if <em>v</em> or <em>w</em> is outside
     *         of the range {@code [0, G.V() - 1)}
     * @return the length of the shortest ancestral path between <em>v</em> and
     *         <em>w</em>; {@code -1} if no such path exists
     */
    public int length(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V())
            throw new java.lang.IndexOutOfBoundsException();
        
        // Our value to return, initialized to "no such path"
        int ans = -1;
        
        // Initialize data structure to help unmark marked vertices
        Stack<Integer> marked = new Stack<>();
        
        // Initialize parallel queues for vertices and their distances
        Queue<Integer> vert = new Queue<>();
        Queue<Integer> dist = new Queue<>();
        
        // enqueue our first synset to search and mark it as seen
        vert.enqueue(v);
        marked.push(v);
        dist.enqueue(0);
        distTo[v] = 0;
        
        // enqueue our other synset to search and mark it as seen
        vert.enqueue(w);
        marked.push(w);
        dist.enqueue(0);
        distTo[w] = 0;
        
        // conduct parallel BFS for shortest ancestral path
        while (!vert.isEmpty()) {
            int i = vert.dequeue();
            int d = dist.dequeue();
            for (int adj: G.adj(i)) {
                if (distTo[adj] == -1) {
                    vert.enqueue(adj);
                    dist.enqueue(d + 1);
                    distTo[adj] = d + 1;
                }
                
                // We've collided, indicating the existence of a common ancestor
                else {
                    
                    // Store our answer to return
                    ans = d + 1 + distTo[adj];
                    
                    // unmark all marked vertices to efficiently re-initialize
                    while (!marked.isEmpty()) {
                        int m = marked.pop();
                        distTo[m] = -1;
                    }
                    
                    // return the answer
                    return ans;
                }
            }
        }
        return ans;
    }

    /**
     * A common ancestor of v and w that participates in a shortest ancestral
     * path; -1 if no such path.
     * 
     * @param v
     * @param w
     * @throws IndexOutOfBoundsException if <em>v</em> or <em>w</em> is outside
     *         of the range {@code [0, G.V() - 1)}
     * @return the synset ID of the common ancestor of <em>v</em> and <em>w</em>
     *         that participates in a shortest ancestral path; {@code -1} if no
     *         such path
     */
    public int ancestor(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V())
            throw new java.lang.IndexOutOfBoundsException();
    }

    /**
     * Length of shortest ancestral path between any vertex in v and any vertex
     * in w; -1 if no such path.
     * @param v
     * @param w
     * @throws NullPointerException if {@code v == null}
     * @throws NullPointerException if {@code w == null}
     * @throws IndexOutOfBoundsException if any vertex in <em>v</em> or
     *         <em>w</em> is outside of the range {@code [0, G.V() - 1)}
     * @return the length of the shortest ancestral path between any vertex in
     *         <em>v</em> and any vertex in <em>w</em>; {@code -1} if no
     *         such path
     */
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null | w == null) throw new java.lang.NullPointerException();
        for (int i: v) if (i < 0 || i >= G.V())
            throw new java.lang.IndexOutOfBoundsException();
        for (int i: w) if (i < 0 || i >= G.V())
            throw new java.lang.IndexOutOfBoundsException();
    }

    /**
     * A common ancestor that participates in shortest ancestral path; -1 if no
     * such path.
     * @param v
     * @param w
     * @throws NullPointerException if {@code v == null}
     * @throws NullPointerException if {@code w == null}
     * @throws IndexOutOfBoundsException if any vertex in <em>v</em> or
     *         <em>w</em> is outside of the range {@code [0, G.V() - 1)}
     * @return the synset ID of a common ancestor that participates in a 
     *         shortest ancestral path between any vertex in <em>v</em> and
     *         any vertex in <em>w</em>; {@code -1} if no such path
     */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null | w == null) throw new java.lang.NullPointerException();
        for (int i: v) if (i < 0 || i >= G.V())
            throw new java.lang.IndexOutOfBoundsException();
        for (int i: w) if (i < 0 || i >= G.V())
            throw new java.lang.IndexOutOfBoundsException();
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

