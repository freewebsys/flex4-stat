package com.freewebsys.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.freewebsys.dao.StatBaseDao;
import com.freewebsys.service.StatBaseService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultCDATA;
import org.dom4j.tree.DefaultElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * User: freewebsys.com
 */
@Service("statBaseService")
public class StatBaseServiceImpl implements StatBaseService {

    @Autowired
    private StatBaseDao statBaseDao;

    //读取xml文件返回Document。
    private Document xmlReader(String confXml) {
        try {
            SAXReader reader = new SAXReader();
            return reader.read(IOUtils.toInputStream(confXml));
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    //主函数，负责循环处理节点。
    public String findStat(String confXml) {

        Document document = xmlReader(confXml);
        System.out.println(document);
        List<DefaultElement> list = document.selectNodes("/stats/stat");
        for (DefaultElement element : list) {
            mergeStatElement(element);
        }
        return document.asXML();
    }

    private DefaultElement mergeStatElement(DefaultElement element) {
        System.out.println(element);
        //处理查询出所有列表显示的元素。
        List<DefaultElement> columnList = element.selectNodes("list/column");

        //找到查询sql。
        Node sqlNode = element.selectSingleNode("sql");
        String sql = sqlNode.getText();
        //获取数据后将节点删除。在客户端不显示。
        element.remove(sqlNode);
        System.out.println(sql);

        List<Map<String, Object>> list = statBaseDao.findStat(sql, null);
        JSONArray jsonArray = new JSONArray();
        for (Map<String, Object> tmpData : list) {
            JSONObject jsonObject = new JSONObject();
            for (DefaultElement column : columnList) {
                System.out.println(column);
                String columnName = column.attributeValue("field");
                Object objValue = tmpData.get(columnName);
                jsonObject.put(columnName, objValue);
                //将一行数据组织成对象，放到数组里面。
            }
            jsonArray.add(jsonObject);
        }
        DefaultElement elementData = new DefaultElement("data");
        elementData.add(new DefaultCDATA(jsonArray.toJSONString()));
        //增加data字段，保存json数据。
        element.add(elementData);
        return element;
    }

    //读取配置文件。
    public String readConfFile(String configName) {
        String basePath = StatBaseServiceImpl.class.getResource("/")
                .toString().replace("file:", "");
        try {
            return FileUtils.readFileToString(new File(basePath + configName), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
