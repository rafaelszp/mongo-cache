package sfieg.mongocache.users;

import org.bson.Document;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Singleton
public class UserCacheWarmer {

  static Set<Document> WARM_SET;
  static {
    WARM_SET = new HashSet<>();
    Document doc = new Document();
    doc.append("id",1L);
    doc.append("login","Tanya");
    doc.append("pwd","Tory");
    doc.append("personId","78");
    doc.append("lastUpdate", LocalDateTime.now());
    WARM_SET.add(doc);
    doc = new Document();
    doc.append("id",21L);
    doc.append("login","Orianna");
    doc.append("pwd","thatsApassword");
    doc.append("personId","1");
    doc.append("lastUpdate", LocalDateTime.now());
    WARM_SET.add(doc);
  }

  @Inject
  UsersService service;

  Logger logger = Logger.getLogger(this.getClass().getName());

  /**
   * Para fins de desenvolvimento, o cache esquenta a cada 10s,
   * todavia é necessário desenvolver mecanismos para inserir de 100 registros por vez para que não haja problemas de
   * OutOfMemony, além disto é importante atualizar somente os registros que não tiverem mudanças.
   * */
  @Schedule(minute = "*",second = "*/10",hour = "*")
  public void warmCache(){
    logger.info("Atualizando usuarios: "+WARM_SET);
    service.updateCache(WARM_SET);
  }


}
