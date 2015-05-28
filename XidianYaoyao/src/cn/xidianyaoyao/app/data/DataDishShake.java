package cn.xidianyaoyao.app.data;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DataDishShake implements Serializable {

	private String dish_id;
	private String dish_image;
	private String dish_name;
	private String dish_score;
	private String dish_price;
	private String dish_taste;
	private String dish_nutrition;
	private String restau_id;
	private String restau_name;
	private String restau_score;
	private String restau_addr;
	private String restau_call;
	private String restau_descr;
	private String restau_lat;
	private String restau_lon;

	public DataDishShake(String dish_id, String dish_image, String dish_name,
			String dish_score, String dish_price, String dish_taste,
			String dish_nutrition, String restau_id, String restau_name,
			String restau_score, String restau_addr, String restau_call,
			String restau_descr, String restau_lat, String restau_lon) {
		this.dish_id = dish_id;
		this.dish_image = dish_image;
		this.dish_name = dish_name;
		this.dish_score = dish_score;
		this.dish_price = dish_price;
		this.dish_taste = dish_taste;
		this.dish_nutrition = dish_nutrition;
		this.restau_id = restau_id;
		this.restau_name = restau_name;
		this.restau_score = restau_score;
		this.restau_addr = restau_addr;
		this.restau_call = restau_call;
		this.restau_descr = restau_descr;
		this.restau_lat = restau_lat;
		this.restau_lon = restau_lon;
	}

	public String getDish_id() {
		return dish_id;
	}

	public void setDish_id(String dish_id) {
		this.dish_id = dish_id;
	}

	public String getDish_image() {
		return dish_image;
	}

	public void setDish_image(String dish_image) {
		this.dish_image = dish_image;
	}

	public String getDish_name() {
		return dish_name;
	}

	public void setDish_name(String dish_name) {
		this.dish_name = dish_name;
	}

	public String getDish_score() {
		return dish_score;
	}

	public void setDish_score(String dish_score) {
		this.dish_score = dish_score;
	}

	public String getDish_price() {
		return dish_price;
	}

	public void setDish_price(String dish_price) {
		this.dish_price = dish_price;
	}

	public String getDish_taste() {
		return dish_taste;
	}

	public void setDish_taste(String dish_taste) {
		this.dish_taste = dish_taste;
	}

	public String getDish_nutrition() {
		return dish_nutrition;
	}

	public void setDish_nutrition(String dish_nutrition) {
		this.dish_nutrition = dish_nutrition;
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
