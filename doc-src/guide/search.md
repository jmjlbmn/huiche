# 自定义筛选
`org.huiche.data.search.Search`, 自定义筛选,用于封装前端传递过来的筛选请求
默认实现了`org.huiche.data.query.Query`,已继承其条件拼装参数
## 获取筛选条件
Predicate get(); 返回筛选条件,需要自行实现
## 自定义筛选示例
```java
public class ArticleSearch implements Search{
    // 标题
    private String title;
    // 关键字
    private String word;
    // 发布时间
    private String startTime;
    // 发布时间
    private String endTime;
    
    @Override
    public Predicate get(){
        return predicates(
            predicate(title,QArticle.article.title::containsIgnoreCase),
            predicate(word,() -> or(
                QArticle.article.title.containsIgnoreCase(word),
                QArticle.article.title.containsIgnoreCase(word))),
            predicate(null != startTime && null != endTime,() -> or(
                QArticle.article.createTime.goe(startTime),
                QArticle.article.createTime.loe(endTime))));
    }
}
```
