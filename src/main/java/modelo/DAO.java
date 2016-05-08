/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.List;

/**
 *
 * @author odeen
 */
public interface DAO
{

    public void insertar(Object objeto);

    public void actualizar(Object objeto);

    public void borrar(Object objeto);

    public Cliente buscarClientePorId(int id);

    public List<Cliente> buscarTodosClientes();

    public List<Cliente> buscarTodosClientes(String orden, Boolean asc,
            Integer tamañoPagina, Integer paginaActual);
    public Long contarTodosClientes();
}
