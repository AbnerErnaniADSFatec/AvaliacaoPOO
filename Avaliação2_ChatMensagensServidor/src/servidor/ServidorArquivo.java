package servidor;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import arquivo.*;
public class ServidorArquivo {
	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(5566);
			System.out.println("Aguardando encvio de arquivo ....");
			Socket socket = serverSocket.accept();
			byte[] objectAsByte = new byte[socket.getReceiveBufferSize()];
			BufferedInputStream buffer = new BufferedInputStream(
					socket.getInputStream());
			buffer.read(objectAsByte);
			Arquivo arquivo = (Arquivo) getObjectFromByte(objectAsByte);
			String diretorio = arquivo.getDiretorioDestino().endsWith("\\") 
					? arquivo.getDiretorioDestino() + arquivo.getNome() : 
						arquivo.getDiretorioDestino() + "\\" + arquivo.getNome();
			System.out.println("Escrevendo arquivo " + diretorio + " ...");
			FileOutputStream fos = new FileOutputStream(diretorio);
			fos.write(arquivo.getConteudo());
			fos.close();
			serverSocket.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static Object getObjectFromByte(byte[] objectAsByte) {
		Object obj = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		try {
			bis = new ByteArrayInputStream(objectAsByte);
			ois = new ObjectInputStream(bis);
			obj = ois.readObject();
			bis.close();
			ois.close();
		}catch (IOException e) {
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return obj;
	}	
}
