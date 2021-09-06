package wfg.service;

import wfg.dao.Article;

public interface ArticleService{

    public int updated(Article article);

    public int deleted(Long id) ;


}
