package com.zhao.commonservice.utils;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ESUtil
 * @Author: zhaolianqi
 * @Date: 2022/6/29 11:23
 * @Version: v1.0
 */
public class ESUtil {

//    private RestHighLevelClient client = null;
    private static ESUtil instance = null;

    public static ESUtil getInstance() {
        if (instance == null){
            synchronized (ESUtil.class){
                if (instance == null){
                    instance = new ESUtil();
                }
            }
        }
        return instance;
    }

//    private RestHighLevelClient getClient(){
//        if (client == null){
//            synchronized (ESUtil.class){
//                if (client == null){
//                    client = new RestHighLevelClient(
//                            RestClient.builder(new HttpHost("es.vmware.com",32770,"http")));
//                }
//            }
//        }
//        return client;
//    }


//    public void createIndex(String index) throws IOException {
//        CreateIndexRequest indexRequest = new CreateIndexRequest(index);
//        CreateIndexResponse createIndexResponse = getClient().indices().create(indexRequest, RequestOptions.DEFAULT);
//        getClient().close();
//        System.out.println(">>>>>>>>>>>>>>> " + createIndexResponse);
//    }
//
//    public void removeIndex(String index) throws IOException {
//        DeleteIndexRequest request = new DeleteIndexRequest(index);
//        AcknowledgedResponse delete = getClient().indices().delete(request, RequestOptions.DEFAULT);
//        System.out.println(">>>>>>>>>>>>>>> " + delete.isAcknowledged());
//    }

//    public boolean indexIsExist(String index) throws IOException {
//        GetIndexRequest request = new GetIndexRequest(index);
//        return getClient().indices().exists(request, RequestOptions.DEFAULT);
//    }

    public void save(String index, String id, Map<String, Object> params) throws IOException {
        IndexRequest indexRequest = new IndexRequest(index);
        //设置你想要的id，不设置会自动生成随机uuid
        indexRequest.id(id == null ? "" : id);
        indexRequest.source(JSON.toJSONString(params), XContentType.JSON);
//        indexRequest.type("scenic");
//        IndexResponse response = getClient().index(indexRequest, RequestOptions.DEFAULT);
//        getClient().close();
//        System.out.println(">>>>>>>>>>>>>>> 保存结果： " + response);
    }

    public void saveBatch(String index, List<Map<String, Object>> paramsList) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        for (Map<String, Object> params : paramsList) {
            Object id = params.get("id");
            bulkRequest.add(
                    new IndexRequest(index)
                            .id(id == null ? "" : id.toString())
                            .source(JSON.toJSONString(params),XContentType.JSON)
            );
        }
//        BulkResponse responses = getClient().bulk(bulkRequest,RequestOptions.DEFAULT);
//        System.out.println(">>>>>>>>>>>>>>> " + responses);
    }

    /**
     * 删除文档
     * @param index 索引
     * @param id 文档id
     * @Author zhaolianqi
     * @Date 2022/6/29 17:55
     */
    public void removeDoc(String index, String id) throws IOException {
        DeleteRequest request = new DeleteRequest(index,id);
//        DeleteResponse delete = getClient().delete(request, RequestOptions.DEFAULT);
//        System.out.println(">>>>>>>>>>>>>>> " + delete.toString());
    }

    /**
     * 更新文档
     * @Author zhaolianqi
     * @Date 2022/6/29 17:57
     */
    public boolean updateDoc(String index, Map<String, Object> params) throws IOException {
        Object id = params.get("id");
        if (id == null)
            return false;
        UpdateRequest request = new UpdateRequest(index,id.toString());
        request.doc(JSON.toJSONString(params),XContentType.JSON);
//        UpdateResponse update = getClient().update(request, RequestOptions.DEFAULT);
//        System.out.println(update.status());
        return true;
    }

    public void search(String index, String keywords) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery(keywords);
        searchSourceBuilder.query(queryBuilder);
        searchRequest.source(searchSourceBuilder);
//        SearchResponse search = getClient().search(searchRequest, RequestOptions.DEFAULT);
    }

}
