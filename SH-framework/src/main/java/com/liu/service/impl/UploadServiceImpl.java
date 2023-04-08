package com.liu.service.impl;

import com.google.gson.Gson;
import com.liu.domain.ResponseResult;
import com.liu.enums.AppHttpCodeEnum;
import com.liu.exception.SystemException;
import com.liu.service.UploadService;
import com.liu.utils.PathUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class UploadServiceImpl implements UploadService {
    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        String orginalFillename=img.getOriginalFilename();
        if (!orginalFillename.endsWith(".png")){
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        String filePath= PathUtils.generateFilePath(orginalFillename);


        return ResponseResult.okResult( upload(img,filePath));
    }
    public String upload(MultipartFile img, String filePath){
        String accessKey="FYq9wBN6irqN0fla3eOhnhcoYwq0Rp-eDlaruFKJ";
        String secretKey="Em4pbGBlR-Zmy5-lIT1P1AF6-DQe2iryrvHrwwkd";
        String bucket="shblog123";
//构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传

//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath ;
        try {
            byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
//            ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes);
            InputStream inputStream=img.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return "http://rria36h2a.hn-bkt.clouddn.com/"+key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (UnsupportedEncodingException | FileNotFoundException ex) {
            //ignore
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return "www";
    }
}
