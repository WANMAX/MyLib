/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author wan
 */
public class FileBRW extends FileRW{
    //大文本一次处理字符数量
    final static int bigLen = 100000;
    //
    private int index=-1;
    private String str;
    private boolean regularExpression = false;
    private Pattern pat;
    private String result;
    
    public FileBRW(File file){
    	this(file.toString());
    }
    public FileBRW(File file,String para){
        this(file.toString(),para);
    }
    public FileBRW(File file,boolean regularExpression){
    	this(file.toString(),regularExpression);
    }
    public FileBRW(File file,boolean regularExpression,String para){
        this(file.toString(),regularExpression,para);
    }
    public FileBRW(String path){
    	super(path);
    }
    public FileBRW(String path,String str){
    	super(path);
    	this.str = str;
    }
    public FileBRW(String path,String str,String charset){
    	super(path);
    	this.str = str;
    	setCharset(charset);
    }
    public FileBRW(String path,boolean regularExpression){
    	super(path);
        if(regularExpression) this.regularExpression =regularExpression;
    }
	public FileBRW(String path,boolean regularExpression,String str){
		super(path);
        if(regularExpression) this.regularExpression =regularExpression;
        if(this.regularExpression){
            pat = Pattern.compile(str);
        }
        else{
            this.str = str;
        }
    }
    public FileBRW(String path,boolean regularExpression,String str,String charset){
        super(path);
        if(regularExpression) this.regularExpression =regularExpression;
        if(this.regularExpression){
            pat = Pattern.compile(str);
        }
        else{
            this.str = str;
        }
        setCharset(charset);
    }
    public int getIndex(){
        return index;
    }
    public String getStr(){
        if(regularExpression){
            return pat.toString();
        }
        else{
            return str;
        }
    }

//以charset格式从文本的head开始查找str
    public int indexOf(String str,int start) throws FileNotFoundException, IOException{
        regularExpression = false;
        index = -1;
        this.str = str;
        String temp;
        while(!(temp = randomGC(start,start+bigLen)).equals("")){
            if(temp.contains(str)){
                index = temp.indexOf(str)+start;
                return index;
            }
            start+=bigLen+1-str.length();
        }
        return index;
    }
    public int indexOf(String str,int start,String charset) throws FileNotFoundException, IOException{
    	setCharset(charset);
    	return indexOf(str,start);
    }
//以charset格式从文本的head开始查找str(支持正则表达式)
    public int indexOf(String str,int start,boolean regularExpression) throws FileNotFoundException, IOException{
    	this.regularExpression = regularExpression;
        if(!this.regularExpression){
            return indexOf(str,start);
        }
        else{
            index = -1;
            pat = Pattern.compile(str);
            String temp;
            while(!(temp = randomGC(start,start+bigLen)).equals("")){
                Matcher mat = pat.matcher(temp);
                if(mat.find()){
                    result = mat.group();
                    index = mat.start()+start;
                    return index;
                }
                start+=bigLen/2;
            }
            return index;
        }
    }
    public int indexOf(String str,int start,boolean regularExpression,String charset) throws FileNotFoundException, IOException{
        setCharset(charset);
        return indexOf(str,start,regularExpression);
    }
//寻找下一个匹配(支持正则表达式)
    public int nextIndex() throws IOException{
    	int start = index +1;
        index = -1;int len = 0;
        if(regularExpression){
            len = bigLen/2;
        }
        else{
            len=this.str.length();
        }
        String temp;
        while(!(temp = randomGC(start,start+bigLen)).equals("")){
            if(regularExpression){
                Matcher mat = pat.matcher(temp);
                if(mat.find()){
                    result = mat.group();
                    index = mat.start()+start;
                    return index;
                }
            }
            else{
                if(temp.contains(this.str)){
                    index = temp.indexOf(this.str)+start;
                    return index;
                }
            }
            start+=bigLen-len;
        }
        return index;
    }
    public int nextIndex(String str) throws IOException{
    	int start = index +1;
        index = -1;int len = 0;
        if(regularExpression){
            pat = Pattern.compile(str);
            len = bigLen/2;
        }
        else{
            this.str=str;
            len=this.str.length();
        }
        String temp;
        while(!(temp = randomGC(start,start+bigLen)).equals("")){
            if(regularExpression){
                Matcher mat = pat.matcher(temp);
                if(mat.find()){
                    result = mat.group();
                    index = mat.start()+start;
                    return index;
                }
            }
            else{
                if(temp.contains(this.str)){
                    index = temp.indexOf(this.str)+start;
                    return index;
                }
            }
            start+=bigLen-len;
        }
        return index;
    }
//返回匹配到文本(仅支持正则表达式)
    public String group() throws IOException{
    	return group(0);
    }
    public String group(int para) throws IOException{
        try{
            if(result!=null){
                Matcher mat = pat.matcher(result);
                mat.find();
                return mat.group(para);
            }
        }
        catch(IndexOutOfBoundsException ex){
            throw new IndexOutOfBoundsException("捕获组 "+para+" 不存在！");
        }
        throw new IOException("indexOf没有选择正则表达式模式\nor没有匹配到结果！");
    }
}
