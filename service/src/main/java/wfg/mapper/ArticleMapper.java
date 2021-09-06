package wfg.mapper;

import wfg.dao.Article;




public interface ArticleMapper {

    /**
     * 新增
     */
    public  int insert(Article article);

    /**
     * 查询
     */

    public Article selectById(Long id);


    /**
     * 更新
     */

    public int updateModified(Article article);

    /**
     * 删除
     */
  public int deletedById(Long id);
}
