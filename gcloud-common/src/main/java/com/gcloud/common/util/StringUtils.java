package com.gcloud.common.util;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;

public class StringUtils extends org.apache.commons.lang.StringUtils {

    private static final Interner<String> INTERNER_POOL = Interners.newWeakInterner();

    public static String syncStringObject(String str) {
        return INTERNER_POOL.intern(str);
    }

    public static String genUuid() {
        synchronized (UUID.class) {
            return UUID.randomUUID().toString();
        }
    }

	// 对指定字符串进行Base64编码
	public static String encode(String str) throws UnsupportedEncodingException {
		return new String(Base64.encodeBase64(str.getBytes("UTF-8")));
	}

	// 对指定字符串进行Base64解码
	public static String decode(String str) throws UnsupportedEncodingException {
		return new String(Base64.decodeBase64(str.getBytes("UTF-8")));
	}

	// 获取指定字段的GETTER
	public static String makeGetMethod(String fieldName) {
		char c = fieldName.charAt(1);
		if(Character.isUpperCase(c)) {
			return "get" + fieldName;
		} else {
			return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		}
	}

	// 获取指定字段的SETTER
	public static String makeSetMethod(String fieldName) {
		char c = fieldName.charAt(1);
		if(Character.isUpperCase(c)) {
			return "set" + fieldName;
		} else {
			return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		}
	}

	/**
	 * 获取同步设置中两个时间之间的相差毫秒数
	 * 
	 * @param sychronizeSetting
	 * @return
	 * @author favey.shaw
	 * @date 2011-5-2
	 */
	public static Long getDiffMillisBetweenDates(String startTime, String stopTime) {
		// 获取任务开始时间
		Calendar startDate = getDate(startTime);

		// 获取任务停止时间
		Calendar stoptDate = getDate(stopTime);

		if (stoptDate.get(Calendar.HOUR_OF_DAY) < startDate.get(Calendar.HOUR_OF_DAY))
			;
		{
			stoptDate.add(Calendar.DATE, 1);
		}

		return stoptDate.getTimeInMillis() - startDate.getTimeInMillis();
	}

	/**
	 * 根据给定的“HH:MM:ss”格式字符串构建日期
	 * 
	 * @param timeStr
	 * @return
	 * @author favey.shaw
	 * @date 2011-5-2
	 */
	@SuppressWarnings("deprecation")
	public static Calendar getDate(String timeStr) {
		String[] timeArray = timeStr.split(":");
		Integer startHour = Integer.parseInt(timeArray[0]);
		Integer startMinute = Integer.parseInt(timeArray[1]);
		Integer startSecond = Integer.parseInt(timeArray[2]);
		Calendar date = Calendar.getInstance(Locale.CHINESE);
		date.set(Calendar.HOUR_OF_DAY, startHour);
		date.set(Calendar.MINUTE, startMinute);
		date.set(Calendar.SECOND, startSecond);
		System.out.println(date.getTime().toLocaleString());
		return date;
	}

	/**
	 * Validates that the specified bucket name is valid for Amazon S3 V2 naming
	 * (i.e. DNS addressable in virtual host style). Throws an
	 * IllegalArgumentException if the bucket name is not valid.
	 * <p>
	 * S3 bucket naming guidelines are specified in <a href=
	 * "http://docs.amazonwebservices.com/AmazonS3/latest/dev/index.html?BucketRestrictions.html"
	 * > http://docs.amazonwebservices.com/AmazonS3/latest/dev/index.html?
	 * BucketRestrictions.html</a>
	 * 
	 * @param bucketName
	 *            The bucket name to validate.
	 * 
	 * @throws IllegalArgumentException
	 *             If the specified bucket name doesn't follow Amazon S3's
	 *             guidelines.
	 */
	public static void validateBucketName(String bucketName) throws IllegalArgumentException {
		/*
		 * From the Amazon S3 bucket naming guidelines in the Amazon S3
		 * Developer Guide
		 * 
		 * To conform with DNS requirements: - Bucket names should not contain
		 * underscores (_) - Bucket names should be between 3 and 63 characters
		 * long - Bucket names should not end with a dash - Bucket names cannot
		 * contain two, adjacent periods - Bucket names cannot contain dashes
		 * next to periods - (e.g., "my-.bucket.com" and "my.-bucket" are
		 * invalid)
		 */
		if (bucketName.getBytes().length != bucketName.length()) {
			throw new IllegalArgumentException("目录名暂时不支持中文");
		}
		if (bucketName == null || "".equals(bucketName.trim()))
			throw new IllegalArgumentException("目录名不能为空");
		if (!bucketName.toLowerCase().equals(bucketName))
			throw new IllegalArgumentException("目录名不能包含大写字母");
		if (bucketName.contains("_"))
			throw new IllegalArgumentException("目录名不能包含 '_'");
		if (bucketName.length() < 3 || bucketName.length() > 63)
			throw new IllegalArgumentException("目录名长度为3-63个字符");
		if (bucketName.endsWith("-"))
			throw new IllegalArgumentException("目录名不能以'-'结尾");
		if (bucketName.contains(".."))
			throw new IllegalArgumentException("目录名不能包含'..'");
		if (bucketName.contains("-.") || bucketName.contains(".-"))
			throw new IllegalArgumentException(" 目录名不能包含'-.'或者'.-'");
	}

	/**
	 * <p>
	 * Checks if a CharSequence is whitespace, empty ("") or null.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.isBlank(null)      = true
	 * StringUtils.isBlank("")        = true
	 * StringUtils.isBlank(" ")       = true
	 * StringUtils.isBlank("bob")     = false
	 * StringUtils.isBlank("  bob  ") = false
	 * </pre>
	 *
	 * @param cs
	 *            the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is null, empty or whitespace
	 * @since 2.0
	 * @since 3.0 Changed signature from isBlank(String) to
	 *        isBlank(CharSequence)
	 */
	public static boolean isBlank(CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(cs.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	private static Random randGen = null;
	private static char[] numbersAndLetters = null;
	private static Object initLock = new Object();

	/**
	 * @Title: randomString @Description: 随机生成n位字串 @return String 返回类型 @throws
	 */
	public static final String randomString(int length) {
		if (length < 1) {
			return null;
		}
		if (randGen == null) {
			synchronized (initLock) {
				if (randGen == null) {
					randGen = new Random();
					numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
				}
			}
		}
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(61)];
		}
		return new String(randBuffer);
	}

	public static String formatDate(Date date, String f) {
		DateFormat format = new SimpleDateFormat(f);
		return format.format(date);
	}

	public static String formatDate(Date date) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}

	public static Date str2Date(String str, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			return new Date();
		}
	}

	public static String getRandomNumber(int length) { // length表示生成字符串的长度
		String base = "0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static String seq(int source, int lenght) {
		String s = String.valueOf(source);
		int sLenght = s.length();
		if (sLenght < lenght) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < lenght - sLenght; i++) {
				sb.append("0");
			}
			sb.append(s);
			return sb.toString();
		}
		return s;
	}

	/**
	 * 对象转换为字符串，需要在类中重写toString方法此类才有意义
	 * 
	 * @param obj
	 *            object类型对象
	 * @return 对象的字符串
	 */
	@SuppressWarnings("unchecked")
	public static String ToString(Object obj) {
		if (obj == null)
			return null;
		// System.out.println("obj: "+obj.toString()+" :
		// "+obj.getClass().getSimpleName());
		String str = "";
		String csn = obj.getClass().getSimpleName();
		if (csn.endsWith("[]")) {
			for (Object o : (Object[]) obj)
				str += (str.isEmpty() ? "" : ",") + o.toString();
		} else if (csn.equals("XWorkList")) {
			for (Object o : (List<Object>) obj)
				str += (str.isEmpty() ? "" : ",") + o.toString();
		} else
			str = obj.toString();
		return str;
	}

	public static Date toDate(String str) {
		if (str == null)
			return null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return date;
	}

	public static Date toDate(String str, String sformat) {
		if (str == null)
			return null;
		SimpleDateFormat format = new SimpleDateFormat(sformat);
		Date date = null;
		try {
			date = format.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return date;
	}

	public static boolean isBettweenDate(String start, String end, Date d) {
		Date d_startDate = toDate(start);
		Date d_endDate = toDate(end);
		if (d_startDate != null)
			if (d.before(d_startDate))
				return false;
		if (d_endDate != null)
			if (d.after(d_endDate))
				return false;
		return true;
	}

	public static String cover(String string, String sep) {
		String[] array = string.split(sep);

		int a1Length = array[0].length() / 2;

		return array[0].substring(0, a1Length) + array[0].substring(a1Length).replaceAll("\\w", "\\*") + string.substring(string.indexOf(sep));
	}

	// 首字母转小写
	public static String toLowerCaseFirstOne(String s) {
		if (Character.isLowerCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
	}

	// 首字母转大写
	public static String toUpperCaseFirstOne(String s) {
		if (Character.isUpperCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
	}
}
