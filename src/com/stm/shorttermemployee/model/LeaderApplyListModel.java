package com.stm.shorttermemployee.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

public class LeaderApplyListModel extends ListDataModel<LeaderApplyView> implements SelectableDataModel<LeaderApplyView>{

	public LeaderApplyListModel(List<LeaderApplyView> baseApplyList) {
		super(baseApplyList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public LeaderApplyView getRowData(String rowKey) {
		List<LeaderApplyView> list=  (List<LeaderApplyView>) getWrappedData();
		Long selectId = Long.parseLong(rowKey);
		for(LeaderApplyView e:list){
			if(e.getApply().getId().equals(selectId)){
				return e;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(LeaderApplyView arg0) {
		return arg0.getApply().getId();
	}

}
