package com.gcloud.common.util;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * @Date 2015-4-3
 * 
 * @Author zhangzj@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * @Description 系统工具类
 */
public class SystemUtil {
	private static Logger LOG = LogManager.getLogger(SystemUtil.class);

	public static boolean isDevMode() {
		return getDevMode();
	}

	/**
	 * 获取当前是否配置为开发模式
	 * 
	 * @return Boolean
	 */
	public static Boolean getDevMode() {
		try {
			// @SuppressWarnings("deprecation")
			// String string = DefaultSettings.get("struts.devMode");
			// String string =
			// DefaultSettings.get(StrutsConstants.STRUTS_DEVMODE);
			String string = "true";
			if (string == null || string.equals(""))
				return false;
			return Boolean.parseBoolean(string);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 获取当前访问用户的IP地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP
		// 如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，那么真正的用户端的真实IP则是取X-Forwarded-For中第一个非unknown的有效IP字符串。
		if (ip != null && ip.length() > 15) {
			if (ip.indexOf(",") > 0) {
				ip = ip.substring(0, ip.indexOf(","));
			}
		}
		return ip;
	}

	public static String getExceptionMsg(Exception e) {
		String errorMsg = "";
		Throwable t = getRootCause(e);
		StackTraceElement[] trace = t.getStackTrace();
		StackTraceElement ste = trace[0];
		errorMsg += "error occurred in method: " + ste.getMethodName()
				+ "      file: " + ste.getFileName() + "      line number: "
				+ ste.getLineNumber();
		return errorMsg;
	}

	public static Throwable getRootCause(Exception e) {
		Throwable root = e;
		while (root.getCause() != null) {
			root = root.getCause();
		}

		return root;
	}

	/**
	 * 执行一条命令
	 * 
	 * @param command
	 *            命令
	 * @return 命令输出结果
	 */
	public static String run(String[] command) {
		Process proc = null;
		try {
			String commandString = "";
			for (String part : command) {
				commandString += part + " ";
			}
			LOG.debug("Running command: " + commandString);
			// System.out.println(commandString);
			Runtime rt = Runtime.getRuntime();
			proc = rt.exec(command);
			StreamConsumer error = new StreamConsumer(proc.getErrorStream());
			StreamConsumer output = new StreamConsumer(proc.getInputStream());
			error.start();
			output.start();
			int returnValue = proc.waitFor();
			output.join();
			if (returnValue != 0) {
				// System.out.println("error");
				throw new Exception(error.getReturnValue());
			}
			return output.getReturnValue();
		} catch (Exception t) {
			LOG.error(t, t);
			// return "failed:"+t.getMessage();
		} finally {
			if (proc != null)
				proc.destroy();
		}
		return "";
	}

	/**
	 * /** 执行一条命令
	 * 
	 * @param command
	 *            命令
	 * @param timeout
	 *            毫秒
	 * @return 命令输出结果
	 * @throws GCloudException
	 */
	public static String run(String[] command, long timeout) throws Exception {

		String errorStr = null;
		Process proc = null;
		TimeWorker timeWorker = null;
		try {
			String commandString = "";
			for (String part : command) {
				commandString += part + " ";
			}
			LOG.debug("Running command: " + commandString);
			// System.out.println(commandString);
			Runtime rt = Runtime.getRuntime();
			proc = rt.exec(command);

			StreamConsumer error = new StreamConsumer(proc.getErrorStream());
			StreamConsumer output = new StreamConsumer(proc.getInputStream());
			error.start();
			output.start();
			timeWorker = new TimeWorker(proc);
			timeWorker.start();
			timeWorker.join(timeout);
			if (timeWorker.exit == null) {
				errorStr = String.format("exec %s  timeout", commandString);
				LOG.error(errorStr);
				throw new Exception("M000_RP0000_0001");
			} else if (timeWorker.exit != 0) {
				errorStr = String.format("exec %s:%s", commandString,
						error.getReturnValue());
				LOG.error(errorStr);
				throw new Exception("M000_RP0000_0002");
				// throw new Exception(error.getReturnValue());
			} else {
				output.join();
				return output.getReturnValue();
			}
		} catch (Exception ex) {
			if (timeWorker != null) {
				timeWorker.interrupt();
				Thread.currentThread().interrupt();
			}
			LOG.error(ex.getMessage());
			throw new Exception("M000_RP0000_0003");
		} finally {
			if (proc != null)
				proc.destroy();
		}

	}

	/**
	 * 执行两条命令，采用管道形式连接
	 * 
	 * @param command1
	 *            命令1
	 * @param command2
	 *            命令2
	 * @return
	 */
	public static String run(String[] command1, String[] command2) {
		Process proc1 = null;
		Process proc2 = null;
		java.io.InputStream in = null;
		java.io.BufferedReader r = null;
		try {
			String commandString1 = "";
			String commandString2 = "";
			for (String part : command1) {
				commandString1 += part + " ";
			}
			for (String part : command2) {
				commandString2 += part + " ";
			}
			// System.out.println("Running command1: " + commandString1);
			// System.out.println("Running command2: " + commandString2);
			LOG.debug(String.format("Runnint command1:%s", commandString1));
			LOG.debug(String.format("Runnint command2:%s", commandString2));
			Runtime rt = Runtime.getRuntime();
			proc1 = rt.exec(command1);
			proc2 = rt.exec(command2);
			in = Piper.pipe(proc1, proc2);
			r = new java.io.BufferedReader(new java.io.InputStreamReader(in));
			StringBuffer sb = new StringBuffer();
			String s = "";
			while ((s = r.readLine()) != null) {
				sb.append(s).append("\r\n");
			}
			String ret = sb.toString();
			// System.out.println("Runturned:"+ret);
			LOG.debug(String.format("Run Result:%s", ret));

			return ret;
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			if (proc1 != null)
				proc1.destroy();
			if (proc2 != null)
				proc2.destroy();
			if (in != null) {
				try {
					in.close();
				} catch (IOException ex) {

				}
			}
			if (r != null) {
				try {
					r.close();
				} catch (IOException ex) {

				}
			}
		}
		System.out.println("Runturned:null");
		return "";
	}

	/**
	 * 执行一条命令并返回执行结果
	 * 
	 * @param command
	 *            命令
	 * @return 0成功，-1失败
	 */
	public static int runAndGetCode(String[] command) {
		Process proc = null;
		try {
			String commandString = "";
			for (String part : command) {
				commandString += part + " ";
			}
			LOG.debug("Running command: " + commandString);
			Runtime rt = Runtime.getRuntime();
			proc = rt.exec(command);
			StreamConsumer error = new StreamConsumer(proc.getErrorStream());
			StreamConsumer output = new StreamConsumer(proc.getInputStream());
			error.start();
			output.start();
			int returnValue = proc.waitFor();
			if (returnValue != 0) {
				error.join();
				LOG.error(command.toString() + " error:"
						+ error.getReturnValue());
				output.join();
				LOG.error(command.toString() + " error-output:"
						+ output.getReturnValue());
			}
			return returnValue;
		} catch (Exception t) {
			LOG.error(t, t);
		} finally {
			if (proc != null)
				proc.destroy();
		}
		return -1;
	}

	/**
	 * 已废弃
	 * 
	 * @param errorMessage
	 */
	public static void shutdownWithError(String errorMessage) {
		LOG.fatal(errorMessage);
		throw new IllegalStateException(errorMessage);
	}

	/**
	 * 已废弃
	 * 
	 * @param filePath
	 * @throws Exception
	 */
	public static void setEucaReadWriteOnly(String filePath) throws Exception {
		File file = new File(filePath);
		try {
			file.setReadable(false, false);
			file.setWritable(false, false);
			file.setExecutable(false, false);
			file.setReadable(true, true);
			file.setWritable(true, true);
			file.setExecutable(true, true);
		} catch (SecurityException ex) {
			LOG.error(ex);
			throw ex;
		}

	}

	/**
	 * 执行一条命令，与run方法不同之处在于错误时返回错误信息
	 * 
	 * @param command
	 *            命令
	 * @return 执行返回值
	 */
	public static String runAndGetValue(String[] command) {
		Process proc = null;
		try {
			String commandString = "";
			for (String part : command) {
				commandString += part + " ";
			}
			LOG.debug("Running command: " + commandString);
			Runtime rt = Runtime.getRuntime();
			proc = rt.exec(command);
			StreamConsumer error = new StreamConsumer(proc.getErrorStream());
			StreamConsumer output = new StreamConsumer(proc.getInputStream());
			error.start();
			output.start();
			int returnValue = proc.waitFor();
			output.join();
			if (returnValue != 0) {
				return error.getReturnValue();
			}
			return output.getReturnValue();
		} catch (Exception t) {
			LOG.error(t, t);
		} finally {
			if (proc != null)
				proc.destroy();
		}
		return "";
	}

	private static class TimeWorker extends Thread {
		private final Process process;
		private Integer exit = null;

		private TimeWorker(Process process) {
			this.process = process;
		}

		public void run() {
			try {
				exit = process.waitFor();
			} catch (Exception e) {
				LOG.error("TimeWorker:" + e.getMessage());
				return;
			}
		}
	}

	/**
	  * @Title: runAndGetCodeAndValue
	  * @Description: 执行一条命令，返回returncode和输出.  result[0] 为code, int 转成string, result[1]命令为输出
	  * @date 2016年11月22日 上午10:05:08
	  *
	  * @param command
	  * @return    
	  */
	public static String[] runAndGetCodeAndValue(String[] command) {
		Process proc = null;
		String[] result = new String[2];
		result[0] = "-1";
		result[1] = "";
		String commandString = "";
		try {
			
			for (String part : command) {
				commandString += part + " ";
			}
			LOG.debug("Running command: " + commandString);
			// System.out.println(commandString);
			Runtime rt = Runtime.getRuntime();
			proc = rt.exec(command);
			StreamConsumer error = new StreamConsumer(proc.getErrorStream());
			StreamConsumer output = new StreamConsumer(proc.getInputStream());
			error.start();
			output.start();
			int returnValue = proc.waitFor();
			output.join();
			result[0] = String.valueOf(returnValue);
			if (returnValue != 0) {
				// System.out.println("error");
				throw new Exception(error.getReturnValue());
			}
			result[1] = output.getReturnValue();
		} catch (Exception t) {
			LOG.error(t, t);
			// return "failed:"+t.getMessage();
		} finally {
			if (proc != null)
				proc.destroy();
		}
		return result;
	}
}
