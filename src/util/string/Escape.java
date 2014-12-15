package util.string;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wan
 */
public class Escape {
    public static String escapeForRegular(String code,String seperator){//把以seperator为间隔符的code正则转义
        code = escapeForRegular(code);
        code = code.replaceAll("(?<=[\\s\\S])("+seperator+")+(?=[\\s\\S])","|");
        code = code.replaceAll(seperator,"");
        return code;
    }
    public static String escapeForRegular(String[]codes){//把String数组正则转义
        String temp = "";
        for(String code:codes){
            temp+=code+"seperator";
        }
        return escapeForRegular(temp,"seperator");
    }
    public static String escapeForRegular(String code){//
        code = code.replaceAll("\\\\", "\\\\\\\\");
        code = code.replaceAll("\\.", "\\\\.");
        code = code.replaceAll("\\+", "\\\\+");
        code = code.replaceAll("\\?", "\\\\?");
        code = code.replaceAll("\\*", "\\\\*");
        code = code.replaceAll("\\[", "\\\\[");
        code = code.replaceAll("\\]", "\\\\]");
        code = code.replaceAll("\\{", "\\\\{");
        code = code.replaceAll("\\}", "\\\\}");
        code = code.replaceAll("\\(", "\\\\(");
        code = code.replaceAll("\\)", "\\\\)");
        code = code.replaceAll("\\$", "\\\\\\$");
        code = code.replaceAll("\\^", "\\\\\\^");
        code = code.replaceAll("\\|", "\\\\|");
        return code;
    }
}
