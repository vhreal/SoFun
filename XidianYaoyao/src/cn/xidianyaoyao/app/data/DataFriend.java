package cn.xidianyaoyao.app.data;

public class DataFriend {

	private String friend_image;
	private String friend_name;
	private String friend_gender;

	public DataFriend(String friend_image, String friend_name,
			String friend_gender) {
		this.friend_image = friend_image;
		this.friend_name = friend_name;
		this.friend_gender = friend_gender;
	}

	public String getFriend_image() {
		return friend_image;
	}

	public void setFriend_image(String friend_image) {
		this.friend_image = friend_image;
	}

	public String getFriend_name() {
		return friend_name;
	}

	public void setFriend_name(String friend_name) {
		this.friend_name = friend_name;
	}

	public String getFriend_gender() {
		return friend_gender;
	}

	public void setFriend_gender(String friend_gender) {
		this.friend_gender = friend_gender;
	}

}
