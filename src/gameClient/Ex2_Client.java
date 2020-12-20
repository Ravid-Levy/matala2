package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.JsonArray;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import api.edge_data;


public class Ex2_Client implements Runnable{
	private static MyFrame _win;
	private static Arena _ar;
	private static long id;
	private static int level;

	public static void main(String[] a) {


		if (a.length > 0) {
			try {
				id = Integer.parseInt(a[0]);
			} catch (NumberFormatException numberFormatException) {
				System.out.println("ID should contains only numbers");
			}
			try {
				level = Integer.parseInt(a[1]);
			} catch (NumberFormatException e) {
				e.printStackTrace();

			}
			test(id, level);
		}

	}
	public static void test(long id,int level){
		Ex2_Client.id=id;
		Ex2_Client.level=level;
		Thread client = new Thread(new Ex2_Client());
		client.start();
	}
	@Override
	public void run() {
		game_service game = Game_Server_Ex2.getServer(level); // you have [0,23] games
		game.login(id);
		String pks = game.getPokemons();
		System.out.println(pks);
		jsontograph(game.getGraph());
		directed_weighted_graph gg = jsontograph(game.getGraph());
		init(game);
		String fs =  game.getPokemons();
		List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
		game.startGame();
		_win.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());
		int ind=0;
		long dt=100;

		while(game.isRunning()) {
			moveAgants(game, gg);
			try {
				if(ind%1==0) {_win.repaint();}
				Thread.sleep(dt);
				ind++;
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		String res = game.toString();

		System.out.println(res);

		System.exit(0);
	}
	/**
	 * Moves each of the agents along the edge,
	 * in case the agent is on a node the next destination (next edge) is chosen (randomly).
	 * @param game
	 * @param gg
	 * @param
	 */
	private static void moveAgants(game_service game, directed_weighted_graph gg) {
		String lg = game.move();
		List<CL_Agent> log = Arena.getAgents(lg, gg);
		_ar.setAgents(log);
		//ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();
		String fs =  game.getPokemons();
		List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
		_ar.setPokemons(ffs);
		for(int i=0;i<log.size();i++) {
			CL_Agent ag = log.get(i);
			int id = ag.getID();
			int src = ag.getSrcNode();
			int dest = ag.getNextNode();
			double v = ag.getValue();
			if(dest==-1) {
				dest = nextNode(game,gg, src);
				game.chooseNextEdge(ag.getID(), dest);
				System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest);
			}
		}
	}
	private static edge_data findEdge(CL_Pokemon p,directed_weighted_graph gg){
		Collection<edge_data> ed=new HashSet<edge_data>();
		for(node_data n:gg.getV()){
			for(edge_data d: gg.getE(n.getKey())){
				if(!(d.getSrc()<=n.getKey()&&d.getDest()<=n.getKey()))
					ed.add(d);
			}
		}
		for(edge_data d:ed){
			double minX=Math.min(gg.getNode(d.getSrc()).getLocation().x(),gg.getNode(d.getDest()).getLocation().x());
			double maxX=Math.max(gg.getNode(d.getSrc()).getLocation().x(),gg.getNode(d.getDest()).getLocation().x());
			double minY=Math.min(gg.getNode(d.getSrc()).getLocation().y(),gg.getNode(d.getDest()).getLocation().y());
			double maxY=Math.max(gg.getNode(d.getSrc()).getLocation().y(),gg.getNode(d.getDest()).getLocation().y());
			if(p.getLocation().x()>=minX&&p.getLocation().x()<=maxX&&p.getLocation().y()>=minY&&p.getLocation().y()<=maxY)
				return d;
		}
		return null;
	}
	private directed_weighted_graph jsontograph(String json){
		directed_weighted_graph ans=new DWGraph_DS();
		try{
			JSONObject ttt=new JSONObject(json);
			JSONArray ags=ttt.getJSONArray("Nodes");
			for(int i=0;i<ags.length();i++){
				JSONObject pp = ags.getJSONObject(i);
				node_data c=new Vertex(pp.getInt("id"));
				String[] location=pp.getString("pos").split(",");
				c.setLocation(new GeoLocation(Double.parseDouble(location[0]),Double.parseDouble(location[1]),Double.parseDouble(location[2])));
				ans.addNode(c);
			}
			ags=ttt.getJSONArray("Edges");
			for(int i=0;i<ags.length();i++){
				JSONObject pp = ags.getJSONObject(i);
				ans.connect(pp.getInt("src"),pp.getInt("dest"),pp.getDouble("w"));
			}
		}catch(JSONException e){}
		return ans;
	}
	/**
	 * a very simple random walk implementation!
	 * @param g
	 * @param src
	 * @return
	 */

	private static int nextNode(game_service game,directed_weighted_graph g, int src) {
		dw_graph_algorithms ga=new DWGraph_Algo();
		ga.init(g);
		String fs =  game.getPokemons();
		List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);

		CL_Pokemon pp=ffs.get(0);
		node_data n=g.getNode(src);
		for(CL_Pokemon p:ffs){
			edge_data d=findEdge(p,g);
			edge_data d1=findEdge(pp,g);
			if(n.getLocation().distance(g.getNode(d.getDest()).getLocation())<n.getLocation().distance(g.getNode(d1.getDest()).getLocation()))
				pp=p;
		}
		edge_data d2=findEdge(pp,g);
		List<node_data>path=ga.shortestPath(src,d2.getDest());
		if(path.size()==1||path.size()>1&&path.get(path.size()-2).getKey()!=d2.getSrc())
			path=ga.shortestPath(src,d2.getSrc());
		int ans = -1;
		System.out.println(findEdge(pp,g));
		System.out.println(path);
		System.out.println(ffs);
		if(path.size()>1)
			ans=path.get(1).getKey();
		return ans;
	}
	private void init(game_service game) {
		String g = game.getGraph();
		String fs = game.getPokemons();
		directed_weighted_graph gg = jsontograph(game.getGraph());
		//gg.init(g);
		_ar = new Arena();
		_ar.setGraph(gg);
		_ar.setPokemons(Arena.json2Pokemons(fs));
		_win = new MyFrame("test Ex2");
		_win.setSize(1000, 700);
		_win.update(_ar);


		_win.show();
		String info = game.toString();
		JSONObject line;
		try {
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("agents");
			System.out.println(info);
			System.out.println(game.getPokemons());
			int src_node = 0;  // arbitrary node, you should start at one of the pokemon
			ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
			for(int a = 0;a<cl_fs.size();a++) { Arena.updateEdge(cl_fs.get(a),gg);}
			for(int a = 0;a<rs;a++) {
				int ind = a%cl_fs.size();
				CL_Pokemon c = cl_fs.get(ind);
				int nn = c.get_edge().getDest();
				if(c.getType()<0 ) {nn = c.get_edge().getSrc();}

				game.addAgent(nn);
			}
		}
		catch (JSONException e) {e.printStackTrace();}
	}
}
