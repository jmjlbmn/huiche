# 更新历史
huiche的版本会参照`MajorVersion.MinorVersion.Revision(主要版本.次要版本.修订版本)` 风格进行版本发布,但前期可能仅会变更`Revision`(包括新增功能的版本),后续有规范的打算
## 1.0.7 <span style="font-size:0.6em;">2018-09-16</span>
- 新增: Sql语句格式化功能
- 新增: LogUtil,方便日志输入异常堆栈
- 新增: ValidationUtil,方便手动进行验证
- 新增: 部分常用插件的pluginManagement
- 新增: update新增设置null或基于字段本身的值进行更新的方法
- 改进: 合并create方法和creates方法
- 改进: 建表工具重构,改善扫描实体类的速度和兼容性,调整主键字段不进行任何更新
- 更新: spring-boot 2.0.5
- 移除: 默认service的部分非必要方法,传统Post风格的Crud控制器
## 1.0.6 <span style="font-size:0.6em;">2018-08-01</span>
- 新增: PasswordEncoder 接口,封装密码加密,starter里面提供默认实现
- 修复: HuiCheExcetion个别构造的code丢失问题
- 改进: StringUtil.json() 方法
- 改进: 合并updates到update方法避免歧义
- 改进: 条件拼装or方法参数为可变数组 不再限定只能2个条件
- 改进: 建表工具,移出小数类型的默认长度
- 改进: 建表工具,Java float类型 映射为 数据库Double类型
- 更新: spring-boot 2.0.4
- 移除: DateUtil 时间字符串转LocalDateTime方法
## 1.0.5 <span style="font-size:0.6em;">2018-06-25</span>
- 改进: web模块预置的 `restful` 风格的增删改查接口
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