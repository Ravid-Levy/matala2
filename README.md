# Directed Graph Game

## DWGraph_Algo
<br />
init() - Init the graph on which this set of algorithms operates on.
<br />
getGraph() - Return the underlying graph of which this class works.
<br />
copy() - copy the graph
<br />
isConnected - check if graph connected or not
<br />
 shortestPathDist - return the shortest path length between given src and dist.
 <br />
 shortestPath - return the way itself from src to dist.
  <br />
save - do save.
  <br />
load - load the save

## DWGraph_DS
  <br />
  the Graph implements
  <br />
  getNode - returns the node_data by the node_id
    <br />
getEdge - returns the data of the edge (src,dest), null if none.
  <br />
  addNode - add a new node to the graph. run in o(1).
  <br />
  connect - Connects an edge with weight w between node src to node dest.
  <br />
  getV - This method returns a pointer (shallow copy) for the collection representing all the nodes in the graph.
  <br />
  getE(int node_id) - get edges.
  <br />
  removeNode(int key) - remove node
  <br />
  removeEdge - remove edge
  <br />
  nodeSize - return node size
  <br />
  edgesize return edge size  
  <br />
getMC - check number of actions
  <br />
SubClass Vertex - 
  <br />
getkey - return key
  <br />
getLocation - return location
  <br />
setlocation - set the location of vert
  <br />
getWeight - get weight of the vert.
  <br />
setWeight - set weight of vert.
  <br />
getInfo - get info from vert.
  <br />
setInfo - Allows changing the remark (meta data) associated with this node
<br />
getTag - Temporal data (aka color: e,g, white, gray, black)
<br />
setTag - set temp data see below.
<br />
## Edge
getSrc - The id of the source node of this edge.
<br />
getDest() - the id of the dest node of this edge.
<br />
getWeight -  the weight of this edge (positive value).
<br />
getinfo - Returns the remark (meta data) associated with this edge.
<br />
setinfo - same
<br />
getTag - get the tag
<br />
settag - set the tag
<br />
## GeoLocation
GeoLocation - constructor
<br />
x- return x
<br />
y- return y
<br />
z - return z
<br />
distanse - get distance
<br />
tostring function
<br />
## Vertex
Vertex - set id for vertex
<br />
getkey - get key of vert
<br />
setkey - set key of vert
<br />
getLocation - get loc of vert
<br />
setLocation - set loc of vert
<br />
getWeight - get weight of vert
<br />
setWeight - set weight of vert
<br />
setinfo - set the info var in vert
<br />
getTag - get tag of vert
<br />
setTag - set tag of vert
<br />
tostring - as his name


