/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.unifiedpush.jpa.dao.impl;

import org.jboss.aerogear.unifiedpush.api.Variant;
import org.jboss.aerogear.unifiedpush.dao.VariantDao;
import org.jboss.aerogear.unifiedpush.jpa.interceptor.JpaOperation;
import org.jboss.aerogear.unifiedpush.jpa.dao.impl.helper.JPATransformHelper;
import org.jboss.aerogear.unifiedpush.model.jpa.AbstractVariantEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class JPAVariantDao extends JPABaseDao implements VariantDao {


    @Override
    public void create(final Variant variant) {
        AbstractVariantEntity entity = JPATransformHelper.toEntity(variant);

        persist(entity);
    }

    @Override
    public void update(final Variant variant) {
        AbstractVariantEntity entity = JPATransformHelper.toEntity(variant);

        merge(entity);
    }

    @Override
    public void delete(final Variant variant) {
        final JpaOperation<Void> deleteVariantOperation = new JpaOperation<Void>() {
            @Override
            public Void perform(final EntityManager entityManager) {
                AbstractVariantEntity entity = entityManager.find(AbstractVariantEntity.class, variant.getId());

                if (entity != null) {
                    entityManager.remove(entity);
                }

                return null;
            }
        };

        jpaExecutor.execute(deleteVariantOperation);
    }


    @Override
    public Variant findByVariantID(final String variantID) {
        final JpaOperation<Variant> queryOperation = new JpaOperation<Variant>() {
            @Override
            public Variant perform(final EntityManager entityManager) {

                AbstractVariantEntity entity = getSingleResultForQuery(entityManager.createQuery("select t from " + AbstractVariantEntity.class.getSimpleName() + " t where t.variantID = :variantID")
                        .setParameter("variantID", variantID));

                return JPATransformHelper.fromEntity(entity);
            }
        };

        return jpaExecutor.execute(queryOperation);
    }

    @Override
    public Variant findByVariantIDForDeveloper(final String variantID, final String loginName) {
        final JpaOperation<Variant> queryOperation = new JpaOperation<Variant>() {
            @Override
            public Variant perform(final EntityManager entityManager) {

                AbstractVariantEntity entity = getSingleResultForQuery(entityManager.createQuery("select t from " + AbstractVariantEntity.class.getSimpleName() + " t where t.variantID = :variantID and t.developer = :developer")
                        .setParameter("variantID", variantID)
                        .setParameter("developer", loginName));

                return JPATransformHelper.fromEntity(entity);
            }
        };

        return jpaExecutor.execute(queryOperation);
    }

    @Override
    public Variant find(final String id) {

        final JpaOperation<AbstractVariantEntity> findVariantOperation = new JpaOperation<AbstractVariantEntity>() {
            @Override
            public AbstractVariantEntity perform(final EntityManager entityManager) {
                AbstractVariantEntity entity = entityManager.find(AbstractVariantEntity.class, id);
                return entity;
            }
        };

        AbstractVariantEntity entity = jpaExecutor.execute(findVariantOperation);
        return JPATransformHelper.fromEntity(entity);
    }

    private AbstractVariantEntity getSingleResultForQuery(Query query) {
        List<AbstractVariantEntity> result = query.getResultList();

        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }
}
