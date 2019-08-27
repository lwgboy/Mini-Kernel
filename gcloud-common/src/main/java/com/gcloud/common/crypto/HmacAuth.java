package com.gcloud.common.crypto;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.codec.binary.Base64;

import com.gcloud.common.util.StringUtils;
import com.google.common.base.Strings;

public class HmacAuth 
{

   /** 
	* @Title makeSubjectString 
	* @param @param parameters
	* @param @return
	* @param @throws UnsupportedEncodingException 
	* @return String
	* @throws 
	* 拼接成的参数为："ActionBundleInstanceAWSAccessKeyIdHAOFWU2UVCSRPNCBDDH9QBucketbucketTestInstanceIdi-100541C7Prefixlinux-testSignatureVersion1Timestamp2013-05-16T03:11:00ZVersion2010-06-15"
	*/
	public static String makeSubjectString( final Map<String, Object> parameters, List<String> excludeKey) throws UnsupportedEncodingException 
	{
	    String paramString = "";
	    Set<String> sortedKeys = new TreeSet<String>( String.CASE_INSENSITIVE_ORDER );
	    sortedKeys.addAll( parameters.keySet( ) );
	    for ( String key : sortedKeys )
	    {
	    	if(excludeKey==null || excludeKey.size()==0 || !excludeKey.contains(key))
	    	{
	    		paramString = paramString.concat( key ).concat( Strings.nullToEmpty( StringUtils.ToString(parameters.get(key)) ).replaceAll("\\+", " ") );
	    	}
	    }
	    try 
	    {
	        return new String(URLCodec.decodeUrl( paramString.getBytes() ) );
	    } 
	    catch ( DecoderException e ) 
	    {
	        return paramString;
	    }
	  }

	  public static String getSignature( final String queryKey, final String subject, final Hmac mac ) 
	  {
		  SecretKeySpec signingKey = new SecretKeySpec( queryKey.getBytes( ), mac.toString( ) );
		  try 
		  {
		      Mac digest = mac.getInstance();
		      digest.init( signingKey );
		      byte[] rawHmac = digest.doFinal( subject.getBytes() );
		      
		      return sanitize( Base64.encodeBase64String(rawHmac) );
		  } 
		  catch ( Exception e ) 
		  {
		      //throw new GCloudException( "Failed to compute signature" );
		  }
		  return "";
	  }
	  
	  @SuppressWarnings("deprecation")
	  protected static String urldecode( final String text ) 
	  {
		  return URLDecoder.decode( text );
	  }

	  protected static String sanitize( final String b64text ) 
	  {
	      return b64text.replace( "=", "" );
	  }
}