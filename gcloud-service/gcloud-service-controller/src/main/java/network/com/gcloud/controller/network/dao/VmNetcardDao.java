package com.gcloud.controller.network.dao;

import org.springframework.stereotype.Repository;

import com.gcloud.controller.network.entity.VmNetcard;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
@Repository
public class VmNetcardDao  extends JdbcBaseDaoImpl<VmNetcard,String>{

}
