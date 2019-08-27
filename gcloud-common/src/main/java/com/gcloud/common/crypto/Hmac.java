package com.gcloud.common.crypto;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum Hmac {
	HmacSHA1,
	HmacSHA256;

    private static Logger LOG = LogManager.getLogger( Hmac.class );
    public Mac getInstance() {
	    try {
	      return Mac.getInstance( this.toString( ) );
	    } catch ( NoSuchAlgorithmException e ) {
        LOG.fatal( e, e );
        throw new RuntimeException( e );
    }
  }
}
