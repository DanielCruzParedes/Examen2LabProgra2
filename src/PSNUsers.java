
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class PSNUsers {

    RandomAccessFile raf;
    RandomAccessFile psn;
    HashTable users;
    File usersFile = new File("users");
    File trophiesFile = new File("trofeos");

    public PSNUsers() {
        try {

            if (!usersFile.exists()) {
                usersFile.mkdir();
            }

            if (!trophiesFile.exists()) {
                trophiesFile.mkdir();
            }
            raf = new RandomAccessFile("users/usuarios.psn", "rw");
            psn = new RandomAccessFile("trofeos/trophies.psn", "rw");

            users = new HashTable();
            reloadHashTable();
        } catch (IOException e) {
            System.out.println("Algo salio mal, chiva te bajan puntos.");
            System.out.println(e);
        }
    }

    private void reloadHashTable() {
        try {
            long fileLength = raf.length();
            while (raf.getFilePointer() < fileLength) {
                String username = raf.readUTF();
                long pos = raf.readLong();
                users.add(username, pos);
            }
        } catch (EOFException e) {
            System.out.println("eofileexception");
        } catch (IOException e) {
            System.out.println("Algo salio mal al reloadear el hashtable");
            System.out.println(e);
        }
    }

    void addUser(String username) {
        try {
            if (users.search(username) != -1) {
                JOptionPane.showMessageDialog(null, "El nombre del usuario ya existe.");
            } else {
                long pos = raf.length();
                raf.seek(pos);//se va hasta el fin del archivo
                raf.writeUTF(username);//usuario
                raf.writeLong(0);//puntos por trofeos
                raf.writeInt(0);//contador de trofeos
                raf.writeBoolean(true);//el registro

                users.add(username, pos);
                JOptionPane.showMessageDialog(null, "Se agrego el usuario correctamente");

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error");
        }
    }

    void deactivateUser(String username) {
        try {
            long posicionEncontrado = users.search(username);

            if (posicionEncontrado == -1) {
                System.out.println("No se encontro al usuario para desactivarlo.");
                return;
            }
            raf.seek(posicionEncontrado);
            raf.writeBoolean(false);
            users.remove(username);
            JOptionPane.showMessageDialog(null, "Se deactivio el usuario con exito.");

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Algo salio mal al deactivear usuario");
        }
    }

    void addTrophieTo(String username, String trophyGame, String trophyName, HashTable.Trophy trophyType) {
        try {
            long posicion = users.search(username);
            if (posicion == -1) {
                JOptionPane.showMessageDialog(null, "No se encontro usuario en addtrophieto");
                return;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fechaActual = dateFormat.format(new Date());

            //Aumenta el numero de trofeos
            raf.seek(posicion);
            int contadorTrofeos = raf.readInt();
            raf.seek(posicion);
            raf.writeInt(contadorTrofeos + 1);
            raf.seek(raf.length()); //se devuelve al final del archivo

            // Agrega el trofeo al archivo llamado psn
            psn.seek(raf.length());
            psn.writeLong(posicion);//codigo
            psn.writeUTF(trophyType.name());//tipo trofeo
            psn.writeUTF(trophyGame);//juego del trofeo
            psn.writeUTF(trophyName);//nombre del trofeo
            psn.writeUTF(fechaActual);//fecha en que se gano

            //agrega al contador de puntos
            raf.seek(posicion);
            int contadorpuntos = raf.readInt();
            raf.seek(posicion);
            raf.writeInt(contadorpuntos + 1);
            JOptionPane.showMessageDialog(null, "Trofeo agregado correctamente.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Algo salio mal en addtrophieto");
        }

    }

    public String playerInfo(String username) throws IOException {
        long pos = users.search(username);
        StringBuilder datos = new StringBuilder();
        if (pos != -1) {
            // Ir a la posición del usuario en el archivo
            raf.seek(pos);

            // Leer los datos del usuario
            int codigousuario = raf.readInt();
            boolean activo = raf.readBoolean();
            String usuario = raf.readUTF();
            int puntostrofeo = raf.readInt();
            int conteotrofeos = raf.readInt();

            // Imprimir los datos del usuario
            datos.append("Datos del usuario:");
            datos.append("Código de usuario: " + codigousuario);
            datos.append("Nombre de usuario: " + usuario);
            datos.append("Activo: " + activo);
            datos.append("Acumulador de puntos por trofeos: " + puntostrofeo);
            datos.append("Contador de trofeos: " + conteotrofeos);

            // Imprimir los trofeos del usuario
            datos.append("Trofeos ganados:");
            try {
                while (psn.getFilePointer() < psn.length()) {
                    int codigo = psn.readInt();
                    if (codigo == codigo) {
                        String tipoTrofeo = psn.readUTF();
                        String juegoTrofeo = psn.readUTF();
                        String Juegotrofeo = psn.readUTF();
                        String fechatrofeo = psn.readUTF();
                        System.out.println(fechatrofeo + " - " + tipoTrofeo + " - " + juegoTrofeo + " - " + Juegotrofeo);
                    } else {
                        psn.readUTF();
                        psn.readUTF();
                        psn.readUTF();
                        psn.readUTF();
                    }
                }
            } catch (Exception e) {
                System.out.println(datos.toString());
                System.out.println("Algo salio mal");

            }
        } else {
            JOptionPane.showMessageDialog(null, "Usuario no encontrado.");
        }
        return datos.toString();
    }

}
