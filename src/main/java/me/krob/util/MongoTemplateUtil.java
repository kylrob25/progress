package me.krob.util;

import com.mongodb.client.result.UpdateResult;
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

    public UpdateResult set(String id, String key, Object value, Class<?> entityClass) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().set(key, value);
        return mongoTemplate.updateFirst(query, update, entityClass);
    }

    public UpdateResult addToSet(String id, String key, Object value, Class<?> entityClass) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().addToSet(key, value);
        return mongoTemplate.updateFirst(query, update, entityClass);
    }

    public UpdateResult pull(String id, String key, Object value, Class<?> entityClass) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().pull(key, value);
        return mongoTemplate.updateFirst(query, update, entityClass);
    }
}
