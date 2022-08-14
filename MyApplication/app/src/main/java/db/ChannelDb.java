package db;


import java.util.ArrayList;
import java.util.List;

import Util.SharedPreferencesUtil;
import entity.Channel;


public class ChannelDb {
private static Channel  c;
	private static List<Channel>   selectedChannel= new ArrayList<>();
	private static List<Channel>   selectedChannes= new ArrayList<>();
	private static final int count=5;
	private static final int page=1;
	static{

		 c=new Channel();
		selectedChannel.add(new Channel(count,"头条",0,"",""));
		String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "");
		if(city.equals("")){
			selectedChannel.add(new Channel(count,"本地",0,"",""));
		}else{
			selectedChannel.add(new Channel(count,city,0,"",""));
		}

//		selectedChannel.add(new Channel(count,"科技库",0,"",""));
//		selectedChannel.add(new Channel(count,"项目",0,"",""));
//
//		selectedChannel.add(new Channel(count,"人才",0,"",""));
//		selectedChannel.add(new Channel(count,"设备",0,"",""));
//		selectedChannel.add(new Channel(count,"实验室",0,"",""));
//		selectedChannel.add(new Channel(count,"专利",0,"",""));
		selectedChannes.addAll(selectedChannel);
	}

	public static  List<Channel> getSelectedChannel(){
		return selectedChannel;
	}

	public static void changeChannel(String channel){
		selectedChannel.get(1).setName(channel);

	}
}
