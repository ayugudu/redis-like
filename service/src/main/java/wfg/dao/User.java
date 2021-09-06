package wfg.dao;

import lombok.Data;

/**
 * 用户实体
 */
@Data
public class User  extends BaseBo {
    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;
}
