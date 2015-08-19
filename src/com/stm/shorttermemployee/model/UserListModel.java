package com.stm.shorttermemployee.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.stm.shorttermemployee.pojo.User;

public class UserListModel extends ListDataModel<User> implements SelectableDataModel<User>{
	public UserListModel(List<User> userList){
		super(userList);
	}


	@SuppressWarnings("unchecked")
	@Override
	public User getRowData(String rowKey) {
		List<User> list = (List<User>)getWrappedData();
		long selectedId = Long.parseLong(rowKey);
		for(User u:list){
			if(u.getId() == selectedId){
				return u;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(User u) {
		return u.getId();
	}

}
