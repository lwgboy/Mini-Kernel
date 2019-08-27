
package com.gcloud.header.storage;

public class StorageErrorCodes {

    // PARAMS
    public static final String INPUT_ZONE_ID_ERROR = "0060001::可用区ID不能为空";
    public static final String INPUT_DISK_ID_ERROR = "0060002::磁盘ID不能为空";
    public static final String INPUT_POOL_ID_ERROR = "0060003::存储池ID不能为空";
    public static final String INPUT_SNAPSHOT_ID_ERROR = "0060004::快照ID不能为空";
    public static final String INPUT_DISK_SIZE_ERROR = "0060005::磁盘大小要大于1";
    public static final String INPUT_DISK_NAME_ERROR = "0060006::磁盘名称长度不能大于255";
    public static final String INPUT_DISK_DESCRIPTION_ERROR = "0060007::磁盘描述长度不能大于255";
    public static final String INPUT_DISK_CATEGORY_ERROR = "0060008::磁盘类型不能为空";
    public static final String INPUT_POOL_NAME_ERROR = "0060009::存储池名不能为空";
    public static final String INPUT_STORAGE_TYPE_ERROR = "0060010::存储类型不能为空";
    public static final String INPUT_PROVIDER_ERROR = "0060011::服务提供者不能为空";
    public static final String INPUT_SNAPSHOT_NAME_ERROR = "0060012::快照名称长度不能大于255";
    public static final String INPUT_SNAPSHOT_DESCRIPTION_ERROR = "0060013::快照描述长度不能大于255";
    public static final String INPUT_CATEGORY_NAME_ERROR = "0060014::磁盘类型不能为空";
    public static final String INPUT_CATEGORY_CODE_ERROR = "0060015::磁盘类型不能为空";

    // COMMON
    public static final String NO_SUCH_PROVIDER = "0060101::找不到此服务提供者";
    public static final String NO_SUCH_STORAGE_TYPE = "0060102::找不到此存储类型";
    public static final String NO_SUCH_DISK_TYPE = "0060103::找不到此磁盘类型";
    public static final String NO_SUCH_DRIVER = "0060104::找不到此驱动";

    // POOL ERRORCODE
    public static final String FAILED_TO_FIND_POOL = "0060201::存储池不存在";
    public static final String POOL_ALREADY_EXISTS = "0060202::存储池已存在";
    public static final String FAILED_TO_CREATE_POOL = "0060203::创建存储池失败";
    public static final String FAILED_TO_DELETE_POOL = "0060204::删除存储池失败";
    public static final String CATEGORY_ALREADY_EXISTS = "0060205::磁盘类型已存在";
    public static final String CONNECT_PROTOCOL_NOT_EXISTS = "0060206::连接协议不存在";

    // VOLUME ERRORCODE
    public static final String FAILED_TO_CREATE_VOLUME = "0060301::创建磁盘失败";
    public static final String FAILED_TO_DELETE_VOLUME = "0060302::删除磁盘失败";
    public static final String FAILED_TO_RESIZE_VOLUME = "0060303::设置磁盘大小失败";
    public static final String FAILED_TO_ATTACH_VOLUME = "0060304::挂载磁盘失败";
    public static final String FAILED_TO_DETACH_VOLUME = "0060305::卸载磁盘失败";
    public static final String FAILED_TO_FIND_VOLUME = "0060306::找不到对应的磁盘";
    public static final String NEW_SIZE_CANNOT_BE_SMALLER = "0060307::磁盘的新大小要大于原来大小";
    public static final String VOLUME_IS_ATTACHED = "0060308::操作失败，磁盘正在被使用";
    public static final String VOLUME_IS_NOT_AVAILABLE = "0060309::操作失败，磁盘状态必须为可用";
    public static final String VOLUME_IS_NOT_IN_USE = "0060310::操作失败，磁盘状态必须为使用中";
    public static final String VOLUME_IS_NOT_ATTACHING = "0060311::操作失败，磁盘状态不是挂载中";
    public static final String VOLUME_IS_NOT_DETACHING = "0060312::操作失败，磁盘状态不是卸载中";
    public static final String REF_IMAGE_IS_NOT_FOUND = "0060313::找不到关联镜像";

    // SNAPSHOT ERRORCODE
    public static final String SNAPSHOT_NOT_FOUND = "0060401::找不到对应的快照";
    public static final String FAILED_TO_CREATE_SNAP = "0060402::创建快照失败";
    public static final String FAILED_TO_DELETE_SNAP = "0060403::删除快照失败";
    public static final String FAILED_TO_RESET_SNAP = "0060404::磁盘还原失败";
    public static final String SNAPSHOT_NOT_SUPPORTED = "0060405::不支持此快照方式";

}
