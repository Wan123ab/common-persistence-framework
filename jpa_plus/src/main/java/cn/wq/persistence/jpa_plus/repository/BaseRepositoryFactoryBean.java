package cn.wq.persistence.jpa_plus.repository;

import cn.wq.persistence.jpa_plus.model.BaseEntity;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/7/9 11:23
 * @desc 自定义RepositoryFactoryBean，扩展原生的JpaRepository
 *
 * 这种配置方法可以避免因为方法名不匹配JPA规则而导致出现类似于方法名中的属性在实体类中找不到的异常
 * 如：No property executeQuery found for type McIdentifier!
 */
public class BaseRepositoryFactoryBean<Dao extends BaseRepository<T>, T extends BaseEntity> extends JpaRepositoryFactoryBean<Dao, T, Long> {
    /**
     * Creates a new {@link JpaRepositoryFactoryBean} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public BaseRepositoryFactoryBean(Class<? extends Dao> repositoryInterface) {
        super(repositoryInterface);
    }

    /**
     * Returns a {@link RepositoryFactorySupport}.
     *
     * @param entityManager
     */
    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new BaseRepositoryFactory(entityManager);
    }

    private static class BaseRepositoryFactory extends JpaRepositoryFactory{

        private final EntityManager em;

        /**
         * Creates a new {@link JpaRepositoryFactory}.
         *
         * @param em must not be {@literal null}
         */
        public BaseRepositoryFactory(EntityManager em) {
            super(em);
            this.em = em;
        }

        /**
         * Callback to create a JpaRepository instance with the given {@link EntityManager}
         *
         * @param information   will never be {@literal null}.
         * @param entityManager will never be {@literal null}.
         * @return
         */
        @Override
        protected JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
            return new BaseRepositoryImpl(information.getDomainType(), entityManager);
        }

        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return BaseRepositoryImpl.class;
        }
    }
}
 
