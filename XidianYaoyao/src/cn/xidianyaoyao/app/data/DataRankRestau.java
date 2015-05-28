package cn.xidianyaoyao.app.data;

public class DataRankRestau {

	private String rank_number;
	private String restau_id;
	private String restau_name;
	private String restau_score;
	private String restau_addr;
	private String restau_call;
	private String restau_descr;
	private String restau_lat;
	private String restau_lon;

	public DataRankRestau(String rank_number, String restau_id,
			String restau_name, String restau_score, String restau_addr,
			String restau_call, String restau_descr, String restau_lat,
			String restau_lon) {
		this.rank_number = rank_number;
		this.restau_id = restau_id;
		this.restau_name = restau_name;
		this.restau_score = restau_score;
		this.restau_addr = restau_addr;
		this.restau_call = restau_call;
		this.restau_descr = restau_descr;
		this.restau_lat = restau_lat;
		this.restau_lon = restau_lon;
	}
	
	public DataRankRestau(String restau_id,
			String restau_name, String restau_score, String restau_addr,
			String restau_call, String restau_descr, String restau_lat,
			String restau_lon) {
		this.restau_id = restau_id;
		this.restau_name = restau_name;
		this.restau_score = restau_score;
		this.restau_addr = restau_addr;
		this.restau_call = restau_call;
		this.restau_descr = restau_descr;
		this.restau_lat = restau_lat;
		this.restau_lon = restau_lon;
	}

	public String getRank_number() {
		return rank_number;
	}

	public void setRank_number(String rank_number) {
		this.rank_number = rank_number;
	}

	public String getRestau_id() {
		return restau_id;
	}

	public void setRestau_id(String restau_id) {
		this.restau_id = restau_id;
	}

	public String getRestau_name() {
		return restau_name;
	}

	public void setRestau_name(String restau_name) {
		this.restau_name = restau_name;
	}

	public String getRestau_score() {
		return restau_score;
	}

	public void setRestau_score(String restau_score) {
		this.restau_score = restau_score;
	}

	public String getRestau_addr() {
		return restau_addr;
	}

	public void setRestau_addr(String restau_addr) {
		this.restau_addr = restau_addr;
	}

	public String getRestau_call() {
		return restau_call;
	}

	public void setRestau_call(String restau_call) {
		this.restau_call = restau_call;
	}

	public String getRestau_descr() {
		return restau_descr;
	}

	public void setRestau_descr(String restau_descr) {
		this.restau_descr = restau_descr;
	}

	public String getRestau_lat() {
		return restau_lat;
	}

	public void setRestau_lat(String restau_lat) {
		this.restau_lat = restau_lat;
	}

	public String getRestau_lon() {
		return restau_lon;
	}

	public void setRestau_lon(String restau_lon) {
		this.restau_lon = restau_lon;
	}

}
