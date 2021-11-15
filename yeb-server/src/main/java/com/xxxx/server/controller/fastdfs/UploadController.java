package com.xxxx.server.controller.fastdfs;

import com.alibaba.fastjson.JSON;
import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.Role;
import com.xxxx.server.service.fastdfs.IUploadService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description： 文件上传
 * @date: 2020/7/9 22:25
 */
@Controller
@RequestMapping("/system/admin/info")
class UploadController {

    @Resource
    private IUploadService uploadService;

    /**
     * 功能描述:  文件上传
     *
     * @param file 要上传的文件
     * @return {@link Map< String, Object>}
     * @data 2020/7/9 22:27
     */
    @PostMapping("/upload")
    @ResponseBody
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file,
                                      @RequestParam("id") Integer id
                                      ) throws IOException {
        Admin admin =new Admin();
        admin.setId(id);



        String filePath = uploadService.upload(file, admin);
        Map<String, Object> map = new HashMap<>();
        map.put("code", "200");
        map.put("message", "文件上传成功");
        map.put("filePath", filePath);

        return map;
    }

}
