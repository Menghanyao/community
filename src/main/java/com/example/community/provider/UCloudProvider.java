package com.example.community.provider;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import cn.ucloud.ufile.http.OnProgressListener;
import com.example.community.exception.CustomizeErrorCode;
import com.example.community.exception.CustomizeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

@Service
public class UCloudProvider {

    @Value("${ucloud.ufile.public-key}")
    private String publicKey;

    @Value("${ucloud.ufile.private-key}")
    private String privateKey;

    @Value("${ucloud.ufile.bucket-name}")
    private String bucketName;

    @Value("${ucloud.ufile.region}")
    private String region;


    public String upload(InputStream fileStream, String mimeType, String fileName) {
        ObjectAuthorization objectAuthorizer = new UfileObjectLocalAuthorization(publicKey, privateKey);
        ObjectConfig config = new ObjectConfig(region, "ufileos.com");
        File file = new File("your file path");

        String generatedFileName = "";
        String[] fileSpliter = fileName.split("\\.");
        if (fileSpliter.length > 1) {
            generatedFileName = UUID.randomUUID().toString() + "." + fileSpliter[fileSpliter.length-1];
        } else {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAILED);
        }

        try {
            PutObjectResultBean response = UfileClient.object(objectAuthorizer, config)
                    .putObject(fileStream, mimeType)
                    .nameAs(generatedFileName)
                    .toBucket(bucketName)
                    /**
                     * 是否上传校验MD5, Default = true
                     */
                    //  .withVerifyMd5(false)
                    /**
                     * 指定progress callback的间隔, Default = 每秒回调
                     */
                    //  .withProgressConfig(ProgressConfig.callbackWithPercent(10))
                    /**
                     * 配置进度监听
                     */
                    .setOnProgressListener(new OnProgressListener() {
                        @Override
                        public void onProgress(long bytesWritten, long contentLength) {

                        }
                    })
                    .execute();

                    if(response != null && response.getRetCode() == 0) {
                        String url = UfileClient.object(objectAuthorizer, config)
                                .getDownloadUrlFromPrivateBucket(generatedFileName, bucketName, 24*60*60*3650)
                                /**
                                 * 使用Content-Disposition: attachment，并且默认文件名为KeyName
                                 */
//                    .withAttachment()
                                /**
                                 * 使用Content-Disposition: attachment，并且配置文件名
                                 */
//                    .withAttachment("filename")
                                .createUrl();
                                return url;
                    } else {
                        throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAILED);
                    }

        } catch (UfileClientException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAILED);
        } catch (UfileServerException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAILED);
        }
    }
}
