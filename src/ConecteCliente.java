import java.io.*;
import java.nio.*;
import java.net.*;

public class ConecteCliente {

	final static String CRLF = new String("\r\n");
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Socket client;
		Entrada teclado = new Entrada();
		String mensagem;
		
		try {
			client = new Socket("192.168.0.137", 1313);
			
			BufferedReader resposta = new BufferedReader(new InputStreamReader(client.getInputStream()));
			DataOutputStream requisicoes = new DataOutputStream(client.getOutputStream());
			
			//while((mensagem = resposta.readLine()).length() != 0) {
				System.out.println(resposta.readLine());
			//}
				
			mensagem = teclado.gets();
				
			requisicoes.writeBytes(mensagem + CRLF + "User-agent: Seu cliente" + CRLF + CRLF);
			
			mensagem = null;
			while((mensagem = resposta.readLine()).length() != 0) {
				System.out.println(mensagem);
			}
			
			client.close();
			resposta.close();
			requisicoes.close();
			mensagem = null;
			teclado = null;
		} 
		catch (Exception e) {
			System.err.println("Não foi possível estabelecer uma conexão com o servidor");
		}
	}
}
