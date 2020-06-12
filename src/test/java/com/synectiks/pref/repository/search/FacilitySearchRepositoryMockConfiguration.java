package com.synectiks.pref.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of FacilitySearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class FacilitySearchRepositoryMockConfiguration {

    @MockBean
    private FacilitySearchRepository mockFacilitySearchRepository;

}
