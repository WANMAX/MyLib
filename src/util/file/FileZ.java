/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import util.string.Escape;

/**
 *
 * @author wan
 */
public class FileZ {
    public final static String SEPARATOR = "/";
    
    public final ZipFile zFile;
    private TreeSet<String> names;
    public FileZ(String zPath) throws IOException{
        try {
            zFile = new ZipFile(zPath);
        } catch (IOException ex) {
            throw new IOException("路径 "+zPath+" 不是zip压缩包\nor不允许读写！");
        }
    }
    public FileZ(ZipFile zFile){
        this.zFile = zFile;
    }
    public TreeSet<String> getNames(){
        return names;
    }
    public void close() throws IOException{
        try {
            zFile.close();
        } catch (IOException ex) {
            throw new IOException("路径 "+zFile.getName()+" 关闭失败！");
        }
    }
//在ZFile目录下寻找名字为name的文件(支持正则表达式)
    public boolean searchFile(String name) {
        names = traverseFile(name);
        if(names.isEmpty())return false;
        else return true;
    }
//遍历ZipFile目录下所有文件
    public TreeSet<String> getAllFile() {
        names = traverseFile("");
        return names;
    }
    private TreeSet<String> traverseFile(String name) {
        TreeSet<String> nameSet = new TreeSet();
        Pattern pat = Pattern.compile(name);
        Matcher mat;
        Enumeration eTemp = zFile.entries();
        ZipEntry zTemp;
        while(eTemp.hasMoreElements()){
            if((zTemp = (ZipEntry)eTemp.nextElement()).isDirectory())
                continue;
            mat = pat.matcher(zTemp.getName());
            if(mat.find())
                nameSet.add(zTemp.getName());
        }
        return nameSet;
    }
//读取zip中的名字为name的文件源码
    public String getCode(String name) throws IOException{
    	return getCode(name,"gbk");
    }
    public String getCode(String name,String charset) throws IOException{
        InputStream in = null;
        BufferedInputStream bin = null;
        InputStreamReader input = null;
        StringBuilder string=new StringBuilder();
        try{
            in = zFile.getInputStream(zFile.getEntry(name));
            bin = new BufferedInputStream(in);
            input = new InputStreamReader(bin,charset);
            char[] b=new char[1024];
            int len;
            while((len=input.read(b))!=-1){
                string.append(new String(b,0,len));
            }
        }
        catch(OutOfMemoryError ex){
            throw new OutOfMemoryError("路径 "+name+" 内容太多！无法一次获取！");
        }
        catch(FileNotFoundException ex){
            throw new IOException("路径 "+name+" 找不到文件！");
        }
        catch(IOException ex){
            throw new IOException("路径 "+name+" 内容获取错误！");
        }
        finally{
            if(input!=null)input.close();
            if(bin!=null)bin.close();
            if(in!=null)in.close();
        }
        return string.toString();
    }
//将mainpath压缩到zippath
    public static void compress(String mainPath,String zipPath) throws IOException{
    	compress(mainPath,zipPath,false);
    }
    public static void compress(String mainPath,String zipPath,boolean remove) throws IOException{
        FileTS fileTS = new FileTS(mainPath);
        fileTS.getAllFile();
        compress(fileTS.getPathes(),zipPath);
        if(remove)FileTS.deleteAll(mainPath);
    }
    public static void compress(File mainFile,String zipPath) throws IOException{
    	compress(mainFile,zipPath,false);
    }
    public static void compress(File mainFile,String zipPath,boolean remove) throws IOException{
        FileTS fileTS = new FileTS(mainFile);
        fileTS.getAllFile();
        compress(fileTS.getPathes(),zipPath);
        if(remove)FileTS.deleteAll(mainFile.toString());
    }
//names压缩到zipPath
    public static void compress(TreeSet<String> names,String zipPath) throws FileNotFoundException, IOException {
        File file;
        if((file = new File(zipPath)).exists())file.delete();
        if(!(file = new File(zipPath.substring(0, zipPath.lastIndexOf(File.separator)))).exists())file.mkdirs();
        FileOutputStream fo = null;
        BufferedOutputStream bo = null;
        ZipOutputStream zo = null;
        String test = zipPath.substring(zipPath.lastIndexOf(File.separator),zipPath.lastIndexOf("."))+File.separator,temp;
        try {
            fo = new FileOutputStream(zipPath);
            bo = new BufferedOutputStream(fo);
            zo = new ZipOutputStream(bo);
            for(String name:names){
                if(name.contains(test)){
                    temp = name.substring(name.indexOf(test)+test.length());
                    temp = temp.replaceAll(Escape.escapeForRegular(File.separator), SEPARATOR);
                    zo.putNextEntry(new ZipEntry(temp));
                }else{
                    if(name.contains(File.separator))temp = name.substring(name.lastIndexOf(File.separator)+File.separator.length());
                    else if(name.contains(":"))temp = name.substring(name.lastIndexOf(":")+":".length());
                    else temp = name;
                    temp = temp.replaceAll(Escape.escapeForRegular(File.separator), SEPARATOR);
                    zo.putNextEntry(new ZipEntry(temp));
                }
                compress(name,zo);
                zo.closeEntry();
            }
        }
        catch (FileNotFoundException ex) {
            throw new FileNotFoundException("名字集路径错误\nor路径 "+zipPath+" 无法创建文件！");
        }
        finally{
            if(zo!=null)zo.close();
            if(bo!=null)bo.close();
            if(fo!=null)fo.close();
        }
    }
    private static void compress(String name,ZipOutputStream zo) throws FileNotFoundException, IOException{
        FileInputStream fi = null;
        BufferedInputStream bi = null;
        try {
            fi = new FileInputStream(name);
            bi = new BufferedInputStream(fi);
            byte[] b = new byte[1024];
            int len;
            while((len = bi.read(b))!=-1){
                zo.write(b, 0, len);
            }
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException("路径 "+name+" 找不到文件！");
        }
        finally{
            if(bi!=null)bi.close();
            if(fi!=null)fi.close();
        }
    }
//(将para[0])解压到path
    public void decompress(String path,TreeSet<String>...para) throws IOException{;
        if(para.length>0){
            TreeSet<String> names = para[0];
        }
        for(String name:names){
            decompress(name,path+name);
        }
    }
    private void decompress(String name,String path) throws IOException{
        path = path.replaceAll(SEPARATOR, Escape.escapeForRegular(File.separator));
        InputStream in = null;
        BufferedInputStream input = null;
        FileOutputStream out = null;
        BufferedOutputStream output = null;
        try{
            in = zFile.getInputStream(zFile.getEntry(name));
            input = new BufferedInputStream(in);
            new File(path.substring(0,path.lastIndexOf(File.separator)+1)).mkdirs();
            out = new FileOutputStream(path,true);
            output = new BufferedOutputStream(out);
            byte[] b = new byte[1024];
            int len=0;
            while((len=input.read(b))!=-1){
                output.write(b,0,len);
            }
        }
        catch(FileNotFoundException ex){
            throw new FileNotFoundException("路径 "+zFile.getName()+File.separator+name+" 找不到文件\nor路径 "+path+" 无法创建文件！");
        }
        catch(IOException ex){
            throw new IOException("路径 "+zFile.getName()+File.separator+name+" 读取失败\nor路径 "+path+" 存储失败！");
        }
        finally{
            if(output!=null)output.close();
            if(out!=null)out.close();
            if(input!=null)input.close();
            if(in!=null)in.close();
        }
    }
}
