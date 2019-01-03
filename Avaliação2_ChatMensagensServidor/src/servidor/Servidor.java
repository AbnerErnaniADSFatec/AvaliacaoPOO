package servidor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import interfaces.SendMsgToAllClients;
public class Servidor extends Thread implements SendMsgToAllClients {
	private static ArrayList<BufferedWriter> clientes;
	private static ServerSocket server;
	private String nome;
	private Socket con;
	private InputStream in;
	private InputStreamReader inr;
	private BufferedReader bfr;
	public Servidor(Socket con) {
		this.con = con;
		try {
			in = con.getInputStream();
			inr = new InputStreamReader(in);
			bfr = new BufferedReader(inr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		try {
			String msg;
			OutputStream ou = this.con.getOutputStream();
			Writer ouw = new OutputStreamWriter(ou);
			BufferedWriter bfw = new BufferedWriter(ouw);
			clientes.add(bfw);
			nome = msg = bfr.readLine();
			while (!"Sair".equalsIgnoreCase(msg) && msg != null) {
				msg = bfr.readLine();
				sendToAll(bfw, msg);
				System.out.println(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void sendToAll(BufferedWriter bwSaida, String msg) throws IOException {
		SendMsgToAllClients.super.sendToAll(bwSaida, msg);
		BufferedWriter bwS;
		for (BufferedWriter bw : clientes) {
			bwS = (BufferedWriter) bw;
			if (!(bwSaida == bwS)) {
				bw.write("(" + nome + "):" + msg + "\r\n");
				bw.flush();
			}
		}
	}
	public static void main(String[] args) {
		try {
			JLabel lblMsg = new JLabel("Porta do Servidor:");
			JTextField txtPort = new JTextField("1234");
			Object[] texts = { lblMsg, txtPort };
			JOptionPane.showMessageDialog(null, texts);
			server = new ServerSocket(Integer.parseInt(txtPort.getText()));
			clientes = new ArrayList<BufferedWriter>();
			JOptionPane.showMessageDialog(null, "Servidor ativo na porta: " + txtPort.getText());
			while (true) {
				System.out.println("Aguardando conex�o...");
				Socket con = server.accept();
				System.out.println("cliente conectado...");
				Thread t = new Servidor(con);
				t.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
