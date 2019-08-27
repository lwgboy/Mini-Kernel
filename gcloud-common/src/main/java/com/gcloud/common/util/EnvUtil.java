package com.gcloud.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnvUtil
{
    private static Logger LOG = LogManager.getLogger(EnvUtil.class);
    
    /** 
    * @Title getMac 
    * @Description  获取MAC
    * @param @return 
    * @return String 
    * @throws 
    */
    public static String getMac()
    {
        String os = System.getProperty("os.name");
        if (os != null && os.startsWith("Windows"))
        {
            return getWindowsMACAddress();
        }
        else
        {
            List<String> address = getLinuxMACAddressList();
            return address.size()>0?address.get(0):"";
        }
    }
    
    /** 
     * @Title getDiskSerialNo 
     * @Description  获取硬盘序列号，用于激活
     * @param @return 
     * @return String 
     * @throws 
     */
     public static String getDiskSerialNo()
     {
         List<String> diskNames = getDiskNames();
         String sn = "";
         String diskName;
         if(diskNames.size() > 0)
         {
             diskName = getDiskNames().get(0);
             sn = getDiskSerialNo(diskName);
             if(StringUtils.isBlank(sn))
             {
            	 sn = getSasDiskSerialNo(diskName);
             }
         }
         
         return sn;
     }
     
     /** 
    * @Title isSerialNoMatch 
    * @Description  检测硬盘序列号是否匹配
    * @param @param serial
    * @param @return 
    * @return boolean 
    * @throws 
    */
    public static boolean isSerialNoMatch(String serial)
     {
         boolean match = false;
         List<String> diskNames = getDiskNames();
         for(String diskName:diskNames)
         {
             if(getDiskSerialNo(diskName).equals(serial))
             {
                 match = true;
                 break;
             }
             else if(getSasDiskSerialNo(diskName).equals(serial))
             {
            	 match = true;
                 break;
             }
         }
         return match;
     }
     
    /**
     * @Title isMacMatch
     * @Description  判断证书中的mac地址是否存在于服务器上
     * @param  mac mac地址
     * @param @return
     * @return boolean
     * @throws
     */
    public static boolean isMacMatch(String mac)
    {
        boolean result = false;
        String os = System.getProperty("os.name");
        if (os != null && os.startsWith("Windows"))
        {
            if (formatMac(getWindowsMACAddress()).equals(formatMac(mac)))
            {
                result = true;
            }
        }
        else
        {
            List<String> address = getLinuxMACAddressList();
            for (String addr : address)
            {
                LOG.debug("该服务器的物理地址为" + addr);
                if (formatMac(addr).equals(formatMac(mac)))
                {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }
    
    /**
     * @Title getWindowsMACAddress
     * @Description 获取windows物理地址
     * @param @return
     * @return String
     * @throws
     */
    public static String getWindowsMACAddress()
    {
        String address = "";
        try
        {
            String command = "cmd.exe /c ipconfig /all";
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    p.getInputStream(), System.getProperty("sun.jnu.encoding")));
            String line;
            while ((line = br.readLine()) != null)
            {
                if (line.indexOf("Physical Address") > 0
                        || line.indexOf("物理地址") > 0 )
                {
                    int index = line.indexOf(":");
                    index += 2;
                    address = line.substring(index);
                    System.out.println(address);
                    if(!address.trim().equals("")){
                        break;
                    }
                }
            }
            br.close();
            return address.trim();
        }
        catch (IOException e)
        {

        }
        return address;
    }
    
    /**
     * @Title getLinuxMACAddressList
     * @Description  服务器的mac列表
     * @param @return
     * @return List<String>
     * @throws
     */
    public static List<String> getLinuxMACAddressList()
    {
        List<String> address = new ArrayList<String>();
        try
        {
            // 获取系统编码
            ProcessBuilder pb = new ProcessBuilder("ifconfig", "-a");
            Process p = pb.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null)
            {
                System.out.println(line);
                if (line.indexOf("Link encap:Ethernet") != -1)//centos 6
                {
                    int index = line.indexOf("HWaddr");
                    address.add(line.substring(index + 7).trim());
                }
                else if(line.indexOf("Ethernet") != -1)//centos 7
                {
                	int index = line.indexOf("ether");
                	address.add(line.substring(index + 6, index + 6 + 17).trim());
                }
            }
            br.close();
        }
        catch (IOException e)
        {
        }
        return address;
    }
    
    /**
     * @Title formatMac
     * @Description 格式化mac
     * @param address mac地址
     * @return String 00-1B-77-2C-9D-8F
     * @throws
     */
    public static String formatMac(String address)
    {
        String mac = "";
        if (address.contains(":"))
        {
            mac = address.replaceAll(":", "-");
        }
        else
        {
            mac = address;
        }
        return mac.toUpperCase();
    }
    
    // 取得要获取的磁盘块设备
    private static List<String> getDiskNames()
    {
        List<String> names = new ArrayList<String>();
        String[] cmd = { "fdisk", "-l" };
        BufferedReader bufferedReader = initCommand(cmd);

        String line;
        if (null == bufferedReader)
        {
            return names;
        }
        try
        {
            while ((line = bufferedReader.readLine()) != null)
            {
                if (line.contains("/dev/s") || line.contains("/dev/h")
                        || line.contains("/dev/v"))
                {
                    String strs[] = line.split(" ");
                    try
                    {
                        if (line.startsWith("Disk"))
                        {
                            String str = strs[1];
                            String name = str.substring(0, str.length() - 1);
                            names.add(name);
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        LOG.debug(e.getMessage());
                    }
                }
            }
        }
        catch (IOException e)
        {
            LOG.debug(e.getMessage());
        }
        finally
        {
            close(bufferedReader);
        }
        return names;
    }
 
    /**
     * 取得名为diskName的设备的设备号
     * @param diskName 设备名称
     * @return 该设备的设备号
     */
    public static String getDiskSerialNo(String diskName)
    {
        String[] cmd = { "hdparm", "-i", diskName };
        BufferedReader bufferedReader = initCommand(cmd);

        String line;
        String code = "";
        if (null == bufferedReader)
        {
            return code;
        }
        try
        {
            while ((line = bufferedReader.readLine()) != null)
            {
                if (line.contains("SerialNo"))
                {
                    String strs[] = line.split(" ");
                    code = strs[strs.length - 1];
                    if (code.contains("SerialNo"))
                    {
                        String[] strs1 = code.split("\\=");
                        code = strs1[strs1.length - 1];
                    }
                    break;
                }
            }
        }
        catch (IOException e)
        {
            LOG.debug(e.getMessage());
        }
        finally
        {
            close(bufferedReader);
        }
        return code;
    }
    
    public static String getSasDiskSerialNo(String diskName)
    {
        String[] cmd = { "smartctl", "-i", diskName };
        BufferedReader bufferedReader = initCommand(cmd);

        String line;
        String code = "";
        if (null == bufferedReader)
        {
            return code;
        }
        try
        {
            while ((line = bufferedReader.readLine()) != null)
            {
                if (line.contains("Serial number"))
                {
                	code = line.substring(14).trim();
                    break;
                }
            }
        }
        catch (IOException e)
        {
            LOG.debug(e.getMessage());
        }
        finally
        {
            close(bufferedReader);
        }
        return code;
    }
    
    //对运行命令进行初始化
    private static BufferedReader initCommand(String[] cmd)
    {
        ProcessBuilder pb = new ProcessBuilder(cmd);
        BufferedReader bufferedReader = null;
        try
        {
            Process p = pb.start();
            bufferedReader = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
        }
        catch (IOException e)
        {
            LOG.debug(e.getMessage());
        }

        return bufferedReader;
    }
    
    

    //关闭流
    private static void close(BufferedReader bufferedReader)
    {
        try
        {
            if (null != bufferedReader)
            {
                bufferedReader.close();
            }
        }
        catch (IOException e)
        {
            LOG.debug(e.getMessage());
        }
    }
    
    public static String getBaseboardSn()
    {
    	String sn = "";
    	try
        {
            ProcessBuilder pb = new ProcessBuilder("dmidecode", "-s", "baseboard-serial-number");
            Process p = pb.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null)
            {
                sn = line;
            }
            br.close();
        }
        catch (IOException e)
        {
        }
    	 return sn;
    }
    
    public static Long getHoursOnPower(String diskName)
    {
    	String[] cmd = { "smartctl", "-a", diskName };
        BufferedReader bufferedReader = initCommand(cmd);

        String line;
        Long time = null;
        if (null == bufferedReader)
        {
            return time;
        }
        try
        {
            while ((line = bufferedReader.readLine()) != null)
            {
                if (line.indexOf("powered") != -1)
                {
                	//正则获取数字
                	String reg = "\\D+(\\d+)\\D+";
            		Pattern p2 = Pattern.compile(reg);  
            		Matcher m2 = p2.matcher(line);  
            		if(m2.find()){  
            			time = Long.parseLong(m2.group(1));
            			
            			//不同硬盘厂商单位会有所不同，有些是天
                		if(line.indexOf("day") != -1)
                		{
                			time = time * 24;
                		}
            		}
            		
                    break;
                }
            }
        }
        catch (IOException e)
        {
            LOG.debug(e.getMessage());
        }
        finally
        {
            close(bufferedReader);
        }
        return time;
    }
    
    public static List<Map<String, Object>> getDiskInfos()
    {
    	List<Map<String, Object>> infos = new ArrayList<Map<String, Object>>();
        List<String> diskNames = getDiskNames();
        for(int i=0;i<diskNames.size();i++)
        {
        	String sn = "";
        	Long hoursOnPower = null;
            String diskName;
            diskName = getDiskNames().get(i);
            sn = getDiskSerialNo(diskName);
            if(StringUtils.isBlank(sn))
            {
           	 	sn = getSasDiskSerialNo(diskName);
            }
            if(StringUtils.isNotBlank(sn))
            {
            	hoursOnPower = getHoursOnPower(diskName);
            	Map<String, Object> info = new HashMap<String, Object>();
            	info.put("diskSn", sn);
            	info.put("hoursOnPower", hoursOnPower);
            	
            	infos.add(info);
            }
        }
        
        return infos;
    }

}
