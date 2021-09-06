package wfg.service.impl;


import wfg.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.RedisCache;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RedisServiceImpl implements RedisService {

    /**
     *  用户点赞文章
     */
    @Value("${user.like.article.key}")
    private String USER_Like_ARTICLES_KEY;


    /**
     * 文章被点赞
     */
    @Value("${article.liked.user.key}")
    private String ARTICLE_LIKED_USER_KEY;

    /**
     *  文章被取消点赞
     */
    @Value("${user.unlike.article.key}")
    private String ARTICLE_UNLIKED_USER_KEY;

    /**
     *  用户总的点赞数
     */
    @Value("${total.like.count.key}")
    private String TOTAL_LIKE_COUNT_KEY;

    @Resource
    RedisCache redisCache;

    Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);
    @Override

    /**
     * 用户点赞
     */
    public void likeArticle(Long articleId, Long likeUserId, Long likePostId) {
        validateParam(articleId,likePostId,likeUserId);
        logger.info("点赞数据存入redis开始，articleId:{}，likedUserId:{}，likedPostId:{}", articleId, likeUserId, likePostId);
        //只有未点赞的用户 才可以
        likeArticleLogicValidate(articleId,likeUserId,likePostId);
        //被点赞用户总点赞数+1
        redisCache.setCacheMapIncrement(TOTAL_LIKE_COUNT_KEY,String.valueOf(likeUserId));
        synchronized (this){
         //用户喜欢的文章+1
            Set<Long> userLikeResult = (Set<Long>) redisCache.getCacheMapKey(USER_Like_ARTICLES_KEY,String.valueOf(likePostId));
            userLikeResult=  userLikeResult == null ? new HashSet<>() : userLikeResult;
            userLikeResult.add(articleId);
            redisCache.setCacheMapKey(USER_Like_ARTICLES_KEY,String.valueOf(likePostId),userLikeResult);
        //文章点赞数+1
            Set<Long> articleLikeResult = (Set<Long>) redisCache.getCacheMapKey(ARTICLE_LIKED_USER_KEY,String.valueOf(articleId));
            articleLikeResult= articleLikeResult==null? new HashSet<>(): articleLikeResult;
            articleLikeResult.add(likePostId);
            redisCache.setCacheMapKey(ARTICLE_LIKED_USER_KEY,String.valueOf(articleId),articleLikeResult);
        logger.info("点赞数据存入redis结束，articleId:{}，likedUserId:{}，likedPostId:{}", articleId, likeUserId, likePostId);
        }

    }

    /**
     * 取消点赞
     * @param articleId 文章id
     * @param likeUserId 被点赞用户id
     * @param likePostId 点赞用户
     */
    @Override
    public void unlikeArticle(Long articleId, Long likeUserId, Long likePostId) {
        validateParam(articleId,likePostId,likeUserId);
        logger.info("取消用户点赞开始");
        unlikeArticleLogicValidate(articleId, likeUserId, likePostId);
        synchronized (this){
        //被点赞用户 总的点赞数-1
        Long count= (Long) redisCache.getCacheMapKey(TOTAL_LIKE_COUNT_KEY,likeUserId);
        redisCache.setCacheMapKey(TOTAL_LIKE_COUNT_KEY,String.valueOf(likeUserId),String.valueOf(--count));
        // 取消用户对该文章的点赞
       Set<Long> userLikeResult= (Set<Long>) redisCache.getCacheMapKey(USER_Like_ARTICLES_KEY,likePostId);
       userLikeResult.remove(articleId);
       redisCache.setCacheMapKey(USER_Like_ARTICLES_KEY,articleId,userLikeResult);


       // 文章点赞数-1
       Set<Long> articleResult = (Set<Long>) redisCache.getCacheMapKey(ARTICLE_LIKED_USER_KEY,articleId);
        articleResult.remove(likePostId);
        redisCache.setCacheMapKey(ARTICLE_LIKED_USER_KEY,articleId,articleResult);
        
            //记录用户取消点赞的状态
            Set<Long> unlikeSet= (Set<Long>) redisCache.getCacheMapKey(ARTICLE_UNLIKED_USER_KEY,articleId);
            unlikeSet= unlikeSet==null? new HashSet<>():unlikeSet;
            unlikeSet.add(likePostId);
            redisCache.setCacheMapKey(ARTICLE_LIKED_USER_KEY,articleId,unlikeSet);
    }
        logger.info("取消点赞数据存入redis结束，articleId:{}，likedUserId:{}，likedPostId:{}", articleId, likeUserId, likePostId);
    }

    /**
     * 统计某篇文章的总点赞数
     * @param articleId
     * @return
     */
    @Override
    public Long countArticleLike(Long articleId) {
      validateParam(articleId);
      Set<Long> articleResult= (Set<Long>) redisCache.getCacheMapKey(ARTICLE_LIKED_USER_KEY,articleId);
      return articleResult==null?0l: articleResult.size();

    }
    /**
     * 统计用户总的文章点赞数
     *
     * @param likedUserId
     * @return
     */
    @Override
    public Long countUserLike(Long likedUserId) {
        validateParam(likedUserId);
        return (Long) redisCache.getCacheMapKey(TOTAL_LIKE_COUNT_KEY,likedUserId);
    }
    /**
     * 获取用户点赞的文章
     *
     * @param likedPostId
     * @return
     */
    @Override
    public List<Long> getUserLikeArticleIds(Long likedPostId) {
        validateParam(likedPostId);
         Set<Long> articleIdResult = (Set<Long>) redisCache.getCacheMapKey(USER_Like_ARTICLES_KEY,likedPostId);
        return articleIdResult.stream().collect(Collectors.toList());
    }

    /**
     * 点赞逻辑校验
     * @param articleId
     * @param likedUserId
     * @param likePostId
     */
    private void likeArticleLogicValidate(Long articleId,Long likedUserId,Long likePostId){
        Set<Long>  articleSet = (Set<Long>) redisCache.getCacheMapKey(USER_Like_ARTICLES_KEY,String.valueOf(likePostId));

        Set<Long> userIdSet = (Set<Long>) redisCache.getCacheMapKey( ARTICLE_LIKED_USER_KEY,String.valueOf(articleId));

        if(articleSet==null){
            return;
        }
        if(userIdSet==null){
            return;
        }
        else {
            if (articleSet.contains(articleId) || userIdSet.contains(likedUserId)) {
                logger.error("该文章已被当前用户点赞，重复点赞，articleId:{}，likedUserId:{}，likedPostId:{}", articleId, likedUserId, likePostId);
            }
        }




    }

/**
 * 取消点赞文章校验
 */

private void unlikeArticleLogicValidate(Long articleId,Long likeUserId,Long likePostId){

    Set<Long> userLikeResult= (Set<Long>) redisCache.getCacheMapKey(USER_Like_ARTICLES_KEY,likePostId);

    Set<Long> articleLikeResult= (Set<Long>) redisCache.getCacheMapKey(ARTICLE_LIKED_USER_KEY,articleId);
  if(userLikeResult==null || !userLikeResult.contains(articleId)
  || articleLikeResult==null|| !articleLikeResult.contains(likePostId)){
      logger.error("该文章未被当前用户点赞，不可以进行取消点赞操作");

  }


}

    /**
     * 参数校验
     */
private  void validateParam(Long... params){
    for(Long param : params){
        if(null == param){
            logger.error("如参存在null值");
        }
    }


}

}
