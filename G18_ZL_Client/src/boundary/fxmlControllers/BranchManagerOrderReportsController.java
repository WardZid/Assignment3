package boundary.fxmlControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import control.MainController;
import entity.MyMessage.MessageType;
import entity.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


/**
 * @author hamza
 *
 */
public class BranchManagerOrderReportsController implements Initializable {

	@FXML
    private TableColumn<?, ?> amountSoldTableCol;

    @FXML
    private TableColumn<?, ?> itemNameTableCol;

    @FXML
    private ListView<String> monthsListView;

    @FXML
    private BarChart<?, ?> reportBarChart;

    @FXML
    private Text reportMonthText;

    @FXML
    private TableView<?> reportTableView;

    @FXML
    private TextField totalItemsSoldTextField;

    @FXML
    private Button viewReportButton;
    
    /* ------------------------------------------------------------------- */
    
    /* ArrayList to save in the ListView */
    private static ArrayList<String> monthsYears;
    
    /* to save the user info */
    private static User user = ClientConsoleController.getUser();
    
    /* the current branch manager's branch ID */
    private static int branchID;
    
    
    
    /* ------------------------------------------------------------------- */

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		MainController.getMyClient().send(MessageType.GET, "store/by/id_user/"+user.getIdUser(), null);
		MainController.getMyClient().send(MessageType.GET, "order/report/sale/months/"+branchID, null);
		monthsListView.getItems().addAll(monthsYears);
		
		monthsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				monthSelectedFromListView();
			}
		});
		
	}
	
	
	/* ----------------------------------------------------------------- */
	
	
	/**
	 * Function to set the monthsYearsArrayList into monthsYears,
	 * So we can show them in ListView.
	 */
	public static void setMonthsYears(ArrayList<String> monthsYearsArrayList) {
		monthsYears = monthsYearsArrayList;
	}
	
	/**
	 * Function to set the current branch manager branchID.
	 */
	public void setBranchID(int branchId) {
		branchID = branchId;
	}
	
	/**
	 * Action when a line is selected in the monthsListView. 
	 */
	public void monthSelectedFromListView() {
		this.viewReportButton.setDisable(false);
		
	}
}
