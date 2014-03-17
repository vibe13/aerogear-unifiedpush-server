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

import org.jboss.aerogear.unifiedpush.api.PushApplication;
import org.jboss.aerogear.unifiedpush.dao.PushApplicationDao;
import org.jboss.aerogear.unifiedpush.jpa.interceptor.JpaOperation;
import org.jboss.aerogear.unifiedpush.jpa.dao.impl.helper.JPATransformHelper;
import org.jboss.aerogear.unifiedpush.model.jpa.PushApplicationEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class JPAPushApplicationDao extends JPABaseDao implements PushApplicationDao {

    @Override
    public void create(PushApplication pushApplication) {
        PushApplicationEntity entity = JPATransformHelper.toEntity(pushApplication);

        persist(entity);
    }

    @Override
    public void update(PushApplication pushApplication) {
        PushApplicationEntity entity = JPATransformHelper.toEntity(pushApplication);

        merge(entity);
    }

    @Override
    public void delete(final PushApplication pushApplication) {
        final JpaOperation<Void> deleteVariantOperation = new JpaOperation<Void>() {
            @Override
            public Void perform(final EntityManager entityManager) {
                PushApplicationEntity entity = entityManager.find(PushApplicationEntity.class, pushApplication.getId());

                if (entity != null) {
                    entityManager.remove(entity);
                }

                return null;
            }
        };

        jpaExecutor.execute(deleteVariantOperation);
    }

    @Override
    public List<PushApplication> findAllForDeveloper(final String loginName) {

        final JpaOperation<List<PushApplication>> queryOperation = new JpaOperation<List<PushApplication>>() {
            @Override
            public List<PushApplication> perform(final EntityManager entityManager) {

                List<PushApplicationEntity> entities = entityManager.createQuery("select pa from " + PushApplicationEntity.class.getSimpleName() + " pa where pa.developer = :developer")
                        .setParameter("developer", loginName).getResultList();

                return JPATransformHelper.fromPushApplicationEntityCollection(entities);
            }
        };

        return jpaExecutor.execute(queryOperation);
    }

    @Override
    public PushApplication findByPushApplicationIDForDeveloper(final String pushApplicationID, final String loginName) {

        final JpaOperation<PushApplication> queryOperation = new JpaOperation<PushApplication>() {
            @Override
            public PushApplication perform(final EntityManager entityManager) {

                PushApplicationEntity entity = getSingleResultForQuery(entityManager.createQuery(
                        "select pa from " + PushApplicationEntity.class.getSimpleName() + " pa where pa.pushApplicationID = :pushApplicationID and pa.developer = :developer")
                        .setParameter("pushApplicationID", pushApplicationID)
                        .setParameter("developer", loginName));

                return JPATransformHelper.fromEntity(entity);
            }
        };

        return jpaExecutor.execute(queryOperation);
    }

    @Override
    public PushApplication findByPushApplicationID(final String pushApplicationID) {

        final JpaOperation<PushApplication> queryOperation = new JpaOperation<PushApplication>() {
            @Override
            public PushApplication perform(final EntityManager entityManager) {

                PushApplicationEntity entity = getSingleResultForQuery(entityManager.createQuery("select pa from " + PushApplicationEntity.class.getSimpleName() + " pa where pa.pushApplicationID = :pushApplicationID")
                        .setParameter("pushApplicationID", pushApplicationID));

                return JPATransformHelper.fromEntity(entity);
            }
        };

        return jpaExecutor.execute(queryOperation);
    }

    @Override
    public PushApplication find(final String id) {
        final JpaOperation<PushApplicationEntity> findVariantOperation = new JpaOperation<PushApplicationEntity>() {
            @Override
            public PushApplicationEntity perform(final EntityManager entityManager) {
                PushApplicationEntity entity = entityManager.find(PushApplicationEntity.class, id);
                return entity;
            }
        };

        PushApplicationEntity entity = jpaExecutor.execute(findVariantOperation);
        return JPATransformHelper.fromEntity(entity);
    }

    private PushApplicationEntity getSingleResultForQuery(Query query) {
        List<PushApplicationEntity> result = query.getResultList();

        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }
}
