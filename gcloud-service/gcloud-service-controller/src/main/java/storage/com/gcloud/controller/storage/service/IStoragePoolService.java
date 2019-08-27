
package com.gcloud.controller.storage.service;

import java.util.List;

import com.gcloud.controller.storage.model.AssociateDiskCategoryPoolParams;
import com.gcloud.controller.storage.model.AssociateDiskCategoryZoneParams;
import com.gcloud.controller.storage.model.CreateDiskCategoryParams;
import com.gcloud.controller.storage.model.DeleteDiskCategoryParams;
import com.gcloud.controller.storage.model.DescribeDiskCategoriesParams;
import com.gcloud.controller.storage.model.DetailDiskCategoryParams;
import com.gcloud.controller.storage.model.ModifyDiskCategoryParams;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.storage.model.DiskCategoryModel;
import com.gcloud.header.storage.model.StoragePoolModel;
import com.gcloud.header.storage.msg.api.pool.ApiPoolsStatisticsReplyMsg;

public interface IStoragePoolService {

    List<DiskCategoryModel> describeDiskCategories(String zoneId);
    
    PageResult<DiskCategoryModel> describeDiskCategories(DescribeDiskCategoriesParams params);

    String createDiskCategory(CreateDiskCategoryParams params);

    void modifyDiskCategory(ModifyDiskCategoryParams params, CurrentUser currentUser);
    
    void deleteDiskCategory(DeleteDiskCategoryParams params, CurrentUser currentUser);
    
    DiskCategoryModel detailDiskCategory(DetailDiskCategoryParams params, CurrentUser currentUser);
    
    PageResult<StoragePoolModel> describeStoragePools(int pageNumber, int pageSize);

    String createStoragePool(String displayName, Integer providerType, String storageType, String poolName, String zoneId, String categoryId, String hostname, String driverName,
            String taskId) throws GCloudException;

    String reportStoragePool(String displayName, Integer providerType, String storageType, String poolName, String categoryCode, String hostname, String driverName)
            throws GCloudException;

    void modifyStoragePool(String poolId, String displayName) throws GCloudException;

    void deleteStoragePool(String poolId) throws GCloudException;

	void associateDiskCategoryZone(AssociateDiskCategoryZoneParams params);
	
	void associateDiskCategoryPool(AssociateDiskCategoryPoolParams params);
	
    ApiPoolsStatisticsReplyMsg poolStatistics(String poolId);
}
