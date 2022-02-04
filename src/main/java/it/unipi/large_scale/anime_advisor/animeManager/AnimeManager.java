package it.unipi.large_scale.anime_advisor.animeManager;
import it.unipi.large_scale.anime_advisor.entity.Anime;

interface AnimeManager<T> {
    //Creazione di un doccumento di tipo Anime ed inserimento nel rispettivo DB
    public void createAnime(Anime anime, T db);

    //Lettura di un documento di tipo Anime dal corrispettivo DB
    public void readAnime(Anime anime, T db);

    //Aggiornamento di un documento di tipo Anime nel corrispettivo DB
    public void updateAnime(Anime anime, T db);

    //Eliminazione di un documento di tipo Anime nel corrispettivo DB
     public void deleteAnime(Anime anime, T db);

 }
