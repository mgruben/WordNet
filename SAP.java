
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
    Digraph G;              // The given digraph
    private int[] distTo;   // For storing shortest paths
    private int[] edgeTo;   // For recreating paths from v to w
    private char[] fam;     // The "family" to which the vertex belongs
    private int sp;         // The shortest path result of the BFS; -1 if none
    private int anc;        // The common ancestor result of the BFS; -1 if none
    private int oth;        // The last synset leading to anc from the other tree
    
    private Stack<Integer> marked;  // Stores which vertices have been marked
    private Queue<Integer> vert;    // Stores the next vertices in BFS
    
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
        sp = -1;
        anc = -1;
        oth = -1;
        marked = new Stack<>();
        vert = new Queue<>();
        
        // Copy the given digraph, so that we can ask it for adjacent vertices
        this.G = new Digraph(G);
        
        // Create a vertex-indexed array to keep track of distances and marking
        distTo = new int[this.G.V()];
        
        // Create a vertex-indexed array to keep track of the path from v to w
        edgeTo = new int[this.G.V()];
        
        /** 
         * Create a vertex-indexed array to keep track of which "family" the
         * given vertex belongs to.
         * 
         * Adopt the convention that 0 means no family (or, unmarked),
         *                          -1 means the V family, and
         *                           1 means the W family.
         */
        fam = new char[this.G.V()];
                
        // Adopt the convention that -1 means that this vertex is unmarked
        Arrays.fill(distTo, -1);
        Arrays.fill(edgeTo, -1);
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
            distTo[v] = 0;
            fam[v] = (char) -1;
        }
        
        // enqueue synsets from our other family to search; mark them as seen
        for (int w: W) {
            // Check for collision in the list, before searching
            if (distTo[w] == 0 && fam[w] == -1) {
                sp = 0;
                anc = w;
                oth = w;
                return;
            }
            else {
                vert.enqueue(w);
                marked.push(w);
                distTo[w] = 0;
                fam[w] = (char) 1;
            }
        }
        
        // conduct parallel BFS for shortest ancestral path
        while (!vert.isEmpty()) {
            int i = vert.dequeue();
            for (int adj: G.adj(i)) {
                if (distTo[adj] == -1 && fam[adj] == 0) {
                    vert.enqueue(adj);
                    marked.push(adj);
                    edgeTo[adj] = i;
                    distTo[adj] = distTo[i] + 1;
                    fam[adj] = fam[i];
                }
                
                /**
                 * We've collided, indicating a successful breadth-first search.
                 * 
                 * Save the state of the BFS in our instance variables, so that
                 * individual methods can return from this state what they want.
                 */
                else if (fam[adj] != fam[i]) {
                    sp = distTo[i] + 1 + distTo[adj];
                    anc = adj;
                    oth = i;
                    return;
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
        while (!marked.isEmpty()){
            int m = marked.pop();
            distTo[m] = -1;
            edgeTo[m] = -1;
            fam[m] = 0;
        }
        
        // Clear the queue of vertices
        vert = new Queue<>();
        
        // Set shortest path and ancestor to "none" code
        sp = -1;
        anc = -1;
        oth = -1;
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
        return ans;
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
        if (V == null | W == null) throw new java.lang.NullPointerException();
        for (int v: V) if (v < 0 || v >= G.V())
            throw new java.lang.IndexOutOfBoundsException();
        for (int w: W) if (w < 0 || w >= G.V())
            throw new java.lang.IndexOutOfBoundsException();
        
        this.parallelBFS(V, W);
        int ans = sp;
        this.cleanBFS();
        return ans;
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
        if (V == null | W == null) throw new java.lang.NullPointerException();
        for (int v: V) if (v < 0 || v >= G.V())
            throw new java.lang.IndexOutOfBoundsException();
        for (int w: W) if (w < 0 || w >= G.V())
            throw new java.lang.IndexOutOfBoundsException();
        
        this.parallelBFS(V, W);
        int ans = anc;
        this.cleanBFS();
        return ans;
    }
    
    public Stack<Integer>[] pathTo(int v, int w) {
        Queue<Integer> V = new Queue<>();
        Queue<Integer> W = new Queue<>();
        
        V.enqueue(v);
        W.enqueue(w);
        
        return pathTo(V, W);
    }
    
    public Stack<Integer>[] pathTo(Iterable<Integer> V, Iterable<Integer> W) {
        if (V == null | W == null) throw new java.lang.NullPointerException();
        for (int v: V) if (v < 0 || v >= G.V())
            throw new java.lang.IndexOutOfBoundsException();
        for (int w: W) if (w < 0 || w >= G.V())
            throw new java.lang.IndexOutOfBoundsException();
        
        this.parallelBFS(V, W);
        Stack[] ans = new Stack[2];
        if (anc == -1) {
            this.cleanBFS();
            return ans;
        }
        ans[0] = new Stack<>();
        int tmp = anc;
        ans[0].push(tmp);
        while (edgeTo[tmp] != -1) {
            ans[0].push(edgeTo[tmp]);
            tmp = edgeTo[tmp];
        }
        
        ans[1] = new Stack<>();
        tmp = oth;
        ans[1].push(anc);
        ans[1].push(tmp);
        while (edgeTo[tmp] != -1) {
            ans[1].push(edgeTo[tmp]);
            tmp = edgeTo[tmp];
        }
                
        this.cleanBFS();
        return ans;
    }
    
    private String pathsToString(Stack<Integer>[] s) {
        if (s[0] == null || s[1] == null) return "";
        
        StringBuilder ans = new StringBuilder();
        
        for (Stack<Integer> st: s) {
            StringBuilder sb = new StringBuilder();
            while (!st.isEmpty()) {
                sb.append(st.pop());
                sb.append("->");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append('\n');
            ans.append(sb);
        }
        
        ans.deleteCharAt(ans.length() - 1);
        
        return ans.toString();
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
            StdOut.println(sap.pathsToString(sap.pathTo(v, w)));
        }
    }
}

