package com.stm.shorttermemployee.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class WorkTimeListModel extends ListDataModel<WorkTimeView> implements SelectableDataModel<WorkTimeView> {
	public WorkTimeListModel(List<WorkTimeView> worktimeList) {
		super(worktimeList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public WorkTimeView getRowData(String rowKey) {
		List<WorkTimeView> list = (List<WorkTimeView>) getWrappedData();
		for (WorkTimeView w : list) {
			if (w.getWorktime().getId().equals(Long.parseLong(rowKey))) {
				return w;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(WorkTimeView arg0) {
		return arg0.getWorktime().getId();
	}

}
