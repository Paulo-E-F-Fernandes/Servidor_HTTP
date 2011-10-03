import java.net.*;

public final class ConecteServidor {
	
	private static Integer porta = 1313;
	private static String path = "Arquivos";
	
	public static void main(String[] args) {
		ServerSocket socketServer;
		
		if(args.length == 2) {
			path = args[0];
			porta = Integer.parseInt(args[1]);
		}
		
		try {
			// Cria um socket para o servidor na porta fornecida pelo administrador.
			// Por default, porta = 1313
			socketServer = new ServerSocket(porta);
			System.err.println("---SERVIDOR iniciado!---\n");
			
			while(true) {
				// Servidor ficar escutando a porta a espera de alguma solicitacao dos cliente
				Socket conexao = socketServer.accept();
				
				// O atributo path representa o diretorio onde estao armazenados os arquivos no servidor
				// Por default path = "Arquivos"
				Servidor requisicaoServidor = new Servidor(conexao, path);
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

