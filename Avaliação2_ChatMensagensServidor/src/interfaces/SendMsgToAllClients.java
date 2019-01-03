package interfaces;
import java.io.BufferedWriter;
import java.io.IOException;
public interface SendMsgToAllClients {
	public default void sendToAll(BufferedWriter bwSaida, String msg) throws IOException{
	}
}
