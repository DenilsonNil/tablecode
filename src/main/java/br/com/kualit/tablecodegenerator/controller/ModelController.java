package br.com.kualit.tablecodegenerator.controller;

import static br.com.kualit.tablecodegenerator.utils.AlertUtils.showWarningMessage;
import static br.com.kualit.tablecodegenerator.utils.StringUtils.configuraNomeProperty;
import static br.com.kualit.tablecodegenerator.utils.StringUtils.configuraTipoProperty;
import static br.com.kualit.tablecodegenerator.utils.StringUtils.modelAttributes;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import br.com.kualit.tablecodegenerator.service.FileService;
import br.com.kualit.tablecodegenerator.utils.AlertUtils;
import br.com.kualit.tablecodegenerator.utils.StringUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class ModelController extends FileController {

	private static final String BOOLEAN_PROPERTY = "BooleanProperty";
	private static final String DOUBLE_PROPERTY = "DoubleProperty";
	private static final String INTEGER_PROPERTY = "IntegerProperty";
	private static final String STRING_PROPERTY = "StringProperty";
	private static final String BASE_IMPORT = "import javafx.beans.property.";
	private static final String SIMPLE = "Simple";

	@FXML
	private TextField txtModelName;

	@FXML
	private TextField txtPropertyName;

	@FXML
	private ChoiceBox<String> cbPropertyType;

	@FXML
	private TextArea txtAttributes;

	@FXML
	private Button btnAddAttribute;

	@FXML
	private Button btnCleanAllData;

	@FXML
	private Button btnCreateJavaFile;

	@FXML
	private Button btnNextScreen;

	@FXML
	private AnchorPane modelRoot;
	
	private Set<String> getsSets = new LinkedHashSet<String>();
	private Set<String> propertiesMethods = new LinkedHashSet<String>();
	private Map<String, String> constructorValues = new HashMap<>();
	private String constructorParams;
	private String constructorInternals;
	private String constructor;
	private String propertyType;

	public void initialize() {
		configureChoiceBox();
		clearTextualFields();
		enableDisableNextScreenButton();
	}

	private void enableDisableNextScreenButton() {
		btnNextScreen.disableProperty().set(!btnNextScreen.disableProperty().get());
	}

	@FXML
	private void clearAllData() {
		if (txtModelName.isDisabled()) {
			txtModelName.setDisable(false);
		}

		clearTextualFields();
		atributos.clear();
		getsSets.clear();
		constructorValues.clear();
		propertiesMethods.clear();
		constructorParams = "";
		constructorInternals = "";
		constructor = "";
		modelAttributes.clear();
		StringUtils.modelName = "";
	}

	private void clearTextualFields() {
		txtModelName.setText("");
		txtPropertyName.setText("");
		txtAttributes.setText("");
	}

	
	protected void createJavaFile() {

		String modelName = txtModelName.textProperty().get();

		if (imports.isEmpty()) {
			AlertUtils.showWarningMessage("Não é possível gravar o arquivo. Não há dados suficientes");

		} else {
			generateFileContent();
			fileService = new FileService(fileContent, modelName);
			fileService.restart();
			
			enableDisableNextScreenButton();

		}

	}

	@Override
	protected void generateFileContent() {
		imports.stream().forEach(i -> fileContent += i);

		fileContent += "public class " + txtModelName.textProperty().get() + " {\n\n";

		atributos.stream().forEach(a -> fileContent += a);

		fileContent += constructor;

		getsSets.stream().forEach(v -> fileContent += v);

		propertiesMethods.stream().forEach(m -> fileContent += m);

		fileContent += "\n}";

	}

	@FXML
	public void generateClassDataByAttribute() {
		String propertyName = txtPropertyName.textProperty().get();
		String modelName = txtModelName.textProperty().get();

		if (modelName.trim().isEmpty()) {

			showWarningMessage("O nome do modelo não pode ser vazio");

		} else if (propertyName.trim().isEmpty()) {

			showWarningMessage("O nome da property não pode ser vazio");

		} else {

			String tipoSelecionado = generateImportsData(propertyName);
			generateAttributesData(tipoSelecionado, propertyName);
			addGetSetsOnClass(tipoSelecionado, propertyName);
			generateConstructorData(modelName, tipoSelecionado, propertyName);
			generateGetProperties(tipoSelecionado, propertyName);
			showDataOnTextArea();
			txtPropertyName.setText(null);
			if (!(txtModelName.isDisabled())) {
				StringUtils.modelName = modelName;
				txtModelName.setDisable(true);
			}
		}
	}

	private void generateGetProperties(String tipoSelecionado, String nomeDaProperty) {

		propertiesMethods.add("\npublic " + tipoSelecionado + " " + nomeDaProperty + "Property(){\n" + "return " + nomeDaProperty + ";\n}");
	}

	private void showDataOnTextArea() {
		StringBuilder content = new StringBuilder();

		atributos.stream().forEach(a -> {
			content.append(a);
			content.append("\n");
		});

		txtAttributes.setText(content.toString());

	}

	private String generateImportsData(String nomeDaProperty) {
		String selectedType = cbPropertyType.valueProperty().get();

		imports.add(BASE_IMPORT + selectedType + ";\n");
		imports.add(BASE_IMPORT + SIMPLE + selectedType + ";\n");

		return selectedType;
	}

	private void generateAttributesData(String tipoSelecionado, String nomeDaProperty) {
		atributos.add("\nprivate " + tipoSelecionado + " " + nomeDaProperty + " = new " + SIMPLE + tipoSelecionado + "();");
		modelAttributes.add(nomeDaProperty.toLowerCase());
	}

	private void addGetSetsOnClass(String selectedType, String propertyName) {

		propertyType = configuraTipoProperty(selectedType);
		String propertyNameCamelCase = configuraNomeProperty(propertyName);

		getsSets.add("\npublic " + propertyType + " get" + propertyNameCamelCase + "(){\nreturn " + propertyName + ".get();\n}");
		getsSets.add("\npublic void set" + propertyNameCamelCase + "(" + propertyType + " " + propertyName + ") {\nthis." + propertyName + ".set(" + propertyName + ");\n}");

	}

	private void generateConstructorData(String nomeModelo, String tipoSelecionado, String nomeDaProperty) {

		generateConstructorParamsAndValues(tipoSelecionado, nomeDaProperty);

		constructor = "\npublic " + nomeModelo + constructorParams + "{\n" + constructorInternals + "}\n\n";
	}

	private void generateConstructorParamsAndValues(String tipoSelecionado, String nomeDaProperty) {
		constructorParams = "";
		constructorParams += "(";
		constructorInternals = "";

		String paramValue = tipoSelecionado.equals("BooleanProperty") ? "false" : nomeDaProperty;

		constructorValues.put(propertyType + " " + nomeDaProperty, "this." + nomeDaProperty + " = new " + SIMPLE + tipoSelecionado + "(" + paramValue + ");");

		constructorValues.entrySet().stream().forEach(e -> {

			constructorParams += e.getKey();
			constructorParams += ", ";
			constructorInternals += e.getValue();
			constructorInternals += "\n";
		});
		constructorParams += ")";
		constructorParams = constructorParams.replace(", )", ")");

	}

	@Override
	protected void configureChoiceBox() {
		ObservableList<String> properties = FXCollections.observableArrayList(STRING_PROPERTY, INTEGER_PROPERTY, DOUBLE_PROPERTY, BOOLEAN_PROPERTY);
		cbPropertyType.setItems(properties);
		cbPropertyType.getSelectionModel().select(0);
	}

	@FXML
	public void nextScreen() {
		if(imports.isEmpty()) {
			AlertUtils.showWarningMessage("Não é possível ir para a próxima tela. Não há dados suficientes");
		}else {
			try {
				
				Pane tableScreenFxml = FXMLLoader.load(getClass().getResource("/TableLayout.fxml"));
				modelRoot.getChildren().setAll(tableScreenFxml);
			} catch (Exception e) {
				AlertUtils.showWarningMessage("Ocorreu um erro ao tentar carregar a tela de construção da tabela\n"+e.getMessage());
				
				
			}
		}
	}

}
