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
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

/**
 *
 * @author wan
 */
public class FileRW {
    public final String path;
    private static String charset = "GBK";
    
    public FileRW(String path){
        this.path = path;
    }
    public FileRW(String path,String charset){
    	this.path = path;this.charset = charset;
    }
    public FileRW(File file){
    	this.path = file.toString();
    }
    public FileRW(File file,String charset){
        this.charset = charset;
        this.path = file.toString();
    }
    protected void setCharset(String charset){
        this.charset = charset;
    }
    public static void main(String args[]) throws ParseException, IOException{
		System.out.println(FileRW.getCode("http://weibo.com/p/1008084d99b09ced3cec4eb26d586facc931c7?k=抗震小英雄涉诈骗&from=501&_from_=huati_topic"));
	}
//以charset格式从文本读取源码
    public static String getCode(String path) throws IOException{
    	return getCode(path,charset);
    }
    public static String getCode(String path,String charset)throws IOException{
        if(path.matches("http://.*")){
        	try{
        		return getCode(new URL(path),charset);
        	}
        	catch(MalformedURLException ex){
        		throw new MalformedURLException("路径 "+path+" 无法正确转为网址！");
        	}
        }
        else{
            return getCode(new File(path),charset);
        }
    }
    public static String getCode(File file) throws IOException{
    	return getCode(file,charset);
    }
    public static String getCode(File file,String charset)  throws IOException{
        InputStream in = null;
        BufferedInputStream bin = null;
        InputStreamReader input = null;
        StringBuilder string=new StringBuilder();
        try{
            in = new FileInputStream(file);
            bin = new BufferedInputStream(in);
            input = new InputStreamReader(bin,charset);
            char[] b=new char[1024];
            int len;
            while((len=input.read(b))!=-1){
                string.append(new String(b,0,len));
            }
        }
        catch(OutOfMemoryError ex){
            throw new OutOfMemoryError("路径 "+file.toString()+" 内容太多！无法一次获取！");
        }
        catch(FileNotFoundException ex){
            throw new FileNotFoundException("路径 "+file.toString()+" 找不到文件！");
        }
        catch(IOException ex){
            throw new IOException("路径 "+file.toString()+" 内容获取错误！");
        }
        finally{
            if(input!=null)input.close();
            if(bin!=null)bin.close();
            if(in!=null)in.close();
        }
        return string.toString();
    }
    public static String getCode(URL url) throws IOException{
    	return getCode(url,charset);
    }
    public static String getCode(URL url,String charset) throws IOException {
        InputStream in = null;
        BufferedInputStream bin = null;
        InputStreamReader input = null;
        StringBuilder string=new StringBuilder();
        try{
            in = url.openConnection().getInputStream();
            bin = new BufferedInputStream(in);
            input = new InputStreamReader(bin,charset);
            char[] b=new char[1024];
            int len;
            while((len=input.read(b))!=-1){
                string.append(new String(b,0,len));
            }
        }
        catch(OutOfMemoryError ex){
            throw new OutOfMemoryError("路径 "+url.toString()+" 内容太多！无法一次获取！");
        }
        catch(IOException ex){
            throw new IOException("路径 "+url.toString()+" 内容获取错误！");
        }
        finally{
            if(input!=null)input.close();
            if(bin!=null)bin.close();
            if(in!=null)in.close();
        }
        return string.toString();
    }
//以charset格式从文本的head到tail读取源码
    public String randomGC(int head,int tail) throws IOException{
    	return randomGC(head,tail,charset);
    }
    public String randomGC(int head,int tail,String charset) throws IOException{
        this.charset = charset;
        FileInputStream fin = null;
        BufferedInputStream bin = null;
        InputStreamReader input = null;
        StringBuilder string=new StringBuilder();
        int i=0;
        try{
            fin = new FileInputStream(path);
            bin = new BufferedInputStream(fin);
            input = new InputStreamReader(bin,this.charset);
            char[] b=new char[1024];
            int len;
            while((len=input.read(b))!=-1){
                i+=len;
                if(i>=head){
                    string.append(new String(b,head-i+len,i-head));
                    while((len=input.read(b))!=-1){
                        if(i>tail){
                            break;
                        }
                        else{
                            i+=len;
                            string.append(new String(b,0,len));
                        }
                    }
                    if(i>tail){
                        string.delete(tail-head, i-head);
                        break;
                    }
                }
            }
        }
        catch(OutOfMemoryError ex){
            throw new OutOfMemoryError("路径 "+path+" 的 "+head+" 到 "+tail+" 内容太多！获取失败！");
        }
        catch(FileNotFoundException ex){
            throw new FileNotFoundException("路径 "+path+" 找不到文件！");
        }
        catch(StringIndexOutOfBoundsException ex){
            throw new StringIndexOutOfBoundsException("请检查head（ "+head+" ）和tail（ "+tail+" ）的值！");
        }
        catch(IOException ex){
            throw new IOException("路径 "+path+" 的 "+head+" 到 "+tail+" 内容获取错误！");
        }
        finally{
            if(input!=null)input.close();
            if(bin!=null)bin.close();
            if(fin!=null)fin.close();
        }
        return string.toString();
    }
//以charset格式写入文本
    public void store(String content) throws IOException{
    	store(content,charset);
    }
    public void store(String content,String charset) throws IOException{
    	File file;
        this.charset = charset;
        if(!(file = new File(path.substring(0,path.lastIndexOf(File.separator)+1))).exists())
	        file.mkdirs();
        FileOutputStream fout = null;
        BufferedOutputStream bout = null;
        OutputStreamWriter output = null;
        try{
            fout = new FileOutputStream(path);
            bout = new BufferedOutputStream(fout);
            output = new OutputStreamWriter(bout,this.charset);
            output.write(content);
        }
        catch(IOException ex){
            throw new IOException("路径 "+path+" 存储失败！");
        }
        finally{
            if(output!=null)output.close();
            if(bout!=null)bout.close();
            if(fout!=null)fout.close();
        }
    }
//续写文本
    public void append(String content) throws IOException{
        FileOutputStream fout = null;
        BufferedOutputStream bout = null;
        OutputStreamWriter output = null;
        try{
            fout = new FileOutputStream(path,true);
            bout = new BufferedOutputStream(fout);
            output = new OutputStreamWriter(bout,charset);
            output.write(content);
        }
        catch(IOException ex){
            throw new IOException("路径 "+path+" 续写失败！");
        }
        finally{
            if(output!=null)output.close();
            if(bout!=null)bout.close();
            if(fout!=null)fout.close();
        }
    }
//把mainFile复制或剪切到secondPath下
    public static void copy(File mainFile,String secondPath) throws IOException{
        copy(mainFile,secondPath,false);
    }
    public static void copy(File mainFile,String secondPath,boolean remove) throws IOException{
        String path = mainFile.toString();
        if(path.contains(File.separator))path = path.substring(path.lastIndexOf(File.separator)+File.separator.length());
        else if(path.contains(":"))path = path.substring(path.indexOf(":")+":".length());
        if(secondPath.charAt(secondPath.length()-1)!=File.pathSeparatorChar)secondPath+=File.separator;
        copy(mainFile.toString(),secondPath+path,remove);
    }
//从mainPath到secondPath复制或剪切文件
    public static void copy(String mainPath,String secondPath) throws IOException{
    	copy(mainPath,secondPath,false);
    }
    public static void copy(String mainPath,String secondPath,boolean remove) throws IOException{
        File file;
        FileInputStream in = null;
        BufferedInputStream input = null;
        FileOutputStream out = null;
        BufferedOutputStream output = null;
        try{
            in = new FileInputStream(mainPath);
            input = new BufferedInputStream(in);
            if(!(file = new File(secondPath.substring(0,secondPath.lastIndexOf(File.separator)+1))).exists())
                file.mkdirs();
            out = new FileOutputStream(secondPath,true);
            output = new BufferedOutputStream(out);
            byte[] b = new byte[1024];
            int len=0;
            while((len=input.read(b))!=-1){
                output.write(b,0,len);
            }
            if(remove){
                new File(mainPath).delete();
            }
        }
        catch(FileNotFoundException ex){
            throw new FileNotFoundException("路径 "+mainPath+" 找不到文件\nor路径 "+secondPath+" 无法创建文件！");
        }
        catch(IOException ex){
            throw new IOException("路径 "+mainPath+" 读取失败\nor路径 "+secondPath+" 存储失败！");
        }
        finally{
            if(output!=null)output.close();
            if(out!=null)out.close();
            if(input!=null)input.close();
            if(in!=null)in.close();
        }
    }
//从mainPath下载文件
    public static void download(String mainPath,String secondPath) throws IOException{
    	try{
    		download(new URL(mainPath),secondPath);
    	}
    	catch(MalformedURLException ex){
    		throw new MalformedURLException("路径 "+mainPath+" 无法正确转为网址！");
    	}
    }
    public static void download(URL mainUrl,String secondPath) throws IOException{
        File file;
        InputStream in = null;
        BufferedInputStream input = null;
        FileOutputStream out = null;
        BufferedOutputStream output = null;
        try{
            in = mainUrl.openConnection().getInputStream();
            input = new BufferedInputStream(in);
            if(!(file = new File(secondPath.substring(0,secondPath.lastIndexOf(File.separator)+1))).exists())
                file.mkdirs();
            out = new FileOutputStream(secondPath,true);
            output = new BufferedOutputStream(out);
            byte[] b = new byte[1024];
            int len=0;
            while((len=input.read(b))!=-1){
                output.write(b,0,len);
            }
        }
        catch(FileNotFoundException ex){
            throw new FileNotFoundException("路径 "+secondPath+" 无法创建文件！");
        }
        catch(IOException ex){
            throw new IOException("路径 "+mainUrl.toString()+" 读取失败\nor路径 "+secondPath+" 存储失败！");
        }
        finally{
            if(output!=null)output.close();
            if(out!=null)out.close();
            if(input!=null)input.close();
            if(in!=null)in.close();
        }
    }
}
