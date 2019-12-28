package com.synectiks.pref.graphql.resolvers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.synectiks.pref.business.service.CmsBranchService;
import com.synectiks.pref.business.service.CmsCityService;
import com.synectiks.pref.business.service.CmsStateService;
import com.synectiks.pref.domain.City;
import com.synectiks.pref.domain.State;
import com.synectiks.pref.domain.vo.CmsBranchVo;
import com.synectiks.pref.repository.UserPreferenceRepository;

/**
 * Query Resolver for preference queries
 *
 */
@Component
public class Query implements GraphQLQueryResolver {

	private final static Logger logger = LoggerFactory.getLogger(Query.class);
    
	private UserPreferenceRepository userPreferenceRepository;
    
	@Autowired
	CmsBranchService cmsBranchService;
	
	@Autowired
	CmsStateService cmsStateService;
	
	@Autowired
	CmsCityService cmsCityService;
	
	public Query(UserPreferenceRepository userPreferenceRepository) {
		this.userPreferenceRepository = userPreferenceRepository;
	}
    
	public List<CmsBranchVo> getBranchList() throws Exception {
    	logger.debug("Query - getBranchList :");
    	return this.cmsBranchService.getBranchList();
    }
	
	public List<State> getStateList() throws Exception {
    	logger.debug("Query - getStateList :");
    	return this.cmsStateService.getStateList();
    }
	
	public List<City> getCityList() throws Exception {
    	logger.debug("Query - getCityList :");
    	return this.cmsCityService.getCityList();
    }
}
