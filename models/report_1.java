package models;

import javafx.beans.property.SimpleStringProperty;

public class report_1
{

	public SimpleStringProperty month = new SimpleStringProperty();
	public SimpleStringProperty conference = new SimpleStringProperty();
	public SimpleStringProperty misc = new SimpleStringProperty();
	public SimpleStringProperty project = new SimpleStringProperty();
	public SimpleStringProperty presentation = new SimpleStringProperty();
	public SimpleStringProperty scrum = new SimpleStringProperty();
	public SimpleStringProperty team = new SimpleStringProperty();


	public String getMonth() {
		return month.get();
	}

	public SimpleStringProperty monthProperty() {
		return month;
	}

	public void setMonth(String month) {
		this.month.set(month);
	}

	public String getConference() {
		return conference.get();
	}

	public SimpleStringProperty conferenceProperty() {
		return conference;
	}

	public void setConference(String conference) {
		this.conference.set(conference);
	}

	public String getMisc() {
		return misc.get();
	}

	public SimpleStringProperty miscProperty() {
		return misc;
	}

	public void setMisc(String misc) {
		this.misc.set(misc);
	}

	public String getProject() {
		return project.get();
	}

	public SimpleStringProperty projectProperty() {
		return project;
	}

	public void setProject(String project) {
		this.project.set(project);
	}

	public String getPresentation() {
		return presentation.get();
	}

	public SimpleStringProperty presentationProperty() {
		return presentation;
	}

	public void setPresentation(String presentation) {
		this.presentation.set(presentation);
	}

	public String getScrum() {
		return scrum.get();
	}

	public SimpleStringProperty scrumProperty() {
		return scrum;
	}

	public void setScrum(String scrum) {
		this.scrum.set(scrum);
	}

	public String getTeam() {
		return team.get();
	}

	public SimpleStringProperty teamProperty() {
		return team;
	}

	public void setTeam(String team) {
		this.team.set(team);
	}
}
