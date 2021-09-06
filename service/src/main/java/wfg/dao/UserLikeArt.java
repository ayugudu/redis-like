package wfg.dao;

import lombok.Data;

/**
 * 用户点赞文章
 */
@Data
public class UserLikeArt extends BaseBo {

    private Long id;
    /**
     * 用户
     */

    private  Long userId;

    /**
     * 文章
     */
    private Long articleId;


}
