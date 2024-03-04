package me.krob.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class MongoTemplateUtil {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void set(String id, String key, Object value, Class<?> entityClass) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().set(key, value);
        mongoTemplate.updateFirst(query, update, entityClass);
    }

    public void addToSet(String id, String key, Object value, Class<?> entityClass) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().addToSet(key, value);
        mongoTemplate.updateFirst(query, update, entityClass);
    }

    public void pull(String id, String key, Object value, Class<?> entityClass) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().pull(key, value);
        mongoTemplate.updateFirst(query, update, entityClass);
    }
}
