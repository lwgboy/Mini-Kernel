
package com.gcloud.controller.storage.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.ResourceProviders;
import com.gcloud.controller.compute.dao.ComputeNodeDao;
import com.gcloud.controller.compute.dao.ZoneDao;
import com.gcloud.controller.compute.entity.AvailableZoneEntity;
import com.gcloud.controller.compute.entity.ComputeNode;
import com.gcloud.controller.storage.dao.DiskCategoryDao;
import com.gcloud.controller.storage.dao.DiskCategoryPoolDao;
import com.gcloud.controller.storage.dao.DiskCategoryZoneDao;
import com.gcloud.controller.storage.dao.StoragePoolDao;
import com.gcloud.controller.storage.dao.StoragePoolZoneDao;
import com.gcloud.controller.storage.dao.VolumeDao;
import com.gcloud.controller.storage.entity.DiskCategory;
import com.gcloud.controller.storage.entity.DiskCategoryPool;
import com.gcloud.controller.storage.entity.DiskCategoryZone;
import com.gcloud.controller.storage.entity.StoragePool;
import com.gcloud.controller.storage.entity.StoragePoolZone;
import com.gcloud.controller.storage.entity.Volume;
import com.gcloud.controller.storage.model.AssociateDiskCategoryPoolParams;
import com.gcloud.controller.storage.model.AssociateDiskCategoryZoneParams;
import com.gcloud.controller.storage.model.CreateDiskCategoryParams;
import com.gcloud.controller.storage.model.DeleteDiskCategoryParams;
import com.gcloud.controller.storage.model.DescribeDiskCategoriesParams;
import com.gcloud.controller.storage.model.DetailDiskCategoryParams;
import com.gcloud.controller.storage.model.ModifyDiskCategoryParams;
import com.gcloud.controller.storage.provider.IStoragePoolProvider;
import com.gcloud.controller.storage.service.IStoragePoolService;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.enums.StorageDiskProtocol;
import com.gcloud.header.compute.enums.StorageType;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import com.gcloud.header.storage.StorageErrorCodes;
import com.gcloud.header.storage.enums.StoragePoolDriver;
import com.gcloud.header.storage.model.DiskCategoryModel;
import com.gcloud.header.storage.model.StoragePoolInfo;
import com.gcloud.header.storage.model.StoragePoolModel;
import com.gcloud.header.storage.msg.api.pool.ApiPoolsStatisticsReplyMsg;
import com.kenai.jffi.Array;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional(propagation = Propagation.REQUIRED)
@Component
public class StoragePoolServiceImpl implements IStoragePoolService {

    @Autowired
    private StoragePoolDao poolDao;

    @Autowired
    private DiskCategoryDao categoryDao;
    
    @Autowired
    private DiskCategoryZoneDao diskCategoryZoneDao;
    
    @Autowired
    private DiskCategoryPoolDao diskCategoryPoolDao;

    @Autowired
    private ComputeNodeDao nodeDao;
    
    @Autowired
    private VolumeDao volumeDao;
    
    @Autowired
    private ZoneDao zoneDao;
    
    @Autowired
    private StoragePoolZoneDao storagePoolZoneDao;

    @Override
    public List<DiskCategoryModel> describeDiskCategories(String zoneId) {
        return categoryDao.diskCategoryList(zoneId, DiskCategoryModel.class);
    }
    
    
    @Override
   	public PageResult<DiskCategoryModel> describeDiskCategories(DescribeDiskCategoriesParams params) {
    	return categoryDao.describeDiskCategories(params, DiskCategoryModel.class);
   	}

	@Override
    public String createDiskCategory(CreateDiskCategoryParams params) {
    	//磁盘类型数据
        DiskCategory entity = new DiskCategory();
        entity.setId(StringUtils.genUuid());
        entity.setName(params.getName());
        entity.setMinSize(params.getMinSize());
        entity.setMaxSize(params.getMaxSize());
        
        try{
        	this.categoryDao.save(entity);
        } catch(Exception e) {
        	log.error("创建磁盘类型失败，原因：【"+ e.getCause() + "::" + e.getMessage() +"】");
        	throw new GCloudException("::创建磁盘类型失败");
        }
        return entity.getId();
    }

    @Override
    public PageResult<StoragePoolModel> describeStoragePools(int pageNumber, int pageSize) {
        return this.poolDao.describeStoragePools(pageNumber, pageSize, StoragePoolModel.class);
    }

    private DiskCategory checkAndGetCategory(String categoryId) throws GCloudException {
        if (categoryId != null) {
            DiskCategory res = this.categoryDao.getById(categoryId);
            if (res != null) {
                return res;
            }
        }
        throw new GCloudException(StorageErrorCodes.NO_SUCH_STORAGE_TYPE);
    }

    private DiskCategory checkAndGetCategory(String zoneId, String code) throws GCloudException {
        Map<String, Object> props = new HashMap<>();
        props.put("zoneId", zoneId);
        props.put("code", code);
        List<DiskCategory> res = this.categoryDao.findByProperties(props);
        if (res.isEmpty()) {
            throw new GCloudException(StorageErrorCodes.NO_SUCH_STORAGE_TYPE);
        }
        return res.get(0);
    }

    @Override
    public String createStoragePool(String displayName, Integer providerType, String storageType, String poolName, String zoneId, String categoryId, String hostname,
                                    String driverName, String taskId) throws GCloudException {
        StorageType type = StorageType.value(storageType);
        if (type == null) {
            throw new GCloudException(StorageErrorCodes.NO_SUCH_STORAGE_TYPE);
        }
        StoragePoolDriver driver = this.checkAndGetDriver(type, driverName);
        this.checkAndGetCategory(categoryId);

        StorageDiskProtocol protocol = StorageDiskProtocol.getByProviderAndStorageType(providerType, driver.name());
        if (protocol == null) {
            throw new GCloudException(StorageErrorCodes.CONNECT_PROTOCOL_NOT_EXISTS);
        }

        StoragePool pool = new StoragePool();
        pool.setId(StringUtils.genUuid());

        if (this.poolDao.find(providerType, storageType, poolName, hostname) != null) {
            throw new GCloudException(StorageErrorCodes.POOL_ALREADY_EXISTS);
        }

        String refId = this.getProvider(providerType).createStoragePool(pool.getId(), storageType, poolName, hostname, taskId);

        pool.setDisplayName(displayName);
        pool.setStorageType(storageType);
        pool.setPoolName(poolName);
        pool.setProvider(providerType);
        pool.setProviderRefId(refId);
        pool.setHostname(hostname);
        pool.setDriver(driver.name());
        pool.setConnectProtocol(protocol.getDiskProtocol().value());

        try {
            this.poolDao.save(pool);
        } catch (DuplicateKeyException dex) {
            log.error("违法唯一性约束:" + dex, dex);
            throw new GCloudException(StorageErrorCodes.POOL_ALREADY_EXISTS);
        }

        return pool.getId();
    }

    @Override
    public String reportStoragePool(String displayName, Integer providerType, String storageType, String poolName, String categoryCode, String hostname, String driverName)
            throws GCloudException {

        if (ProviderType.get(providerType) == null) {
            throw new GCloudException("provider type " + providerType + " not found");
        }

        StorageType type = StorageType.value(storageType);
        if (type == null) {
            throw new GCloudException(StorageErrorCodes.NO_SUCH_STORAGE_TYPE);
        }

        if (StringUtils.isBlank(poolName)) {
            throw new GCloudException(StorageErrorCodes.INPUT_POOL_NAME_ERROR);
        }

        if (StringUtils.isBlank(displayName)) {
            displayName = hostname + "_local";
        }

        ComputeNode node = null;
        if (StringUtils.isNotBlank(hostname)) {
            node = nodeDao.findUniqueByProperty(ComputeNode.HOSTNAME, hostname);
        }
        if (node == null) {
            throw new GCloudException("node " + hostname + " not found");
        }

        DiskCategory category = this.checkAndGetCategory(node.getZoneId(), categoryCode);

        StoragePoolDriver driver = this.checkAndGetDriver(type, driverName);

        StorageDiskProtocol protocol = StorageDiskProtocol.getByProviderAndStorageType(providerType, driver.name());
        if (protocol == null) {
            throw new GCloudException(StorageErrorCodes.CONNECT_PROTOCOL_NOT_EXISTS);
        }

        StoragePool pool = this.poolDao.find(providerType, storageType, poolName, hostname);
        if (pool != null) {
            if (pool.getDriver() == null || !pool.getDriver().equals(type.getDefaultDriver().name())) {
                pool.setDriver(type.getDefaultDriver().name());
                this.poolDao.update(pool);
            }
        } else {
            pool = new StoragePool();
            pool.setId(StringUtils.genUuid());
            pool.setDisplayName(displayName);
            pool.setStorageType(storageType);
            pool.setPoolName(poolName);
            pool.setProvider(providerType);
            pool.setProviderRefId(pool.getId());
            pool.setHostname(hostname);
            pool.setDriver(driver.name());
            pool.setConnectProtocol(protocol.getDiskProtocol().value());
            try {
                this.poolDao.save(pool);
            } catch (DuplicateKeyException dex) {
                log.error("违法唯一性约束:" + dex, dex);
                throw new GCloudException(StorageErrorCodes.POOL_ALREADY_EXISTS);
            }
        }
        return pool.getId();
    }

    private StoragePoolDriver checkAndGetDriver(StorageType type, String driverName) throws GCloudException {
        if (StringUtils.isBlank(driverName)) {
            return type.getDefaultDriver();
        } else {
            StoragePoolDriver driver = StoragePoolDriver.get(driverName);
            if (driver == null) {
                throw new GCloudException(StorageErrorCodes.NO_SUCH_DRIVER);
            }
            return driver;
        }
    }

    @Override
    public void modifyStoragePool(String poolId, String displayName) throws GCloudException {
        synchronized (StoragePoolServiceImpl.class) {
            StoragePool pool = this.poolDao.checkAndGet(poolId);
            if (!StringUtils.equals(pool.getDisplayName(), displayName)) {
                List<String> updateFields = new ArrayList<>();
                updateFields.add(pool.updateDisplayName(displayName));
                this.poolDao.update(pool, updateFields);
                CacheContainer.getInstance().put(CacheType.STORAGEPOOL_NAME, poolId, displayName);
            }
        }
    }

    @Override
    public void deleteStoragePool(String poolId) throws GCloudException {
        synchronized (StoragePoolServiceImpl.class) {
            StoragePool pool = this.poolDao.checkAndGet(poolId);
            this.getProvider(pool.getProvider()).deleteStoragePool(pool.getStorageType(), pool.getProviderRefId(), pool.getPoolName());
            this.poolDao.deleteById(poolId);
        }
    }
    
    @Override
	public void modifyDiskCategory(ModifyDiskCategoryParams params, CurrentUser currentUser) {
		DiskCategory category = categoryDao.getById(params.getId());
		if(category == null) {
			//TODO 错误代码
			log.error("::不存在该磁盘类型");
			throw new GCloudException("::不存在该磁盘类型");
		}
		
		Map<String, Object> tmpParams = new HashMap<>();
		tmpParams.put(Volume.CATEGORY, params.getId());
		List<Volume> volumeList = volumeDao.findByProperties(tmpParams);
		if(volumeList != null && !volumeList.isEmpty()) {
			//TODO 错误代码
			log.error("::不能修改该磁盘类型，已创建相关磁盘");
			throw new GCloudException("::不能修改该磁盘类型，已创建相关磁盘");
		}
		
		List<String> fields = new ArrayList<>();
		fields.add(DiskCategory.MIN_SIZE);
		fields.add(DiskCategory.MAX_SIZE);
		
		category.setMinSize(params.getMinSize() == null ? 0 : params.getMinSize());
		category.setMaxSize(params.getMaxSize() == null ? 0 : params.getMaxSize());
		
		try{
			categoryDao.update(category, fields);
		} catch(Exception e) {
			log.error("修改磁盘类型失败，原因：【"+ e.getCause() + "::" + e.getMessage() +"】");
			//TODO 错误代码
			throw new GCloudException("::修改磁盘类型失败");
		}
		
	}

	@Override
	public void deleteDiskCategory(DeleteDiskCategoryParams params, CurrentUser currentUser) {
		DiskCategory category = categoryDao.getById(params.getId());
		if(category == null) {
			//TODO 错误代码
			log.error("::不存在该磁盘类型");
			throw new GCloudException("::不存在该磁盘类型");
		}
		
		Map<String, Object> tmpParams = new HashMap<>();
		tmpParams.put(Volume.CATEGORY, params.getId());
		List<Volume> volumeList = volumeDao.findByProperties(tmpParams);
		if(volumeList != null && !volumeList.isEmpty()) {
			//TODO 错误代码
			log.error("::不能删除该磁盘类型，已创建相关磁盘");
			throw new GCloudException("::不能删除该磁盘类型，已创建相关磁盘");
		}
		
		Map<String, Object> poolTmpParams = new HashMap<>();
		poolTmpParams.put(DiskCategoryPool.DISK_CATEGORY_ID, params.getId());
		List<DiskCategoryPool> poolList = diskCategoryPoolDao.findByProperties(poolTmpParams);
		if(poolList != null && !poolList.isEmpty()) {
			//TODO 错误代码
			log.error("::不能删除该磁盘类型，已关联存储池");
			throw new GCloudException("::不能删除该磁盘类型，已关联存储池");
		}
		
		try{
			categoryDao.deleteById(params.getId());
		} catch(Exception e) {
			log.error("删除磁盘类型失败，原因：【"+ e.getCause() + "::" + e.getMessage() +"】");
			//TODO 错误代码
			throw new GCloudException("::删除磁盘类型失败");
		}
	}

	@Override
	public DiskCategoryModel detailDiskCategory(DetailDiskCategoryParams params, CurrentUser currentUser) {
		DiskCategoryModel responese = new DiskCategoryModel();
		List<DiskCategoryModel> diskCategoryList = categoryDao.detailDiskCategory(params, DiskCategoryModel.class);
		if(diskCategoryList != null && !diskCategoryList.isEmpty()) {
			responese = diskCategoryList.get(0);
		}
		return responese;
	}

    private IStoragePoolProvider getProvider(int providerType) {
        IStoragePoolProvider provider = ResourceProviders.checkAndGet(ResourceType.STORAGE_POOL, providerType);
        return provider;
    }

	@Override
	public void associateDiskCategoryZone(AssociateDiskCategoryZoneParams params) {
		String diskCategoryId = params.getDiskCategoryId();
		String zoneId = params.getZoneId();
		
		DiskCategory diskCategory = categoryDao.getById(diskCategoryId);
		if(diskCategory == null) {
			//TODO 错误代码
			log.error("::不存在该磁盘类型");
			throw new GCloudException("::不存在该磁盘类型");
		}
		
		AvailableZoneEntity zone = zoneDao.getById(zoneId);
		if(zone == null) {
			log.error("::不存在该可用区");
			throw new GCloudException("::不存在该可用区");
		}
		

		Map<String, Object> dczParams = new HashMap<>();
		dczParams.put(DiskCategoryZone.DISK_CATEGORY_ID, diskCategoryId);
		dczParams.put(DiskCategoryZone.ZONE_ID, zoneId);
		
		List<DiskCategoryZone> dczList = diskCategoryZoneDao.findByProperties(dczParams);
		if(dczList != null && !dczList.isEmpty()) {
			log.error("::该磁盘类型已经关联了该可用区");
			throw new GCloudException("::该磁盘类型已经关联了该可用区");
		}
		
		DiskCategoryZone data = new DiskCategoryZone();
		data.setDiskCategoryId(diskCategoryId);
		data.setZoneId(zoneId);
		
		try{
			diskCategoryZoneDao.save(data);
		} catch(Exception e) {
			log.error("磁盘类型关联可用区失败，原因：【"+ e.getCause() + "::" + e.getMessage() +"】");
			throw new GCloudException("::磁盘类型关联可用区失败");
		}
		
	}

	@Override
	public void associateDiskCategoryPool(AssociateDiskCategoryPoolParams params) {
		String diskCategoryId = params.getDiskCategoryId();
		String zoneId = params.getZoneId();
		String poolId = params.getPoolId();
		
		DiskCategory dc = categoryDao.getById(diskCategoryId);
		if(dc == null) {
			log.error("::不存在该磁盘类型");
			throw new GCloudException("::不存在该磁盘类型");
		}
		
		AvailableZoneEntity zone = zoneDao.getById(zoneId);
		if(zone == null){
			log.error("::不存在该可用区");
			throw new GCloudException("::不存在该可用区");
		}
		
		//判断磁盘类型是否绑定了可用区
		Map<String, Object> dczParams = new HashMap<>();
		dczParams.put(DiskCategoryZone.DISK_CATEGORY_ID, diskCategoryId);
		dczParams.put(DiskCategoryZone.ZONE_ID, zoneId);
		List<DiskCategoryZone> dczList = diskCategoryZoneDao.findByProperties(dczParams);
		if(dczList == null || dczList.isEmpty()) {
			log.error("::该磁盘类型没有关联该可用区");
			throw new GCloudException("::该磁盘类型没有关联该可用区");
		}
		
		//判断存储池是否绑定了可用区
		Map<String, Object> spzParams = new HashMap<>();
		spzParams.put(StoragePoolZone.STORAGE_POOL_ID, poolId);
		spzParams.put(StoragePoolZone.ZONE_ID, zoneId);
		List<StoragePoolZone> spzList = storagePoolZoneDao.findByProperties(spzParams);
		if(spzList == null || spzList.isEmpty()) {
			log.error("::该存储池没有关联该可用区");
			throw new GCloudException("::该存储池没有关联该可用区");
		}

//		List<DiskCategoryPool> poolList = new ArrayList<>();
//		
//		for (DiskCategoryZone dcz : dczList) {
//			for (StoragePoolZone spz : spzList) {
//				if(spz.getZoneId().equals(dcz.getZoneId())) {
//					DiskCategoryPool pool = new DiskCategoryPool();
//					pool.setDiskCategoryId(dcz.getDiskCategoryId());
//					pool.setZoneId(dcz.getZoneId());
//					pool.setStoragePoolId(spz.getStoragePoolId());
//					poolList.add(pool);
//				}
//			}
//		}
		
		//判断是否是同一个可用区
		DiskCategoryZone dcz = dczList.get(0);
		StoragePoolZone spz = spzList.get(0);
		if(!dcz.getZoneId().equals(spz.getZoneId())) {
			log.error("::无法关联该存储池，不在同一个可用区");
			throw new GCloudException("::无法关联该存储池，不在同一个可用区");
		}
		
		//判断是否已经关联过
		Map<String, Object> dcpParams = new HashMap<>();
		dcpParams.put(DiskCategoryPool.DISK_CATEGORY_ID, dcz.getDiskCategoryId());
		dcpParams.put(DiskCategoryPool.ZONE_ID, dcz.getZoneId());
		dcpParams.put(DiskCategoryPool.STORAGE_POOL_ID, spz.getStoragePoolId());
		List<DiskCategoryPool> dcpList = diskCategoryPoolDao.findByProperties(dcpParams);
		if(dcpList != null && !dcpList.isEmpty()) {
			log.error("::已关联该存储池");
			throw new GCloudException("::已关联该存储池");
		}
		
		//关联
		DiskCategoryPool dcp = new DiskCategoryPool();
		dcp.setDiskCategoryId(dcz.getDiskCategoryId());
		dcp.setZoneId(dcz.getZoneId());
		dcp.setStoragePoolId(spz.getStoragePoolId());
		
		try{
			diskCategoryPoolDao.save(dcp);
		} catch(Exception e) {
			log.error("关联存储池失败，原因：【"+ e.getCause() + "::" + e.getMessage() +"】");
			throw new GCloudException("::关联存储池失败");
		}
	}

	@Override
	public ApiPoolsStatisticsReplyMsg poolStatistics(String poolId) {
		ApiPoolsStatisticsReplyMsg reply = new ApiPoolsStatisticsReplyMsg();
		List<StoragePool> pools = new ArrayList<StoragePool>();
		if(StringUtils.isNotBlank(poolId)) {
			StoragePool storagePool = this.poolDao.checkAndGet(poolId);
			pools.add(storagePool);
            
		} else {
			pools = poolDao.findAll();
		}
		for(StoragePool pool:pools) {
			StoragePoolInfo info = this.getProvider(pool.getProvider()).getPoolSize(pool);
            reply.setPoolNum(reply.getPoolNum() + 1);
            reply.setPoolTotalSize(reply.getPoolTotalSize() + info.getTotalSize());
            reply.setPoolAvailSize(reply.getPoolAvailSize() + info.getAvailSize());
            reply.setPoolUsedSize(reply.getPoolUsedSize() + info.getUsedSize());
		}
		return reply;
	}
}
