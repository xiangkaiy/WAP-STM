package com.stm.shorttermemployee.model;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.stm.shorttermemployee.pojo.SteLeave;

public class SteLeaveModel extends ListDataModel<SteLeave> implements SelectableDataModel<SteLeave> {

	public SteLeaveModel(List<SteLeave> handledLeaves) {
		super(handledLeaves);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SteLeave getRowData(String rowKey) {
		List<SteLeave> list = (List<SteLeave>) getWrappedData();
		Long selectedId = Long.parseLong(rowKey);
		for (SteLeave s : list) {
			if (s.getId().equals(selectedId)) {
				return s;
			}
		}
		return null;
	}

	@Override
	public Object getRowKey(SteLeave s) {
		return s.getId();
	}

}
