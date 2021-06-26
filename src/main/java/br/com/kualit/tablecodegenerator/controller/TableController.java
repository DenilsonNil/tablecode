package br.com.kualit.tablecodegenerator.controller;

import static br.com.kualit.tablecodegenerator.utils.StringUtils.listMethodName;
import static br.com.kualit.tablecodegenerator.utils.StringUtils.modelAttributes;

import java.util.LinkedHashSet;
import java.util.Set;

import br.com.kualit.tablecodegenerator.model.Column;
import br.com.kualit.tablecodegenerator.service.FileService;
import br.com.kualit.tablecodegenerator.utils.AlertUtils;
import br.com.kualit.tablecodegenerator.utils.StringUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableController extends FileController {

	private static final String CLASS_NAME = "TableController";
	private static final String COLLECTION_IMPORT_BASE = "import javafx.collections.";
	private static final String BASE_IMPORT = "import javafx.scene.control.";
	private static final String TABLE_COLUMN = "TableColumn";
	private static final String FXML_ANNOTATION = "@FXML";
	private static final String STRING = "String";
	private static final String INTEGER = "Integer";
	private static final String DOUBLE = "Double";
	private static final String BOOLEAN = "Boolean";

	@FXML
	private TextField txtTableId;

	@FXML
	private TextField txtColumnName;

	@FXML
	private ChoiceBox<String> cbColumnDataType;

	@FXML
	private RadioButton rbEditable;

	@FXML
	private RadioButton rbShowCheck;

	@FXML
	private RadioButton rbOnlyExibition;

	@FXML
	private ChoiceBox<String> cbAttributes;

	@FXML
	private Button btnAddColumn;

	@FXML
	private Button btnCleanAllData;

	@FXML
	private Button btnCreateJavaFile;

	private Set<String> valueSet = new LinkedHashSet<>();

	@FXML
	private TableView<Column> table;

	@FXML
	private TableColumn<Column, String> col_name;

	@FXML
	private TableColumn<Column, String> col_editable;

	@FXML
	private TableColumn<Column, String> col_check;

	private ObservableList<Column> columnList;

	@FXML
	public void generateColumnData() {
		String tableId = txtTableId.textProperty().get();
		String columnName = txtColumnName.textProperty().get();

		if (tableId.trim().isEmpty()) {
			AlertUtils.showWarningMessage("O nome da tabela é obrigatório");
		}

		else if (columnName.trim().isEmpty()) {
			AlertUtils.showWarningMessage("O nome da coluna é obrigatório");
		}

		else {

			txtTableId.disableProperty().set(true);

			String attribute = generateAttributesData(tableId, columnName);
			generateConfigColumnData(columnName, attribute);
			reorganize();
		}

	}

	private void generateConfigColumnData(String columnName, String attribute) {
		valueSet.add("\n" + columnName + ".setCellValueFactory(new PropertyValueFactory<>(" + "\"" + attribute + "\"));");
		boolean columnEditable = rbEditable.isSelected();
		boolean columnHasCheckBox = rbShowCheck.isSelected();

		if (columnEditable) {

			valueSet.add("\n" + columnName + ".setCellFactory(TextFieldTableCell.forTableColumn());");
			imports.add("\n" + BASE_IMPORT + "cell.CheckBoxTableCell;");
		}

		else if (columnHasCheckBox) {
			valueSet.add("\n" + columnName + ".setCellFactory(CheckBoxTableCell.forTableColumn(" + columnName + "));");
			imports.add("\n" + BASE_IMPORT + "cell.TextFieldTableCell;");
		}

		Column column = new Column(columnName, columnEditable ? "Sim" : "Não", columnHasCheckBox ? "Sim" : "Não");
		columnList.add(column);
		table.setItems(columnList);
	}

	@FXML
	public void cleanData() {
		txtTableId.textProperty().set("");
		txtColumnName.textProperty().set("");
		txtTableId.disableProperty().set(false);
		atributos.clear();
		columnList.clear();
		rbOnlyExibition.selectedProperty().set(true);
		valueSet.clear();

	}

	private String generateAttributesData(String tableId, String columnName) {
		String attribute = cbAttributes.valueProperty().get();
		String selectedType = cbColumnDataType.valueProperty().get();
		atributos.add("\n\n" + FXML_ANNOTATION + "\nprivate TableView<" + StringUtils.modelName + "> " + tableId.toLowerCase() + ";");
		atributos.add("\n\n" + FXML_ANNOTATION + "\nprivate " + TABLE_COLUMN + "<" + StringUtils.modelName + ", " + selectedType + "> " + columnName.toLowerCase() + ";");
		return attribute;
	}

	public void initialize() {

		configureTable();
		configureChoiceBox();
		cleanTableIdField();
		configureImports();
		txtColumnName.textProperty().set("");

		ToggleGroup toggleGroup = new ToggleGroup();
		rbEditable.setToggleGroup(toggleGroup);
		rbOnlyExibition.setToggleGroup(toggleGroup);
		rbShowCheck.setToggleGroup(toggleGroup);
		reorganize();
	}

	private void configureTable() {

		col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
		col_editable.setCellValueFactory(new PropertyValueFactory<>("editable"));
		col_check.setCellValueFactory(new PropertyValueFactory<>("showCheckBox"));

		columnList = FXCollections.observableArrayList();
		table.setItems(columnList);
	}

	private void configureImports() {
		imports.add(COLLECTION_IMPORT_BASE + "FXCollections;\n");
		imports.add(COLLECTION_IMPORT_BASE + "ObservableList;\n");
		imports.add("import javafx.fxml.FXML;\n");
		imports.add(BASE_IMPORT + TABLE_COLUMN + ";\n");
		imports.add(BASE_IMPORT + "TableView;\n");
		imports.add(BASE_IMPORT + "cell.PropertyValueFactory;\n");
	}

	private void cleanTableIdField() {
		txtTableId.textProperty().set("");
		
	}

	@Override
	protected void configureChoiceBox() {
		ObservableList<String> properties = FXCollections.observableArrayList(STRING, INTEGER, DOUBLE, BOOLEAN);
		cbColumnDataType.setItems(properties);
		ObservableList<String> attributes = FXCollections.observableArrayList(modelAttributes);
		cbAttributes.setItems(attributes);
	}
	
	
	private void reorganize() {
		txtColumnName.textProperty().set("");
		cbAttributes.getSelectionModel().select(0);
		cbColumnDataType.getSelectionModel().select(0);
		rbOnlyExibition.setSelected(true);
	}

	@Override
	protected void generateFileContent() {
		imports.stream().forEach(i -> {
			fileContent += i;
		});

		fileContent += "\n\npublic class " +CLASS_NAME+" {\n";

		atributos.stream().forEach(a -> {

			fileContent += a;
		});

		fileContent += "\n\npublic void initialize() {\n";

		valueSet.add("\n\n" + txtTableId.textProperty().get() + ".setItems("+listMethodName+");");

		valueSet.stream().forEach(v -> {
			fileContent += v;
		});

		fileContent += "\n}";

		fileContent += StringUtils.getListMethod() + "\n\n}";

	}

	@Override
	protected void createJavaFile() {
		if (atributos.isEmpty()) {
			AlertUtils.showWarningMessage("Não é possível gravar o arquivo. Não há dados suficientes");

		} else {
			generateFileContent();

			fileService = new FileService(fileContent, CLASS_NAME);
			fileService.restart();
		
			
			cleanData();
		}

	}

}
