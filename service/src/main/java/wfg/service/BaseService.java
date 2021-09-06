package wfg.service;

import java.util.Collection;
import java.util.List;

/**
 * 基本业务的实现
 */
public interface BaseService<T> {

    /**
     * 删除
     * @return
     */
    public <T> int  deleted(T t);

    /**
     * 新增
     */

    public  int insert(T obj);

    /**
     * 修改
     */
     public int updated(T t);
    /**
     * 根据id查找
     */

  public T selectById(int id);
    /**
     * 根据条件进行批量查询
      */

    public List<T> selectList(T obj);

    /**
     * 根据id批量查询
     */

    Collection<T> selectByIds(List<Long> idList);
    /**
     * 分页查询
     */
}
