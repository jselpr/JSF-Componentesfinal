/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abmc;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;

/**
 *
 * @author joselopezruiz
 */
@FacesComponent(value = "confirmacion")
public class Confirmacion extends UINamingContainer  {
    private UIComponent comp;
    
    
    
    private String message;

    public UIComponent getComp() {
        return comp;
    }

    public void setComp(UIComponent comp) {
        this.comp = comp;
    }
    
    
    

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
    
}
