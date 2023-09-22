public class Entry {

    String username;
    long posicion;
    Entry siguiente;
    
    public Entry(String username, long posicion) {
        this.username=username;
        this.posicion=posicion;
        siguiente=null;
    }
    
    
    
}
