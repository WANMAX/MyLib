package jre.shrink;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.file.FileRW;
import util.file.FileTS;
import util.file.FileZ;
import util.string.Escape;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wan
 */
public class LibShrink {
    private static String jarPath;
    private static String classSetPath;
    private final static String[] JAR_ARR = new String[]{"rt","charsets"};
    private final static String LIB_PATH = "D:"+File.separator+"java"+File.separator+"类库"+File.separator+"lib"+File.separator+"jre"+File.separator+"lib"+File.separator;
    public static void getClassesPath() throws IOException{
        String classes = errer1+errer2+errer3,temp = FileRW.getCode(classSetPath);
        TreeSet<String> fileSet = null;
        temp = temp.replaceAll("\\][\\s\\S]+?\\[", "\\]\n\\[");
        temp = temp.replaceAll("\\[Loaded |\\[Opened .*?\\]| from.*?\\]", "");
        temp = temp.replaceAll("\n+", "\n");
        if(jarPath.contains("dist")){
            FileTS fileTS = new FileTS(jarPath.substring(0, jarPath.indexOf("dist"))+"src"+File.separator);
            if(!fileTS.searchFile(Escape.escapeForRegular(".java"))){
                temp = temp.replaceAll(Escape.escapeForRegular("."),Escape.escapeForRegular(File.separator));
                new FileRW(classSetPath).store(temp);
                return;
            }
            TreeSet<String> pathes = fileTS.getPathes();
            String str;
            Pattern pat = Pattern.compile("import (.*);");
            Matcher mat;
            for(String path:pathes){
                mat = pat.matcher(FileRW.getCode(path));
                while(mat.find())
                    classes+=mat.group(1)+"\n";
            }
        }
        classes+=temp;
        classes = classes.replaceAll(Escape.escapeForRegular("."),Escape.escapeForRegular(File.separator));
        new FileRW(classSetPath).store(classes);
    }
    public static void copyClass() throws IOException {
        FileInputStream fin = null;
        InputStreamReader inr = null;
        BufferedReader input = null;
        try{
            fin = new FileInputStream(classSetPath);
        }
        catch(FileNotFoundException ex){
            throw new FileNotFoundException("路径 "+classSetPath+" 没有找到文件！");
        }
        inr = new InputStreamReader(fin);
        input = new BufferedReader(inr);
        String path = input.readLine();
        String temp1,temp2,temp3;
        File file;
        boolean exists;
        while(path != null)  
        {
            try {
                exists = false;
                for(String jar:JAR_ARR){
                    temp1 = LIB_PATH+jar+File.separator+path+".class";
                    if(new File(temp1).exists()){
                        FileRW.copy(temp1,classSetPath.substring(0, classSetPath.lastIndexOf(File.separator))+File.separator+"lib"+File.separator+jar+File.separator+path+".class");
                        exists = true;
                        break;
                    }
                }
                if(!exists){
                    FileZ z = new FileZ(jarPath);
                    if(!z.searchFile(Escape.escapeForRegular(path.replaceAll(Escape.escapeForRegular(File.separator), FileZ.SEPARATOR)+".class"))){
                        System.out.println("路径 "+path+".class 找不到文件！");
                    }
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            path = input.readLine();
        }
        input.close();
        inr.close();
        fin.close();
    }
//errer:Couldn't load main class
    private final static String errer1 = "java.util.zip.ZStreamRef\n"+
                                        "java.util.zip.ZipFile$ZipFileInflaterInputStream\n"+
                                        "java.util.zip.InflaterInputStream\n"+
                                        "java.util.zip.Inflater\n"+
                                        "java.util.NoSuchElementException\n";
//errer:Couldn't Exit
    private final static String errer2 = "java.lang.Throwable$WrappedPrintStream\n" +
                                        "java.lang.Throwable$PrintStreamOrWriter\n";
//errer:The JVM could not be started
    private final static String errer3 = "java.util.LinkedHashSet\n" +
                                        "java.lang.NumberFormatException\n" +
                                        "java.io.DeleteOnExitHook\n" +
                                        "java.io.DeleteOnExitHook$1\n" +
                                        "java.awt.EventFilter\n" +
                                        "sun.awt.SunToolkit$2\n";
    public static void errer3() throws IOException{
        FileTS.copy(LIB_PATH+File.separator+"fonts",classSetPath.substring(0, classSetPath.lastIndexOf(File.separator))+File.separator+"lib");
        FileRW.copy(LIB_PATH+File.separator+"fontconfig.bfc", classSetPath.substring(0, classSetPath.lastIndexOf(File.separator))+File.separator+"lib"+File.separator+"fontconfig.bfc");
    }
    
    public static void libShrink(String Path) throws IOException, InterruptedException{//jarPath必须为绝对路径
        if(Path.equals("")) throw new IOException("libShrink的参数路径为空，请正确输入该jar的路径！");
        jarPath = Path;
        String dirs = jarPath.substring(0, jarPath.lastIndexOf(File.separator)+1),name = jarPath.substring(jarPath.lastIndexOf(File.separator)+1,jarPath.lastIndexOf(".")),type = jarPath.substring(jarPath.lastIndexOf("."));
        if(!new File(dirs+"jre"+File.separator+"class.txt").exists()){
            new File(dirs+"jre"+File.separator).mkdir();
        }
        else{
            new File(dirs+"jre"+File.separator+"class.txt").delete();
        }
        Process process =null;
        try{
            process = Runtime.getRuntime().exec("cmd /c cd "+dirs+" && java -verbose:class -jar "+name+type+" >> "+"jre"+File.separator+"class.txt");
            process.waitFor();
        }
        catch(InterruptedException ex){
            throw new InterruptedException("cmd异常终止！");
        }
        catch(IOException ex){
            throw new IOException("cmd运行错误！");
        }
        classSetPath = dirs+"jre"+File.separator+"class.txt";
        getClassesPath();
        copyClass();
        new File(classSetPath).delete();
        for(String str:JAR_ARR){
            FileZ.compress(dirs+"jre"+File.separator+"lib"+File.separator+str,dirs+"jre"+File.separator+"lib"+File.separator+str+".jar",true);
        }
        errer3();
    }
    public static void main(String[] args) throws IOException, InterruptedException{
        libShrink("D:\\java\\keyboard mapping\\dist\\Keyboard_Mapping.jar");
    }
}
