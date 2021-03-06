package com.dounine.japi.core.impl;

import com.alibaba.fastjson.JSON;
import com.dounine.japi.JapiClient;
import com.dounine.japi.core.IBuiltIn;
import com.dounine.japi.exception.JapiException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by huanghuanlai on 2017/1/19.
 */
public class BuiltInActionImpl implements IBuiltIn {
    private static final Logger LOGGER = LoggerFactory.getLogger(BuiltInActionImpl.class);

    private List<String> types = new ArrayList<>();
    private static final BuiltInActionImpl builtIn = new BuiltInActionImpl();

    static {
        builtIn.types.add("org.springframework.validation.BindingResult");
        builtIn.types.add("javax.servlet.http.HttpServletRequest");
        builtIn.types.add("javax.servlet.http.HttpServletResponse");
        builtIn.types.add("org.springframework.web.multipart.MultipartFile");
    }

    private BuiltInActionImpl() {

        URL url = null;
        if(null!=JapiClient.getClassLoader()){
            url = JapiClient.getClassLoader().getResource("/action-builtIn-types.txt");
            if(!new File(url.getFile()).exists()){
                url = JapiClient.getClassLoader().getResource("/japi/action-builtIn-types.txt");
            }
        }


        File builtInFile = null;
        if (null != url) {
            builtInFile = new File(url.getFile());
        }
        if (url == null) {
//            LOGGER.warn("action-builtIn-types.txt 文件不存在,使用默认参数.");
        } else {
            String builtInFilePath = builtInFile.getAbsolutePath();
            try {
                File file = new File(builtInFilePath);
                if(file.exists()){
                    String typesStr = FileUtils.readFileToString(file, Charset.forName("utf-8"));
                    typesStr = typesStr.replaceAll("\\s", "");//去掉回车
                    types.addAll(Arrays.asList(typesStr.split(",")));
                }

            } catch (IOException e) {
                throw new JapiException(e.getMessage());
            }
        }
    }

    public static BuiltInActionImpl getInstance() {
        return builtIn;
    }

    private List<String> getBuiltInTypes() {
        return types;
    }

    @Override
    public boolean isBuiltInType(String keyType) {
        List<String> keyTypes = getBuiltInTypes();
        boolean isBuiltIn = false;
        for (String key : keyTypes) {
            if (key.equals(keyType) || key.endsWith(keyType)) {
                isBuiltIn = true;
                break;
            }
        }
        return isBuiltIn;
    }
}
