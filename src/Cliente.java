import java.io.*;
import java.nio.*;
import java.net.*;

public class Cliente {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Socket client;
		Entrada teclado = new Entrada();
		String mensagem;
		
		try {
			client = new Socket("localhost", 1313);
			BufferedReader resposta = new BufferedReader(new InputStreamReader(client.getInputStream()));
			DataOutputStream requisicoes = new DataOutputStream(client.getOutputStream());
			
			System.out.println(resposta.readLine());
			System.out.println(resposta.readLine());
			
			boolean boolTeste = true;
			do {
				mensagem = teclado.gets();
				if(mensagem != null) {
					boolTeste = false;
				}
			} while(boolTeste);
			
			requisicoes.writeUTF(mensagem + '\n');
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
