
public class HashTable {

    private Entry inicio = null;
    private int size = 0;

    public HashTable() {
    }

    public boolean isEmpty() {
        return inicio == null;
    }

    public void add(String username, long pos) {

        Entry newEntry = new Entry(username, pos);
        if (isEmpty() == true) {
            inicio = newEntry;
        } else {
            Entry tmp = inicio;
            while (tmp.siguiente != null) {
                tmp = tmp.siguiente;
            }
            tmp.siguiente = newEntry;
        }
        size++;
    }

    public boolean remove(String username) {
        if (inicio == null) {
            return false;
        }
        if (!isEmpty()) {
            if (inicio.username.equals(username)) {
                inicio = inicio.siguiente;
                size--;
                return true;
            } else {
                Entry tmp = inicio;
                while (tmp.siguiente != null) {
                    if (tmp.siguiente.username.equals(username)) {
                        tmp.siguiente = tmp.siguiente.siguiente;
                        size--;
                        return true;
                    }
                    tmp = tmp.siguiente;
                }
            }
        }
        return false;
    }

    //Long porque se retorna la posicion
    public long search(String username) {
        Entry tmp = inicio;
        while (tmp != null) {
            if (tmp.username.equals(username)) {
                return tmp.posicion;
            }
            tmp = tmp.siguiente;
        }
        return -1;
    }

    public enum Trophy {
        PLATINO(5),
        ORO(3),
        PLATA(2),
        BRONCE(1);

        public int points;

        Trophy(int points) {
            this.points = points;
        }
    }
    
    

}
