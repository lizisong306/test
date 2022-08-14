package Util;


/**
 * 
 * @author Administrator
 *	輸入框校驗工具類
 */
public class EditCheckUtils {

	
	public static String checkEt(String userName,String userPwd){
		String str="";
		if (userName.equals("")) {
			str+="用户名不能为空,";
		}if (userPwd.equals("")) {
			str+="密码不能为空,";
		}
		if (!str.equals("")) {
			return str+"请检查后重新输入!";
		}else{
			if (userName.length()!=11){
				str+="手机号不合法,";
			}
		}
		return "";
		
	}
	public static String regist(String userPwd,String name){
		String str="";
		if (userPwd.equals("")) {
			str+="密码不能为空,";
		}if (name.equals("")) {
			str+="昵称不能为空,";
		}
		if (!str.equals("")) {
			return str+"请检查后重新输入!";
		}else{

		}
		return "";

	}
	public static String yanzhen(String userphone,String useryzm){
		String str="";
		if (userphone.equals("")) {
			str+="手机号不能为空,";
		}if (useryzm.equals("")) {
			str+="验证码不能为空,";
		}
		if (!str.equals("")) {
			return str+"请检查后重新输入!";
		}else{
			if (userphone.length()!=11){
				str+="手机号不合法,";
			}
		}
		return "";

	}

	public static String submitinfo(String name){
		String str="";
		if (name.equals("")) {
			str+="姓名不能为空,";
		}
		if (!str.equals("")) {
			return str+"请检查后重新输入!";
		}
		return "";

	}

	String txs,tx,name,person,mobil,mail,yewo,tel;
	public static String comp_info(String name,String txs,String tx,String tel,String person,String mobil,String emali,String yemo){
		String str="";
		if (name.equals("")) {
			str+="姓名不能为空,";
		}if (txs.equals("")) {
			str+="地区不能为空,";
		}if (tx.equals("")) {
			str+="行业不能为空,";
		}
		if (tel.equals("")) {
			str+="手机号不能为空,";
		}
		if (person.equals("")) {
			str+="联系人不能为空,";
		}
		if (mobil.equals("")) {
			str+="电话不能为空,";
		}
		if (yemo.equals("")) {
			str+="主营业务不能为空,";
		}
		if (emali.equals("")) {
			str+="邮箱不能为空,";
		}
		if (!str.equals("")) {
			return str+"请检查后重新输入!";
		}
		return "";

	}

	public static String unit_info(String name,String txs,String tx,String tel,String person){
		String str="";
		if (name.equals("")) {
			str+="单位名称不能为空,";
		}if (txs.equals("")) {
			str+="单位类型不能为空,";
		}if (tx.equals("")) {
			str+="所在地不能为空,";
		}
		if (tel.equals("")) {
			str+="联系方式不能为空,";
		}
		if (person.equals("")) {
			str+="职位不能为空,";
		}

		if (!str.equals("")) {
			return str+"请检查后重新输入!";
		}
		return "";

	}
	 
}
