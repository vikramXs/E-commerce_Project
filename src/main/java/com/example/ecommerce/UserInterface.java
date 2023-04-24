package com.example.ecommerce;

import javafx.beans.Observable;
import javafx.beans.value.ObservableListValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class UserInterface {

    GridPane loginPage;
    HBox headerBar;

    HBox footerbar;
    Button signInButton;

    Label welcomeLabel;

    VBox body;
    Customer loggedInCustomer;

    ProductList productList = new ProductList();

    VBox productPage;

    Button placeOrderButton = new Button("Place Order");

    ObservableList<Product> itemInCart = FXCollections.observableArrayList();
     public BorderPane createContent(){
        BorderPane root = new BorderPane();
        root.setPrefSize(800,600);
       //root.getChildren().add(loginPage);
         root.setTop(headerBar);
        //root.setCenter(loginPage);
         body = new VBox();
         body.setPadding(new Insets(10));
         body.setAlignment(Pos.CENTER);
         root.setCenter(body);
         productPage = productList.getAllProducts();
         body.getChildren().add(productPage);

         root.setBottom(footerbar);

        return root;
    }

    public UserInterface(){
         createLoginPage();
         createHeaderBar();
         createFooterBar();
    }

    private void createLoginPage(){
        Text userNameText = new Text("User Name");
        Text passwordText = new Text("Password");

        TextField userName = new TextField("vikram@gmail.com");
        userName.setPromptText("Type Your userName Here");
        PasswordField password = new PasswordField();
        password.setText("abc123");
        password.setPromptText("Type Your Password Here");
        Label messageLabel = new Label("Hi");

        Button loginButton = new Button("Login");

        loginPage = new GridPane();
       // loginPage.setStyle(" -fx-background-color: grey;");
        loginPage.setAlignment(Pos.CENTER);
        loginPage.setHgap(10);
        loginPage.setVgap(10);
        loginPage.add(userNameText,0,0);
        loginPage.add(userName,1,0);
        loginPage.add(passwordText,0,1);
        loginPage.add(password,1,1);
        loginPage.add(messageLabel,0,2);
        loginPage.add(loginButton,1, 2);

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String name = userName.getText();
                String pass = password.getText();
                Login login = new Login();
                loggedInCustomer = login.customerLogin(name,pass);
                if(loggedInCustomer!=null){
                    messageLabel.setText("Welcome " +loggedInCustomer.getName());
                    welcomeLabel.setText("Welcome."+ loggedInCustomer.getName());
                    headerBar.getChildren().add(welcomeLabel);
                    body.getChildren().clear();
                    body.getChildren().add(productPage);

                }
                else{
                    messageLabel.setText("Login Failed !! please give correct username and password.");
                }
            }
        });
    }

    private void createHeaderBar(){
         Button homeButton = new Button();
        Image image = new Image("C:\\Users\\vikram\\IdeaProjects\\E-commerce\\src\\imageE.jpg");
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(80);
        homeButton.setGraphic(imageView);

         TextField searchBar = new TextField();
         searchBar.setPromptText("Search here");
         searchBar.setPrefWidth(180);

         Button searchButton = new Button("Search");

         signInButton = new Button("Sign In");
         welcomeLabel = new Label();

         Button cartButton = new Button("Cart");

         Button orderButton = new Button("Orders");

         headerBar = new HBox();
         //    headerBar.setStyle(" -fx-background-color: grey;");
         headerBar.setPadding(new Insets(10));
         headerBar.setSpacing(10);
         headerBar.setAlignment(Pos.CENTER);
         headerBar.getChildren().addAll(homeButton,searchBar,searchButton,signInButton,cartButton,orderButton);

         signInButton.setOnAction(new EventHandler<ActionEvent>() {
             @Override
             public void handle(ActionEvent actionEvent) {
                body.getChildren().clear(); // remove everything
                body.getChildren().add(loginPage); //put login page
                 headerBar.getChildren().remove(signInButton);
             }
         });

         cartButton.setOnAction(new EventHandler<ActionEvent>() {
             @Override
             public void handle(ActionEvent actionEvent) {
                 body.getChildren().clear();
                 VBox prodPage = productList.getProductsInCart(itemInCart);
                 prodPage.setAlignment(Pos.CENTER);
                 prodPage.setSpacing(10);
                 prodPage.getChildren().add(placeOrderButton);
                 body.getChildren().add(prodPage);
                 footerbar.setVisible(false);// all cases need to be handled for this
             }
         });

         placeOrderButton.setOnAction(new EventHandler<ActionEvent>() {
             @Override
             public void handle(ActionEvent actionEvent) {
                 //need list of product ans customer
                 if(itemInCart==null){
                     //please select the product first to place order
                     showDialog("Please add some products in cart to place order!");
                     return;
                 }
                 if(loggedInCustomer == null){
                     showDialog("Login first to place order");
                     return;
                 }
                 int count = Order.placeMultipleOrder(loggedInCustomer,itemInCart);
                 if(count != 0){
                     showDialog("order for "+count+" products placed successfully!");
                 }else {
                     showDialog("Order failed!!");
                 }
             }
         });

         homeButton.setOnAction(new EventHandler<ActionEvent>() {
             @Override
             public void handle(ActionEvent actionEvent) {
                 body.getChildren().clear();
                 body.getChildren().add(productPage);
                 footerbar.setVisible(true);
                 if(loggedInCustomer== null && headerBar.getChildren().indexOf(signInButton) == -1){
                         headerBar.getChildren().add(signInButton);
                 }
             }
         });

    }

    private void createFooterBar(){
         Button buyNowButton = new Button("Buy Now");
         Button addToCartButton = new Button("Add to Cart");

         footerbar = new HBox();
        //    headerBar.setStyle(" -fx-background-color: grey;");
        footerbar.setPadding(new Insets(10));
        footerbar.setSpacing(10);
        footerbar.setAlignment(Pos.CENTER);
        footerbar.getChildren().addAll(buyNowButton,addToCartButton);

        buyNowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product = productList.getSelectedProduct();
                if(product==null){
                    //please select the product first to place order
                    showDialog("Please select the product first to place order!");
                    return;
                }
                if(loggedInCustomer == null){
                    showDialog("Login first to place order");
                    return;
                }
                boolean status = Order.placeOrder(loggedInCustomer,product);
                if(status == true){
                    showDialog("order placed successfully!");
                }else {
                    showDialog("Order failed!!");
                }
            }
        });

        addToCartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product = productList.getSelectedProduct();
                if(product==null){
                    //please select the product first to place order
                    showDialog("Please select the product first to add it to Cart!");
                    return;
                }
                itemInCart.add(product);
                showDialog("Selected Items has been added to the Cart successfully. ");
            }
        });
     }

     private void showDialog(String message){
         Alert alert = new Alert(Alert.AlertType.INFORMATION);
         alert.setHeaderText(null);
         alert.setContentText(message);
         alert.setTitle("Message");
         alert.showAndWait();
     }
}
