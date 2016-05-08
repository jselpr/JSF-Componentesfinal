/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;



/**
 *
 * @author odeen
 */

@Transactional(propagation=Propagation.REQUIRED)
@Repository(value = "dao")
public class DAOImpl implements DAO {

    @PersistenceContext
    private EntityManager em;
    
    public void insertar(Object objeto) {
        em.persist(objeto);
    }

    public void actualizar(Object objeto) {
        objeto = em.merge(objeto);
        em.persist(objeto);
    }

    public void borrar(Object objeto) {
        objeto = em.merge(objeto);
        em.remove(objeto);
    }

    public Cliente buscarClientePorId(int id) {
        return em.find(Cliente.class, id);
    }

    public List<Cliente> buscarTodosClientes() {
        return em.createQuery("SELECT c FROM Cliente c").getResultList();
    }
    public List<Cliente> buscarTodosClientes(String orden,Boolean asc,
            Integer tamañoPagina, Integer paginaActual) {
        String consulta = "SELECT c FROM Cliente c";
        if(orden!=null){
            consulta += " ORDER BY c."+orden+" "+((asc)?"ASC":"DESC");
        }
        return em.createQuery(consulta)
                .setMaxResults(tamañoPagina)
                .setFirstResult(tamañoPagina*paginaActual)
                .getResultList();
    }
    public Long contarTodosClientes(){
        String consulta = "SELECT COUNT(c) FROM Cliente c";
        return (Long) em.createQuery(consulta).getSingleResult();
    }
    
}
