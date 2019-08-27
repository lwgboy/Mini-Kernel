package com.gcloud.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
 /**
 * @Date 2013-5-16
 *
 * @Author dengxm@g-cloud.com.cn
 *
 * @Copyright 2013 www.g-cloud.com.cn Inc. All rights reserved.
 *
 * @Description 
 */

public class StreamConsumer extends Thread {
    private InputStream is;
    private File file;
    private String returnValue;

    public StreamConsumer(InputStream is) {
        this.is = is;
        returnValue = "";
    }

    public StreamConsumer(InputStream is, File file) {
        this(is);
        this.file = file;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void run() {
        BufferedOutputStream outStream = null;
        FileOutputStream fileOutputStream = null;
        BufferedInputStream inStream = null;
        try {
            inStream = new BufferedInputStream(is);
            if (file != null) {
                fileOutputStream = new FileOutputStream(file);
				outStream = new BufferedOutputStream(fileOutputStream);
            }
            byte[] bytes = new byte[102400];
            int bytesRead;
            while ((bytesRead = inStream.read(bytes)) > 0) {
                returnValue += new String(bytes, 0, bytesRead);
                if (outStream != null) {
                    outStream.write(bytes, 0, bytesRead);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (outStream != null) 
            {
                try 
                {
					outStream.close();
				} 
                catch (IOException e) 
				{
					e.printStackTrace();
				}
            }
            if (fileOutputStream != null) 
            {
                try 
                {
                    fileOutputStream.close();
                } 
                catch (IOException e) 
                {
                    e.printStackTrace();
                }
            }
            if (is != null) 
            {
                try 
                {
                    is.close();
                } 
                catch (IOException e) 
                {
                    e.printStackTrace();
                }
            }
            if (inStream != null) 
            {
                try 
                {
                    inStream.close();
                } 
                catch (IOException e) 
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
