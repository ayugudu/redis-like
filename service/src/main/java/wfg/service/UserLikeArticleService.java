package wfg.service;


import wfg.dao.UserLikeArt;

public interface UserLikeArticleService{
   /**
    * 按条件查询
    * @param userLikeArt
    * @return
    */
   UserLikeArt selectByOne(UserLikeArt userLikeArt);

   /**
    * 增加
    * @param userLikeArt
    * @return
    */
   int insert(UserLikeArt userLikeArt);

   /**
    * 删除
    * @param id
    * @return
    */

   int deleted(Long id);

}
