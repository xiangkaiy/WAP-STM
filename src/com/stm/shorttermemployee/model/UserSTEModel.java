package com.stm.shorttermemployee.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class UserSTEModel extends ListDataModel<UserSTEView> implements SelectableDataModel<UserSTEView> {
	public UserSTEModel(List<UserSTEView> userList) {
		super(userList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserSTEView getRowData(String rowKey) {
		List<UserSTEView> list = (List<UserSTEView>) getWrappedData();
		long selectedId = Long.parseLong(rowKey);
		for (UserSTEView u : list) {
			if (u.getUser().getId() == selectedId) {
				return u;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(UserSTEView u) {
		return u.getUser().getId();
	}

}