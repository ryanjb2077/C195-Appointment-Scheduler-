package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class customer
{


	public SimpleIntegerProperty id = new SimpleIntegerProperty();
	public SimpleStringProperty name = new SimpleStringProperty();
	public SimpleStringProperty address = new SimpleStringProperty();
	public SimpleStringProperty city = new SimpleStringProperty();
	public SimpleStringProperty country = new SimpleStringProperty();
	public SimpleStringProperty postal_code = new SimpleStringProperty();
	public SimpleStringProperty phone = new SimpleStringProperty();




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

	public String getAddress() {
		return address.get();
	}

	public SimpleStringProperty addressProperty() {
		return address;
	}

	public String getCity() {
		return city.get();
	}

	public SimpleStringProperty cityProperty() {
		return city;
	}

	public String getCountry() {
		return country.get();
	}

	public SimpleStringProperty countryProperty() {
		return country;
	}

	public String getPostal_code() {
		return postal_code.get();
	}

	public SimpleStringProperty postal_codeProperty() {
		return postal_code;
	}

	public String getPhone() {
		return phone.get();
	}

	public SimpleStringProperty phoneProperty() {
		return phone;
	}



	public void setId(int id) {
		this.id.set(id);
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public void setAddress(String address) {
		this.address.set(address);
	}

	public void setCity(String city) {
		this.city.set(city);
	}

	public void setCountry(String country) {
		this.country.set(country);
	}

	public void setPostal_code(String postal_code) {
		this.postal_code.set(postal_code);
	}

	public void setPhone(String phone) {
		this.phone.set(phone);
	}


}
