package MusicXML;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class MainController {

	// declare variables
	@FXML
	private Button addbtn;

	@FXML
	private Button savebtn;

	@FXML
	private Button viewbtn;

	@FXML
	private Button convertbttn;

	@FXML
	private Button changebttn;

	@FXML
	private ListView<File> listview;

	@FXML
	private TextArea txtTextArea;

	@FXML
	private TextArea xmlTextArea;

	@FXML
	private TextField txtPath;

	@FXML
	private Tab txtTab;

	@FXML
	private Tab xmlTab;

	@FXML
	private TabPane tp;

	@FXML
	private Alert alert;

	@FXML
	private TextField txtDetected;
	
	@FXML TreeView<String> treeView;

	File outputFile;
	private boolean textChanged = false;

	// this method sets the action for when the "Upload File" button is pressed,
	// only one file can be opened at a time and must be a .txt file
	public void addSongAction(ActionEvent event) {
		FileChooser fc = new FileChooser();

		// filter file extension -- remove below code if no extension filter is needed
		fc.getExtensionFilters().add(new ExtensionFilter("TXT Files", "*.txt"));

		List<File> selectedFiles = fc.showOpenMultipleDialog(null);

		if (selectedFiles != null) {
			if (selectedFiles.size() > 1) {
				for (int i = 0; i < selectedFiles.size(); i++) {
					listview.getItems().add(selectedFiles.get(i).getAbsoluteFile());
					showOnlyFileName();
				}
			} else {

				listview.getItems().add(selectedFiles.get(0).getAbsoluteFile());
				showOnlyFileName();
			}
		} else {
			System.out.println("no file selected");
		}
	}

	public void saveAction(ActionEvent event) {
		File file = outputFile;
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new ExtensionFilter("XML Files", "*.xml"));
		File dest = fc.showSaveDialog(null);
		if (dest != null) {
			try {
				Files.copy(file.toPath(), dest.toPath());
			} catch (IOException ex) {
				System.out.println("no destination selected");
			}
		}
	}

	public void viewAction(ActionEvent event) {
		tp.getSelectionModel().select(txtTab);
		txtPath.clear();
		txtDetected.clear();
		txtTextArea.clear();
		File tab;

		if (listview.getSelectionModel().getSelectedItem() != null) {
			tab = listview.getSelectionModel().getSelectedItem();
			txtPath.appendText(tab.getAbsolutePath());
			try {
				Scanner reader = new Scanner(tab);

				while (reader.hasNextLine()) {
					txtTextArea.appendText(reader.nextLine());
					txtTextArea.appendText("\n");
				}
				reader.close();
			} catch (IOException e) {
				System.out.println("File scan error");
				e.printStackTrace();
			}

			// change this when program is able to detect instrument type
			txtDetected.appendText("Guitar");
		} else {
			System.out.println("No file selected");
		}
	}

	public void convertAction(ActionEvent event) throws SAXException, ParserConfigurationException, IOException {
		File tab;
		if (textChanged) {
			String tmpTab;
			tmpTab = txtTextArea.getText().replaceAll("\n", System.getProperty("line.separator"));

			try {
				File file = new File("tmpFile.txt");

				if (!file.exists()) {
					file.createNewFile();
				}

				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(tmpTab);
				bw.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

			GuitarXMLOut convertedFile = new GuitarXMLOut();
			outputFile = convertedFile.convertToXML(new File("tmpFile.txt"));

			txtTextArea.appendText("\n");
			txtTextArea.appendText(">> Conversion complete");
			
			XMLViewAction(outputFile);
			
			textChanged = false;
		} else if (listview.getSelectionModel().getSelectedItem() != null) {
			tab = listview.getSelectionModel().getSelectedItem();

			GuitarXMLOut convertedFile = new GuitarXMLOut();
			outputFile = convertedFile.convertToXML(tab);

			txtTextArea.appendText("\n");
			txtTextArea.appendText(">> Conversion complete");
			
			XMLViewAction(outputFile);
		} else {
			System.out.println("No file selected and/or no tab pasted");
		}
	}

	public void changeAction(ActionEvent event) {
		alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Instrument Type");
		alert.setHeaderText("Are you sure you want to change instrument type?");
		alert.setContentText("Make a selection to modify:");

		ButtonType detectbttn = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		ButtonType guitarbttn = new ButtonType("Bass");
		ButtonType drumsbttn = new ButtonType("Drums");
		alert.getButtonTypes().setAll(detectbttn, guitarbttn, drumsbttn);
		alert.show();
	}

	public void showOnlyFileName() {
		listview.setCellFactory(
				(Callback<ListView<File>, ListCell<File>>) new Callback<ListView<File>, ListCell<File>>() {
					public ListCell<File> call(ListView<File> param) {
						return new ListCell<File>() {
							@Override
							protected void updateItem(File item, boolean empty) {
								super.updateItem(item, empty);
								setText(item == null || empty ? null : item.getName());
							}
						};
					}
				});
	}

	public void textAreaChange() {
		txtTextArea.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> observable, final String oldValue,
					final String newValue) {
				if (newValue.equals(""))
					textChanged = false;
				else
					textChanged = true;
			}
		});
	}

	public void clickList() {
		//txtTextArea.clear();
	}
	
	public void XMLViewAction(File outputFile) throws SAXException, ParserConfigurationException, IOException {
		TreeItem<String> root = readData(outputFile);
		treeView.setRoot(root);
	}
	
	
	// XML Output TreeView
	private static class TreeItemCreationContentHandler extends DefaultHandler {

	    private TreeItem<String> item = new TreeItem<>();

	    @Override
	    public void endElement(String uri, String localName, String qName) throws SAXException {
	        // finish this node by going back to the parent
	        this.item = this.item.getParent();
	    }

	    @Override
	    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
	        // start a new node and use it as the current item
	        TreeItem<String> item = new TreeItem<>(qName);
	        this.item.getChildren().add(item);
	        this.item = item;
	    }

	    @Override
	    public void characters(char[] ch, int start, int length) throws SAXException {
	        String s = String.valueOf(ch, start, length).trim();
	        if (!s.isEmpty()) {
	            // add text content as new child
	            this.item.getChildren().add(new TreeItem<>(s));
	        }
	    }

	}

	public static TreeItem<String> readData(File file) throws SAXException, ParserConfigurationException, IOException {
	    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
	    SAXParser parser = parserFactory.newSAXParser();
	    XMLReader reader = parser.getXMLReader();
	    TreeItemCreationContentHandler contentHandler = new TreeItemCreationContentHandler();

	    // parse file using the content handler to create a TreeItem representation
	    reader.setContentHandler(contentHandler);
	    reader.parse(file.toURI().toString());

	    // use first child as root (the TreeItem initially created does not contain data from the file)
	    TreeItem<String> item = contentHandler.item.getChildren().get(0);
	    contentHandler.item.getChildren().clear();
	    return item;
	}
}