package com.gcloud.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.gcloud.core.exception.GCloudException;

public class SerializeUtils {

	/**
	 * 对象序列化为字符串
	 */
	public static String serialize(Object obj) throws GCloudException {

		ByteArrayOutputStream byteArrayOutputStream = null;
		ObjectOutputStream objectOutputStream = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(obj);
			/*String serStr = byteArrayOutputStream.toString("ISO-8859-1");
			serStr = java.net.URLEncoder.encode(serStr, "UTF-8");*/// 编码后字符串不是乱码（不编也不影响功能）
			
			return byteArrayOutputStream.toString("ISO-8859-1");//转化成单字节编码
		} catch (Exception e) {
			//LOG.error("serialize error", e);
			throw new GCloudException("serialize error");
		} finally {
			if (objectOutputStream != null) {
				try {
					objectOutputStream.close();
				} catch (Exception ex) {
					//LOG.error("objectOutputStream close error", ex);
				}
			}
			if (byteArrayOutputStream != null) {
				try {
					byteArrayOutputStream.close();
				} catch (Exception ex) {
					//LOG.error("byteArrayOutputStream close error", ex);
				}
			}

		}
	}

	/**
	 * 字符串 反序列化为 对象
	 */
	public static Object unSerialize(String serStr) throws GCloudException {

		ByteArrayInputStream byteArrayInputStream = null;
		ObjectInputStream objectInputStream = null;

		try {
			/*String redStr = java.net.URLDecoder.decode(serStr, "UTF-8");*/
			byteArrayInputStream = new ByteArrayInputStream(serStr.getBytes("ISO-8859-1"));
			objectInputStream = new ObjectInputStream(byteArrayInputStream);
			Object obj = objectInputStream.readObject();

			return obj;
		} catch (Exception e) {
			//LOG.error("unSerialize error", e);
			throw new GCloudException("unSerialize error");
		} finally {

			if (objectInputStream != null) {
				try {
					objectInputStream.close();
				} catch (Exception ex) {
					//LOG.error("objectOutputStream close error", ex);
				}
			}
			if (byteArrayInputStream != null) {
				try {
					byteArrayInputStream.close();
				} catch (Exception ex) {
					//LOG.error("byteArrayOutputStream close error", ex);
				}
			}
		}
	}

	public static byte[] serializeToByte(Object obj) throws GCloudException {

		ByteArrayOutputStream byteArrayOutputStream = null;
		ObjectOutputStream objectOutputStream = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(obj);
			byte[] bytes = byteArrayOutputStream.toByteArray();

			return bytes;

		} catch (Exception e) {
			//LOG.error("serialize error", e);
			throw new GCloudException("serialize error");
		} finally {
			if (objectOutputStream != null) {
				try {
					objectOutputStream.close();
				} catch (Exception ex) {
					//LOG.error("objectOutputStream close error", ex);
				}
			}
			if (byteArrayOutputStream != null) {
				try {
					byteArrayOutputStream.close();
				} catch (Exception ex) {
					//LOG.error("byteArrayOutputStream close error", ex);
				}
			}

		}

	}

	public static Object unSerializeFromByte(byte[] bytes) throws GCloudException {

		ByteArrayInputStream byteArrayInputStream = null;
		ObjectInputStream objectInputStream = null;

		try {

			byteArrayInputStream = new ByteArrayInputStream(bytes);
			objectInputStream = new ObjectInputStream(byteArrayInputStream);
			return objectInputStream.readObject();
			
		} catch (Exception e) {
			//LOG.error("unSerialize error", e);
			throw new GCloudException("unSerialize error");
		} finally {

			if (objectInputStream != null) {
				try {
					objectInputStream.close();
				} catch (Exception ex) {
					//LOG.error("objectOutputStream close error", ex);
				}
			}
			if (byteArrayInputStream != null) {
				try {
					byteArrayInputStream.close();
				} catch (Exception ex) {
					//LOG.error("byteArrayOutputStream close error", ex);
				}
			}
		}

	}
}
