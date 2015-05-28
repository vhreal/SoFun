package cn.xidianyaoyao.app.data;

public class DataDishEvaluate {

	private String head;
	private String name;
	private String gender;
	private String score;
	private String time;
	private String summary;

	public DataDishEvaluate(String time, String head, String name,
			String gender, String score, String summary) {
		this.time = time;
		this.head = head;
		this.name = name;
		this.gender = gender;
		this.score = score;
		this.summary = summary;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

}
