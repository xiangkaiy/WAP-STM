package com.stm.shorttermemployee.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.stm.shorttermemployee.pojo.Paper;

public class PaperListModel extends ListDataModel<Paper> implements SelectableDataModel<Paper> {
	public PaperListModel(List<Paper> questionList) {
		super(questionList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Paper getRowData(String rowKey) {
		List<Paper> list = (List<Paper>) getWrappedData();
		Long selectedId = Long.parseLong(rowKey);
		for (Paper u : list) {
			if (u.getId().equals(selectedId)) {
				return u;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(Paper u) {
		return u.getId();
	}

}
