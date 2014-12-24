package com.csaa.aa.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;

@Path("/search")
public class SearchService {

	public SearchService() {
		super();
		System.out.println("Inside SearchService() constructor");
	}
	@POST
	@Path("/{index}/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String search(@PathParam("index")String index, @PathParam("type") String type, String body){
		System.out.println("Entering SearchService.getResults()");
		System.out.println("Index " + index);
		
		System.out.println("Type " + type);
		System.out.println("Body " + body);
		Client client = ESClient.getClient();
		SearchResponse searchResponse = client.prepareSearch(index).setSource(body).execute().actionGet();

		return searchResponse.toString();
	}
}
