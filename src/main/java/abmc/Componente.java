/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abmc;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import modelo.DAO;

/**
 *
 * @author exes
 */
@FacesComponent(value = "miComponenteABMC")
public class Componente extends UINamingContainer {

    private String orden;
    private Boolean ascen;
    private Long tamañoPagina;
    private Long paginaActual = 0L;

    private Long numeroRegistros = 0L;
    private Long numeroPaginas = 0L;

    public List consultar(String nombreFuncion) {
        DAO dao = (DAO) this.getAttributes().get("dao");
        Class clase = dao.getClass();
        try {
            Method m = clase.getDeclaredMethod(nombreFuncion,
                    String.class, Boolean.class, Integer.class, Integer.class);
            numeroRegistros = (Long) evaluar("#{dao." + this.getAttributes().get("listadoCount") + "()}");
            numeroPaginas = ((Double) Math.ceil((double)numeroRegistros / tamañoPagina)).longValue();
            return (List) m.invoke(dao, orden, ascen, tamañoPagina.intValue(), paginaActual.intValue());

        } catch (Exception ex) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage("dt", new FacesMessage("Error al invocar "
                    + nombreFuncion + " " + ex.getMessage()));
            numeroPaginas = 0L;
            return Collections.EMPTY_LIST;
        }
    }

    public String ordenar(String orden, Boolean ascendente) {
        this.orden = orden;
        this.ascen = ascendente;
        return "";
    }

    public Boolean ordenActual(String orden, Boolean ascendente) {
        return !(orden.equals(this.orden)
                && Objects.equals(ascendente, this.ascen));
    }

    private Object evaluar(String expresion) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
        ELContext elContext = context.getELContext();
        ValueExpression vex = expressionFactory.createValueExpression(elContext, expresion, Object.class);
        return vex.getValue(elContext);
    }

    private enum Estado {

        LISTADO, EDICION, INSERCION, BORRADO
    };
    private Estado estado = Estado.LISTADO;

    public Long getPaginaActual() {
        return paginaActual;
    }

    public void setPaginaActual(Long paginaActual) {
        this.paginaActual = paginaActual;
    }

    public Boolean getEnListado() {
        return estado == Estado.LISTADO;
    }

    public Boolean getEnEdicion() {
        return estado == Estado.EDICION;
    }

    public Boolean getEnInsercion() {
        return estado == Estado.INSERCION;
    }

    public Boolean getEnBorrado() {
        return estado == Estado.BORRADO;
    }

    private Object actual;

    public Object getActual() {
        return actual;
    }

    @Override
    public void markInitialState() {
        super.markInitialState(); //To change body of generated methods, choose Tools | Templates.
        String facetaOrigen = "listado";
        String destino = "dt";
        moverContenidoFacetaAComponente(facetaOrigen, destino);
//        moverContenidoFacetaAComponente("borrado", "panelBorrado");
//        moverContenidoFacetaAComponente("edicion", "panelEdicion");
//        moverContenidoFacetaAComponente("insercion", "panelInsercion");
        if (this.orden == null) {
            this.orden = (String) this.getAttributes().get("ord");
            this.ascen = (Boolean) this.getAttributes().get("asc");
            this.tamañoPagina = (Long) this.getAttributes().get("tamañoPagina");
        }

    }

    private void moverContenidoFacetaAComponente(String facetaOrigen, String destino) {
        UIComponent facetaListado = this.getFacet(facetaOrigen);
        if (facetaListado instanceof UIPanel) {
            List<UIComponent> children = facetaListado.getChildren();
            findComponent(destino).getChildren().addAll(0, children);
        } else {
            findComponent(destino).getChildren().add(0, facetaListado);
        }
    }

    public String iniciarBorrado(Object actual) {
        estado = Estado.BORRADO;
        this.actual = actual;
        return "";
    }

    public String iniciarEdicion(Object actual) {
        estado = Estado.EDICION;
        this.actual = actual;
        return "";
    }

    public String iniciarInsertar() {
        estado = Estado.INSERCION;
        String clase = (String) this.getAttributes().get("clase");
        try {
            Class cl = Class.forName(clase);
            this.actual = cl.newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public String cancelarBorrado() {
        estado = Estado.LISTADO;
        this.actual = null;
        return "";
    }

    public String cancelarEdicion() {
        estado = Estado.LISTADO;
        this.actual = null;
        return "";
    }

    public String cancelarInsercion() {
        estado = Estado.LISTADO;
        this.actual = null;
        return "";
    }

    public String borrar() {
        String mensaje;
        try {
            DAO dao = (DAO) this.getAttributes().get("dao");
            dao.borrar(actual);
            estado = Estado.LISTADO;
            mensaje = "Registro borrado.";
        } catch (Exception e) {
            mensaje = "No se pudo borrar: " + e.getMessage();
        }
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage("dt", new FacesMessage(mensaje));
        return "";
    }
    public String borrar(Object actual) {
        String mensaje;
        try {
            DAO dao = (DAO) this.getAttributes().get("dao");
            dao.borrar(actual);
            mensaje = "Registro borrado.";
        } catch (Exception e) {
            mensaje = "No se pudo borrar: " + e.getMessage();
        }
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage("dt", new FacesMessage(mensaje));
        return "";
    }

    public String editar() {
        String mensaje;
        try {
            DAO dao = (DAO) this.getAttributes().get("dao");
            dao.actualizar(actual);
            estado = Estado.LISTADO;
            mensaje = "Registro guardado.";
        } catch (Exception e) {
            mensaje = "No se pudo guardar: " + e.getMessage();
        }
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage("dt", new FacesMessage(mensaje));
        return "";
    }

    public String insertar() {
        String mensaje;
        try {
            DAO dao = (DAO) this.getAttributes().get("dao");
            dao.insertar(actual);
            estado = Estado.LISTADO;
            mensaje = "Registro insertado.";
        } catch (Exception e) {
            mensaje = "No se pudo insertar: " + e.getMessage();
        }
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage("dt", new FacesMessage(mensaje));
        return "";
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Map<String, Object> valores = (Map<String, Object>) state;
        estado = (Estado) valores.get("estado");
        actual = valores.get("actual");

        tamañoPagina = (Long) valores.get("tamañoPagina");
        ascen = (Boolean) valores.get("asc");
        orden = (String) valores.get("orden");
        paginaActual = (Long) valores.get("paginaActual");
    }

    @Override
    public Object saveState(FacesContext context) {
        Map<String, Object> valores = new HashMap<>();
        valores.put("estado", estado);
        valores.put("actual", actual);

        valores.put("tamañoPagina", tamañoPagina);
        valores.put("asc", ascen);
        valores.put("orden", orden);
        valores.put("paginaActual", paginaActual);
        return valores;
    }

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }

    public Boolean getAscen() {
        return ascen;
    }

    public void setAscen(Boolean ascen) {
        this.ascen = ascen;
    }

    public Long getTamañoPagina() {
        return tamañoPagina;
    }

    public void setTamañoPagina(Long tamañoPagina) {
        this.tamañoPagina = tamañoPagina;
    }

    public Long getNumeroPaginas() {
        return numeroPaginas;
    }

    public void setNumeroPaginas(Long numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }

}
