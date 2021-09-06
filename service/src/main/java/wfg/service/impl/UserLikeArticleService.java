package wfg.service.impl;



import wfg.dao.UserLikeArt;
import wfg.mapper.UserLikeArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLikeArticleService implements wfg.service.UserLikeArticleService {

    @Autowired
    UserLikeArticleMapper userLikeArticleMapper;

    @Override
    public UserLikeArt selectByOne(UserLikeArt userLikeArt) {
        return userLikeArticleMapper.selectByOne(userLikeArt);
    }

    @Override
    public int insert(UserLikeArt userLikeArt) {
        return userLikeArticleMapper.insert(userLikeArt);
    }

    @Override
    public int deleted(Long id) {
        return userLikeArticleMapper.deletedById(id);
    }
}
