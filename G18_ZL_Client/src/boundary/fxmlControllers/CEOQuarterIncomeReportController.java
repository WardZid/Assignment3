package boundary.fxmlControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import control.MainController;
import entity.MyMessage.MessageType;
import entity.Quarters;
import entity.Store;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;

/* ------------------------------------------------ */
/*            \/ Important Comments  \/             */
/*         PLEASE REMOVE COMMENT WHEN OVER          */
/* ------------------------------------------------ */
/*


 */

public class CEOQuarterIncomeReportController implements Initializable {
	
	/* ------------------------------------------------ */
    /*               \/ FXML Variables \/               */
    /* ------------------------------------------------ */
	
	@FXML
    private Button buttonCompare;
	
	@FXML
    private Button buttonShow;

    @FXML
    private CheckBox checkBoxCompare;

    @FXML
    private ChoiceBox<String> choiceBoxQuarter1;

    @FXML
    private ChoiceBox<String> choiceBoxQuarter2;

    @FXML
    private ChoiceBox<String> choiceBoxYear1;

    @FXML
    private ChoiceBox<String> choiceBoxYear2;
    
    @FXML
    private ChoiceBox<String> choiceBoxBranch1;
    
    @FXML
    private ChoiceBox<String> choiceBoxBranch2;

    @FXML
    private BarChart<String, Double> barChartIncome;
	
	/* ------------------------------------------------ */
    /*               \/ Help Variables \/               */
    /* ------------------------------------------------ */
    
    // XYChart series1 to insert values in the bar chart 
    XYChart.Series<String, Double> series1 = new XYChart.Series<String, Double>();
    
    // XYChart series2 to insert values in the bar chart 
    XYChart.Series<String, Double> series2 = new XYChart.Series<String, Double>();
    
    // number of day in every month of the year
    private HashMap<String, Integer> daysOfMonth = new HashMap<String,Integer>();
    
    // the selected years and months
    private String year1,year2,quarter1,quarter2;
    
    // the selected branches
    private int branch1,branch2;
    
    // the selected quarters
    private Quarters Q1,Q2;
    
    // the first month in the selected quarter
    private int firstMonthInQuarter1=1,firstMonthInQuarter2=1; 
    
    // the number of selected choice boxes in every row
    private int selected1=0,selected2=0;
    
    private ArrayList<Double> results1,results2;
	
	/* ------------------------------------------------ */
    /*            \/ initialize function \/             */
    /* ------------------------------------------------ */
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		buttonCompare.setDisable(false);
		initHelpVariables();
		initChoiceBoxes();
		choiceBoxYear1.setOnAction(this::onYear1Selection);
		choiceBoxYear2.setOnAction(this::onYear2Selection);
		choiceBoxQuarter1.setOnAction(this::onQuarter1Selection);
		choiceBoxQuarter2.setOnAction(this::onQuarter2Selection);
		choiceBoxBranch1.setOnAction(this::onBranch1Selection);
		choiceBoxBranch2.setOnAction(this::onBranch2Selection);
	}
	
	/* ------------------------------------------------ */
    /*               \/ Action Methods \/               */
    /* ------------------------------------------------ */
	
	/**
	 * Action to do when branch1 has a selection
	 * 
	 * @param event
	 */
	private void onBranch1Selection(ActionEvent event) {
		if(choiceBoxBranch1.getSelectionModel().getSelectedItem() != null) {
			branch1 = Store.valueOf(choiceBoxBranch1.getSelectionModel().getSelectedItem()).ordinal();
			selected1++;
			if(selected1 == 3) {
				buttonShow.setDisable(false);
			}
		}
	}
	
	/**
	 * Action to do when branch2 has a selection
	 * 
	 * @param event
	 */
	private void onBranch2Selection(ActionEvent event) {
		if(choiceBoxBranch2.getSelectionModel().getSelectedItem() == null) return;
		branch2 = Store.valueOf(choiceBoxBranch2.getSelectionModel().getSelectedItem()).ordinal();
		selected2++;
		if(selected2 == 3) {
			buttonCompare.setDisable(false);
		}
	}
	
	/**
	 * Action to do when year1 has a selection
	 * 
	 * @param event
	 */
	public void onYear1Selection(ActionEvent event) {
		if(choiceBoxYear1.getSelectionModel().getSelectedItem() == null) return;
		year1 = choiceBoxYear1.getSelectionModel().getSelectedItem();
		selected1++;
		if(selected1 == 3) {
			buttonShow.setDisable(false);
		}
	}
	
	/**
	 * Action to do when year2 has a selection
	 * 
	 * @param event
	 */
	public void onYear2Selection(ActionEvent event) {
		if(choiceBoxYear2.getSelectionModel().getSelectedItem() == null) return;
		year2 = choiceBoxYear2.getSelectionModel().getSelectedItem();
		selected2++;
		if(selected2 == 3) {
			buttonCompare.setDisable(false);
		}
	}
	
	/**
	 * Action to do when quarter1 has a selection
	 * 
	 * @param event
	 */
	public void onQuarter1Selection(ActionEvent event) {
		if(choiceBoxQuarter1.getSelectionModel().getSelectedItem() == null) return;
		quarter1 = choiceBoxQuarter1.getSelectionModel().getSelectedItem();
		Q1 = Quarters.valueOf(choiceBoxQuarter1.getSelectionModel().getSelectedItem());
    	int i = Q1.ordinal();
    	firstMonthInQuarter1 += 3*i;
    	selected1++;
		if(selected1 == 3) {
			buttonShow.setDisable(false);
		}
	}
	
	/**
	 * Action to do when quarter2 has a selection
	 * 
	 * @param event
	 */
	public void onQuarter2Selection(ActionEvent event) {
		if(choiceBoxQuarter2.getSelectionModel().getSelectedItem() == null) return;
		quarter2 = choiceBoxQuarter2.getSelectionModel().getSelectedItem();
		Q2 = Quarters.valueOf(choiceBoxQuarter2.getSelectionModel().getSelectedItem());
    	int j = Q2.ordinal();
    	firstMonthInQuarter2 += 3*j;
    	selected2++;
		if(selected2 == 3) {
			buttonCompare.setDisable(false);
		}
	}
	
	/**
	 * Action to do when show button is pressed
	 * 
	 * @param event
	 */
	@SuppressWarnings("unchecked")
	public void onbuttonShow(ActionEvent event) {
		if(buttonShow.getText().equals("Show")) {
			results1 = (ArrayList<Double>)MainController.getMyClient().send(MessageType.GET, "order/income/quarter/"+branch1+"/"+firstMonthInQuarter1+"/"+year1, null);
			//results2 = (ArrayList<Double>)MainController.getMyClient().send(MessageType.GET, "order/income/quarter/0/4/2022", null);
			//System.out.println(results2.toString());
			initSeries1();
			choiceBoxBranch2.setDisable(false);
			choiceBoxQuarter2.setDisable(false);
			choiceBoxYear2.setDisable(false);
			buttonShow.setText("Compare");
			barChartIncome.getData().add(series1);
			
		}
		else { 
			if(!choiceBoxBranch2.getSelectionModel().isEmpty() && !choiceBoxYear2.getSelectionModel().isEmpty() && !choiceBoxQuarter2.getSelectionModel().isEmpty()) {
				results1 = (ArrayList<Double>)MainController.getMyClient().send(MessageType.GET, "order/income/quarter/"+branch1+"/"+firstMonthInQuarter1+"/"+year1, null);
				results2 = (ArrayList<Double>)MainController.getMyClient().send(MessageType.GET, "order/income/quarter/"+branch2+"/"+firstMonthInQuarter2+"/"+year2, null);
				initSeries1();
				initSeries2();
				barChartIncome.getData().set(0, series1);
				if(barChartIncome.getData().size() == 1) {
					barChartIncome.getData().add(series2);
				}
				else {
					barChartIncome.getData().set(1, series2);
				}
			}
		}
	}
	
	/**
	 * Action to do when compare button is pressed
	 * 
	 * @param event
	 */
	public void onbuttonCompare(ActionEvent event) {
		//results2 = (ArrayList<Double>)MainController.getMyClient().send(MessageType.GET, "order/income/quarter/"+branch2+"/"+firstMonthInQuarter2+"/"+year2, null);
		//initSeriesOfQuarterToCompare();
		barChartIncome.getData().clear();
		series1.getData().clear();
		series2.getData().clear();
		choiceBoxBranch1.getSelectionModel().clearSelection();
		choiceBoxBranch2.getSelectionModel().clearSelection();
		choiceBoxQuarter1.getSelectionModel().clearSelection();
		choiceBoxQuarter2.getSelectionModel().clearSelection();
		choiceBoxYear1.getSelectionModel().clearSelection();
		choiceBoxYear2.getSelectionModel().clearSelection();
		choiceBoxBranch2.setDisable(true);
		choiceBoxQuarter2.setDisable(true);
		choiceBoxYear2.setDisable(true);
		selected1=0;
		selected2=0;
		buttonShow.setText("Show");
	}
	
	/* ------------------------------------------------ */
    /*                 \/ Help Methods \/               */
    /* ------------------------------------------------ */
	
	private void initSeries1() {
		//series1 = new XYChart.Series<String, Double>(); 
		series1 = new XYChart.Series<String, Double>(); 
		//barChartIncome.getData().clear();
		for(int i=0 ; i<3 ; i++) {
			series1.getData().add(new XYChart.Data<String, Double>( (firstMonthInQuarter1+i)+"" , results1.get(i)) );
		}
		series1.setName(quarter1+" - "+year1);
	}
    
	private void initSeries2() {
		//series1 = new XYChart.Series<String, Double>(); 
		series2 = new XYChart.Series<String, Double>(); 
		//barChartIncome.getData().clear();
		for(int i=0 ; i<3 ; i++) {
			series2.getData().add(new XYChart.Data<String, Double>( (firstMonthInQuarter2+i)+"" , results2.get(i)) );
		}
		series2.setName(quarter2+" - "+year2);
	}
	
	/**
	 * Method to initialize all choice boxes
	 */
	private void initChoiceBoxes() {
		initYear1ChoiceBox();
		initYear2ChoiceBox();
		initQuarter1ChoiceBox();
		initQuarter2ChoiceBox();
		initBranch1ChoiceBox();
		initBranch2ChoiceBox();
	}
	
	/**
	 * Method to initialize year1 choice box
	 */
	private void initYear1ChoiceBox() {
		for(int i = 2017 ; i<2030 ; i++) {
			choiceBoxYear1.getItems().add(i+"");
		}
	}
	
	/**
	 * Method to initialize year2 choice box
	 */
	private void initYear2ChoiceBox() {
		for(int i = 2017 ; i<2030 ; i++) {
			choiceBoxYear2.getItems().add(i+"");
		}
	}
	
	/**
	 * Method to initialize month1 choice box
	 */
	private void initQuarter1ChoiceBox() {
		for(Quarters q : Quarters.values()) {
			choiceBoxQuarter1.getItems().add(q.toString());
		}
	}
	
	/**
	 * Method to initialize month2 choice box
	 */
	private void initQuarter2ChoiceBox() {
		for(Quarters q : Quarters.values()) {
			choiceBoxQuarter2.getItems().add(q.toString());
		}
	}
	
	/**
	 * Method to initialize the branch1 choice box
	 */
	private void initBranch1ChoiceBox() {
		for(Store s : Store.values()){
			choiceBoxBranch1.getItems().add(s.toString());
		}
	}
	
	/**
	 * Method to initialize the branch1 choice box
	 */
	private void initBranch2ChoiceBox() {
		for(Store s : Store.values()){
			choiceBoxBranch2.getItems().add(s.toString());
		}
	}
	
	/**
	 * To initialize the help variables.
	 */
	private void initHelpVariables(){
		daysOfMonth.put("1", 31);
		daysOfMonth.put("2", 29);
		daysOfMonth.put("3", 31);
		daysOfMonth.put("4", 30);
		daysOfMonth.put("5", 31);
		daysOfMonth.put("6", 30);
		daysOfMonth.put("7", 31);
		daysOfMonth.put("8", 31);
		daysOfMonth.put("9", 30);
		daysOfMonth.put("10", 31);
		daysOfMonth.put("11", 30);
		daysOfMonth.put("12", 31);
	}
}
