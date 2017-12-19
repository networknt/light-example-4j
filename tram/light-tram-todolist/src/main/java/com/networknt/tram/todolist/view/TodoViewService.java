package com.networknt.tram.todolist.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TodoViewService {

  private static TransportClient transportClient;

  private ObjectMapper objectMapper = new ObjectMapper();

  static {
    try {
      transportClient =  new PreBuiltTransportClient(Settings.builder().put("client.transport.ignore_cluster_name", true).build())
              .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 8080));
    } catch (Exception e) {
      //TODO
    }

  }

  public List<TodoView> search(String value) {

    if (!transportClient.admin().indices().prepareExists(TodoView.INDEX).execute().actionGet().isExists()) {
      return Collections.emptyList();
    }

    SearchResponse response = transportClient.prepareSearch(TodoView.INDEX)
            .setTypes(TodoView.TYPE)
            .setQuery(QueryBuilders.termQuery("_all", value))
            .get();

    List<TodoView> result = new ArrayList<>();

    for (SearchHit searchHit : response.getHits()) {
      try {
        result.add(objectMapper.readValue(searchHit.getSourceAsString(), TodoView.class));
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    return result;
  }

  public void index(TodoView todoView) {
    try {
      IndexResponse ir = transportClient
          .prepareIndex(TodoView.INDEX, TodoView.TYPE, todoView.getId())
          .setSource(objectMapper.writeValueAsString(todoView), XContentType.JSON)
          .get();
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public void remove(String id) {
    transportClient.prepareDelete(TodoView.INDEX, TodoView.TYPE, id).get();
  }
}
