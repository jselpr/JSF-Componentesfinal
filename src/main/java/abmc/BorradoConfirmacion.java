/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abmc;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.FacesBehavior;

/**
 *
 * @author odeen
 */
@FacesBehavior("Borrado.Confirmacion")
public class BorradoConfirmacion extends ClientBehaviorBase
{
    
    
    
  @Override
  public String getScript( ClientBehaviorContext behaviorContext) {
      //devuelve el componente
      Componente componente = ((Componente)behaviorContext.getComponent().getParent().getParent().getParent().getParent().getParent());
      behaviorContext.getParameters();
      UIComponent compo = behaviorContext.getComponent();
      String mes = (String) compo.getAttributes().get("mensaje");
      int count = compo.getChildCount();
      
      return "return confirm($(window))";
  }
}