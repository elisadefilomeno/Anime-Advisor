package it.unipi.large_scale.anime_advisor.animeManager;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import javax.print.Doc;
import java.lang.constant.DynamicCallSiteDesc;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AnimeManagerMongoDBAgg {

    //top 10 anime by specified field(Genres (multiple),year,source, type
    public HashMap<Integer,String> topTenAnimeByField(MongoCollection<Document>doc,String field,int year,String t,String[] genres,int limitg){
        //By Year
        HashMap<Integer,String> results=new HashMap<Integer,String>();
        if(field.equals("premiered")){
            try{
                MongoCursor<Document> cursor =doc.aggregate(
                        Arrays.asList(
                        Aggregates.match(Filters.eq("premiered",year)),
                        Aggregates.sort(Sorts.descending("scored")),
                        Aggregates.limit(10)

                        )
                ).iterator();
                int temp=1;
                while (cursor.hasNext()){
                    results.put(temp,cursor.next().get("name").toString());
                    temp++;
                }
                return results;

            } catch (MongoException e){
                System.out.println("An error has occurred:"+e);
                return null;
            }
        }
        //By genre, User MUST specify which genre
        if(field.equals("genre")){
            try{
                MongoCursor<Document> cursor;
                if(limitg==1) {
                   cursor= doc.aggregate(
                            Arrays.asList(
                                    Aggregates.match(Filters.all("genre", genres)),
                                    Aggregates.sort(Sorts.descending("scored")),
                                    Aggregates.limit(10)

                            )
                    ).iterator();
                }
                else{
                  cursor=  doc.aggregate(
                            Arrays.asList(
                                    Aggregates.match(Filters.all("genre", genres)),
                                    Aggregates.sort(Sorts.descending("scored"))
                            )
                    ).iterator();

                }
                int temp=1;
                while (cursor.hasNext()){
                    results.put(temp,cursor.next().get("name").toString());
                    temp++;
                }
                return results;

            } catch (MongoException e){
                System.out.println("An error has occurred:"+e);
                return null;

            }
        }
        if(field.equals("type")){
            try{
                MongoCursor<Document> cursor =doc.aggregate(
                        Arrays.asList(
                                Aggregates.match(Filters.eq("type",t)),
                                Aggregates.sort(Sorts.descending("scored")),
                                Aggregates.limit(10)

                        )
                ).iterator();
                int temp=1;
                while (cursor.hasNext()){
                    results.put(temp,cursor.next().get("name").toString());
                    temp++;
                }
                return results;

            } catch (MongoException e){
                System.out.println("An error has occurred:"+e);
                return null;

            }
        }
        if(field.equals("source")){
            try{
                MongoCursor<Document> cursor =doc.aggregate(
                        Arrays.asList(
                                Aggregates.match(Filters.eq("source",t)),
                                Aggregates.sort(Sorts.descending("scored")),
                                Aggregates.limit(10)

                        )
                ).iterator();
                int temp=1;
                while (cursor.hasNext()){
                    results.put(temp,cursor.next().get("name").toString());
                    temp++;
                }
                return results;

            } catch (MongoException e){
                System.out.println("An error has occurred:"+e);
                return null;

            }
        }
        System.out.println("Wrong field specified");
        return null;

    }
    //entity with the highes average score in an year or all over the years
    //entity can be Studio Genre or Producer
    //NOTE: when you use this in the interface method create a new hash map there with INDEX-NAME
    public void highAvgEntity (MongoCollection<Document>doc,String entity, int year){
        if(year!=0) {
            try {
                MongoCursor<Document> cursor =doc.aggregate(
                        Arrays.asList(
                                Aggregates.match(Filters.eq("premiered", year)),
                                Aggregates.unwind("$".concat(entity)),
                                Aggregates.group("$".concat(entity),Accumulators.avg("average","$scored")),
                                Aggregates.sort(Sorts.descending("average")),
                                Aggregates.limit(10)

                        )
                ).iterator();
                while(cursor.hasNext()) {
                    Document temp = cursor.next();
                    System.out.println(entity+": "+temp.get("_id").toString()+" Average: "+ temp.get("average"));
                }
            } catch (MongoException e){
                System.out.println("An error has occurred:"+e);

            }
        }
        else {
            try {
                MongoCursor<Document> cursor =doc.aggregate(
                        Arrays.asList(
                                Aggregates.match(Filters.ne(entity,"Unknown")),
                                Aggregates.unwind("$".concat(entity)),
                                Aggregates.group("$".concat(entity),Accumulators.avg("average","$scored")),
                                Aggregates.sort(Sorts.descending("average")),
                                Aggregates.limit(10)

                        )
                ).iterator();
                while(cursor.hasNext()){
                    Document temp=cursor.next();
                    System.out.println(entity+": "+temp.get("_id").toString()+" Average: "+ temp.get("average"));
                }
            } catch (MongoException e){
                System.out.println("An error has occurred:"+e);

            }


        }
    }

    //entity with the most users following their products or most followed genres
    public void mostFollowersEntity(MongoCollection<Document>doc,String entity){
        try {

            doc.aggregate(
                    Arrays.asList(
                            Aggregates.match(Filters.ne(entity,"Unknown")),
                            Aggregates.unwind("$".concat(entity)),
                            Aggregates.group("$".concat(entity),Accumulators.sum("followers","$members")),
                            Aggregates.sort(Sorts.descending("followers")),
                            Aggregates.limit(10)

                    )
            ).forEach(d -> System.out.println(d.toJson()));

        } catch (MongoException e) {
            System.out.println("An error has occurred:" + e);
        }
    }


    //number of Entity's production per type by Studio Producer
    public void entityProdByType(MongoCollection<Document>doc,String entity){
        try {

            Map<String, Object> multiIdMap = new HashMap<String, Object>();
            multiIdMap.put(entity, "$".concat(entity));
            multiIdMap.put("type", "$type");

            Document groupFields = new Document(multiIdMap);
            MongoCursor<Document> cursor =doc.aggregate(
                    Arrays.asList(
                            Aggregates.match(Filters.ne(entity,"Unknown")),
                            Aggregates.unwind("$".concat(entity)),
                            Aggregates.group(new Document(entity,"$".concat(entity)).append("type","$type"),Accumulators.sum("productions",1))

                    )
            ).iterator();
            int pos=1;
            while(cursor.hasNext()){
                Document temp=cursor.next();
                Document en=temp.get("_id", Document.class);
                if(entity.equals("studio")) {
                    en.get("studio");
                    System.out.println("Studio:"+en.get("studio").toString()+" Type:"+en.get("type").toString()+" Productions:"+temp.get("productions").toString());
                }
                if(entity.equals("producer")) {
                    en.get("producer");
                    System.out.println("Producer:"+en.get("producer").toString()+" Type:"+en.get("type").toString()+" Productions:"+temp.get("productions").toString());
                }
            }


        } catch (MongoException e) {
            System.out.println("An error has occurred:" + e);
        }
    }


    }


