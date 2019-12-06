###SQL通用工具类

一、设计初衷：更好地以面向对象的方式操作sql，包括常用的增删改查操作，支持较为  
复杂查询，如子查询、连接查询、exists、union、having、group by、sql函数等操作。

二、如果是非常复杂的sql操作，比如很复杂的sql查询，那么仍然建议使用原生sql  
进行操作，利于后期维护、优化和升级。

###开发计划
一、第一个版本（version 1.0）  2019-7-16 ~ 2019-8-15  
1、将字符操作和lamda操作分离成2套API，目前是混在一起，造成链式调用混乱不可控。   
2、支持常见单表查询操作  
3、支持多表复杂查询  
（1）子查询  
（2）join查询  
（3）where多条件查询，支持and、or条件嵌套  
（4）支持group by  
（5）支持having操作  
（6）支持order by    
（7）查询条件支持动态sql（默认判断是否为null或empty，可自定义判断器）  
（8）支持部分sql函数操作  
（9）支持exists操作  

二、第二个版本（version 2.0）  2019-8-16 ~ 2019-9-15  
1、支持增删改操作  
2、支持其他sql操作，如创建索引，建表，修改列，创建数据库用户，授权等等。  
3、代码重用优化、引入合适的设计模式  
4、使用内部类进行约束，规范链式调用操作  


2019-7-23 更新日志  
1、支持having操作（lamda只支持一个条件，字符操作支持多个）

2019-8-2 更新日志   
1、原生SQL和HQL操作支持in占位符，并标明使用注意事项      
2、修复因为没有标注@Transactional导致调用Query#executeUpdate() 
执行报错（提示需要事务）的bug        
3、修复RS中字段多于或少于自定义VO导致转换失败的bug

2019-8-3 更新日志       
1、新增抽象基类Model，封装了常用单表CRUD以及批量删除、保存、查询所有       
、分页查询、根据条件查询、更新、删除等操作。用以支持AR（Active Record）模式，      
实体类只需要继承Model即可调用这些方法。      
2、AbstractEntity基类现已继承Model，因此实体继承AbstractEntity即可！         
``java      

    @Data
    public abstract class Model<T extends Model, ID> implements Serializable {
    
        public boolean save() throws Exception {
    
            getBaseDaoJpa().save(this);
            return true;
        }
    
        public boolean update() throws Exception {
    
            getBaseDaoJpa().update(this);
            return true;
        }
    }
``

2019-8-5 更新日志       
1、BaseDaoJpaImpl新增EntityManagerFactory，用于创建     
应用托管的EntityManager，便于手动控制事务。        
2、支持设置batch_size进行批量保存、更新操作（原生EntityManager     
既没提供批量保存，也没提供根据batch_size进行批量更新同步的API）
``java

    @Repository
    public class BaseDaoJpaImpl<ID> implements BaseDao<ID> {
    
        /**
         * 注入容器托管的EntityManager，事务需要配合@Transactional使用，不能手动控制
         */
        @PersistenceContext
        private EntityManager entityManager;
    
        /**
         * 注入容器托管的EntityManagerFactory，可用于创建应用托管的EntityManager
         * 需要手动创建、开启、提交和回滚事务
         */
        @PersistenceUnit
        private EntityManagerFactory entityManagerFactory;
``

2019-8-7 更新日志
1、新增根据实体类和sql操作类型构建增删改SQLWrapper的方法           
SqlBuilderUtils.buildSql(T, SqlType)。
2、现在通用dao接口BaseDao的save和update方法已默认通过（1）构建，           
然后调用executeSql去执行。其实现类可直接继承或者进行重写。      
之所以这么设计是为了增加扩展性。目前BaseDao通过JPA实现，提供了默认      
save和update方法，但是其他的持久层框架并未提供相应实现。
3、修改BaseDao和其实现类中的部分泛型T必须继承Model。      
4、分页查询和批量查询接口新增排序入参数组，默认按照id升序排列。        
5、删除SQL中分页功能，因为不同DB分页方言不一样。分页功能改为          
由各持久层框架实现。    
``java

    /**
     * 插入指定的持久化对象
     *
     * @param t 实体对象
     * @throws Exception sql错误抛出异常
     */
    default <T extends Model> void save(T t) throws Exception {
        SQLWrapper sqlWrapper = SqlBuilderUtils.buildSql(t, SqlType.INSERT);

        executeSql(sqlWrapper.getSql(), sqlWrapper.getParams());
    }

    /**
     * 修改指定的持久化对象
     *
     * @param t 实体对象
     * @throws Exception sql错误抛出异常
     */
    default <T extends Model> void update(T t) throws Exception{
        SQLWrapper sqlWrapper = SqlBuilderUtils.buildSql(t, SqlType.UPDATE);

        executeSql(sqlWrapper.getSql(), sqlWrapper.getParams());
    }
    

``

截止到2019-8-7 下午7:51，jpa-test项目已能满足单表CRUD和较复杂       
连表操作的需求，用于一般的中小型WEB项目基本不会有问题。       
同时在开发的过程中，不断地加入了一些新的功能，比如AR          
模式、批量作业时事务操作、集成JPA等等，并在扩展性方面做了一些改进，     
除了JPA外也可以集成其他流行的持久层框架。由于时间精力的原因，后续      
短时间内不打算升级2.0版本，只会在现有的基础上做些bug修复、代码调整        
和新增简单功能的工作。


