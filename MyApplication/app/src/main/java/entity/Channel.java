package entity;

public class Channel {
	private int id;
	private String name;
	private int order;
	private String weburl;
	private String hweburl;
	
	public Channel(){
		
	}
	public Channel(int id,String name, int order, String weburl, String hweburl) {
		super();
		this.id=id;
		this.name = name;
		this.order = order;
		this.weburl = weburl;
		this.hweburl = hweburl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getWeburl() {
		return weburl;
	}
	public void setWeburl(String weburl) {
		this.weburl = weburl;
	}
	public String getHweburl() {
		return hweburl;
	}
	public void setHweburl(String hweburl) {
		this.hweburl = hweburl;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
}
