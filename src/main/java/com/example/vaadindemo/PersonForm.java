package com.example.vaadindemo;

import java.util.ArrayList;
import java.util.List;

import com.example.vaadindemo.domain.Person;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

public class PersonForm extends Form implements FormFieldFactory {
	private static final long serialVersionUID = 1L;
	
	private List<String> visibleFields = new ArrayList<String>();
	private GridLayout gridLayout = new GridLayout(2, 4);
	
	BeanItem<Person> personBeanItem;
	BeanContainer<Long, Person> personBeanContainer = new BeanContainer<Long, Person>(Person.class);
	private Window mainWindow = new Window();
	  Window formWindow = new Window();
	
	  //Button fillFormButton = new Button("Wypełnij formularz");
	Button submitFormButton = new Button("OK");
	Button cancelFormButton = new Button("Anuluj");
	
	public PersonForm(BeanItem<Person> personBeanItem, final Window formWindow){
		this.personBeanItem = personBeanItem;
		visibleFields.add("firstName");
		visibleFields.add("lastName");
		visibleFields.add("yob");
		
		
		//this.formWindow = formWindow;
		formWindow.setModal(true);
		formWindow.getContent().setSizeUndefined();

		
		setLayout(gridLayout);
		setValidationVisibleOnCommit(true);
		//1
		setFormFieldFactory(this);
		//2
		setItemDataSource(personBeanItem);
		//3
		setVisibleItemProperties(visibleFields);
		
		gridLayout.addComponent(submitFormButton, 0, 3);
		gridLayout.addComponent(cancelFormButton, 1, 3);
		
		
		
		submitFormButton.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				//zapis do bazy
				personBeanContainer.addBean(new Person());
				commit();
				//getWindow().getParent().removeWindow(formWindow);
				
			}
		});
		cancelFormButton.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				 mainWindow.removeWindow(formWindow);
				
			}
		});
	}
	
	@Override
	protected void attachField(Object propertyId, Field field) {
		String property = (String) propertyId;
		
		if ("firstName".equals(property)){
			gridLayout.addComponent(field, 0, 0);
		}
		else if ("lastName".equals(property)){
			gridLayout.addComponent(field, 0, 1);
		}
		else if ("yob".equals(property)){
			gridLayout.addComponent(field, 0, 2);
		}
	}
	
	@Override
	public Field createField(Item item, Object propertyId, Component uiContext) {
		String property = (String) propertyId;
		
		if ("firstName".equals(property)){
			TextField firstNameTF = new TextField("Imię");
			firstNameTF.setRequired(true);
			firstNameTF.setRequiredError("Imię jest wymagane");
			firstNameTF.setColumns(30);
			return firstNameTF;
		}
		else if ("lastName".equals(property)){
			TextField lastNameTF = new TextField("Nazwisko");
			lastNameTF.setRequired(true);
			lastNameTF.setRequiredError("Nazwisko jest wymagane");
			lastNameTF.setColumns(30);
			return lastNameTF;
		}
		else if ("yob".equals(property)){
			TextField yobTF = new TextField("Rok urodzenia");
			yobTF.setColumns(4);
			//yobTF.addValidator(new IntegerRangeValidator(1940,2012));
			return yobTF;
		}
		
		return null;
	}
	
	

}
