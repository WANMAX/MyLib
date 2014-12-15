/**
 * 
 */
package util.string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.TreeMap;


/**
 * @author wan
 *简易科学计算器，自动对字符串进行运算，返回结果
 *（支持自定义扩展，只需要修改PRIORITY和MATHCONSTANT两个词典）
 */
public class Calculator {
	public static final Dictionary PRIORITY = new Dictionary(
			"+","0","-","0","*","1","/","1","^","3");
	public static final Dictionary MATHCONSTANT = new Dictionary("PI",String.valueOf(Math.PI));
	private static class Dictionary extends TreeMap<String,Double> {
		private Dictionary(String...paras){
			super();
			if(paras.length%2==1)
				try {
					throw new Exception("ERROR：值并不是成对出现！");
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			for(int i = 0;i<paras.length;i+=2){
				this.put(paras[i], Double.parseDouble(paras[i+1]));
			}
		}
		private double compare(String str1,String str2){
			return this.get(str1)-this.get(str2);
		}
	}
	/**
	 * @param paras 已切分的表达式
	 * @return 表达式和结果
	 */
	public static String CNP(String[] paras){
		StringBuilder sb = new StringBuilder();
		for(String str:paras){
			sb.append(str);
		}
		try {
			sb.append("="+calculator(paras));
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			return "ERROR：请检查表达式！";
		}
		return sb.toString();
	}
	/**
	 * @param para 未切分的表达式
	 * @return 表达式和结果
	 */
	public static String CNP(String para){
		try{
			return para+"="+calculator(para);			
		}catch(Exception e){
			return "ERROR：请检查表达式！";
		}
	}
	/**
	 * @param paras 已切分的表达式
	 * @return 表达式的运算结果
	 * @throws Exception 抛出错误以便终止程序
	 */
	public static double calculator(String[] paras) throws Exception{
		String[] parasClone = new String[paras.length];
		for(int i = 0;i<paras.length;i++){
			if(MATHCONSTANT.containsKey(paras[i])){
				parasClone[i] = String.valueOf(MATHCONSTANT.get(paras[i]));
			}
			else
				parasClone[i] = paras[i];
		}
		return entity(parasClone);
	}
	/**
	 * @param para 未切分的表达式
	 * @return 表达式的运算结果
	 * @throws Exception 抛出错误以便终止程序
	 */
	public static double calculator(String para) throws Exception{
		for(String str:MATHCONSTANT.keySet()){
			para = para.replaceAll(str, String.valueOf(MATHCONSTANT.get(str)));
		}
		ArrayList<String> tempList = new ArrayList<String>();
		String[] paras;
		int temp,i=0;
		do{
			temp = i;
			i = getString(para,i);
			tempList.add(para.substring(temp, i));
		}
		while(i<para.length());
		paras = new String[tempList.size()];
		for(i = 0;i<tempList.size();i++){
			paras[i] = tempList.get(i);
		}
		return entity(paras);
	}
	/**
	 * 运算实体，用于计算
	 * @param paras 已切分的表达式
	 * @return 表达式的运算结果
	 * @throws Exception 抛出错误以便终止程序
	 */
	private static Double entity(String[]paras) throws Exception{
		if(paras.length==1)return Double.valueOf(paras[0]);
		Stack<String> st = new Stack<String>();
		int i;
		if(paras[0].equals("(")){
			i = getOffset(paras,-1);
			if(i==paras.length)return Double.valueOf(paras[1]);
			st.push(String.valueOf(entity(Arrays.copyOfRange(paras, 1, i-1))));
			st.push(paras[i]);
			i+=2;
		}
		else{
			st.push(paras[0]);
			st.push(paras[1]);
			i = 3;
		}
		int temp;String numb1,symbol;
		while(i<paras.length){
			if(st.isEmpty()){
				st.push(paras[i-1]);
				st.push(paras[i]);
				i+=2;
				temp = i-1;
			}
			else{
				temp = i-1;
			}
			i = getOffset(st.peek(),paras,i);
			if(paras[i-1].equals(")")){
				int temp2 = temp;
				while(!paras[temp2].equals("(")){
					temp2++;
				}
				paras[i-1] = String.valueOf(entity(Arrays.copyOfRange(paras, temp2+1, i-1)));	
				for(;temp<temp2;temp++){
					st.push(paras[temp]);
				}
			}
			else{
				for(;temp+1<i;temp++){
					st.push(paras[temp]);
				}
				symbol=st.pop();
				numb1=st.pop();
				paras[i-1]=String.valueOf(DNC(numb1,symbol,paras[i-1]));
			}
		}
		st.push(paras[i-1]);
		return ROC(st);
	}
	/**
	 * 通过比较优先级，返回优先计算的位置
	 * @param str 用于对比的第一个符号
	 * @param paras 已切分的表达式
	 * @param start 用于对比的第二个符号的位置
	 * @return 优先计算的位置
	 */
	private static int getOffset(String str,String[] paras,int start){
		if(start+2>paras.length){
			return start;
		}
		if(paras[start-1].equals("(")){
			int num = 1;
			for(;num>0;start++){
				if(paras[start].equals("("))
					num++;
				else if(paras[start].equals(")"))
					num--;
			}
			return start;
		}
		if(PRIORITY.compare(str, paras[start])<0){
			return getOffset(paras,start);
		}
		else{
			return start;
		}
	}
	/**
	 * 通过比较优先级，返回优先计算的位置
	 * @param paras 已切分的表达式
	 * @param start 用于对比的第一个符号的位置（第二个为start+2）
	 * @return 优先计算的位置
	 */
	private static int getOffset(String[] paras,int start){
		if(start+4>paras.length){
			return start+2;
		}
		if(paras[start+1].equals("(")){
			int num = 1;
			for(start+=2;num>0;start++){
				if(paras[start].equals("("))
					num++;
				else if(paras[start].equals(")"))
					num--;
			}
			return start;
		}
		if(PRIORITY.compare(paras[start], paras[start+2])<0){
			return getOffset(paras,start+2);
		}
		else{
			return start+2;
		}
	}
	/**
	 * 对表达式栈进行逆序计算
	 * @param st 表达式栈（已经经过处理）
	 * @return 表达式栈的运算结果
	 * @throws Exception 抛出错误以便终止程序
	 */
	private static double ROC(Stack<String> st) throws Exception{
		String result = st.pop();
		String numb1,symbol;
		while(!st.empty()){
			symbol = st.pop();
			numb1 = st.pop();
			result = String.valueOf(DNC(numb1,symbol,result));
		}
		return Double.parseDouble(result);
	}
	/**
	 * @param paras 分别为第一个数字，运算符号，第二个数字
	 * @return 返回计算结果
	 * @throws Exception 抛出错误以便终止程序
	 */
	private static double DNC(String...paras) throws Exception{
		if(paras.length!=3)throw new Exception();
		if(paras[1].equals("+")){
			return Double.parseDouble(paras[0])+Double.parseDouble(paras[2]);
		}
		else if(paras[1].equals("-")){
			return Double.parseDouble(paras[0])-Double.parseDouble(paras[2]);
		}
		else if(paras[1].equals("*")){
			return Double.parseDouble(paras[0])*Double.parseDouble(paras[2]);
		}
		else if(paras[1].equals("/")){
			return Double.parseDouble(paras[0])/Double.parseDouble(paras[2]);
		}
		else if(paras[1].equals("^")){
			return Math.pow(Double.parseDouble(paras[0]),Double.parseDouble(paras[2]));
		}
		else
			throw new Exception();
	}
	/**
	 * 用于切分表达式
	 * @param str 未切分的表达式
	 * @param start 当前符号的起始位置
	 * @return 下一个符号的起始位置
	 */
	private static int getString(String str,int start){
		if(!Character.isDigit(str.charAt(start)))
			return start+1;
		for(start+=1;start<str.length();start++){
			if(!Character.isDigit(str.charAt(start))&&str.charAt(start)!='.')
				return start;
		}
		return start;
	}
	/**
	 * 用于测试和说明
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		String[] paras = {
			"(","PI",")"	
		};
		String paras1 = "1/2*3^(1/2)+2^3*6";
		String[] paras2 = {
				"PI","+","2","^","(","2","+","3",")","/","4"
		};
		String[] paras3 = {
				"2","^","5","/","4"
		};
		System.out.println(Calculator.CNP(paras1));
	}
}
