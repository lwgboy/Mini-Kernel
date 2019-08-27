package com.gcloud.core.async;

/**
 * Created by yaowj on 2018/9/26.
 *
 * 改为返回AsyncResult 为满足后续复杂场景逻辑处理。
 *
 * 1. RUNNING 表示还在运行中，继续循环。 （例如创建中检测，状态还是创建中）
 * 2. SUCCEED 表示async执行完成，而且是成功。 （例如创建中检测，状态还是已创建）
 * 3. FAILED  表示async执行完成，而且是失败。（例如创建中检测，状态还是已经失败，或者已经找不到对应的资源。（创建失败被删除））
 * 4. EXCEPTION 是抛出异常后赋值，不要在其他地方赋值。
 * 5. TIMEOUT 表示超时后，还没有检测到结果。（例如创建中检测，超时后，状态还是创建中）
 *
 * ！！！如果需要增加返回值，请修改AsyncBase中处理逻辑
 *
 */

public enum AsyncStatus {
    RUNNING, SUCCEED, FAILED, EXCEPTION, TIMEOUT
}
