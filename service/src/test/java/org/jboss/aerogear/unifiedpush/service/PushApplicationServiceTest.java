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
package org.jboss.aerogear.unifiedpush.service;

import org.jboss.aerogear.unifiedpush.api.PushApplication;
import org.jboss.aerogear.unifiedpush.service.impl.PushApplicationServiceImpl;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.UUID;

public class PushApplicationServiceTest {

    private final PushApplicationService pushApplicationService = new PushApplicationServiceImpl();

    @Test
    public void addPushApplication() {
        PushApplication pa = new PushApplication();
        pa.setName("PushApplication");
        final String uuid = UUID.randomUUID().toString();
        pa.setPushApplicationID(uuid);

        pushApplicationService.addPushApplication(pa);

        PushApplication stored = pushApplicationService.findByPushApplicationID(uuid);
        assertNotNull(stored);
        assertNotNull(stored.getId());
        assertEquals(pa.getName(), stored.getName());
        assertEquals(pa.getPushApplicationID(), stored.getPushApplicationID());
    }

    @Test
    public void updatePushApplication() {
        PushApplication pa = new PushApplication();
        pa.setName("PushApplication");
        final String uuid = UUID.randomUUID().toString();
        pa.setPushApplicationID(uuid);

        pushApplicationService.addPushApplication(pa);

        PushApplication stored = pushApplicationService.findByPushApplicationID(uuid);
        assertNotNull(stored);

        stored.setName("FOO");
        pushApplicationService.updatePushApplication(stored);
        stored = pushApplicationService.findByPushApplicationID(uuid);
        assertEquals("FOO", stored.getName());
    }

    @Test
    public void findByPushApplicationID() {
        PushApplication pa = new PushApplication();
        pa.setName("PushApplication");
        final String uuid = UUID.randomUUID().toString();
        pa.setPushApplicationID(uuid);

        pushApplicationService.addPushApplication(pa);

        PushApplication stored = pushApplicationService.findByPushApplicationID(uuid);
        assertNotNull(stored);
        assertNotNull(stored.getId());
        assertEquals(pa.getName(), stored.getName());
        assertEquals(pa.getPushApplicationID(), stored.getPushApplicationID());

        stored = pushApplicationService.findByPushApplicationID("123");
        assertNull(stored);

    }

    @Test
    public void findAllPushApplicationsForDeveloper() {

        assertTrue(pushApplicationService.findAllPushApplicationsForDeveloper("admin").isEmpty());

        PushApplication pa = new PushApplication();
        pa.setName("PushApplication");
        final String uuid = UUID.randomUUID().toString();
        pa.setPushApplicationID(uuid);
        pa.setDeveloper("admin");

        pushApplicationService.addPushApplication(pa);

        assertFalse(pushApplicationService.findAllPushApplicationsForDeveloper("admin").isEmpty());
        assertEquals(1, pushApplicationService.findAllPushApplicationsForDeveloper("admin").size());
    }

    @Test
    public void removePushApplication() {
        PushApplication pa = new PushApplication();
        pa.setName("PushApplication");
        final String uuid = UUID.randomUUID().toString();
        pa.setPushApplicationID(uuid);
        pa.setDeveloper("admin");

        pushApplicationService.addPushApplication(pa);

        assertFalse(pushApplicationService.findAllPushApplicationsForDeveloper("admin").isEmpty());
        assertEquals(1, pushApplicationService.findAllPushApplicationsForDeveloper("admin").size());

        pushApplicationService.removePushApplication(pa);

        assertTrue(pushApplicationService.findAllPushApplicationsForDeveloper("admin").isEmpty());
        assertNull(pushApplicationService.findByPushApplicationID(uuid));
    }

    @Test
    public void findByPushApplicationIDForDeveloper() {
        PushApplication pa = new PushApplication();
        pa.setName("PushApplication");
        final String uuid = UUID.randomUUID().toString();
        pa.setPushApplicationID(uuid);
        pa.setDeveloper("admin");

        pushApplicationService.addPushApplication(pa);

        PushApplication queried =  pushApplicationService.findByPushApplicationIDForDeveloper(uuid, "admin");
        assertNotNull(queried);
        assertEquals(uuid, queried.getPushApplicationID());

        assertNull(pushApplicationService.findByPushApplicationIDForDeveloper(uuid, "admin2"));
        assertNull(pushApplicationService.findByPushApplicationIDForDeveloper("123-3421", "admin"));
    }
}
