package wfg.service;

import java.util.List;

/**
 *  redis 服务设计
 */
public interface RedisService {
    /**
     * 用户点赞某篇文章
     */
    void likeArticle(Long articleId,Long likeUserId,Long likePostId);

    /**
     * 取消点赞
     * @param articleId 文章id
     * @param likeUserId 被点赞用户id
     * @param likePostId 点赞用户
     */
    void unlikeArticle(Long articleId,Long likeUserId,Long likePostId );

    /**
     * 统计某篇文章点赞数
     */
    Long countArticleLike(Long articleId);

    /**
     * 统计用户总的文章点赞数
     */
    Long countUserLike(Long likedUserId);


    /**
     * 获取用户点赞的文章
     */

    List<Long> getUserLikeArticleIds(Long likedPostid);

}
