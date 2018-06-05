# HuiChe-Core
HuiChe 基础核心模块,仅依赖slf4j记录日志和jsr305用于静态分析(检查可能出现的错误)
- `consts` 内置的一些常量类
- `enums` 内置的一些枚举类及`值枚举`定义接口
- `exception` 内置的自定义异常及自定义错误定义接口
- `json` 内置的json转换接口,需要自己实现或使用starter时,会创建基于jackson的实现,直接注入即可使用
- `util` 内置的工具类
  - `Assert` 断言工具类,用于逻辑处理,抛出异常,经过全局异常处理,返回统一响应,很常用
  - `CaptchaUtil` 验证码工具类,提供获取6位或指定位数的数字验证码工具类
  - `CheckUtil` 校验工具类
  - `ConstUtil` 常量工具类
  - `DataUtil` 提供数据转换,复制等处理
  - `EnumUtil` 枚举工具类
  - `HuiCheUtil` 封装了一些基础判断方法,如equals和是否空值等判断
  - `StreamUtil` 扩展stream,目前仅分组收集器toList转换类型的扩展
  - `StringUtil` 字符串处理工具类,很常用