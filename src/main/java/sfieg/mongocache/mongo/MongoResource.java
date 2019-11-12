package sfieg.mongocache.mongo;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import java.util.Arrays;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@SuppressWarnings("all")
public class MongoResource {

  @ApplicationScoped
  @ExampleCachedDB @Default
  @Produces
  public MongoDatabase createDB(){

    String host = System.getProperty("mongocache.mongo.HOST");
    String db = System.getProperty("mongocache.mongo.DB");
    String user = System.getProperty("mongocache.mongo.USER");
    String pwd = System.getProperty("mongocache.mongo.PWD");
    int port = Integer.parseInt(System.getProperty("mongocache.mongo.PORT"));

    ServerAddress address = new ServerAddress(host,port);
    MongoCredential credentials = MongoCredential.createCredential(user, db, pwd.toCharArray());

    MongoClient client = MongoClients.create(
            MongoClientSettings.builder()
                    .applyToClusterSettings(builder ->
                            builder.hosts(Arrays.asList(address)))
                    .credential(credentials)
                    .codecRegistry(this.codecRegistry())
                    .build());

    MongoDatabase database = client.getDatabase(db);
    this.createIndexes(database);
    return database;
  }

  /*
  * É importante criar os índices para realizar `text search`, que tem performance maior que o regex
  * */
  private void createIndexes( MongoDatabase database){
    MongoCollection<Document> collection = database.getCollection("users");
    collection.createIndex(Indexes.text("login"));
    collection.createIndex(Indexes.ascending("userId"), new IndexOptions().unique(true));
  }

  private CodecRegistry codecRegistry() {
    CodecRegistry pojoCodecRegistry = fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build())
    );
    return pojoCodecRegistry;
  }
}
