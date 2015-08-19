package com.stm.shorttermemployee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.stm.shorttermemployee.dao.DepartmentDAO;
import com.stm.shorttermemployee.dao.PropertyDAO;
import com.stm.shorttermemployee.pojo.Department;
import com.stm.shorttermemployee.pojo.Property;
import com.stm.shorttermemployee.util.Constants;

@Component
public class DepartmentService {
	@Autowired
	private DepartmentDAO departDAO;

	@Autowired
	private PropertyDAO propertydao;

	@Transactional
	public Department getDepart(Long departId) {
		return (Department) departDAO.retrieve(Department.class, departId);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Department> getAllDepartment() {
		return (List<Department>) departDAO.retrieveAll(Department.class);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public int getReferenceScore() {
		List<Property> list = (List<Property>) propertydao.retrieveByField(Property.class, "name", Constants.PROPERTY_REFERENCESCORE);
		int result = 0;
		if (list != null && list.size() > 0) {
			result = Integer.parseInt(list.get(0).getValue());
		}
		return result;
	}

	@Transactional
	public void modifyReferenceScore(String newScore) {
		List<Property> list = (List<Property>) propertydao.retrieveByField(Property.class, "name", Constants.PROPERTY_REFERENCESCORE);
		if (list != null && list.size() > 0) {
			Property p = list.get(0);
			p.setValue(newScore);
			propertydao.update(p);
		}

	}
}
