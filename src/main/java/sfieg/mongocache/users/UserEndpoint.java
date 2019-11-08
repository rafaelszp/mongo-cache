package sfieg.mongocache.users;

import org.bson.Document;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@SuppressWarnings("all")
@Produces(MediaType.APPLICATION_JSON)
@Path("/users")

public class UserEndpoint {

  @Inject
  UsersService service;

  @GET
  public Response get(@QueryParam("start") int start, @QueryParam("q") String query) {
    List<Document> result = service.fetch(start, query);
    return Response.ok(result)
            .header("Count", result.size())
            .build();
  }

  @GET
  @Path("/search/{term}")
  public Response slowGet(@PathParam("term") String term,@QueryParam("start") int start) {
    List<Document> result = service.slowFetch(start, term);
    return Response.ok(result)
            .header("Count", result.size())
            .build();
  }

  @GET
  @Path("/{id:\\d+}")
  public Response get(@PathParam("id") Long id) {
    Document result = service.fetchById(id);
    return Response.ok(result).build();
  }
}
