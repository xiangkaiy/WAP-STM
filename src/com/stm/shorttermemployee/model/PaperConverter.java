package com.stm.shorttermemployee.model;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.stm.shorttermemployee.pojo.Paper;
import com.stm.shorttermemployee.service.TrainService;

@ManagedBean(name = "paperConverter")
@Scope("session")
@Controller
public class PaperConverter implements Converter {
	public List<Paper> paperList = null;

	@Autowired
	private TrainService trainservice;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String submittedValue) {
		if (submittedValue.trim().equals("")) {
			return null;
		} else {
			try {
				long number = Long.parseLong(submittedValue);
				if (paperList == null) {
					paperList = trainservice.getPaperList();
				}

				for (Paper s : paperList) {
					if (s.getId() == number) {
						return s;
					}
				}

			} catch (NumberFormatException exception) {
				throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid player"));
			}
		}

		return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if (value == null || value.equals("")) {
			return "";
		} else {
			return String.valueOf(((Paper) value).getId());
		}
	}

}
