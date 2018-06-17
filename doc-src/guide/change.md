# 更新历史
huiche的版本会参照`MajorVersion.MinorVersion.Revision(主要版本.次要版本.修订版本)` 风格进行版本发布,但前期可能仅会变更`Revision`(包括新增功能的版本),后续有规范的打算.另外
## 1.0.4 <span style="font-size:0.6em;">2018-06-17</span>
- 新增: 文档和使用指南 [huiche.org](http://huiche.org)
- 新增: 数据迁移工具 `huiche-transfer`, 作为扩展模块
- 新增: 建表工具关键字`user`
- 更新: spring-boot 2.0.3
- 更新: jetbrains-annotations 16.0.2
## 1.0.3 <span style="font-size:0.6em;">2018-06-10</span>
- 新增: StreamUtil,扩展stream的groupby `org.huiche.core.util.StreamUtil`
- 改进: Query新增扩展列和排除列方法 `org.huiche.data.query.Query`
- 完善: javadoc
## 1.0.2 <span style="font-size:0.6em;">2018-06-03</span>
- 修复: BaseCrudDao的`exists`问题
- 更新: druid 1.1.10
## 1.0.1 <span style="font-size:0.6em;">2018-05-27</span>
- 修复: 全局异常捕获,错误堆栈打印问题 `org.huiche.config.ErrorHandler`
- 改进: BaseCrudDao `org.huiche.dao.BaseCrudDao`
- 改进: Query `org.huiche.data.query.Query`
## 1.0.0 <span style="font-size:0.6em;">2018-05-21</span>
- 初始版本