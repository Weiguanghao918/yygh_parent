package com.atguigu.servicecmn.service;

import com.atguigu.yygh.model.cmn.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Guanghao Wei
 * @create 2023-03-07 15:48
 */
public interface DictService extends IService<Dict> {
    List<Dict> findByParentId(Long id);

    void exportData(HttpServletResponse response);

    void importData(MultipartFile file);

    String getDictName(String dictCord, String value);

    List<Dict> findByDictCode(String dictCord);
}
