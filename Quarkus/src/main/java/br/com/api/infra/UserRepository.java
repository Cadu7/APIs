package br.com.api.infra;

import br.com.api.domain.interfaces.IUserRepository;
import br.com.api.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.logging.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.ObjLongConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
@Transactional
public class UserRepository implements IUserRepository, PanacheRepositoryBase<User, UUID> {
    
    @Override
    public Optional<User> findByUserName(String userName) {
        return find("user_name = ?1", userName).firstResultOptional();
    }
    
    @Override
    public void persistOrUpdate(User user) {
        findByIdOptional(user.getId())
                .ifPresentOrElse(userdb -> {
                            String sql = createSQL();
                            Map<String, Object> params = getParams(user);
                            update(sql, params);
                        }
                        ,
                        () -> persist(user));
        
    }
    
    private String createSQL() {
        Class<User> userClass = User.class;
        List<Field> fields = Arrays.stream(userClass.getDeclaredFields()).filter(field -> !field.getName().contains("hibernate")).collect(Collectors.toList());
        
        List<String> sqlBuild = new ArrayList<>();
        for (Field field : fields) {
            String fieldType = field.getName().toLowerCase();
            String name = field.getName().toLowerCase();
            if (field.isAnnotationPresent(Column.class)) {
                fieldType = field.getAnnotation(Column.class).name();
            }
            if (!field.isAnnotationPresent(Id.class)) {
                sqlBuild.add(fieldType + " = :" + name + " ");
            }
        }
        return sqlBuild.stream().collect(Collectors.joining(",")) + " WHERE id = :id ";
    }
    
    private Map<String, Object> getParams(User user) {
        Class<User> userClass = User.class;
        List<Method> methods = Arrays.stream(userClass.getDeclaredMethods()).collect(Collectors.toList());
        List<Field> fields = Arrays.stream(userClass.getDeclaredFields()).collect(Collectors.toList());
        
        Map<String, Object> params = new HashMap<>();
        
        for (Field field : fields) {
            
            for (Method method : methods) {
                String fieldName = field.getName().toLowerCase();
                if (method.getName().toLowerCase().endsWith("get" + fieldName)) {
                    try {
                        params.put(fieldName, method.invoke(user));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                    
                }
                
            }
        }
        return params;
    }
    
    @Override
    public Optional<User> findById(String userId) {
        return Optional.ofNullable(findById(UUID.fromString(userId)));
    }
    
    @Override
    public void delete(UUID id) {
        deleteById(id);
    }
    
    @Override
    public Optional<User> findByCPF(String cpf) {
        return find("cpf = ?1", cpf).firstResultOptional();
    }
    
    @Override
    public void getUsersPage(Integer page, Integer size, ObjLongConsumer<List<User>> result) {
        int beginIndex = page * size;
        int lastIndex = (page + 1) * size;
        PanacheQuery<User> all = findAll();
        long totalPages = Math.round((float) all.count() / size);
        List<User> users = all.range(beginIndex, lastIndex).list();
        result.accept(users, totalPages);
    }
    
}
