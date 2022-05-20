package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class appointment
{

	public SimpleIntegerProperty id = new SimpleIntegerProperty();
	public SimpleStringProperty name = new SimpleStringProperty();
	public SimpleStringProperty contact = new SimpleStringProperty();
	public SimpleStringProperty title = new SimpleStringProperty();
	public SimpleStringProperty type = new SimpleStringProperty();
	public SimpleStringProperty start = new SimpleStringProperty();
	public SimpleStringProperty end = new SimpleStringProperty();


	public int getId() {
		return id.get();
	}

	public SimpleIntegerProperty idProperty() {
		return id;
	}

	public String getName() {
		return name.get();
	}

	public SimpleStringProperty nameProperty() {
		return name;
	}

	public String getContact() {
		return contact.get();
	}

	public SimpleStringProperty contactProperty() {
		return contact;
	}

	public String getTitle() {
		return title.get();
	}

	public SimpleStringProperty titleProperty() {
		return title;
	}

	public String getType() {
		return type.get();
	}

	public SimpleStringProperty typeProperty() {
		return type;
	}

	public String getStart() {
		return start.get();
	}

	public SimpleStringProperty startProperty() {
		return start;
	}

	public String getEnd() {
		return end.get();
	}

	public SimpleStringProperty endProperty() {
		return end;
	}


	public void setId(int id) {
		this.id.set(id);
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public void setContact(String contact) {
		this.contact.set(contact);
	}

	public void setTitle(String title) {
		this.title.set(title);
	}

	public void setType(String type) {
		this.type.set(type);
	}

	public void setStart(String start) {
		this.start.set(start);
	}

	public void setEnd(String end) {
		this.end.set(end);
	}
}
