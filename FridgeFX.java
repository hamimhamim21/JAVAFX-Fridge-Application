import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.LocalDate;
import java.util.*;
import java.io.*;
import javafx.collections.*;
import javafx.collections.transformation.*;
import javafx.scene.control.cell.*;
import javafx.beans.property.*;
import java.util.Optional;

//Name: HAMIM SHAIKH
//Student ID: 19901125
//Subject Code: CSE4OAD



public class FridgeFX extends Application {
	


	// used as ChoiceBox value for filter
	//private String choiceBox;
	
	// the data source controller
	private FridgeDSC fridgeDSC;
	

	public void init() throws Exception {
		// creating an instance of the data source controller to be used
		// in this application
		

		/* TODO 2-01 - TO COMPLETE ****************************************
		 * call the data source controller database connect method
		 * NOTE: that database connect method throws exception
		 */
		 try
		 {
			 fridgeDSC = new FridgeDSC();
			fridgeDSC.connect();
		 }
		 catch(Exception e) 
		{
            System.out.println(e);
            e.printStackTrace();
		}
			 
	}

	public void start(Stage stage) throws Exception {

		/* TODO 2-02 - TO COMPLETE ****************************************
		 * - this method is the start method for your application
		 * - set application title
		 * - show the stage
		 */
		 build(stage);
		 stage.setTitle(getClass().getName());
		 stage.show();


		/* TODO 2-03 - TO COMPLETE ****************************************
		 * currentThread uncaught exception handler
		 */
	}

	public void build(Stage stage) throws Exception {

        
        /* If you are getting the FX up and running before the DSC then you need to comment out the DSC calls above
         * and manually create an array of groceries to add to the tableview array.
         * 
         * LocalDate date = LocalDate.now();
         * Item myItem = new Item("Mars Bar", true);
         * Grocery mygr1 = new Grocery(1,myItem,date,10,FridgeDSC.SECTION.COOLING);
         * Grocery mygr2 = new Grocery(2,myItem,date,15,FridgeDSC.SECTION.COOLING);
         * ArrayList<Grocery> mygrs = new ArrayList<>();
         * mygrs.add(mygr1);
         * mygrs.add(mygr2);
         *
         * then below *after* the TableView has been set up
         * 
         * tableView.setItems(tableData);
         *
         * add the manually created array
         * tableData.addAll(mygrs);
         */ 


		// Create table data (an observable list of objects)
		ObservableList<Grocery> tableData = FXCollections.observableArrayList();

		// Define table columns
		TableColumn<Grocery, String> idColumn = new TableColumn<Grocery, String>("Id");
		TableColumn<Grocery, String> itemNameColumn = new TableColumn<Grocery, String>("Item");
		TableColumn<Grocery, Integer> quantityColumn = new TableColumn<Grocery, Integer>("QTY");
		TableColumn<Grocery, String> sectionColumn = new TableColumn<Grocery, String>("Section");
		TableColumn<Grocery, String> daysAgoColumn = new TableColumn<Grocery, String>("Bought");
		
		/* TODO 2-04 - TO COMPLETE ****************************************
		 * for each column defined, call their setCellValueFactory method 
		 * using an instance of PropertyValueFactory
		 */
		 idColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
		 itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("ItemName"));
		 quantityColumn.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
		 sectionColumn.setCellValueFactory(new PropertyValueFactory<>("Section"));
		 daysAgoColumn.setCellValueFactory(new PropertyValueFactory<>("DaysAgo"));
		
		
		// Create the table view and add table columns to it
		TableView<Grocery> tableView = new TableView<Grocery>();


		/* TODO 2-05 - TO COMPLETE ****************************************
		 * add table columns to the table view create above
		 */
		tableView.getColumns().add(idColumn);
		tableView.getColumns().add(itemNameColumn);
		tableView.getColumns().add(quantityColumn);
		tableView.getColumns().add(sectionColumn);
		tableView.getColumns().add(daysAgoColumn);

		//	Attach table data to the table view
		tableView.setItems(tableData);


		/* TODO 2-06 - TO COMPLETE ****************************************
		 * set minimum and maximum width to the table view and each columns
		 */
		 idColumn.setMinWidth(50);
		 itemNameColumn.setMinWidth(350);
		 quantityColumn.setMinWidth(50);
		 sectionColumn.setMinWidth(100);
		 daysAgoColumn.setMinWidth(130);
		 
		 //To avoid getting an extra column
		 tableView.setColumnResizePolicy(tableView.CONSTRAINED_RESIZE_POLICY);
		 
		/* TODO 2-07 - TO COMPLETE ****************************************
		 * call data source controller get all groceries method to add
		 * all groceries to table data observable list
		 */
		 tableData.addAll(fridgeDSC.getAllGroceries());
		 
	

		// =====================================================
		// ADD the remaining UI elements
		// NOTE: the order of the following TODO items can be 
		// 		 changed to satisfy your UI implementation goals
		// =====================================================

		/* TODO 2-08 - TO COMPLETE ****************************************
		 * filter container - part 1
		 * add all filter related UI elements you identified
		 // */
		 
		 	TextField filterTF = new TextField();
			FilteredList<Grocery> filteredList = new FilteredList<>(tableView.getItems(), grocery -> true);
			SortedList<Grocery> sortedList = new SortedList<>(filteredList);

			sortedList.comparatorProperty().bind(tableView.comparatorProperty());
			tableView.setItems(sortedList);
			
			
			
			String choice1 = "ITEM";
			String choice2 = "SECTION";
			String  choice3 = "BOUGHT_DAYS_AGO";
			ChoiceBox<String>  choices = new ChoiceBox<String>();
			choices.getItems().addAll(choice1, choice2, choice3);
			choices.setValue(choice1);
			//Label filterLB = new Label("Filter by Item Name: ");
			
			CheckBox  cb1 = new CheckBox("Show Expires Only");
			boolean state = false;
			cb1.setDisable(true);
		
			
			filterTF.textProperty().addListener(((observable, oldValue, newValue) ->
			{
				filteredList.setPredicate(grocery ->
					{
						if (newValue == null || newValue.isEmpty())
						{
							return true;
						}
						String filterString = newValue.toUpperCase();

						//if (grocery.getItemName().toUpperCase().contains(filterString)||
							//grocery.getDaysAgo().toUpperCase().contains(filterString)||
							//grocery.getSection().toString().toUpperCase().contains(filterString))
						if (grocery.getItemName().toUpperCase().contains(filterString)&& choices.getValue()=="ITEM")
						{
							return true;
						}
						else if(grocery.getSection().toString().toUpperCase().contains(filterString) && choices.getValue()=="SECTION")
						{
							return true;
						}
			
						//if choice box days ago then take filter string and turn into integer(will require try and catch) use Integer.parse.
						// compare it with days ago
						else 
						{
							//Refresh
							if(choices.getValue() == "BOUGHT_DAYS_AGO")
							{		
									try
									{	
										int filtdays =Integer.parseInt(extractInt(filterString));
										LocalDate date = grocery.getDate();
										int somedays = (int)fridgeDSC.calcDaysAgo(date);
										if( filtdays<=somedays)
										{
											return true;
										}
										else
										{
											return false;
										}										
									}
									catch(Exception exception)
									{
											
									  tableView.refresh();
									  //throw new IllegalStateException("something went wrong",exception);
									  return true;
										  
									}

							}
							else 
							{
								return false;
							}
				
						}
					});
			  tableView.refresh();
			  }));
				 

		/* TODO 2-09 - TO COMPLETE ****************************************
		 * filter container - part 2:
		 * - addListener to the "Filter By" ChoiceBox to clear the filter
		 *   text field vlaue and to enable the "Show Expire Only" CheckBox
		 *   if "BOUGHT_DAYS_AGO" is selected
		 */
		 //Creating checkBOX can expire 
			
		
			choices.valueProperty().addListener((observable, oldValue, newValue) ->
			{
				// Get font size
				if(choices.getValue().trim()=="BOUGHT_DAYS_AGO")
				{
					filterTF.setText("");
					cb1.setDisable(false);
					
			    }
				else if(choices.getValue().trim()=="ITEM")
				{
					filterTF.setText("");
					cb1.setDisable(true);
					cb1.setSelected(false);
				}
				else
				{
					filterTF.setText("");
					cb1.setDisable(true);
				}
			});
			
			//Show expire only check box
			cb1.setOnAction((e)->
			{
				filterTF.requestFocus();
				filterTF.setText("");
				filteredList.setPredicate(grocery->
				{
					if(cb1.isSelected() )
					{	

						return grocery.getItem().canExpire();
					}
					else
					{
						return true;		 
					}
					
				});
				
			});	
			
		 
		 

		/* TODO 2-10 - TO COMPLETE ****************************************
		 * filter container - part 2:
		 * - addListener to the "Filter By" ChoiceBox to clear and set focus 
		 *   to the filter text field and to enable the "Show Expire Only" 
		 *   CheckBox if "BOUGHT_DAYS_AGO" is selected
		 *
		 * - setOnAction on the "Show Expire Only" Checkbox to clear and 
		 *   set focus to the filter text field
		 */

		/* TODO 2-11 - TO COMPLETE ****************************************
		 * filter container - part 3:
		 * - create a filtered list
		 * - create a sorted list from the filtered list
		 * - bind comparators of sorted list with that of table view
		 * - set items of table view to be sorted list
		 * - set a change listener to text field to set the filter predicate
		 *   of filtered list
		 */		


		/* TODO 2-12 - TO COMPLETE ****************************************
		 * ACTION buttons: ADD, UPDATE ONE, DELETE, EXIT
		 * - ADD button sets the add UI elements to visible;
		 *   NOTE: the add input controls and container may have to be
		 * 		   defined before these action controls & container(s)
		 * - UPDATE ONE and DELETE buttons action need to check if a
		 *   table view row has been selected first before doing their
		 *   action; hint: should you also use an Alert confirmation?
         * - EXIT button. Use stage.close() after making sure that data is synced
		 */		
		 
		 
		//Creating Buttons Add, clear and save 
		Button addBt = new Button("ADD");
		Button clearBT =  new Button("CLEAR");
		Button saveBT = new Button("SAVE");
		
		
		//Creating textfield and label for quantity
		TextField quantitytf = new TextField();
		Label quantity_label = new Label("Quantity");
		quantity_label.setStyle("-fx-font-size : 15");
		quantitytf.setMaxWidth(130);
		
		//Wrapping them in VBox 		
		VBox quantityAddComponent = new VBox();
		quantityAddComponent.getChildren().addAll(quantity_label,quantitytf);
		
		//Creating combobox and label for adding items 
		Label item_combo = new Label("Item");
		item_combo.setStyle("-fx-font-size: 15");
        ComboBox<String> itemAddCombo = new ComboBox<>();
		itemAddCombo.setPromptText("Select Item");
		List<Item> mytemp = fridgeDSC.getAllItems();
		
		for(Item itemname:mytemp)
		{
			itemAddCombo.getItems().add(itemname.getName());
		}
		
		//Wrapping the combobox and label in VBox
		VBox itemAddComponent = new VBox();
		itemAddComponent.getChildren().addAll(item_combo,itemAddCombo);
		
		//Creating choiceBox and label for adding sections
		ChoiceBox <FridgeDSC.SECTION>choiceadd = new ChoiceBox<FridgeDSC.SECTION>();
		choiceadd.getItems().addAll(FridgeDSC.SECTION.values());
		choiceadd.setValue(FridgeDSC.SECTION.COOLING);
		
		Label sectionAdd = new Label("Section");
		sectionAdd.setStyle("-fx-font-size: 15");
		
		

		//Wrapping choicebox and lable in VBox
		VBox sectionAddComponent = new VBox();
		sectionAddComponent.getChildren().addAll(sectionAdd,choiceadd);
		
		
		
		//Wrapping all ADD button components in required structure
		HBox addBTcomponents = new HBox();
		HBox clearsave = new HBox();
		clearsave.getChildren().addAll(clearBT,saveBT);
		clearsave.setStyle("-fx-alignment: center");
		addBTcomponents.getChildren().addAll(itemAddComponent,sectionAddComponent,quantityAddComponent);
		addBTcomponents.setSpacing(100);
		addBTcomponents.setStyle("-fx-alignment: center");
		VBox addBox = new VBox();
		addBox.getChildren().addAll(addBTcomponents,clearsave);
		addBox.setVisible(false);
		

		 
		//AddBt set on action 
		addBt.setOnAction((e)->
		{
			addBox.setVisible(true);
		});

		//Save button set on action
		saveBT.setOnAction((e)->
		{
			try 
			{
				int newID = fridgeDSC.addGrocery(itemAddCombo.getValue(),Integer.parseInt(quantitytf.getText()),choiceadd.getValue());
				Grocery gr = new Grocery(newID,fridgeDSC.searchItem(itemAddCombo.getValue()),Integer.parseInt(quantitytf.getText()),choiceadd.getValue());
				tableData.add(gr);
				addBox.setVisible(false);
			}
			catch(Exception exception)
			{
				throw new RuntimeException(exception.getMessage());
			}			
		});
		
		//Clear Button set on action
		clearBT.setOnAction((e)->{
			
			addBox.setVisible(false);
			itemAddCombo.setValue(null);
			choiceadd.setValue(FridgeDSC.SECTION.COOLING);
			quantitytf.setText("");
			
			
			
		});
		
		//Update Button creation
		Button updateBT = new Button("UPDATE ONE");
		//Update Button set on action
		updateBT.setOnAction(e->
		{
			Grocery grocery = tableView.getSelectionModel().getSelectedItem();
			try
			{
					// Must call the DSC method first
					fridgeDSC.useGrocery(grocery.getId());
					
					for (Grocery g:tableData)
					{
						if(g.getId()==grocery.getId())
						{
							g.updateQuantity();
							tableView.getColumns().get(0).setVisible(false);
							tableView.getColumns().get(0).setVisible(true);
							tableView.refresh();
							break;
						}
					}
			}
			catch(Exception exception)
			{
				Alert error = new Alert(Alert.AlertType.ERROR);
				error.setTitle("Error");
				error.setContentText(exception+"There is only one : "+grocery.getItemName()+"("+" bought on\n"+grocery.getDateStr()+")"+" - use Delete instead");
				error.showAndWait();
				
			}
			
			
		});

		
	
		
		
		
		//Delete Button creation
		Button deleteBT = new Button("DELETE");
	
		//Delete button set on action
		deleteBT.setOnAction(e ->
		{
			// ask for confirmation
			Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
			confirm.setTitle("Really Delete?");
			confirm.setContentText("Are you sure you want to delete this Item?");

			Optional<ButtonType> result = confirm.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK)
			{
				Grocery grocery = tableView.getSelectionModel().getSelectedItem();


				try
				{
					// Must call the DSC method first
					fridgeDSC.removeGrocery(grocery.getId());
					tableData.remove(grocery);
				}
				catch(Exception exception)
				{
					throw new RuntimeException(exception.getMessage());
				}
			}
		});
		

		//Exit button creation 
		Button exitBT = new Button("EXIT");
		
		//Exit button set on action
		exitBT.setOnAction(e->
		{
			Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
			confirm.setTitle("Really Delete?");
			confirm.setContentText("Are you sure you want to exit?");

			Optional<ButtonType> result = confirm.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK)
				stage.close();
		});
		

		/* TODO 2-13 - TO COMPLETE ****************************************
		 * add input controls and container(s)
		 * - Item will list item data from the data source controller list
		 *   all items method
		 * - Section will list all sections defined in the data source
		 *   controller SECTION enum
		 * - Quantity: a texf field, self descriptive
		 * - CANCEL button: clears all input controls
		 * - SAVE button: sends the new grocery information to the data source
		 *   controller add grocery method; be mindful of exceptions when any
		 *   or all of the input controls are empty upon SAVE button click
		 */	

		// =====================================================================
		// SET UP the Stage
		// =====================================================================
		// 

		/* TODO 2-14 - TO COMPLETE ****************************************
         * - Create primary VBox container, add it to the scene add external style sheet to the scene
         *   - VBox root = new VBox(...);
		 *   - add all your containers, controls to the root VBox
         *   - add root container to the scene
         */
		
		HBox Horizonelements = new HBox(filterTF,choices,cb1);
		HBox bottomButtons = new HBox(addBt,updateBT,deleteBT,exitBT);
		VBox underAddButtons = new VBox(bottomButtons,addBox);
        VBox root = new VBox(); // modify
		root.getChildren().addAll(Horizonelements,tableView,underAddButtons);
		Scene scene = new Scene(root);
		//Adding CSS worksheet
		scene.getStylesheets().add(
		"fridge.css");
        /*  
         *   - add external style sheet to the scene - scene.getStylesheets().add(..);
         *   - add scene to stage
		 */
         
        stage.setScene(scene);
	}
	
	//Method to return only integers
	public String extractInt(String str) 
    { 
        // Replacing every non-digit number 
        // with a space(" ") 
        str = str.replaceAll("[^\\d]", " "); 
  
        // Remove extra spaces from the beginning 
        // and the ending of the string 
        str = str.trim(); 
  
        // Replace all the consecutive white 
        // spaces with a single space 
        str = str.replaceAll(" +", " "); 
  
        if (str.equals("")) 
            return ""; 
  
        return str; 
    }



	public void stop() throws Exception {

		/* TODO 2-15 - TO COMPLETE ****************************************
		 * call the data source controller database disconnect method
		 * NOTE: that database disconnect method throws exception
		 */
		 try 
		 {
			fridgeDSC = new FridgeDSC();
			fridgeDSC.disconnect();
		 }
		 catch(Exception e) 
		{
            System.out.println(e);
            e.printStackTrace();
		}
	}
}
