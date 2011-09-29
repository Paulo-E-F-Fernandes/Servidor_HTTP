import java.net.*;

public final class ConecteServidor {
	
	/**
	 * @param args
	 * 
	 * @author paulo fernandes
	 * Método principal que inicia o servidor HTTP
	 */
	public static void main(String[] args) {
		ServerSocket socketServer;
		
		try {
			// Cria um socket para o servidor na porta 1313
			socketServer = new ServerSocket(1313);
			System.err.println("\n---SERVIDOR iniciado!\n");
			
			while(true) {
				// Servidor ficar escutando a porta a espera de alguma solicitacao dos cliente
				Socket conexao = socketServer.accept();
				
				Servidor requisicaoServidor = new Servidor(conexao);
				// Cria uma thread para executar a requisicao do cliente
				Thread execucao = new Thread(requisicaoServidor);
				// Inicia a thread para executar a requisicao do cliente
				execucao.start();
			}
		}
		catch (Exception e) {
			System.err.println("\nNão foi possível criar o servidor\n");
		}
	}
}		

