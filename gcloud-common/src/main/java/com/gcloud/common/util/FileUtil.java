package com.gcloud.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

public class FileUtil {
	//转移文件
	public static void moveFile(String path,File file,int maxSize) throws Exception{
		
			File desDir=new File(FilenameUtils.getPath(path));
			if(!desDir.exists())
				desDir.mkdir();
	        FileOutputStream fos = new FileOutputStream(path);
	        FileInputStream fis = new FileInputStream(file);
	    try{
	        byte[] buffer = new byte[1024];
	        int len = 0;
	        while((len = fis.read(buffer)) > 0){
	            fos.write(buffer, 0, len);
	        }
		}catch(Exception e){
			throw e;
		}finally{
			fos.close();
			fis.close();
		}
	}
	
	public static boolean hasType(String [] types,String type){
		List<String> list=Arrays.asList(types);
		return list.contains(type.toLowerCase());
	}
	
	public static byte[] getBytes(File file)throws Exception{  
        byte[] buffer = null;  
        try {  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (IOException e) {  
            throw e;
        }  
        return buffer;  
    }  
	
	public static List<File> listFiles(File dir,List<String> extensions){
		List<File> files=new ArrayList<File>();
		for(File file:dir.listFiles()){
			if(file.isDirectory()){
				files.addAll(listFiles(file,extensions));
			}else{
				if(extensions.contains(FilenameUtils.getExtension(file.getName()).toLowerCase()))
					files.add(file);
			}
		}
		return files;
	}
	public static List<File> listFiles(File dir){
		List<File> files=new ArrayList<File>();
		for(File file:dir.listFiles()){
			if(file.isDirectory()){
				files.addAll(listFiles(file));
			}else{
				files.add(file);
			}
		}
		return files;
	}
	
	/**
	  * @Title: getDirectoryString
	  * @Description: 返回目录形式的文件路径。即如果最后没有 "/" 则加上，有则不管。
	  * @date 2015-5-21 上午9:22:03
	  *
	  * @param path
	  * @return    
	  */
	public static String getDirectoryString(String path){
		path = path.endsWith(File.separator) ? path : path + File.separator;
		return path;
	}
	
	/**
     * 将文本文件中的内容读入到buffer中
     * @param buffer buffer
     * @param filePath 文件路径
     * @throws IOException 异常
     * @author cn.outofmemory
     * @date 2013-1-7
     */
    public static void readToBuffer(StringBuffer buffer, String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
    }

    /**
     * 读取文本文件内容
     * @param filePath 文件所在路径
     * @return 文本内容
     * @throws IOException 异常
     * @author cn.outofmemory
     * @date 2013-1-7
     */
    public static String readFile(String filePath) throws IOException {
        StringBuffer sb = new StringBuffer();
        FileUtil.readToBuffer(sb, filePath);
        return sb.toString();
    }
    
    public static void writeFile(String path, String content)throws Exception{
        FileOutputStream fop = null;
        File file;

        try {

             file = new File(path);
             fop = new FileOutputStream(file);
    
             // if file doesnt exists, then create it
             if (!file.exists()) {
                 file.createNewFile();
             }
    
             // get the content in bytes
             byte[] contentInBytes = content.getBytes();
    
             fop.write(contentInBytes);
             fop.flush();
             fop.close();
        } 
        catch(IOException e) {
        } 
        finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } 
            catch (IOException e) {
            }
        }
    }

    public static String pathString(String... names){

        String result = "";
        if(names != null && names.length > 0){

            String first = names[0];
            if(!first.endsWith("/") && first.length() > 0){
                first += "/";
            }
            result += first;
            for(int i = 1; i < names.length; i++){
                String name = names[i];
                if("".equals(name)){
                    continue;
                }

                if(name.startsWith("/")){
                    name = name.substring(1);
                }

                if(!name.endsWith("/") && name.length() > 0){
                    name += "/";
                }

                result += name;
            }
        }

        return result;

    }

    public static String fileString(String... names){
        String result = pathString(names);
        if(result != null && !"".equals(result)){
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
    
    public static long getFileSize(String path) throws Exception{
    	String[] cmd = null;
        cmd = new String[]{"ls", path, "-l"};
        String res = SystemUtil.run(cmd);
        String[] arrs = res.split(" ");
        long size = Long.parseLong(arrs[4]);
        return size;
    }
}
