package com.sbt.rnd.meetup2017.transport.api.document;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SBT-Nefedev-GV on 01.02.2017.
 */
public class DocumentFilter implements Serializable {

	private DocumentAccTypeFilter byAcc;

	private DocumentState state;

	private Date dateDoc;

	private DocumentFilter() {
	}

	public static DocumentFilter byAcc(DocumentAccTypeFilter byAcc) {
		return new DocumentFilter();
	}

	public DocumentFilter byState(DocumentState state) {
		this.state = state;
		return this;
	}

	public DocumentFilter byDate(Date date) {
		this.dateDoc = date;
		return this;
	}

	public DocumentAccTypeFilter getByAcc() {
		return byAcc;
	}

	public DocumentState getState() {
		return state;
	}

	public Date getDateDoc() {
		return dateDoc;
	}

	@Override
    public String toString() {
        return "DocumentFilter{" +
                "byAcc='" + byAcc + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
