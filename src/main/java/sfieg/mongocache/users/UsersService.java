package sfieg.mongocache.users;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import sfieg.mongocache.mongo.ExampleCachedDB;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class UsersService {

  @Inject
  @ExampleCachedDB
  MongoDatabase database;

  private MongoCollection<Document> collection() {
    return database.getCollection("users");
  }

  /*
   * Para entender como funciona o text search visite: https://docs.mongodb.com/manual/text-search/
   * */
  public List<Document> fetch(int start, String query) {
    MongoCollection<Document> collection = collection();

    List<Document> result = new ArrayList<>();
    int skip = start > 0 ? start - 1 : 0;

    FindIterable<Document> documents = query != null && query.length() > 0 ? collection.find(Filters.text(query)) : collection.find();
    documents
            .projection(
                    fields()
            )
            .sort(Sorts.ascending("id"))
            .skip(skip).limit(10)
            .iterator().forEachRemaining(result::add);
    return result;
  }

  private Bson fields() {
    return Projections.fields(
            Projections.include(Arrays.asList("id", "login", "personId")),
            Projections.exclude("_id")
    );
  }


  public Document fetchById(Long id) {
    Document result = collection().find(Filters.eq("id", id)).projection(fields()).first();
    return result;
  }

  /**
   * Utilizar regex em grandes collections deixa a consulta mais lenta, mas pode ser utilizada da seguinte forma
   */
  public List<Document> slowFetch(int start, String term) {
    List<Document> result = new ArrayList<>();
    int skip = start > 0 ? start - 1 : 0;
    String pattern = ".*" + term + ".*";
    collection().find(Filters.regex("login", pattern,"i"))
            .projection(fields())
            .skip(skip).limit(10)
            .iterator().forEachRemaining(result::add);

    return result;
  }

  public void updateCache(Set<Document> warmSet) {
    collection().bulkWrite(
            warmSet.stream().map(document -> new ReplaceOneModel<>(
                    Filters.eq("id",document.getLong("id")),document, new UpdateOptions().upsert(true)) //Insere se não existe, atualiza se existe
            )
            .collect(Collectors.toList())
    );
  }
}
