package com.gcloud.controller.compute.utils;

import com.gcloud.controller.compute.prop.NoVncProp;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.service.SpringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;

/**
 * handle token file for noVNC.
 */
@Slf4j
public class NoVncUtil {

    private static boolean writeFileContent(String filePath, String content) {
        try {
            FileWriter fw = new FileWriter(filePath);
            fw.write(content);
            fw.flush();
            fw.close();
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return false;
    }

    public static void addToken(String instanceId, String vnc_ip, int vnc_port) throws GCloudException {
        // format, inside the bracket. [tokenId: host:port]
        NoVncProp prop = SpringUtil.getBean(NoVncProp.class);
        String content = String.format("%s: %s:%d", instanceId, vnc_ip, vnc_port);
        String filePath = prop.getTokenDir() + "/" + instanceId;
        File file = new File(filePath);

        if (file.exists()) {
            if (!file.delete()) {
                throw new GCloudException("delete exist token file failed:" + filePath);
            }
        }
        try {
            if (! file.createNewFile()) {
                throw new GCloudException("create token file failed: " + filePath);
            }
        } catch (Exception e) {
            throw new GCloudException("create token file failed:" + e.getMessage());
        }
        if (!writeFileContent(filePath, content)) {
            throw new GCloudException("write token file failed: " + filePath);
        }
    }

    public static void deleteExpiredToken(long expireMillionSeconds) {
        NoVncProp prop = SpringUtil.getBean(NoVncProp.class);
        File dir = new File(prop.getTokenDir());
        if (!dir.exists()) {
            log.error("novnc token dir not exist: " + prop.getTokenDir());
            return;
        }
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File f : files) {
            Path path = Paths.get(prop.getTokenDir() + "/" + f.getName());
            try {
                BasicFileAttributes bfs = Files.readAttributes(path, BasicFileAttributes.class);
                FileTime ft = bfs.creationTime();
                if (ft.toMillis() + expireMillionSeconds < (new Date()).getTime()) {
                    log.info("delete token file: " + f.getName());
                    deleteToken(f.getName());
                }
            }
            catch (Exception e) {
                log.error("handle expired token file got exception: " + f.getName() + e.getMessage());
            }
        }
    }

    public static void deleteToken(String instanceId) throws GCloudException {
        NoVncProp prop = SpringUtil.getBean(NoVncProp.class);
        String filePath = prop.getTokenDir() + "/" + instanceId;
        File f = new File(filePath);
        f.delete();
    }

    public static String generateVncUrl(String instanceId) {
        NoVncProp prop = SpringUtil.getBean(NoVncProp.class);
        String url = "http://" + prop.getNoVncHost() + ":" + prop.getNoVncPort()
                + "/vnc.html?host=" + prop.getWebsockifyHost()
                + "&port=" + prop.getWebsockifyPort()
                + "&path=websockify/?token=" + instanceId;

        log.info("generate vnc url: " + url);
        return url;
    }
}
