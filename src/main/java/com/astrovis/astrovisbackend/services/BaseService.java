package com.astrovis.astrovisbackend.services;


import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;


public abstract class BaseService<Mapper,Record,Example> {




    private final Log log = LogFactory.getLog(this.getClass());

    public Mapper mapper;

    @Autowired
    private ApplicationContext applicationContext;



    protected Log getLog(){
        return log;
    }

    public int countByExample(Example example) {
        initMapper();
        try {

            Method countByExample = mapper.getClass().getDeclaredMethod("countByExample", example.getClass());
            Object result = countByExample.invoke(mapper, example);

            return Integer.parseInt(String.valueOf(result));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.debug(e.getStackTrace());
        }

        return 0;
    }


    public int deleteByExample(Example example) {
        initMapper();
        try {

            Method deleteByExample = mapper.getClass().getDeclaredMethod("deleteByExample", example.getClass());
            Object result = deleteByExample.invoke(mapper, example);

            return Integer.parseInt(String.valueOf(result));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.debug(e.getStackTrace());
        }

        return 0;
    }


    public int deleteByPrimaryKey(Integer id) {
        initMapper();
        try {

            Method deleteByPrimaryKey = mapper.getClass().getDeclaredMethod("deleteByPrimaryKey", id.getClass());
            Object result = deleteByPrimaryKey.invoke(mapper, id);

            return Integer.parseInt(String.valueOf(result));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.debug(e.getStackTrace());
        }

        return 0;
    }


    public int insert(Record record) {
        initMapper();
        try {

            Method insert = mapper.getClass().getDeclaredMethod("insert", record.getClass());
            Object result = insert.invoke(mapper, record);

            return Integer.parseInt(String.valueOf(result));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.debug(e.getStackTrace());
            e.printStackTrace();
        }

        return 0;
    }


    public int insertSelective(Record record) {
        initMapper();
        try {

            Method insertSelective = mapper.getClass().getDeclaredMethod("insertSelective", record.getClass());
            Object result = insertSelective.invoke(mapper, record);

            return Integer.parseInt(String.valueOf(result));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.debug(e.getStackTrace());
        }

        return 0;
    }


    public List<Record> selectByExampleWithBLOBs(Example example) {
        initMapper();
        try {

            Method selectByExampleWithBLOBs = mapper.getClass().getDeclaredMethod("selectByExampleWithBLOBs", example.getClass());
            Object result = selectByExampleWithBLOBs.invoke(mapper, example);

            return (List<Record>) result;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.debug(e.getStackTrace());
        }

        return null;
    }


    public List<Record> selectByExample(Example example) {
        initMapper();
        try {

            Method selectByExample = mapper.getClass().getDeclaredMethod("selectByExample", example.getClass());
            Object result = selectByExample.invoke(mapper, example);

            return (List<Record>) result;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.debug(e.getStackTrace());
        }

        return null;
    }


    public List<Record> selectByExampleWithBLOBsForStartPage(Example example, Integer pageNum, Integer pageSize) {
        initMapper();
        try {

            Method selectByExampleWithBLOBs = mapper.getClass().getDeclaredMethod("selectByExampleWithBLOBs", example.getClass());
            PageHelper.startPage(pageNum, pageSize, false);
            Object result = selectByExampleWithBLOBs.invoke(mapper, example);

            return (List<Record>) result;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.debug(e.getStackTrace());
        }

        return null;
    }


    public List<Record> selectByExampleForStartPage(Example example, Integer pageNum, Integer pageSize) {
        initMapper();
        try {

            Method selectByExample = mapper.getClass().getDeclaredMethod("selectByExample", example.getClass());
            PageHelper.startPage(pageNum, pageSize, false);
            Object result = selectByExample.invoke(mapper, example);

            return (List<Record>) result;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.debug(e.getStackTrace());
        }

        return null;
    }


    public List<Record> selectByExampleWithBLOBsForOffsetPage(Example example, Integer offset, Integer limit) {
        initMapper();
        try {

            Method selectByExampleWithBLOBs = mapper.getClass().getDeclaredMethod("selectByExampleWithBLOBs", example.getClass());
            PageHelper.offsetPage(offset, limit, false);
            Object result = selectByExampleWithBLOBs.invoke(mapper, example);

            return (List<Record>) result;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.debug(e.getStackTrace());
        }

        return null;
    }


    public List<Record> selectByExampleForOffsetPage(Example example, Integer offset, Integer limit) {
        initMapper();
        try {

            Method selectByExample = mapper.getClass().getDeclaredMethod("selectByExample", example.getClass());
            PageHelper.offsetPage(offset, limit, false);
            Object result = selectByExample.invoke(mapper, example);

            return (List<Record>) result;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.debug(e.getStackTrace());
        }

        return null;
    }


    public Record selectFirstByExample(Example example) {
        initMapper();
        try {

            Method selectByExample = mapper.getClass().getDeclaredMethod("selectByExample", example.getClass());
            List<Record> result = (List<Record>) selectByExample.invoke(mapper, example);

            if (null != result && result.size() > 0) {
                return result.get(0);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.debug(e.getStackTrace());
            e.printStackTrace();
        }

        return null;
    }


    public Record selectFirstByExampleWithBLOBs(Example example) {
        initMapper();
        try {

            Method selectByExampleWithBLOBs = mapper.getClass().getDeclaredMethod("selectByExampleWithBLOBs", example.getClass());
            List<Record> result = (List<Record>) selectByExampleWithBLOBs.invoke(mapper, example);

            if (null != result && result.size() > 0) {
                return result.get(0);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.debug(e.getStackTrace());
        }

        return null;
    }


    public Record selectByPrimaryKey(Integer id) {
        initMapper();
        try {

            Method selectByPrimaryKey = mapper.getClass().getDeclaredMethod("selectByPrimaryKey", id.getClass());
            Object result = selectByPrimaryKey.invoke(mapper, id);

            return (Record) result;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.debug(e.getStackTrace());
        }

        return null;
    }


    public int updateByExampleSelective(@Param("record") Record record, @Param("example") Example example) {
        initMapper();
        try {

            Method updateByExampleSelective = mapper.getClass().getDeclaredMethod("updateByExampleSelective", record.getClass(), example.getClass());
            Object result = updateByExampleSelective.invoke(mapper, record, example);

            return Integer.parseInt(String.valueOf(result));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.debug(e.getStackTrace());
        }
        return 0;
    }


    public int updateByExampleWithBLOBs(@Param("record") Record record, @Param("example") Example example) {
        initMapper();
        try {

            Method updateByExampleWithBLOBs = mapper.getClass().getDeclaredMethod("updateByExampleWithBLOBs", record.getClass(), example.getClass());
            Object result = updateByExampleWithBLOBs.invoke(mapper, record, example);

            return Integer.parseInt(String.valueOf(result));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.debug(e.getStackTrace());
        }

        return 0;
    }


    public int updateByExample(@Param("record") Record record, @Param("example") Example example) {
        initMapper();
        try {

            Method updateByExample = mapper.getClass().getDeclaredMethod("updateByExample", record.getClass(), example.getClass());
            Object result = updateByExample.invoke(mapper, record, example);

            return Integer.parseInt(String.valueOf(result));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.debug(e.getStackTrace());
        }

        return 0;
    }


    public int updateByPrimaryKeySelective(Record record) {
        initMapper();
        try {

            Method updateByPrimaryKeySelective = mapper.getClass().getDeclaredMethod("updateByPrimaryKeySelective", record.getClass());
            Object result = updateByPrimaryKeySelective.invoke(mapper, record);

            return Integer.parseInt(String.valueOf(result));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.debug(e.getStackTrace());
        }

        return 0;
    }


    public int updateByPrimaryKeyWithBLOBs(Record record) {
        initMapper();
        try {

            Method updateByPrimaryKeyWithBLOBs = mapper.getClass().getDeclaredMethod("updateByPrimaryKeyWithBLOBs", record.getClass());
            Object result = updateByPrimaryKeyWithBLOBs.invoke(mapper, record);

            return Integer.parseInt(String.valueOf(result));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.debug(e.getStackTrace());
        }

        return 0;
    }


    public int updateByPrimaryKey(Record record) {
        initMapper();
        try {

            Method updateByPrimaryKey = mapper.getClass().getDeclaredMethod("updateByPrimaryKey", record.getClass());
            Object result = updateByPrimaryKey.invoke(mapper, record);

            return Integer.parseInt(String.valueOf(result));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.debug(e.getStackTrace());
        }

        return 0;
    }


    public int deleteByPrimaryKeys(String ids) {
        initMapper();
        try {
            if (StringUtils.isBlank(ids)) {
                return 0;
            }

            String[] idArray = ids.split("-");
            int count = 0;
            for (String idStr : idArray) {
                if (StringUtils.isBlank(idStr)) {
                    continue;
                }
                Integer id = Integer.parseInt(idStr);
                Method deleteByPrimaryKey = mapper.getClass().getDeclaredMethod("deleteByPrimaryKey", id.getClass());
                Object result = deleteByPrimaryKey.invoke(mapper, id);
                count += Integer.parseInt(String.valueOf(result));
            }

            return count;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.debug(e.getStackTrace());
        }

        return 0;
    }


    public void initMapper() {
        if (this.mapper == null) {
            this.mapper = applicationContext.getBean(getMapperClass());
        }
    }

    /**
     * 获取类泛型class
     *
     * @return
     */
    public Class<Mapper> getMapperClass() {
        return (Class<Mapper>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

}
