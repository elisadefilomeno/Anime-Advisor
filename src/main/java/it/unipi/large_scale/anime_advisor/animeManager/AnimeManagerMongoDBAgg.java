package it.unipi.large_scale.anime_advisor.animeManager;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AnimeManagerMongoDBAgg {

    //top 10 anime by specified field
    public void topTenAnimeByField(MongoCollection<Document>doc,String field,int year,String t,String[] genres){
        //By Year
        if(field.equals("premiered")){
            try{
                doc.aggregate(
                        Arrays.asList(
                        Aggregates.match(Filters.eq("premiered",year)),
                        Aggregates.sort(Sorts.descending("scored")),
                        Aggregates.limit(10)

                        )
                ).forEach(d->System.out.println(d.toJson()));

            } catch (MongoException e){
                System.out.println("An error has occurred:"+e);
            }
        }
        //By genre, User MUST specify which genre
        if(field.equals("genre")){
            try{
                doc.aggregate(
                        Arrays.asList(
                                Aggregates.match(Filters.all("genre",genres)),
                                Aggregates.sort(Sorts.descending("scored")),
                                Aggregates.limit(10)

                        )
                ).forEach(d->System.out.println(d.toJson()));

            } catch (MongoException e){
                System.out.println("An error has occurred:"+e);
            }
        }
        if(field.equals("type")){
            try{
                doc.aggregate(
                        Arrays.asList(
                                Aggregates.match(Filters.eq("type",t)),
                                Aggregates.sort(Sorts.descending("scored")),
                                Aggregates.limit(10)

                        )
                ).forEach(d->System.out.println(d.toJson()));

            } catch (MongoException e){
                System.out.println("An error has occurred:"+e);
            }
        }
        if(field.equals("source")){
            try{
                doc.aggregate(
                        Arrays.asList(
                                Aggregates.match(Filters.eq("source",t)),
                                Aggregates.sort(Sorts.descending("scored")),
                                Aggregates.limit(10)

                        )
                ).forEach(d->System.out.println(d.toJson()));

            } catch (MongoException e){
                System.out.println("An error has occurred:"+e);
            }
        }

    }
    //entity with the highes average score in an year or all over the years
    //entity can be Studio Genre or Producer
    public void highAvgEntity (MongoCollection<Document>doc,String entity, int year){
        if(year!=0) {
            try {
                doc.aggregate(
                        Arrays.asList(
                                Aggregates.match(Filters.eq("premiered", year)),
                                Aggregates.unwind("$".concat(entity)),
                                Aggregates.group("$".concat(entity),Accumulators.avg("average","$scored")),
                                Aggregates.sort(Sorts.descending("average")),
                                Aggregates.limit(10)

                        )
                ).forEach(d -> System.out.println(d.toJson()));

            } catch (MongoException e) {
                System.out.println("An error has occurred:" + e);
            }
        }
        else {
            try {
                doc.aggregate(
                        Arrays.asList(
                                Aggregates.unwind("$".concat(entity)),
                                Aggregates.group("$".concat(entity),Accumulators.avg("average","$scored")),
                                Aggregates.sort(Sorts.descending("average")),
                                Aggregates.limit(10)

                        )
                ).forEach(d -> System.out.println(d.toJson()));

            } catch (MongoException e) {
                System.out.println("An error has occurred:" + e);
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


    //number of Entity's production by type Studio Producer
    public void entityProdByType(MongoCollection<Document>doc,String entity, int year){
        try {
            Map<String, Object> multiIdMap = new HashMap<String, Object>();
            multiIdMap.put(entity, "$".concat(entity));
            multiIdMap.put("type", "$type");

            Document groupFields = new Document(multiIdMap);

            doc.aggregate(
                    Arrays.asList(
                            Aggregates.match(Filters.ne(entity,"Unknown")),
                            Aggregates.unwind("$".concat(entity)),
                            Aggregates.group(groupFields,Accumulators.sum("followers","$members"))

                    )
            ).forEach(d -> System.out.println(d.toJson()));

        } catch (MongoException e) {
            System.out.println("An error has occurred:" + e);
        }
    }


    }


