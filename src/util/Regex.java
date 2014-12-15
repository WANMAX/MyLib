/**
 * 
 */
package util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wan
 *
 */
public class Regex {
	public final static String CHINESE = "[u4e00-u9fa5]";
	public final static String RIGHTURL = "(http|https|ftp)://([a-zA-Z0-9\\.-]+(:[a-zA-Z0-9\\.&amp;%\\$-]+)*@)*((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|localhost|([a-zA-Z0-9-]+\\.)*[a-zA-Z0-9-]+\\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(/($|[a-zA-Z0-9\\.,\\?\\'\\\\\\+&amp;%\\$#=~_-]+))*";
	public final static String DATE = "((((19){1}|(20){1})d{2})|d{2})[01]{1}d{1}[0-3]{1}d{1}";
	public final static String URL = "";
			
	public static String[] getAllChinese(String str){
		return get(str,CHINESE);
	}
	public static String[] getAllCHinese(String str,int start){
		return getAllChinese(str.substring(start));
	}
	public static String[] getAllChinese(String str,int start,int end){
		return getAllChinese(str.substring(start,end));
	}
	public static String[] getURLs(String str){
		return get(str,RIGHTURL);
	}
	public static String[] getDates(String str){
		return get(str,DATE);
	}
	public static String[] get(String str,String regex){
		Pattern pat = Pattern.compile(regex);
		Matcher mat = pat.matcher(str);
		ArrayList<String> temp = new ArrayList<String>();
		while(mat.find()){
			temp.add(mat.group());
		}
		if(temp.size()==0)
			return null;
		String[] temp2 = new String[temp.size()];
		for(int i = 0;i<temp.size();i++)
			temp2[i] = temp.get(i);
		temp = null;
		return temp2;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		for(String str:getURLs("http://www.baidu.com/s?ie=utf-8&f=3&rsv_bp=1&tn=baidu&wd=%E7%BD%91%E5%9D%80%20%E6%AD%A3%E5%88%99%E8%A1%A8%E8%BE%BE%E5%BC%8F&rsv_enter=1&rsv_sug3=21&rsv_sug4=1591&rsv_sug1=4&rsv_sug2=0&inputT=2916&sug=%E7%BD%91%E5%9D%80%E6%AD%A3%E5%88%99%E8%A1%A8%E8%BE%BE%E5%BC%8F&oq=%E7%BD%91%E5%9D%80%20%E6%AD%A3%E5%88%99&rsv_n=1&rsp=0")){
			System.out.println(str);
		}
	}

}
