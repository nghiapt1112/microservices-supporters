package demo.domain.service;

import com.nghia.libraries.mongo.infrustructure.repository.impl.AbstractCustomRepository;
import demo.domain.User;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends AbstractCustomRepository<User> {

    public List<User> find(String name) {
        Query searchQuery = new Query();
        Criteria criteria = Criteria.where("username").is(name);

        searchQuery.addCriteria(criteria);
        return super.find(searchQuery);
    }

}
