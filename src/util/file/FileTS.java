/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util.file;

import java.io.File;
import java.io.IOException;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 *
 * @author wan
 */
public class FileTS {
    public final String path;
    private final File mainFile;
    private TreeSet<String> pathes;
    public FileTS(String path){
        this.path = path;
        mainFile = new File(this.path);
    }
    public FileTS(File file){
        this.path = file.toString();
        mainFile = file;
    }
    public TreeSet<String> getPathes(){
        return pathes;
    }
    
//在mainFile目录下寻找名字为name的文件(支持正则表达式)
    public boolean searchFile(String path){
        pathes = traverseFile(path);
        if(pathes.isEmpty())return false;
        else return true;
    }
//遍历mainFile目录下所有文件
    public TreeSet<String> getAllFile(){
        pathes = traverseFile("");
        return pathes;
    }
//删除mainFest目录下所有文件包括空文件夹
    public static void deleteAll(String mainPath){
        deleteAll(new File(mainPath));
    }
    private static void deleteAll(File mainFile){
        File[] files = mainFile.listFiles();
        if(files!=null){
            for(File file:files){
                deleteAll(file);
            }
        }
        mainFile.delete();
    }
//从mainPath到secondPath复制或剪切文件
    public static void copy(String mainPath,String secondPath) throws IOException{
    	copy(mainPath,secondPath,false);
    }
    public static void copy(String mainPath,String secondPath,boolean remove) throws IOException{
        FileTS fileTS = new FileTS(mainPath);
        TreeSet<String> pathes = fileTS.getAllFile();
        if(mainPath.charAt(mainPath.length()-1)==File.separatorChar)mainPath = mainPath.substring(0, mainPath.length()-1);
        if(mainPath.contains(File.separator))mainPath = mainPath.substring(mainPath.lastIndexOf(File.separator)+File.separator.length());
        else if(mainPath.contains(":"))mainPath.substring(mainPath.indexOf(":")+":".length());
        mainPath+=File.separator;
        if(secondPath.charAt(secondPath.length()-1)!=File.separatorChar)secondPath+=File.separator;
        for(String path:pathes){
            FileRW.copy(path,secondPath+mainPath+path.substring(path.indexOf(mainPath)+mainPath.length()), remove);
        }
    }
    public static void copy(File mainFile,String secondPath) throws IOException{
    	copy(mainFile.toString(),secondPath,false);
    }
    public static void copy(File mainFile,String secondPath,boolean remove) throws IOException{
        copy(mainFile.toString(),secondPath,remove);
    }
    
    private TreeSet<String> traverseFile(String str){
        return traverseFile(mainFile,str);
    }
    private TreeSet<String> traverseFile(File mainFile,String str){
        Pattern pat = Pattern.compile(str);
        TreeSet<String> pathSet = new TreeSet();
        File[] files = mainFile.listFiles();
        if(files==null){
            if(pat.matcher(mainFile.getAbsolutePath()).find())
                pathSet.add(mainFile.getAbsolutePath());
            return pathSet;
        }
        for(File file:files){
            pathSet.addAll(traverseFile(file,str));
        }
        return pathSet;
    }
}
