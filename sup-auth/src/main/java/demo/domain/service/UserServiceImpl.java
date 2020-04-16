package demo.domain.service;

import demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl {
    @Autowired
    protected MongoTemplate mongoTemplate;

    public List<User> find(String name) {
        Query searchQuery = new Query();
        Criteria criteria = Criteria.where("username").is(name);

        searchQuery.addCriteria(criteria);
        return mongoTemplate.find(searchQuery, User.class);
    }

    public User createOne(User user) {
         mongoTemplate.insert(user);
         return user;
    }
}
