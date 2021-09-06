package wfg.task;

import wfg.dao.Article;
import wfg.dao.UserLikeArt;
import wfg.service.ArticleService;
import wfg.service.UserLikeArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import redis.RedisCache;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;

/**
 * 定时任务
 * redis 同步到mysql
 */
public class ScheduleTask {

    private Logger logger = LoggerFactory.getLogger(ScheduleTask.class);

    DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    /**
     * 用户点赞文章key
     */
    @Value("${user.like.article.key}")
    private String USER_LIKE_ARTICLE_KEY;



    /**
     * 文章被点赞的key
     */
    @Value("${article.liked.user.key}")
    private String ARTICLE_LIKED_USER_KEY;
    /**
     *  文章被取消点赞
     */
    @Value("${user.unlike.article.key}")
    private String ARTICLE_UNLIKED_USER_KEY;
    @Resource
    private ArticleService articleService;

    @Resource
    private UserLikeArticleService userLikeArticleService;


    @Resource
    RedisCache redisCache;
    @Scheduled(cron="0 0 0/1 * * ?")
  public void  redisDataToMysql(){
    logger.info("开始执行数据持久化到Mysql 任务", LocalDateTime.now().format(formatter));
   //更新文章总的点赞数
     Map<String, Set<Long>> artLikeMap= redisCache.getCacheMap(ARTICLE_LIKED_USER_KEY);
     for(Map.Entry<String,Set<Long>> entry:artLikeMap.entrySet()){
         //开始同步点赞
        synchronizeTotalLikeCount(entry.getKey(), entry.getValue());

        synchronizeUserLikeArticle(entry.getKey(), entry.getValue());
     }

     //将取消点赞的状态进行同步
      Map<String,Set<Long>> artUnlikeMap = redisCache.getCacheMap(ARTICLE_UNLIKED_USER_KEY);
     for(Map.Entry<String,Set<Long>> entry:artUnlikeMap.entrySet()){
         synchronizeUserUnLikeArticle(entry.getKey(),entry.getValue());
     }


    }

    /**
     * 同步总的点赞数到数据库
     */
    private void synchronizeTotalLikeCount(String articleId,Set<Long> userIdSet ){
        Long count = Long.valueOf(userIdSet.size());
        Long articleIdL=Long.parseLong(articleId);
        Article article =buildArticle(count,articleIdL);
        articleService.updated(article);

    }
    /**
     * 同步点赞到数据库
     */
    private void synchronizeUserLikeArticle(String articleId,Set<Long> userIdSet ){
      for(Long userId: userIdSet){
         Long articleIdL = Long.parseLong(articleId);
        UserLikeArt userLikeArt = buildUserLikeArti(articleIdL,userId);
        if(userLikeArticleService.selectByOne(userLikeArt)==null){
            userLikeArticleService.insert(userLikeArt);
          }
      }
    }
    /**
     * 同步取消点赞到数据库
     */

    private void synchronizeUserUnLikeArticle(String userId,Set<Long> articleIdSet){
      for(Long articleId : articleIdSet){
          Long userIdL = Long.parseLong(userId);
          UserLikeArt userLikeArt = buildUserLikeArti(articleId,userIdL);
          UserLikeArt tmp=userLikeArticleService.selectByOne(userLikeArt);
          if(tmp!=null){
              userLikeArticleService.deleted( tmp.getId());
          }

      }


    }
    /**
     * 建造 article
     */
    private Article buildArticle(Long count,Long articleId){
        Article article = new Article();
        article.setId(articleId);
        article.setTotalLike(count);
        return article;
    }
    /**
     * 构造UserLikeArt对象
     *
     * @param articleId
     * @param userId
     * @return
     */
    private UserLikeArt buildUserLikeArti(Long articleId, Long userId) {
        UserLikeArt userLikeArticle = new UserLikeArt();
        userLikeArticle.setArticleId(articleId);
        userLikeArticle.setUserId(Long.valueOf(userId));
        return userLikeArticle;
    }

}
