package wfg.service.impl;

import wfg.dao.Article;
import wfg.mapper.ArticleMapper;
import wfg.service.ArticleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ArticleServiceImpl implements ArticleService {

   @Resource
   ArticleMapper articleMapper;
    @Override
    public int deleted(Long id) {
        return articleMapper.deletedById(id);
    }


  @Override
  public int updated(Article article) {
    return articleMapper.updateModified(article);
  }
}
