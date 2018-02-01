package hello;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class UserRepositoryImpl implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void create(User user) {
        entityManager.persist(user);
    }

    @Override
    public void delete(User user) {
        if (entityManager.contains(user))

            entityManager.remove(user);

        else

            entityManager.remove(entityManager.merge(user));


    }

    @Override
    public void update(User user) {
        entityManager.merge(user);

        //  return;
    }

    @Override
    public User getById(long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User getByEmail(String email) {
        return entityManager.createQuery("from User where email=:email", User.class).setParameter("email", email).getSingleResult();
    }

    @Override
    public boolean existByEmail(String email) {
        List<User> resultList = entityManager.createQuery("from User where email=:email", User.class).setParameter("email", email).getResultList();
        return !resultList.isEmpty();
    }

    public List<User> getAllUsers() {
        List<User> resultList = entityManager.createQuery("from User", User.class).getResultList();
        return resultList;
    }
}
