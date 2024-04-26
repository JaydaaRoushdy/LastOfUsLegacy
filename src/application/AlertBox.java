package application;

import javafx.stage.*;
import model.characters.Fighter;
import model.characters.Hero;
import model.characters.Medic;
import engine.Game;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;


public class AlertBox {
	
	public static void display(String header,String mid, String text) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(header);
        alert.setHeaderText(mid);
        alert.setContentText(text);
        Window owner = Main.secondStage;
        alert.initOwner(owner);
        alert.showAndWait();		
	}
	public static void display2(int index) {
		Hero current = Game.heroes.get(index);
		String s="Type: ";
		if(current instanceof Fighter) 
			s+= "FIGHTER";
		else if(current instanceof Medic)
			s+= "MEDIC";
		else 
			s+= "EXPLORER";
		s+= "\n" + "Current HP: " + current.getCurrentHp() + "\n" + "Attack Damage: " + current.getAttackDmg()
		+ "\n" +"Max Actions/Turn: " + current.getMaxActions();
		Alert alert = new Alert(Alert.AlertType.NONE);
		alert.setTitle("Hero Info");
        alert.setHeaderText("Name: " + current.getName());
        alert.setContentText(s);
        Window owner = Main.secondStage;
        alert.initOwner(owner);
        alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
        alert.showAndWait();
        
	}

}
