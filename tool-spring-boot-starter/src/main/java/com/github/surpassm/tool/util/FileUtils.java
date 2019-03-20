package com.github.surpassm.tool.util;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mc
 * Create date 2019/1/7 16:25
 * Version 1.0
 * Description
 */
public class FileUtils {


    static String FILE_UPLOAD_PATH = "/upload/";

    public static com.github.surpassm.common.pojo.File upload(MultipartFile file, HttpServletRequest request, String path) throws Exception {
        String fileName = System.currentTimeMillis() + "." + getFileType(file);
        int size = (int) file.getSize();
        String filePath = "/" + path + "/" + nowDate() + "/";
        File dest = new File(rootPath() + filePath + "/" + fileName);
        //判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            //保存文件
            file.transferTo(dest);
            return com.github.surpassm.common.pojo.File.builder()
                    .fileName(getFileName(file))
                    .fileSuffix("." + getFileType(file))
                    .url(filePath + fileName)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFileType(MultipartFile file) {
        String[] s = file.getOriginalFilename().split("\\.");
        List list = new ArrayList();
        for (String s1 : s) {
            list.add(s1);
        }
        if (list.size() > 1) {
            return list.get(list.size() - 1).toString();
        }
        return null;
    }

	/**
	 * 获取当前系统日期
	 */
	public static String nowDate() {
		return DateUtil.getDateTimeAsString(LocalDateTime.now(),"yyyy-MM-dd");
	}

    /**
     * 获取项目根路径
     */
    public static String rootPath() throws Exception {
        // 参数为空
        File directory = new File("");
        return directory.getCanonicalPath();
    }

    /**
     * 获取请求http
     *
     * @param request
     * @return
     */
    public static String http(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }

    /**
     * 获取文件名称
     */
    public static String getFileName(MultipartFile file) {
        String filename = file.getOriginalFilename().replace("." + getFileType(file), "");
        return filename;
    }

    /**
     * 删除文件
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static boolean delFile(String filePath) throws Exception {
        if (StringUtils.isEmpty(filePath)) {
            return false;
        }
        //网络路径处理得到/upload/**路径
        if (filePath.contains(FILE_UPLOAD_PATH)) {
            filePath = (filePath.substring(filePath.indexOf(FILE_UPLOAD_PATH))).replace("/", "\\");
        }
        //获取项目所在绝对路径
        String rootPath = FileUtils.rootPath();
        //拼接获取文件真实路径
        String absolutePath = rootPath + filePath;
        File file = new File(absolutePath);
        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            file.delete();
            return true;
        } else {
            return false;
        }
    }
}
