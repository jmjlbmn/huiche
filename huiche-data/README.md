# HuiChe-Data
HuiChe 数据(实体类)模块
- `entity` 内置的基础实体类,Long类型主键,String类型的创建和修改时间
- `page` 用于分页查询的请求和响应类
- `query` 数据查询接口提供条件拼接和查询列处理等默认方法
- `search` 自定义数据筛选接口,用于后台管理前端传递的数据筛选请求
- `validation` 用于实体类创建和更新时进行验证,目前仅`ValidOnlyCreate`接口,用于标记仅在创建时进行验证