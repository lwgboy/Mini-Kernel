package com.gcloud.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MyXmlSerializer<TYPE> {
	private JAXBContext context;
	 private Class<TYPE> request;
	 private Logger logger=LogManager.getLogger(MyXmlSerializer.class);
	 //private Class<RTYPE> responseClass;
	
	@SuppressWarnings("unchecked")
	public MyXmlSerializer() {
		request =(Class<TYPE>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		//responseClass=(Class<RTYPE>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}
	
	@SuppressWarnings("unchecked")
	public TYPE Xml2Object(String xml) throws Exception {
		String encodeName = System.getProperty("sun.jnu.encoding");
		ByteArrayInputStream bis=null;
		if(encodeName.equals("GBK"))
		{
			bis = new ByteArrayInputStream(xml.getBytes());
		}
		else
		{
			bis = new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8")));
		}
		//
		try {
			context=JAXBContext.newInstance(request);
			Unmarshaller u=context.createUnmarshaller();
			TYPE obj=(TYPE)u.unmarshal(bis);
			return obj;
		} catch (JAXBException e) {
			logger.error(e);
			throw new Exception(e);
		}
	}

	public String Object2Xml(TYPE response) throws Exception {
		try {
			context=JAXBContext.newInstance(request);
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			Marshaller u1=context.createMarshaller();
			u1.setProperty(Marshaller.JAXB_ENCODING, "GB2312");
			u1.marshal(response, bos);
			String xmlOut=new String(bos.toByteArray());
			int index=xmlOut.indexOf(">")+1;
			return xmlOut.substring(index);
		} catch (JAXBException e) {
			logger.error(e);
			throw new Exception(e);
		}

	}
}
