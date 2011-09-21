import java.io.*;
import java.nio.*;
import java.net.*;

public class ConecteServidor {

	final static String CRLF = new String("\r\n");
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServerSocket server;
		
		try {
			server = new ServerSocket(1313);
			
			while(true) {
				Socket conexao = server.accept();
				BufferedReader requisicao = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
				DataOutputStream out = new DataOutputStream(conexao.getOutputStream());
				
				out.writeUTF("Conexão estabelecida\n");
				out.writeUTF("Identifique-se\n");
				
				System.out.println(requisicao.readLine());
			}
		} 
		catch (Exception e) {
			System.err.println("Não foi possível criar o servidor");
		}
		
		
	}

}
