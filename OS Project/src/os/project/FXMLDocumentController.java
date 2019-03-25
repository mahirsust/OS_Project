/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os.project;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class FXMLDocumentController implements Initializable {

    @FXML
    private Button my_computer;
    @FXML
    private Button recycle_bin;
    private Button shut_down;
    private Button restart;
    @FXML
    static public AnchorPane pane;
    public int tag = 0;
    //@FXML
    //private ChoiceBox<String> choice;
    @FXML
    private ChoiceBox<String> cb;
    @FXML
    private Text text;
    @FXML
    private Button searchBtn;
    @FXML
    private TextField file_name;
    @FXML
    private TextField directory_name;
    public String name1="";
    boolean found;

    //public ObservableList<String> choiceList = FXCollections.observableArrayList("Shut Down", "Restart");
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cb.getItems().addAll("Shut Down", "Restart");
        //choice.setItems(choiceList);
        pane = new AnchorPane();
        pane.setPrefSize(1366, 768);

        cb.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue)
                -> {
            if (newValue == "Shut Down") {
                shut_down.fire();
            } else if (newValue == "Restart") {
                restart.fire();
            }
        }
        );
    }

    @FXML
    private void openMyComputer(ActionEvent event) {
        //tag=0;
        mao(my_computer);
        //System.out.println(tag+"pc");
        if (tag > 0) {
            try {
                //Runtime.getRuntime().exec("cmd /c start explorer");
                Runtime.getRuntime().exec("cmd /c start shell:mycomputerFolder");
                tag = 0;
            } catch (Exception e) {

            }
        }
    }

    @FXML
    private void openRecycleBin(ActionEvent event) {
        //tag=0;
        mao(recycle_bin);
        //System.out.println(tag+"rc");
        if (tag > 0) {
            try {
                Process pr = Runtime.getRuntime().exec("cmd /c start shell:RecycleBinFolder");
                pr.waitFor();
                tag = 0;
            } catch (Exception e) {
            }
        }
    }

    private void handleShutDown(ActionEvent event) {
        try {
            Process pr = Runtime.getRuntime().exec("cmd /c shutdown -s"); // for shutdown

        } catch (Exception e) {

        }
    }

    private void handleRestart(ActionEvent event) {
        try {

            Process pr = Runtime.getRuntime().exec("cmd /c shutdown -r"); // for restart

        } catch (Exception e) {

        }
    }

    public void mao(Button in) {
        final Delta dragDelta = new Delta();
        in.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        System.out.println("Double clicked");
                        tag++;
                        if (in == my_computer) {
                            try {
                                //Runtime.getRuntime().exec("cmd /c start explorer");
                                Runtime.getRuntime().exec("cmd /c start shell:mycomputerFolder");
                                tag = 0;
                            } catch (Exception e) {

                            }
                        } else if (in == recycle_bin) {
                            try {
                                Process pr = Runtime.getRuntime().exec("cmd /c start shell:RecycleBinFolder");
                                pr.waitFor();
                                tag = 0;
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }
        });
        if (tag == 0) {
            in.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    // record a delta distance for the drag and drop operation.
                    dragDelta.x = in.getLayoutX() - mouseEvent.getSceneX();
                    dragDelta.y = in.getLayoutY() - mouseEvent.getSceneY();
                    in.setCursor(Cursor.MOVE);
                    tag = 0;
                }
            });
            in.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    in.setCursor(Cursor.HAND);
                    tag = 0;
                }
            });
            in.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    in.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
                    in.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
                    tag = 0;
                }
            });

            in.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    in.setCursor(Cursor.HAND);
                    tag = 0;
                }
            });
        }

    }

    @FXML
    private void searchBtnClicked(ActionEvent event) {
       text.setText("");
       name1 = "";
       found = false;
       String name = file_name.getText();
       String directory = directory_name.getText();
       findFile(name,new File(directory));
       System.out.println("Here " + name1);
       if(found == false) text.setText("No file Found!!");
       else{
           String st = "File found in " + name1;
           text.setText(st);
       } 
    }
    
    public void findFile(String name,File file)
    {
        File[] list = file.listFiles();
        if(list!=null)
        for (File fil : list)
        {
            if (fil.isDirectory())
            {
                findFile(name,fil);
            }
            else if (name.equalsIgnoreCase(fil.getName()))
            {
                //System.out.println(fil.getParentFile());
                found = true;
                name1 += fil.getParentFile().toString();
                name1 += '\n';
            }
        }
    }

    // records relative x and y co-ordinates.
    class Delta {

        double x, y;
    }

    private static FXMLDocumentController instance;

    public FXMLDocumentController() {
        instance = this;
    }
// static method to get instance of view

    public static FXMLDocumentController getInstance() {
        return instance;
    }
}
