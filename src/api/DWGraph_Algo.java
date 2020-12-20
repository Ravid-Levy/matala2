package api;

import com.google.gson.*;
import gameClient.util.Point3D;
import netscape.javascript.JSObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms{
    private directed_weighted_graph galgo;
    /**
     * Init the graph on which this set of algorithms operates on.
     *
     * @param g
     */
    @Override
    public void init(directed_weighted_graph g) {
        this.galgo = g;
    }

    /**
     * Return the underlying graph of which this class works.
     *
     * @return
     */
    @Override
    public directed_weighted_graph getGraph() {
        return this.galgo;
    }

    /**
     * Compute a deep copy of this weighted graph.
     *
     * @return
     */
    @Override
    public directed_weighted_graph copy() {
        directed_weighted_graph NewGraph = new DWGraph_DS();
        for (node_data n : this.galgo.getV()){
            node_data node = new Vertex(n.getKey());
            NewGraph.addNode(node);
        }
        for (node_data n : this.galgo.getV()){
            for (edge_data e : this.galgo.getE(n.getKey())){
                NewGraph.connect(e.getSrc(),e.getDest(),e.getWeight());
            }
        }
        return NewGraph;
    }

    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node. NOTE: assume directional graph (all n*(n-1) ordered pairs).
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        Collection<node_data> nodes = this.galgo.getV();
        directed_weighted_graph newGraph = new DWGraph_DS();
        for (node_data node : nodes) {
            newGraph.addNode(node);
            Collection<edge_data> edges = this.galgo.getE(node.getKey());
            for (edge_data e : edges) {
                newGraph.connect(e.getDest(),e.getSrc(),e.getWeight());
            }
        }
        return false;
    }
    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if(this.galgo.getNode(src) == null){return -1;}
        if(this.galgo.getNode(dest) == null) {return -1;}
        Queue<node_data> que = new LinkedList<node_data>();
        for (node_data node : this.galgo.getV()){
            node.setWeight(Double.MAX_VALUE);
        }
        node_data start = this.galgo.getNode(src);
        start.setWeight(0);
        que.add(start);
        node_data end = this.galgo.getNode(dest);
        while(que.size() != 0){
            node_data cur = que.poll();
            for (edge_data edge : this.galgo.getE(cur.getKey())){
                node_data ni = galgo.getNode(edge.getDest());
                double niw = ni.getWeight();
                double edgew = edge.getWeight() + cur.getWeight();
                if(niw > edgew){
                    ni.setWeight(edgew);
                    que.add(ni);
                    ni.setTag(cur.getKey());
                }
            }

        }
        if(end.getWeight() != Double.MAX_VALUE){
            return end.getWeight();
        }


        return -1;
    }

    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        double dis = shortestPathDist(src,dest);
        if(dis == -1 ){return null;}
        LinkedList<node_data> path1 = new LinkedList<node_data>();
        LinkedList<node_data> path = GetList(this.galgo.getNode(dest),src,path1);
        return path;
    }
    private LinkedList<node_data> GetList(node_data curr , int src , LinkedList<node_data> path ){
         if(curr.getKey() == src) {
             path.addFirst(curr);
         }
            else {
                path.addFirst(curr);
                return GetList(this.galgo.getNode(curr.getTag()),src,path);}

        return path;
}

    /**
     * Saves this weighted (directed) graph to the given
     * file name - in JSON format
     *
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        Gson MJson =new  Gson();
        JsonObject JSONGraph = new JsonObject();
        JsonArray JsonNodes = new JsonArray();
        JsonArray JsonEdges = new JsonArray();
        JSONGraph.add("Edges",JsonEdges);
        JSONGraph.add("Nodes",JsonNodes);
        for (node_data node: this.galgo.getV()){
            JsonObject JSONode = new JsonObject();
            JsonNodes.add(JSONode);
            String loc = node.getLocation().x() + "," +node.getLocation().y()+","+node.getLocation().z();
            int id = node.getKey();
            JSONode.addProperty("pos",loc);
            JSONode.addProperty("id",id);
            for (edge_data edge : this.galgo.getE(id)){
                JsonObject JSONObjectn = new JsonObject();
                JsonEdges.add(JSONObjectn);
                int src = edge.getSrc();
                double w = edge.getWeight();
                int dest = edge.getDest();
                JSONObjectn.addProperty("src",src);
                JSONObjectn.addProperty("dest",dest);
                JSONObjectn.addProperty("w",w);
            }
        }
        try {
            File JSFile = new File(file);
            FileWriter MyFileWriter = new FileWriter(file);
            boolean b = JSFile.createNewFile();
            if(b == true ){
                MyFileWriter.write(MJson.toJson(JSONGraph));
            }
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     *
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        directed_weighted_graph GraphNew = new DWGraph_DS();
        try {
            File MyFile = new File(file);
            JsonObject js = new JsonParser().parse(new FileReader(file)).getAsJsonObject();
            for (JsonElement jsonedNode : js.getAsJsonArray("Nodes")) {
                int id = ((JsonObject) jsonedNode).get("id").getAsInt();
                node_data node = new Vertex(id);
                GraphNew.addNode(node);
                String PosStr = ((JsonObject) jsonedNode).get("pos").getAsString();
                String[] locations = PosStr.split(",");
                double x = Double.parseDouble(locations[0]);
                double y = Double.parseDouble(locations[1]);
                double z = Double.parseDouble(locations[2]);
                geo_location GeoLoc = new Point3D(x, y, z);
                node.setLocation(GeoLoc);
            }
            for (JsonElement JSONEdge : js.getAsJsonArray("Edges")) {
                int src = ((JsonObject) JSONEdge).get("src").getAsInt();
                double w = ((JsonObject) JSONEdge).get("w").getAsDouble();
                int dest = ((JsonObject) JSONEdge).get("dest").getAsInt();
                GraphNew.connect(src, dest, w);
            }
        }
            catch(FileNotFoundException e){
                return false;
            }

        return true;
    }

}
