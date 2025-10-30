/*
 * (Licença omitida para brevidade)
 */
package pt.psoft.g1.psoftg1.usermanagement.repositories;

import java.util.List;
import java.util.Optional;

// A excepção NotFoundException não deve ser conhecida pelo repositório.
// A camada de serviço é que a deve lançar.
// import pt.psoft.g1.psoftg1.exceptions.NotFoundException;

import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.usermanagement.services.SearchUsersQuery;

/**
 * Interface (Contrato) do Repositório de User.
 * Define as operações de persistência necessárias pela camada de Domínio/Aplicação.
 * Esta interface é "agnóstica", não sabe nada sobre SQL, H2, ou Mongo.
 */
public interface UserRepository {

	<S extends User> List<S> saveAll(Iterable<S> entities);

	<S extends User> S save(S entity);

	/**
	 * Alterado de Long para String para corresponder ao modelo de domínio User.java
	 */
	Optional<User> findById(String id);

	/**
	 * Método 'default getById' REMOVIDO.
	 * A lógica de negócio (verificar se está 'enabled' ou lançar NotFound)
	 * pertence à camada de Serviço (UserService), não ao repositório.
	 */
	// default User getById(final Long id) { ... } // <-- REMOVIDO

	Optional<User> findByUsername(String username);

	List<User> searchUsers(Page page, SearchUsersQuery query);

	List<User> findByNameName(String name);

	List<User> findByNameNameContains(String name);

	void delete(User user);
}