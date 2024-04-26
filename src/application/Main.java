package application;

import java.io.File;
//import java.io.File;
import java.io.IOException;
import java.util.ConcurrentModificationException;

//import java.util.List;

import engine.Game;
import exceptions.GameActionException;
import exceptions.InvalidTargetException;
import exceptions.MovementException;
import exceptions.NoAvailableResourcesException;
import exceptions.NotEnoughActionsException;
import javafx.application.Application;
import javafx.application.Platform;
import model.characters.Character;
import model.characters.Direction;
import javafx.scene.shape.*;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.characters.Fighter;
import model.characters.Hero;
import model.characters.Medic;
import model.characters.Zombie;
import model.collectibles.Supply;
import model.collectibles.Vaccine;
import model.world.Cell;
import model.world.CharacterCell;
import model.world.CollectibleCell;
//import model.world.TrapCell;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
//import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.image.Image;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
//import javafx.scene.media.AudioClip; 




public class Main extends Application implements EventHandler<ActionEvent> {
	Button attack = new Button();
	Button useSpecial = new Button();
	Button cure = new Button();
	Button endTurn = new Button();
	Button LEFT = new Button();
	Button RIGHT = new Button();
	Button UP = new Button();
	Button DOWN = new Button();
	Scene scene3, scene4;
	static int h=0;
	static DropShadow shadow= new DropShadow();
	static GridPane g;
	static Hero selected;
	static Character target;
	static Text t;
	public static Stage secondStage;
	static StackPane s;
	static Rectangle r;
	static HBox layout;
	static Color greyish= Color.rgb(128, 128, 128, 0.3);
	@Override
	public void start(Stage primaryStage) throws IOException {
		try {
			Game.loadHeroes("Heroes.csv");
		}
		catch (IOException e) {
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText("Error");
			errorAlert.setContentText("No Available Heroes to show");
			errorAlert.showAndWait();
			return;
		}
		//scene 1
		ImageView view= new ImageView(new Image("/IntroPic.png"));
		StackPane firstWindowLayout = new StackPane(view);
		Scene scene1 = new Scene(firstWindowLayout, 1634, 912);
		primaryStage.setScene(scene1);
		primaryStage.setFullScreen(true);
		primaryStage.show();

		// Wait for 2.5 seconds and switch to the second window
		Duration duration = Duration.seconds(2.5);
		Timeline timeline = new Timeline(new KeyFrame(duration, event -> {
			secondStage = new Stage();
			secondStage.setTitle("Last Of Us: Legacy");
			String audio= "src/Theme.mp3";
			Media m= new Media(new File(audio).toURI().toString());
			MediaPlayer media= new MediaPlayer(m);
			media.play();
			//scene 2
			ImageView view2= new ImageView(new Image("/trial.png"));
			view2.setFitWidth(1920);
			view2.setFitHeight(1000);
			view2.fitWidthProperty().bind(secondStage.widthProperty()); 
			view2.fitHeightProperty().bind(secondStage.heightProperty());
			ImageView strt = new ImageView(new Image("/start.png"));
			strt.setFitWidth(220);
			strt.setFitHeight(60);
			strt.setOnMouseClicked(e -> {
				secondStage.setScene(scene3);
				secondStage.setFullScreen(true);
			});
			StackPane secondWindowLayout = new StackPane(view2, strt);
			strt.setTranslateX(430);
			strt.setTranslateY(350);
			Scene scene2 = new Scene(secondWindowLayout, 1920, 1000);
			secondStage.setScene(scene2);
			secondStage.setFullScreen(true);
			secondStage.show();
			//scene 3
			ImageView back= new ImageView(new Image("/backgg.jpeg"));
			back.setFitWidth(1920);
			back.setFitHeight(1000);
			GridPane heros= new GridPane();
			heros.setPadding(new Insets(0,30,10,30));
			heros.setHgap(30);
			heros.setVgap(30);
			BorderPane choose = new BorderPane();
			StackPane bg= new StackPane(back, choose);
			choose.setLeft(heros);
			g= new GridPane();
			for(int i=0; i<Game.availableHeroes.size(); i++) {
				Hero her= Game.availableHeroes.get(i);
				ImageView heroPic= new ImageView(new Image("/" + her.getName() + ".png"));
				heroPic.setFitWidth(180);
				heroPic.setFitHeight(200);
				Text heroInfo = new Text(getHeroInfo(i));
				heroInfo.setFont(Font.font("Papyrus", FontWeight.BOLD, 40));
				heroInfo.setFill(Color.CRIMSON);
				heroInfo.setEffect(shadow);
				final int index = i;
				heroPic.setOnMouseEntered(e -> choose.setRight(heroInfo));
				heroPic.setOnMouseClicked(e -> {
					h = index;
					selected= Game.availableHeroes.get(index);
					Game.startGame(Game.availableHeroes.get(h));
					fillMap();
					ImageView mapBack= new ImageView(new Image("/mapBack.jpg"));
					g.setGridLinesVisible(true);
					g.setVisible(true);
					s= new StackPane();
					StackPane.setMargin(g, new Insets(40,0,0,100));	
					ImageView att= new ImageView(new Image("/attack.png"));
					att.setFitHeight(50);
					att.setFitWidth(85);
					attack.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
					attack.setGraphic(att);
					ImageView spe= new ImageView(new Image("/special.png"));
					spe.setFitHeight(70);
					spe.setFitWidth(110);
					useSpecial.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
					useSpecial.setGraphic(spe);
					ImageView cu= new ImageView(new Image("/cure.png"));
					cu.setFitHeight(50);
					cu.setFitWidth(85);
					cure.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
					cure.setGraphic(cu);
					ImageView end= new ImageView(new Image("/endturn.png"));
					end.setFitHeight(70);
					end.setFitWidth(120);
					endTurn.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
					endTurn.setGraphic(end);
					allHeroes();
					r = new Rectangle(0,0,300,400);
					r.setFill(Color.BLACK);
					getCurrentInfo();
					EventHandler<ActionEvent> b= new EventHandler<ActionEvent>(){
			            @Override
			            public void handle(ActionEvent event) {
			                handleButtonClick(event);
			                g.getChildren().clear(); 
			                g.setGridLinesVisible(false);
			                g.getColumnConstraints().clear();
			                g.getRowConstraints().clear();
			                fillMap();
			                getCurrentInfo();
			                layout.getChildren().clear();
			                allHeroes();
			                r = new Rectangle(0,0,300,400);
			                r.setFill(Color.BLACK);
			                s.getChildren().addAll(r,t,layout);
			                StackPane.setMargin(t, new Insets(-350,0,0,1000));
			                StackPane.setMargin(r, new Insets(-350,0,0,1000));
			                StackPane.setMargin(layout, new Insets(-800,0,0,900));
			                //StackPane.setMargin(j, new Insets(-700,0,0,700));
			            }			            
			        };
					attack.setOnAction(b);
					useSpecial.setOnAction(b);
					cure.setOnAction(b);
					endTurn.setOnAction(e2 -> {
						try {
							Game.endTurn();
							 if (Game.checkGameOver()) {
								 Popup n= new Popup();
								 StackPane co= new StackPane();
								 Rectangle now= new Rectangle(0,0,300,100);
								 Button ok= new Button("Quit");
								 ok.setOnMouseClicked(e3 -> Platform.exit());
								 Text y = new Text ("YOU LOST");
				                 y.setFont(Font.font("Times New Roman", FontWeight.BOLD,42));
								 y.setFill(Color.RED);
								 VBox v = new VBox();
								 v.getChildren().addAll(y,ok);
								 co.getChildren().addAll(now,v);
								 n.getContent().add(co);
								 n.show(secondStage);
				                }	                
						}
						catch(Exception ex) {
							String msg = ex.getMessage();
//							if(ex instanceof InvalidTargetException)
//								msg=((InvalidTargetException)ex).getMessage();
//							else 
//								msg= ((NotEnoughActionsException)ex).getMessage();
							if (! (ex instanceof ConcurrentModificationException))
								AlertBox.display("Error","Invalid Action: ", msg);						
						}
						g.getChildren().clear(); 
		                g.setGridLinesVisible(false);
		                g.getColumnConstraints().clear();
		                g.getRowConstraints().clear();
		                fillMap();
		                getCurrentInfo();
		                layout.getChildren().clear();
		                allHeroes();
		                r = new Rectangle(0,0,300,400);
		                r.setFill(Color.BLACK);
		                s.getChildren().addAll(r,t,layout);
		                StackPane.setMargin(t, new Insets(-350,0,0,1000));
		                StackPane.setMargin(r, new Insets(-350,0,0,1000));
		                StackPane.setMargin(layout, new Insets(-800,0,0,900));
					});
					
					ImageView up= new ImageView(new Image("/UP.png"));
					ImageView down= new ImageView(new Image("/DOWN.png"));
					ImageView left= new ImageView(new Image("/LEFT.png"));
					ImageView right= new ImageView(new Image("/RIGHT.png"));
					up.setFitWidth(55);
					up.setFitHeight(55);
					down.setFitWidth(55);
					down.setFitHeight(55);
					left.setFitWidth(55);
					left.setFitHeight(55);
					right.setFitWidth(55);
					right.setFitHeight(55);
					UP.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
					DOWN.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
					LEFT.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
					RIGHT.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
					UP.setGraphic(up);
					DOWN.setGraphic(down);
					LEFT.setGraphic(left);
					RIGHT.setGraphic(right);
					LEFT.setOnAction(b);
					RIGHT.setOnAction(b);		
					UP.setOnAction(b);
					DOWN.setOnAction(b);
					StackPane.setMargin(layout, new Insets(-800,0,0,900));
					StackPane.setMargin(t, new Insets(-350,0,0,1000));
	                StackPane.setMargin(r, new Insets(-350,0,0,1000));
					StackPane.setMargin(attack, new Insets(700, 0,0,500));
					StackPane.setMargin(useSpecial, new Insets(700, 0,0,750));
					StackPane.setMargin(cure, new Insets(700, 0, 0 , 1000));
					StackPane.setMargin(endTurn, new Insets(840, 0, 0 , 750));
					StackPane.setMargin(LEFT, new Insets(400, 0,0,650));
					StackPane.setMargin(RIGHT, new Insets(400, 0,0,850));
					StackPane.setMargin(UP, new Insets(300, 0,0,750));
					StackPane.setMargin(DOWN, new Insets(500, 0,0,750));
					s.getChildren().addAll(mapBack,g, attack, useSpecial, cure, endTurn, LEFT, RIGHT, UP, DOWN,r,t,layout);
					scene4 = new Scene(s, 1920, 1000);	
					secondStage.setScene(scene4);
					secondStage.setFullScreen(true);
				});
				GridPane.setConstraints(heroPic, i%2, i/2);
				heros.getChildren().add(heroPic);
			}
		    scene3 = new Scene(bg, 1920, 1000);							
		    
			// Close the first window
			primaryStage.close();
		}));
		timeline.play();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void handleButtonClick(ActionEvent e) {
		if (Game.heroes.contains(selected)) {		
		if(e.getSource() == attack) {
			try {
				selected.setTarget(target);
				selected.attack();
				 if (Game.checkGameOver()) {
					 Popup n= new Popup();
					 StackPane co= new StackPane();
					 Rectangle now= new Rectangle(0,0,300,100);
					 Button ok= new Button("Quit");
					 ok.setOnMouseClicked(e3 -> Platform.exit());
					 Text y = new Text ("YOU LOST");
	                 y.setFont(Font.font("Times New Roman", FontWeight.BOLD,42));
					 y.setFill(Color.RED);
					 VBox v = new VBox();
					 v.getChildren().addAll(y,ok);
					 co.getChildren().addAll(now,v);
					 n.getContent().add(co);
					 n.show(secondStage);
	                }
			}
			catch(GameActionException ex) {
				String msg;
				if(ex instanceof InvalidTargetException)
					msg=((InvalidTargetException)ex).getMessage();
				else 
					msg= ((NotEnoughActionsException)ex).getMessage();
				AlertBox.display("Error","Invalid Action: " ,msg);	
			}
		}
		else if(e.getSource()== useSpecial) {
			try {
				selected.setTarget(target);
				selected.useSpecial();
			}
			catch(GameActionException ex) {
				String msg;
				if(ex instanceof NoAvailableResourcesException)
					msg= ((NoAvailableResourcesException)ex).getMessage();
				else
					msg=((InvalidTargetException)ex).getMessage();
				AlertBox.display("Error","Invalid Action: ", msg);
			}
		}
		else if(e.getSource() == cure) {
			try {
				selected.setTarget(target);
				selected.cure();
				if (Game.checkWin()) {
					Popup n= new Popup();
					StackPane co= new StackPane();
					Rectangle now= new Rectangle(0,0,300,100);
					Button ok= new Button("Quit");
					ok.setOnMouseClicked(e3 -> Platform.exit());
					Text y = new Text ("YOU WIN");
	                y.setFont(Font.font("Times New Roman", FontWeight.BOLD,42));
					y.setFill(Color.RED);
					VBox v = new VBox();
					v.getChildren().addAll(y,ok);
					co.getChildren().addAll(now,v);
					n.getContent().add(co);
					n.show(secondStage);
                }
			}
			catch(GameActionException ex) {
				String msg;
				if(ex instanceof NoAvailableResourcesException)
					msg= ((NoAvailableResourcesException)ex).getMessage();
				else if(ex instanceof InvalidTargetException)
					msg=((InvalidTargetException)ex).getMessage();
				else
					msg= ((NotEnoughActionsException)ex).getMessage();
				AlertBox.display("Error","Invalid Action: ", msg);
			}
		}

		else {
			Direction d;
			if(e.getSource() == LEFT) 
				d= Direction.LEFT;
			else if(e.getSource() == RIGHT)
				d= Direction.RIGHT;
			else if(e.getSource() == UP)
				d= Direction.UP;
			else 
				d= Direction.DOWN;
			try {
				int currhp = selected.getCurrentHp();
				selected.move(d);
				int newhp = selected.getCurrentHp();
				if(newhp<currhp) {
					int trap = currhp-newhp;
					AlertBox.display("Caution","Oops", "You have entered a trap"+ "\n" +"Your current HP has been decremented by " + trap);
				
				}
				if (Game.checkGameOver()) {
					Popup n= new Popup();
					 StackPane co= new StackPane();
					 Rectangle now= new Rectangle(0,0,300,100);
					 Button ok= new Button("Quit");
					 ok.setOnMouseClicked(e3 -> Platform.exit());
					 Text y = new Text ("YOU LOST");
	                 y.setFont(Font.font("Times New Roman", FontWeight.BOLD,42));
					 y.setFill(Color.RED);
					 VBox v = new VBox();
					 v.getChildren().addAll(y,ok);
					 co.getChildren().addAll(now,v);
					 n.getContent().add(co);
					 n.show(secondStage);
                }
			}
			catch(GameActionException ex) {
				String msg;
				if(ex instanceof MovementException)
					msg=((MovementException)ex).getMessage();
				else
					msg= ((NotEnoughActionsException)ex).getMessage();
				AlertBox.display("Error", "Invalid Action: ",msg);
			}
		}
	}
		else {
			if (Game.heroes.size()>0)
				selected = Game.heroes.get(0);
			else  {
				Popup n= new Popup();
				 StackPane co= new StackPane();
				 Rectangle now= new Rectangle(0,0,300,100);
				 Button ok= new Button("Quit");
				 ok.setOnMouseClicked(e3 -> Platform.exit());
				 Text y = new Text ("YOU LOST");
                y.setFont(Font.font("Times New Roman", FontWeight.BOLD,42));
				 y.setFill(Color.RED);
				 VBox v = new VBox();
				 v.getChildren().addAll(y,ok);
				 co.getChildren().addAll(now,v);
				 n.getContent().add(co);
				 n.show(secondStage);
                }
				
		}
}
	
	public static String getHeroInfo(int i) {
		Hero h1= Game.availableHeroes.get(i);
		String s="Name: " + h1.getName() + "\n" + "Type: ";
		if(h1 instanceof Fighter) 
			s+= "FIGHTER";
		else if(h1 instanceof Medic)
			s+= "MEDIC";
		else 
			s+= "EXPLORER";
		s+= "\n" + "Maximum Health Points: " + h1.getMaxHp() + "\n" + "Maximum Actions/Turn: " + h1.getMaxActions() 
		+ "\n" + "Attack Damage: " + h1.getAttackDmg();

		return s;
	}

	public static void fillMap() {	
		for(int i=0; i<Game.map.length; i++) {
			for(int j=0; j<Game.map[i].length; j++) {
				StackPane pane = new StackPane();
				Rectangle rec = new Rectangle(0,0,50,50);
				pane.getChildren().addAll(rec);
				rec.setFill(Color.DARKOLIVEGREEN);
				Cell c= Game.map[i][j];
				if(c.isVisible()) {
					if(c instanceof CharacterCell) {
						Character curr= ((CharacterCell) c).getCharacter();
						if(curr instanceof Zombie) {			
							ImageView zomb = new ImageView(new Image("/Zombie.jpeg",30,30,false,false));
							pane.getChildren().add(zomb);
							zomb.setOnMouseClicked(e -> target=curr);
						}
						if(curr instanceof Hero) {
							String name= curr.getName();
							ImageView heroCurr= new ImageView(new Image("/" + name + ".png",30,30,false,false));
							pane.getChildren().add(heroCurr);
							heroCurr.setOnMouseClicked(e -> {
								if(e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) 
									target= curr;
								else {
									selected=((Hero)curr);
									r = new Rectangle(0,0,300,400);
									r.setFill(Color.BLACK);
									getCurrentInfo();
									StackPane.setMargin(r, new Insets(-350,0,0,1000));
									StackPane.setMargin(t, new Insets(-350,0,0,1000));
									s.getChildren().addAll(r,t);
								}    
							});
						}
					}
					if(c instanceof CollectibleCell) {
						if(((CollectibleCell)c).getCollectible() instanceof Vaccine) {
							ImageView vaccine= new ImageView(new Image("/Vaccine.png",30,30,false,false));
							pane.getChildren().add(vaccine);
						}
						if(((CollectibleCell)c).getCollectible() instanceof Supply) {
							Image supply = new Image("/Supply.png",30,30,false,false);
							pane.getChildren().add(new ImageView(supply));
						}
					}
				}
				else {
					rec.setFill(greyish);
					//rec.setFill(Color.TRANSPARENT);
				}
				g.add(pane , j, 14-i);
			}
		}
		g.setGridLinesVisible(true);
		g.setVisible(true);
	}
	
	public static void getCurrentInfo() {
		Hero current = selected;
		String s="Name: " + current.getName() + "\n" + "Type: ";
		if(current instanceof Fighter) 
			s+= "FIGHTER";
		else if(current instanceof Medic)
			s+= "MEDIC";
		else 
			s+= "EXPLORER";
		s+= "\n" + "Current HP: " + current.getCurrentHp() + "\n" + "Attack Damage: " + current.getAttackDmg()
		+ "\n" +"Action Points: " + current.getActionsAvailable() + "\n" + "Supplies: " + selected.getSupplyInventory().size()
		+ "\n" + "Vaccines: " + current.getVaccineInventory().size();
		t= new Text(s);
		t.setFont(Font.font("Papyrus", FontWeight.BOLD, 32));
		t.setFill(Color.CRIMSON);
		t.setEffect(shadow);
	}
	public static void allHeroes() {
		layout = new HBox(20);
		layout.setMaxHeight(100);
		Text my = new Text("My Heroes: ");
		Button m;
		my.setFont(Font.font("Papyrus", FontWeight.BOLD,24));
		my.setFill(Color.WHITE);
		layout.getChildren().add(my);
		for(int i = 0 ; i<Game.heroes.size(); i++) {
			m = new Button(Game.heroes.get(i).getName());
			m.setDisable(false);
			//m.setFont(Font.font("Papyrus", FontWeight.BOLD,24));
			//m.setBackground(Background.fill(Color.WHITE));
			layout.getChildren().add(m);
			final int in = i;
			m.setOnMouseClicked(e -> {
				AlertBox.display2(in);
			});
		}
	}

	@Override
	public void handle(ActionEvent arg0) {
		// TODO Auto-generated method stub		
	}
	
}

