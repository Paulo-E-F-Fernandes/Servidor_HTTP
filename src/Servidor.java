import java.io.*;
import java.net.*;
import java.util.*;

public final class Servidor implements Runnable {
	
	final static String CRLF = new String("\r\n");
	private String PATH;
	private Socket socket;
	private String pedido;
	private String comando;
	private String cabecalho;
	
	// Construtor
	Servidor(Socket socket, String dir) {
		this.socket = socket;
		
		// Código para padronizar o nome do diretorio base
		//-----------------------
		if(dir.startsWith("/")) {
			dir = " " + dir;
		}
		if(dir.endsWith("/")) {
			dir = dir + " ";
		}
		
		dir = dir.replaceFirst(" /", " ");
		dir = dir.replaceFirst("/ ", " ");
		//-----------------------
		
		this.PATH = "./" + (dir.trim());
	}
	
	// Implementa o método run() da interface Runnable.
	public void run() {
		try {
			processaRequisicao();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	// Método para processar as requisicoes enviadas pelo cliente
	public void processaRequisicao() throws IOException {
		BufferedReader requisicao = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		DataOutputStream resposta = new DataOutputStream(socket.getOutputStream());
		
		// Confirmando ao cliente que a conexao foi estabelecida
		resposta.writeBytes("HTTP 1.1 200 OK!" + CRLF);
		
		pedido = requisicao.readLine();
		
		System.out.println(pedido);
		
		cabecalho = null;
		while((cabecalho = requisicao.readLine()).length() != 0) {
			System.out.println(cabecalho);
		}
		
		StringTokenizer token = new StringTokenizer(pedido);
		// Obtendo o comando(GET, HEAD, ...)
		comando = token.nextToken();
		// Obtendo o arquivo a ser enviado
		pedido = PATH + token.nextToken();
		
		System.out.println(pedido);
		
		respostaPedido(resposta);
		
		System.out.println();
		
		resposta.close();
		requisicao.close();
		socket.close();
	}
	
	// Verifica qual resposta sera enviada
	public void respostaPedido(DataOutputStream DOS) {
		if(this.comando.equals("GET")) {
			comandoGet(DOS);
		}
		else
			if(this.comando.equals("HEAD")) {
				comandoHead(DOS);
			}
			else {
				if(this.comando.equals("POST") || this.comando.equals("DELETE") || this.comando.equals("PUT")) {
					comandoSemSuporte(DOS);
				}
				else {
					comandoBad(DOS);
				}
			}
	}
	
	private void comandoGet(DataOutputStream DOS)
	{
		FileInputStream file = null;
		String status = null;
		String content = null;
		String length = null;
		boolean existeArquivo = true;
		
		try {
			file = new FileInputStream(this.pedido);
			// Para ter acesso ao tamanho do arquivo
			File arquivo = new File(this.pedido);
			status = "HTTP 1.1 200 OK!" + CRLF;
			content = "Content-type: " + contentType(pedido) + CRLF;
			length = "Content-length: " + arquivo.length() + CRLF;
		}
		catch(FileNotFoundException e) {
			status = "HTTP 1.1 404 Not Found!" + CRLF;
			content = "Content-type: text/html" + CRLF;
			length = "Content-length: 0" + CRLF;
			existeArquivo = false;
		}
		
		try {
			DOS.writeBytes(status);
			DOS.writeBytes(content);
			DOS.writeBytes(length);
			DOS.writeBytes(CRLF);
			
			// Se o arquivo solicitado existir 
			if(existeArquivo) {
				// Para enviar o arquivo solicitado ao cliente
				// ---------------------------
				// Cria um buffer de 1K para comportar os bytes no caminho para o socket.
				byte[] buffer = new byte[1024];
				int bytes = 0;
				// Copiar o arquivo requisitado dentro da cadeia de saída do socket.
				while((bytes = file.read(buffer)) != -1 ) {
					DOS.write(buffer, 0, bytes);
				}
				// ---------------------------
			}
			// Se o arquivo solicitado nao for encontrado
			else {
				DOS.writeBytes("<html><head><title>404 Not Found</title></head>" +
						"<body><p>Not Found.</p></body></html>");
			}
			
			DOS.writeBytes(CRLF);
			file.close();
		}
		catch(Exception e) {
			System.err.println(e);
		}	
	}
	
	// Como foi implementado apenas os metodos GET e HEAD, os demais metodos suportados pelo HTTP 1.1 utiliza este metodo para responder ao cliente
	private void comandoSemSuporte(DataOutputStream DOS) {
		try {
			DOS.writeBytes("Comando nao implementado!" + CRLF);
			DOS.writeBytes(CRLF);
		}
		catch(Exception e) {
			System.err.println(e);
		}
	}
	
	private void comandoHead(DataOutputStream DOS) {
		FileInputStream file = null;
		String status = null;
		String content = null;
		String length = null;
		
		try {
			file = new FileInputStream(this.pedido);
			File arquivo = new File(this.pedido);
			status = "HTTP 1.1 200 OK!" + CRLF;
			content = "Content-type: " + contentType(pedido) + CRLF;
			length = "Content-length: " + arquivo.length() + CRLF;
		}
		catch(FileNotFoundException e) {
			status = "HTTP 1.1 404 Not Found!" + CRLF;
			content = "Content-type: " + contentType(pedido) + CRLF;
			length = "Content-length: 0" + CRLF;
		}
	
		try {
			DOS.writeBytes(status);
			DOS.writeBytes(content);
			DOS.writeBytes(length);
			DOS.writeBytes(CRLF);
			file.close();
		}
		catch(Exception e) {
			System.err.println(e);
		}
	}
	
	private void comandoBad(DataOutputStream DOS) {
		try {
			DOS.writeBytes("HTTP 1.1 400 Bad Request!" + CRLF);
			DOS.writeBytes(CRLF);
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	// Metodo para definir o tipo de conteudo do arquivo solicitado.
	// Metodo retirado do material 'tarefas_programacao.doc' disponibilizado no site do livro do Kurose  
	private static String contentType(String fileName)
	{
		if(fileName.endsWith(".htm") || fileName.endsWith(".html")) {
			return "text/html";
		}
		if(fileName.endsWith(".gif")) {
			return "image/gif";
		}
		if(fileName.endsWith(".jpeg") || fileName.endsWith(".jpg")) {
			return "image/jpeg";
		}
		return "application/octet-stream";
	}
}
