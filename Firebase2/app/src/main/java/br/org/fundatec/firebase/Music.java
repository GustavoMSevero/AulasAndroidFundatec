package br.org.fundatec.firebase;

/**
 * Created by tecnico on 20/06/2017.
 */

class Music {

    private String user;
    private String title;
    private String id;

    @Override
    public String toString() {
        return "Música: "+ title + " - \nSolicitante: " + user;
    }

    public Music(String user, String title, String id) {
        this.user = user;
        this.title = title;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
