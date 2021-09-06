package wfg.dao;

import lombok.Data;

import java.sql.Blob;

/**
 * 文章实体
 */
@Data
public class Article  extends BaseBo {
    private Long id;

    /**
     * 用户
     */
    private Long userId;
    /**
     * 文章内容
     */
    private Blob  content;
    /**
     *文章名字
     */

    private String articleName;

    /**
     *总的被点赞数
     */

    private Long totalLike;



}

