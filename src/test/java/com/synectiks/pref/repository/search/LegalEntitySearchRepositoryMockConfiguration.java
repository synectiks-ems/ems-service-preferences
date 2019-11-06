package com.synectiks.pref.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link LegalEntitySearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class LegalEntitySearchRepositoryMockConfiguration {

    @MockBean
    private LegalEntitySearchRepository mockLegalEntitySearchRepository;

}
