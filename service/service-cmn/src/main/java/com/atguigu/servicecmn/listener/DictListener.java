package com.atguigu.servicecmn.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.servicecmn.mapper.DictMapper;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;

import java.util.Map;

/**
 * @author Guanghao Wei
 * @create 2023-03-07 17:03
 */
public class DictListener extends AnalysisEventListener<DictEeVo> {
    private DictMapper dictMapper;

    /**
     * 以构造方法的形式将mapper传过来
     * @param mapper
     */
    public  DictListener(DictMapper mapper){
        this.dictMapper=mapper;
    }

    /**
     * 这一步主要是一行一行的读数据
     * 读出一行封装为Dict对象，插入数据库
     * @param dictEeVo
     * @param analysisContext
     */
    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        Dict dict=new Dict();
        BeanUtils.copyProperties(dictEeVo,dict);
        dictMapper.insert(dict);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
