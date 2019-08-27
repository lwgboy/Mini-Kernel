package com.gcloud.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by yaowj on 2018/11/27.
 */
public class GenerateUpdate {

    public static void main(String[] args) {

        //换成对应实体类
        Class clazz = GenerateUpdate.class;

        Field[] fields = clazz.getDeclaredFields();

        for(Field field : fields){
            if(isExclude(field)){
                continue;
            }

            System.out.println(String.format("public static final String %s = \"%s\";", underlineUpper(field.getName()), field.getName()));
        }

        for(Field field : fields){

            if(isExclude(field)){
                continue;
            }

            String fileName = field.getName();
            String upperName = StringUtils.toUpperCaseFirstOne(fileName);
            String typeName = field.getType().getSimpleName();
            System.out.println(String.format("public String update%s (%s %s){", upperName, typeName, fileName));
            System.out.println(String.format("this.set%s(%s);", upperName, fileName));
            System.out.println(String.format("return %s;", underlineUpper(field.getName())));
            System.out.println(String.format("}"));
        }


    }

    public static String underlineUpper(String str){

        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < str.length(); i++){

            char c = str.charAt(i);
            if(Character.isUpperCase(c)){
                sb.append("_");
            }
            sb.append(c);
        }

        return sb.toString().toUpperCase();

    }

    public static boolean isExclude(Field field){
        if(Modifier.isStatic(field.getModifiers())){
            return true;
        }

        return false;
    }

}
