package api;


import java.util.*;

public class DWGraph_DS implements directed_weighted_graph {

    private Map<Integer, node_data> nodes = new HashMap<>();
    private Map<Integer, Map<Integer, edge_data>> EdgesOut = new HashMap<>();//edges out
    private Map<Integer, HashSet<Integer>> EdgesIn = new HashMap<>();// edges in
    private int MCount = 0;
    private int ESize = 0;

    /**
     * returns the node_data by the node_id,
     *
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        if (nodes.containsKey(key)) {
            return nodes.get(key);
        }
        return null;
    }

    /**
     * returns the data of the edge (src,dest), null if none.
     * Note: this method should run in O(1) time.
     *
     * @param src
     * @param dest
     * @return
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        if(nodes.containsKey(src) && nodes.containsKey(dest)) {
            if (this.EdgesOut.get(src).containsKey(dest)) {
                return this.EdgesOut.get(src).get(dest);
            }
        }
        return null;
    }

    /**
     * adds a new node to the graph with the given node_data.
     * Note: this method should run in O(1) time.
     *
     * @param n
     */
    @Override
    public void addNode(node_data n) {
        if(!this.nodes.containsKey(n.getKey())) {
            this.nodes.put(n.getKey(), n);
            this.EdgesOut.put(n.getKey(),new HashMap<Integer, edge_data>());
            this.EdgesIn.put(n.getKey(),new HashSet<Integer>());
        }
    }

    /**
     * Connects an edge with weight w between node src to node dest.
     * * Note: this method should run in O(1) time.
     *
     * @param src  - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w    - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        if(w < 0){
            System.err.println("weight less than 0");
            return;
        }
        if (!this.nodes.containsKey(src) || !this.nodes.containsKey(dest)) {
            System.err.println("src or dest is not exist on the graph.");
            return;
        }
        else {
            if (getEdge(src, dest) == null) {
                ESize++;
            } else {
                EdgesIn.get(dest).remove(src);
            }
            EdgesOut.get(src).put(dest, new Edge(nodes.get(src), nodes.get(dest), w));
            EdgesIn.get(dest).add(src);
            MCount++;
        }
        }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_data> getV() {
        return this.nodes.values();

    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     * Note: this method should run in O(k) time, k being the collection size.
     *
     * @param node_id
     * @return Collection<edge_data>
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        Collection<edge_data> edgee = new HashSet<edge_data>();
        for (edge_data Edge: EdgesOut.get(node_id).values()){
            edgee.add(Edge);
        }
        return edgee;
    }

    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(k), V.degree=k, as all the edges should be removed.
     *
     * @param key
     * @return the data of the removed node (null if none).
     */
    @Override
    public node_data removeNode(int key) {
        if (!this.nodes.containsKey(key)) {
            return null;
        }
        node_data Nod = this.getNode(key);
        for (edge_data nei : this.getE(key)) {
            this.EdgesOut.get(key).remove(nei.getDest());
        }
        for (Integer ED : this.EdgesIn.get(key)){
            this.EdgesOut.get(ED).remove(key);
        }
        this.EdgesIn.remove(key);
        return this.nodes.remove(key);
    }

    /**
     * Deletes the edge from the graph,
     * Note: this method should run in O(1) time.
     *
     * @param src
     * @param dest
     * @return the data of the removed edge (null if none).
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        if (!(getEdge(src, dest) == null)) {
            this.EdgesIn.get(dest).remove(src);
            this.ESize--;
            this.MCount++;
        }
        return this.EdgesOut.get(src).remove(dest);
    }

    /**
     * Returns the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int nodeSize() {
        return this.nodes.size();
    }

    /**
     * Returns the number of edges (assume directional graph).
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int edgeSize() {
        return ESize;
    }

    /**
     * Returns the Mode Count - for testing changes in the graph.
     *
     * @return
     */
    @Override
    public int getMC() {
        return this.MCount;
    }

    public class Vertex implements node_data {
        private geo_location geo;
        private Set<node_data> verts = new HashSet<>();
        private double weight;
        private int tag = 0;
        private int key;
        String info;

        public Vertex(GeoLocation g, int k) {
            this.geo = g;
            this.key = k;
        }


        /**
         * Returns the key (id) associated with this node.
         *
         * @return
         */
        @Override
        public int getKey() {
            return this.key;
        }

        /**
         * Returns the location of this node, if
         * none return null.
         *
         * @return
         */
        @Override
        public geo_location getLocation() {
            return this.geo;
        }

        /**
         * Allows changing this node's location.
         *
         * @param p - new new location  (position) of this node.
         */
        @Override
        public void setLocation(geo_location p) {
            this.geo = p;
        }

        /**
         * Returns the weight associated with this node.
         *
         * @return
         */
        @Override
        public double getWeight() {
            return weight;
        }

        /**
         * Allows changing this node's weight.
         *
         * @param w - the new weight
         */
        @Override
        public void setWeight(double w) {
            weight = w;
        }

        /**
         * Returns the remark (meta data) associated with this node.
         *
         * @return
         */
        @Override
        public String getInfo() {
            return info;
        }

        /**
         * Allows changing the remark (meta data) associated with this node.
         *
         * @param s
         */
        @Override
        public void setInfo(String s) {
        this.info = s;
        }

        /**
         * Temporal data (aka color: e,g, white, gray, black)
         * which can be used be algorithms
         *
         * @return
         */
        @Override
        public int getTag() {
            return this.tag;
        }

        /**
         * Allows setting the "tag" value for temporal marking an node - common
         * practice for marking by algorithms.
         *
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(int t) {
            tag=t;
        }


    }
}
