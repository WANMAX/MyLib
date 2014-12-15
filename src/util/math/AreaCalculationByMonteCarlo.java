/**
 * 
 */
package util.math;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author wan
 *
 */
public class AreaCalculationByMonteCarlo {//用蒙特卡洛方法计算任意直线多边形的面积
	public static int testNum = 100000;//随机次数
	ArrayList<Point> points;
	MainArea mainArea;//随机区域(仅支持矩形！)
	public AreaCalculationByMonteCarlo(ArrayList<Point> points,MainArea mainArea){
		this.points = points;
		this.mainArea = mainArea;
	}
	public AreaCalculationByMonteCarlo(ArrayList<Point> points,MainArea mainArea,int testNum){
		this.points = points;
		this.mainArea = mainArea;
		AreaCalculationByMonteCarlo.testNum = testNum;
	}
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO 自动生成的方法存根
		//单位长
		final double LENGTH = 100;
		//
		double probability = 0;
		MainArea mainArea = new MainArea(-LENGTH,LENGTH,LENGTH*2,LENGTH*2);
		ArrayList<Point> points1 = new ArrayList<Point>();
		points1.addAll(Arrays.asList(new Point(-LENGTH,-LENGTH),
				new Point(-LENGTH,LENGTH),new Point(0,LENGTH),new Point(0,-LENGTH)
				));
		AreaCalculationByMonteCarlo test1 = new AreaCalculationByMonteCarlo(points1,mainArea);
		probability += test1.probabilityCalculating();
		ArrayList<Point> points2 = new ArrayList<Point>();
		points2.addAll(Arrays.asList(new Point(0,LENGTH),
				new Point(0,0),new Point(LENGTH,0)
				));
		AreaCalculationByMonteCarlo test2 = new AreaCalculationByMonteCarlo(points2,mainArea);
		probability += test2.probabilityCalculating();
		System.out.print(probability);
	}
	public double areaCalculating() throws Exception{//计算面积
		return probabilityCalculating()*mainArea.getArea();
	}
	public double probabilityCalculating() throws Exception{//计算概率
		int count = 0;
		Point p;
		Field field = new Field(points);
		for(int i = 0;i<testNum;i++ ){
			p = new Point(mainArea.x+Math.random()*mainArea.width,mainArea.y-Math.random()*mainArea.height);
			if(field.test(p))count++;
		}
		return (double)count/testNum;
	}
}
class MainArea{
	public double x,y,height,width;
	public MainArea(double x,double y,double width,double height){//(x,y)为矩形左上顶点的坐标
		this.x = x;this.y = y;
		this.width = width;
		this.height = height;
	}
	public double getArea(){
		return width*height;
	}
}
class Field{
	public static final double PERIGON = 360;//圆周
	public static double errorRange = 1e-9;//误差范围
	private ArrayList<Point> points; 
	public Field(ArrayList<Point> points){
		this.points = points;
	}
	public boolean test(Point p) throws Exception{//测试p点在不在区域内
		double angle = 0;
		if(points.size()<3) throw new Exception("顶点数小于3！");
		for(int n = 0;n<points.size()-1;n++){
			angle += getAngle(p,points.get(n),points.get(n+1));
		}
		angle += getAngle(p,points.get(0),points.get(points.size()-1));
		if(Math.abs(angle-PERIGON)<errorRange||Double.isNaN(angle))return true;
		else return false;
	}
	public static double getAngle(Point p,Point a,Point b){//返回∠apb
		Point c = a.relativePoint(p);
		Point d = b.relativePoint(p);
		return Math.acos((c.x*d.x+c.y*d.y)/(Math.sqrt((c.x*c.x+c.y*c.y)*(d.x*d.x+d.y*d.y))))*180/Math.PI;
	}
}
class Point{
	public Point(double x,double y){
		this.x = x;this.y = y;
	}
	public double x;
	public double y;
	public Point relativePoint(Point b){
		return new Point(x-b.x,y-b.y);
	}
	public String toString(){
		return "("+x+","+y+")";
	}
}