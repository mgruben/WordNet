
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
    private Digraph G;              // The given digraph
    private int[] distToLeft;       // For storing shortest paths
    private int[] distToRight;      // For storing shortest paths
    private int sp;         // The shortest path result of the BFS; -1 if none
    private int anc;        // The common ancestor result of the BFS; -1 if none

    private Stack<Integer> marked;  // Stores which vertices have been marked
    private Queue<Integer> vert;    // Stores the next vertices in BFS
    private Queue<Boolean> fam;     // The "family" to which the vertex belongs
                                    // true is the V or left family, and
                                    // false is the W or right family.
    
    /**
     * Constructor takes a digraph (not necessarily a DAG).
     * 
     * @param G 
     * @throws NullPointerException if {@code G == null}
     */
    public SAP(Digraph G) {
        // check for bad input
        if (G == null) throw new java.lang.NullPointerException();
        
        // Initialize our state variables
        sp = Integer.MAX_VALUE;
        anc = -1;
        marked = new Stack<>();
        vert = new Queue<>();
        fam = new Queue<>();
        
        // Copy the given digraph, so that we can ask it for adjacent vertices
        this.G = new Digraph(G);
        
        // Create a vertex-indexed array to keep track of distances and marking
        distToLeft = new int[this.G.V()];
        distToRight = new int[this.G.V()];
                
        // Adopt the convention that -1 means that this vertex is unmarked
        Arrays.fill(distToLeft, -1);
        Arrays.fill(distToRight, -1);
    }
    
    /**
     * Conduct a parallel breadth-first search on the given digraph, so that
     * other methods (such as length() and ancestor()) can query for the fields
     * that they need.
     * 
     * This method leaves the BFS state fields in a dirty state; each method
     * that calls this method is responsible for cleaning the BFS itself.
     * 
     * @param V The synset IDs of the first synset family in the parallel BFS
     * @param W The synset IDs of the other synset family in the parallel BFS
     */
    private void parallelBFS(Iterable<Integer> V, Iterable<Integer> W) {
        
        /**
         * Note that, although we're enqueueing all synsets from V family first,
         * followed by all synsets from W family, we're still running a parallel
         * breadth-first search.
         * 
         * That is, we're still considering all vertices of distance k before
         * we consider any vertices of distance (k + 1).
         * 
         * So, although we could append a synset from V family, then a synset
         * from W family, we don't obtain a more correct algorithm through that
         * interleaving.
         */
        
        // enqueue synsets from our first family to search; mark them as seen
        for (int v: V) {
            vert.enqueue(v);
            marked.push(v);
            distToLeft[v] = 0;
            fam.enqueue(true);
        }
        
        // enqueue synsets from our other family to search; mark them as seen
        for (int w: W) {
            // Check for collision in the list, before searching
            if (distToLeft[w] == 0) {
                sp = 0;
                anc = w;
                return;
            }
            else {
                vert.enqueue(w);
                marked.push(w);
                distToRight[w] = 0;
                fam.enqueue(false);
            }
        }
        
        // conduct parallel BFS for shortest ancestral path
        while (!vert.isEmpty()) {
            int i = vert.dequeue();
            boolean fromLeft = fam.dequeue();
            for (int adj: G.adj(i)) {
                if (fromLeft) {
                    // We've already been here before from this family
                    if (distToLeft[adj] != -1) continue;
                    
                    /**
                     * We've collided, indicating a successful breadth-first search.
                     * 
                     * Note that, if the Digraph contains cycles, we won't know that
                     * we've found the shortest ancestral path until the distance
                     * exceeds the length of the shortest ancestral path found so
                     * far.
                     * 
                     * Accordingly, check distance against that length, and return
                     * when distance exceeds that best length.
                     * 
                     * Save the state of the BFS in our instance variables, so that
                     * individual methods can return from this state what they want.
                     */
                    if (distToRight[adj] != -1 &&
                        distToLeft[i] + 1 + distToRight[adj] < sp) {
                            sp = distToLeft[i] + 1 + distToRight[adj];
                            anc = adj;
                    }
                    
                    vert.enqueue(adj);
                    marked.push(adj);
                    fam.enqueue(fromLeft);
                    distToLeft[adj] = distToLeft[i] + 1;
                    
                    // Return early, if possible
                    if (distToLeft[i] + 1 > sp) return;
                }
                    
                else {
                    // We've already been here before from this family
                    if (distToRight[adj] != -1) continue;
                    /**
                     * We've collided, indicating a successful breadth-first search.
                     * 
                     * Note that, if the Digraph contains cycles, we won't know that
                     * we've found the shortest ancestral path until the distance
                     * exceeds the length of the shortest ancestral path found so
                     * far.
                     * 
                     * Accordingly, check distance against that length, and return
                     * when distance exceeds that best length.
                     * 
                     * Save the state of the BFS in our instance variables, so that
                     * individual methods can return from this state what they want.
                     */
                    if (distToLeft[adj] != -1 &&
                        distToRight[i] + 1 + distToLeft[adj] < sp) {
                            sp = distToRight[i] + 1 + distToLeft[adj];
                            anc = adj;
                    }
                    
                    vert.enqueue(adj);
                    marked.push(adj);
                    fam.enqueue(fromLeft);
                    distToRight[adj] = distToRight[i] + 1;
                    
                    // Return early, if possible
                    if (distToRight[i] + 1 > sp) return;
                }
            }
        }
        
        // if we're here, our breadth-first search did not find an ancestor
    }
    
    /**
     * This method is used to unwind the results of a BFS.
     * 
     * This method should <b>always</b> be called before the method that called
     * {@code parallelBFS} returns.
     * 
     * This method should be called after the desired state variables from the
     * just-conducted BFS have been saved to a temporary variable for return.
     */
    private void cleanBFS() {
        /** 
         * Unmark all marked vertices to efficiently re-initialize.
         * 
         * This sets all entries in distTo and edgeTo to -1, and
         * sets all entries is fam to 0.
         */
        while (!marked.isEmpty()) {
            int m = marked.pop();
            distToLeft[m] = -1;
            distToRight[m] = -1;
        }
        
        // Clear the queue of vertices
        vert = new Queue<>();
        fam = new Queue<>();
        
        // Set shortest path and ancestor to "none" code
        sp = Integer.MAX_VALUE;
        anc = -1;
    }
    /**
     * Length of shortest ancestral path between v and w; -1 if no such path.
     * 
     * @param v The synset ID of the first synset in the sap
     * @param w The synset ID of the other synset in the sap
     * @throws IndexOutOfBoundsException if <em>v</em> or <em>w</em> is outside
     *         of the range {@code [0, G.V() - 1)}
     * @return the length of the shortest ancestral path between <em>v</em> and
     *         <em>w</em>; {@code -1} if no such path exists
     */
    public int length(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V())
            throw new java.lang.IndexOutOfBoundsException();
        
        Queue<Integer> V = new Queue<>();
        V.enqueue(v);
        
        Queue<Integer> W = new Queue<>();
        W.enqueue(w);
        
        this.parallelBFS(V, W);
        int ans = sp;
        this.cleanBFS();
        if (ans == Integer.MAX_VALUE) return -1;
        else return ans;
    }

    /**
     * A common ancestor of v and w that participates in a shortest ancestral
     * path; -1 if no such path.
     * 
     * @param v The synset ID of the first synset in the sap
     * @param w The synset ID of the other synset in the sap
     * @throws IndexOutOfBoundsException if <em>v</em> or <em>w</em> is outside
     *         of the range {@code [0, G.V() - 1)}
     * @return the synset ID of the common ancestor of <em>v</em> and <em>w</em>
     *         that participates in a shortest ancestral path; {@code -1} if no
     *         such path
     */
    public int ancestor(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V())
            throw new java.lang.IndexOutOfBoundsException();
        
        Queue<Integer> V = new Queue<>();
        V.enqueue(v);
        
        Queue<Integer> W = new Queue<>();
        W.enqueue(w);
        
        this.parallelBFS(V, W);
        int ans = anc;
        this.cleanBFS();
        return ans;
    }

    /**
     * Length of shortest ancestral path between any vertex in v and any vertex
     * in w; -1 if no such path.
     * 
     * @param V The iterable of the first synset family in the sap
     * @param W The iterable of the other synset family in the sap
     * @throws NullPointerException if {@code v == null}
     * @throws NullPointerException if {@code w == null}
     * @throws IndexOutOfBoundsException if any vertex in <em>V</em> or
     *         <em>W</em> is outside of the range {@code [0, G.V() - 1)}
     * @return the length of the shortest ancestral path between any vertex in
     *         <em>V</em> and any vertex in <em>W</em>; {@code -1} if no
     *         such path
     */
    public int length(Iterable<Integer> V, Iterable<Integer> W) {
        if (V == null || W == null) throw new java.lang.NullPointerException();
        for (int v: V) if (v < 0 || v >= G.V())
            throw new java.lang.IndexOutOfBoundsException();
        for (int w: W) if (w < 0 || w >= G.V())
            throw new java.lang.IndexOutOfBoundsException();
        
        this.parallelBFS(V, W);
        int ans = sp;
        this.cleanBFS();
        if (sp == Integer.MAX_VALUE) return -1;
        else return ans;
    }

    /**
     * A common ancestor that participates in shortest ancestral path; -1 if no
     * such path.
     * 
     * @param V The iterable of the first synset family in the sap
     * @param W The iterable of the other synset family in the sap
     * @throws NullPointerException if {@code v == null}
     * @throws NullPointerException if {@code w == null}
     * @throws IndexOutOfBoundsException if any vertex in <em>V</em> or
     *         <em>W</em> is outside of the range {@code [0, G.V() - 1)}
     * @return the synset ID of a common ancestor that participates in a 
     *         shortest ancestral path between any vertex in <em>V</em> and
     *         any vertex in <em>W</em>; {@code -1} if no such path
     */
    public int ancestor(Iterable<Integer> V, Iterable<Integer> W) {
        if (V == null || W == null) throw new java.lang.NullPointerException();
        for (int v: V) if (v < 0 || v >= G.V())
            throw new java.lang.IndexOutOfBoundsException();
        for (int w: W) if (w < 0 || w >= G.V())
            throw new java.lang.IndexOutOfBoundsException();
        
        this.parallelBFS(V, W);
        int ans = anc;
        this.cleanBFS();
        return ans;
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

