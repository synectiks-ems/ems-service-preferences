package com.synectiks.pref.dataimport.loader;

import java.util.Optional;

import org.dhatim.fastexcel.reader.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

import com.synectiks.pref.dataimport.AllRepositories;
import com.synectiks.pref.dataimport.DataLoader;
import com.synectiks.pref.domain.Branch;
import com.synectiks.pref.domain.City;
import com.synectiks.pref.domain.College;
import com.synectiks.pref.domain.State;
import com.synectiks.pref.exceptions.MandatoryFieldMissingException;
import com.synectiks.pref.service.util.CommonUtil;


public class BranchDataLoader extends DataLoader {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private AllRepositories allRepositories;
	private String sheetName;
	
	public BranchDataLoader(String sheetName, AllRepositories allRepositories) {
		super(sheetName, allRepositories);
		this.allRepositories = allRepositories;
		this.sheetName = sheetName;
	}
	
	@Override
	public <T> T getObject(Row row, Class<T> cls) throws InstantiationException, IllegalAccessException, MandatoryFieldMissingException {
		StringBuilder sb = new StringBuilder();	
		Branch obj = CommonUtil.createCopyProperties(cls.newInstance(), Branch.class);
		
		String branchName = row.getCellAsString(0).orElse(null);
		if(CommonUtil.isNullOrEmpty(branchName)) {
			sb.append("branch_name, ");
			logger.warn("Mandatory field missing. Field name - branch_name");
		}else {
			obj.setBranchName(branchName);
		}
		
		String address = row.getCellAsString(1).orElse(null);
		if(CommonUtil.isNullOrEmpty(address)) {
			sb.append("address, ");
			logger.warn("Mandatory field missing. Field name - address_1");
		}else {
			obj.setAddress(address);
		}
		
		
		String branchHead = row.getCellAsString(2).orElse(null);
		if(CommonUtil.isNullOrEmpty(branchHead)) {
			sb.append("branch_head, ");
			logger.warn("Mandatory field missing. Field name - branch_head");
		}else {
			obj.setBranchHead(branchHead);
		}
		
		String collegeName = row.getCellAsString(3).orElse(null);
		if(CommonUtil.isNullOrEmpty(collegeName)) {
			sb.append("college_id, ");
			logger.warn("Mandatory field missing. Field name - college_id");
		}else {
			College college = new College();
			college.setCollegeName(collegeName);
			Optional<College> c = this.allRepositories.findRepository("college").findOne(Example.of(college));
			if(c.isPresent()) {
				obj.setCollege(c.get());
			}else {
				sb.append("college_id, ");
				logger.warn("College not found. Given college name : "+collegeName);
			}
		}
		
		String stateName = row.getCellAsString(6).orElse(null);
		if(CommonUtil.isNullOrEmpty(stateName)) {
			sb.append("state_id, ");
			logger.warn("Mandatory field missing. Field name - state_id");
		}else {
			State state = new State();
			state.setStateName(stateName);
			Optional<State> c = this.allRepositories.findRepository("state").findOne(Example.of(state));
			if(c.isPresent()) {
				obj.setState(c.get());
			}else {
				sb.append("state_id, ");
				logger.warn("State not found. Given state name : "+stateName);
			}
		}
		
		String cityName = row.getCellAsString(5).orElse(null);
		if(CommonUtil.isNullOrEmpty(cityName)) {
			sb.append("city_id, ");
			logger.warn("Mandatory field missing. Field name - city_id");
		}else {
			City city = new City();
			city.setCityName(cityName);
			Optional<City> c = this.allRepositories.findRepository("city").findOne(Example.of(city));
			if(c.isPresent()) {
				obj.setCity(c.get());
			}else {
				sb.append("city_id, ");
				logger.warn("City not found. Given city name : "+cityName);
			}
		}
		if(sb.length() > 0) {
			String msg = "Field name - ";
			throw new MandatoryFieldMissingException(msg+sb.substring(0, sb.lastIndexOf(",")));
		}
		return (T)obj;
	}
	
}
