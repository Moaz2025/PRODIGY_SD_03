package com.example.prodigy_sd_03;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContactManager extends Application {

    private List<Contact> contacts;
    private ListView<Contact> contactListView;
    private TextField nameField, phoneField, emailField;
    private Button addButton, editButton, deleteButton, clearButton;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Contact Manager");

        contacts = new ArrayList<>();
        loadContactsFromFile(); // Load contacts from file

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        Label nameLabel = new Label("Name:");
        GridPane.setConstraints(nameLabel, 1, 0);
        nameField = new TextField();
        GridPane.setConstraints(nameField, 2, 0);

        Label phoneLabel = new Label("Phone:");
        GridPane.setConstraints(phoneLabel, 1, 1);
        phoneField = new TextField();
        GridPane.setConstraints(phoneField, 2, 1);

        Label emailLabel = new Label("Email:");
        GridPane.setConstraints(emailLabel, 1, 2);
        emailField = new TextField();
        GridPane.setConstraints(emailField, 2, 2);

        addButton = new Button("Add");
        addButton.setOnAction(e -> addContact());
        GridPane.setConstraints(addButton, 0, 3);

        editButton = new Button("Edit");
        editButton.setOnAction(e -> editContact());
        GridPane.setConstraints(editButton, 1, 3);

        deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> deleteContact());
        GridPane.setConstraints(deleteButton, 2, 3);

        clearButton = new Button("Clear");
        clearButton.setOnAction(e -> clearFields());
        GridPane.setConstraints(clearButton, 3, 3);

        contactListView = new ListView<>();
        contactListView.getItems().addAll(contacts);
        contactListView.setOnMouseClicked(e -> displayContactDetails());

        grid.getChildren().addAll(nameLabel, nameField, phoneLabel, phoneField, emailLabel, emailField, addButton, editButton, deleteButton, clearButton, contactListView);

        Scene scene = new Scene(grid, 630, 420);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
        contactListView.getStyleClass().add("list-view");
        nameField.getStyleClass().add("text-field");
        phoneField.getStyleClass().add("text-field");
        emailField.getStyleClass().add("text-field");
        Image icon = new Image("file:icon.png");
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addContact() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();

        Contact contact = new Contact(name, phone, email);
        contacts.add(contact);
        contactListView.getItems().add(contact);
        saveContactsToFile(); // Save contacts to file
        clearFields();
    }

    private void editContact() {
        Contact selectedContact = contactListView.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            selectedContact.setName(nameField.getText());
            selectedContact.setPhone(phoneField.getText());
            selectedContact.setEmail(emailField.getText());
            contactListView.refresh(); // Refresh list view to reflect changes
            saveContactsToFile(); // Save contacts to file
            clearFields();
        } else {
            showAlert("Error", "Please select a contact to edit.");
        }
    }

    private void deleteContact() {
        Contact selectedContact = contactListView.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            contacts.remove(selectedContact);
            contactListView.getItems().remove(selectedContact);
            saveContactsToFile(); // Save contacts to file
            clearFields();
        } else {
            showAlert("Error", "Please select a contact to delete.");
        }
    }

    private void displayContactDetails() {
        Contact selectedContact = contactListView.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            nameField.setText(selectedContact.getName());
            phoneField.setText(selectedContact.getPhone());
            emailField.setText(selectedContact.getEmail());
        }
    }

    private void clearFields() {
        nameField.clear();
        phoneField.clear();
        emailField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void saveContactsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("contacts.txt"))) {
            for (Contact contact : contacts) {
                writer.println(contact.getName() + "," + contact.getPhone() + "," + contact.getEmail());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadContactsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("contacts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0];
                    String phone = parts[1];
                    String email = parts[2];
                    contacts.add(new Contact(name, phone, email));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}


