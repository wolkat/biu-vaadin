package com.example.vaadindemo;

import com.example.vaadindemo.domain.Person;
import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class VaadinApp extends Application {

	private static final long serialVersionUID = 1L;
	private Window mainWindow = new Window();
	private static String[] visibleCols = new String[] { "id",
         "firstName", "lastName", "yob" };
	private BeanContainer<Long, Person> personBeanContainer = new BeanContainer<Long, Person>(Person.class);
	 Window formWindow = new Window();
	@Override
	public void init() {
		
		formWindow.setModal(true);
		formWindow.getContent().setSizeUndefined();
		
		final Button createButton = new Button("Dodaj");
		final Button updateButton = new Button("Edytuj");
		final Button deleteButton = new Button("Usuń");
		final Button fillFormButton = new Button("Wypełnij formularz");
		 
		createButton.setEnabled(true);
		updateButton.setEnabled(false);
		deleteButton.setEnabled(false);
		
		Label firstNameLabel = new Label("Imię");
		Label lastNameLabel = new Label("Nazwisko");
		Label yobLabel = new Label("Rok urodzenia");
		
		final TextField firstNameTF = new TextField();
		final TextField lastNameTF = new TextField();
		final TextField yobTF = new TextField();
		
		Button submitFormButton = new Button("OK");
		Button cancelFormButton = new Button("Anuluj");
		
		final GridLayout gridLayout = new GridLayout(2, 4);
		submitFormButton.setWidthUnits(50);
		gridLayout.setSpacing(true);
		
		gridLayout.addComponent(firstNameLabel, 0, 0);
		gridLayout.addComponent(lastNameLabel, 0, 1);
		gridLayout.addComponent(yobLabel, 0, 2);
		gridLayout.addComponent(firstNameTF, 1, 0);
		gridLayout.addComponent(lastNameTF, 1, 1);
		gridLayout.addComponent(yobTF, 1, 2);
		
		gridLayout.addComponent(submitFormButton, 0, 3);
		gridLayout.addComponent(cancelFormButton, 1, 3);
		
		personBeanContainer.setBeanIdProperty("id");
		personBeanContainer.addBean(new Person(1L, "Bolek", "Krzywousty", 1023));
		personBeanContainer.addBean(new Person(2L, "Lolek", "Krzywousty", 1024));
		personBeanContainer.addBean(new Person(3L, "Tola", "Krzywousty", 1025));
		
		final Table table = new Table();
		table.setContainerDataSource(personBeanContainer);
		table.setVisibleColumns(visibleCols);
		table.setSelectable(true);
		table.setImmediate(true);
		
		table.addListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateButton.setEnabled(true);
        		deleteButton.setEnabled(true);
        		mainWindow.removeComponent(gridLayout);
			}
		});
		fillFormButton.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				formWindow.removeComponent(gridLayout);
				
				formWindow.addComponent(gridLayout);
				
				//formWindow.addComponent(createButton);
				//formWindow.addComponent(deleteFormButton);
				mainWindow.addWindow(formWindow);
				//mainWindow.removeWindow(formWindow);
				//formWindow.replaceComponent(gridLayout, gridLayout);
				}
		});

		createButton.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				table.select(null);
				firstNameTF.setValue("");
				lastNameTF.setValue("");
				yobTF.setValue("");
				mainWindow.addComponent(gridLayout);
				updateButton.setEnabled(false);
        		deleteButton.setEnabled(false);
			}
		});
		
		updateButton.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				String firstNameUpdate = personBeanContainer.getContainerProperty(table.getValue(), "firstName").toString();
				String lastNameUpdate = personBeanContainer.getContainerProperty(table.getValue(), "lastName").toString();
				String yobUpdate = personBeanContainer.getContainerProperty(table.getValue(), "yob").toString();
				
				firstNameTF.setValue(firstNameUpdate);
				lastNameTF.setValue(lastNameUpdate);
				yobTF.setValue(yobUpdate);
				mainWindow.addComponent(gridLayout);
				updateButton.setEnabled(false);
        		deleteButton.setEnabled(false);
			}
		});
		
		deleteButton.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				personBeanContainer.removeItem(table.getValue());
				table.select(null);
				table.commit();
				updateButton.setEnabled(false);
        		deleteButton.setEnabled(false);
			}
		});
		
		
		submitFormButton.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				Long id = (Long) table.getValue();
				if (id != null) {
					personBeanContainer.removeItem(id);
					personBeanContainer.addBean(new Person(id, firstNameTF.getValue().toString(), lastNameTF.getValue().toString(), Integer.parseInt(yobTF.getValue().toString())));
				}
				else {
					personBeanContainer.addBean(new Person((Long) personBeanContainer.lastItemId()+1, firstNameTF.getValue().toString(), lastNameTF.getValue().toString(), Integer.parseInt(yobTF.getValue().toString())));
				}
				table.commit();
				table.select(null);
				table.setSortContainerPropertyId("id");
				table.sort();
				mainWindow.removeComponent(gridLayout);
				updateButton.setEnabled(false);
        		deleteButton.setEnabled(false);
			}
		});
		cancelFormButton.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				mainWindow.removeComponent(gridLayout);
			}
		});
		
		HorizontalLayout buttonPanel = new HorizontalLayout();
		buttonPanel.addComponent(createButton);
		buttonPanel.addComponent(updateButton);
		buttonPanel.addComponent(deleteButton);
		
		buttonPanel.setSpacing(true);
		
		mainWindow.addComponent(fillFormButton);
		mainWindow.addComponent(table);
		mainWindow.addComponent(buttonPanel);
		setMainWindow(mainWindow);
		
	}
}
