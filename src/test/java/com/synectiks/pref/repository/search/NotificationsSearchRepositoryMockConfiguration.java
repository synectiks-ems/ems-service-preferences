package com.synectiks.pref.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of NotificationsSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class NotificationsSearchRepositoryMockConfiguration {

    @MockBean
    private NotificationsSearchRepository mockNotificationsSearchRepository;

}
