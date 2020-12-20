package gameClient;
import api.edge_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

public class CL_Pokemon {
	private edge_data Edge;
	private double value;
	private int type;
	private Point3D pos;
	private double mindist;
	private int min_ro;
	private boolean avalable;

	public CL_Pokemon(Point3D p, int t, double v, double s, edge_data e) {
		type = t;
		value = v;
		set_edge(e);
		pos = p;
		mindist = -1;
		min_ro = -1;
		avalable=false;
	}
	public static CL_Pokemon init_from_json(String json) {
		CL_Pokemon ans = null;
		try {
			JSONObject p = new JSONObject(json);
			int id = p.getInt("id");

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return ans;
	}
	public String toString() {return "F:{v="+value+", t="+ type +", location:"+ pos +"}";}
	public edge_data get_edge() {
		return Edge;
	}

	public void set_edge(edge_data _edge) {
		this.Edge = _edge;
	}

	public Point3D getLocation() {
		return pos;
	}
	public int getType() {return type;}
	public double getValue() {return value;}

	public double getMindist() {
		return mindist;
	}

	public void setMindist(double mid_dist) {
		this.mindist = mid_dist;
	}

	public int getMin_ro() {
		return min_ro;
	}

	public boolean isAvalable() {
		return avalable;
	}

	public void setAvalable(boolean avalable) {
		this.avalable = avalable;
	}

	public void setMin_ro(int min_ro) {
		this.min_ro = min_ro;
	}
}
