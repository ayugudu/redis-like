package wfg.mapper;

import wfg.dao.UserLikeArt;

public interface UserLikeArticleMapper {

    /**
     * 新增
     */
    public  int insert(UserLikeArt userLikeArt);

    /**
     * 查询
     */

    public UserLikeArt selectById(Long id);
    /**
     * 按条件查询
     */

    public UserLikeArt selectByOne(UserLikeArt userLikeArt);


    /**
     * 更新
     */

    public int updateModified(UserLikeArt userLikeArt);

    /**
     * 删除
     * @return
     */
    public int deletedById(Long id);

}
