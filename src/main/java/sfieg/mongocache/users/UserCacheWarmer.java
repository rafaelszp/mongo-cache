package sfieg.mongocache.users;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Singleton
public class UserCacheWarmer {

  static Set<UserDTO> WARM_SET;
  static {
    WARM_SET = new HashSet<>();
    UserDTO usr = new UserDTO(11L, "Tanya", "Tory", 78L, LocalDateTime.now());
    WARM_SET.add(usr);
    usr = new UserDTO(20L, "Orianna", "thatsApassword", 1L, LocalDateTime.now());
    WARM_SET.add(usr);
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
//    if(true) return ;
    logger.info("Atualizando usuarios: "+WARM_SET);
    service.updateCache(WARM_SET);
  }


}
