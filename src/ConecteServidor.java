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
		//Servidor servidor = new Servidor();
		//String comando;
		
		try {
			server = new ServerSocket(1313);
			
			while(true) {
				Socket conexao = server.accept();
				BufferedReader requisicao = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
				DataOutputStream out = new DataOutputStream(conexao.getOutputStream());
				
				//comando = new String("Conexão estabelecida!");
				
				out.writeUTF("Conexão estabelecida!" + CRLF);
				out.writeUTF("Identifique-se!" + CRLF);

				//do {
					//conexao = server.accept();
					System.out.println(requisicao.readLine());
					//comando = new String(requisicao.readLine());
					//System.out.println(comando);
					//System.out.println(comando.charAt(0));
				//}while((comando.compareTo("QUIT")) != 0);
				
				//out.close();
				//requisicao.close();
				//conexao.close();
			}
		} 
		catch (Exception e) {
			System.err.println("Não foi possível criar o servidor");
		}
		
		
	}

}
