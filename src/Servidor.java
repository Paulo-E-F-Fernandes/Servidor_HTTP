import java.io.*;
import java.net.*;
import java.util.*;

public final class Servidor implements Runnable {
	
	final static String CRLF = new String("\r\n");
	private Socket socket;
	private String pedido;
	private String comando;
	private String cabecalho;
	
	// Construtor
	Servidor(Socket socket) {
		this.socket = socket;
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
		
		resposta.writeBytes("HTTP 1.1 200 OK!" + CRLF);
		
		pedido = requisicao.readLine();
		
		cabecalho = null;
		while((cabecalho = requisicao.readLine()).length() != 0) {
			System.out.println(cabecalho);
		}
		
		StringTokenizer token = new StringTokenizer(pedido);
		comando = token.nextToken();
		pedido = "." + token.nextToken();
		
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
				
			}
			else {
				comandoBad(DOS);
			}
			
	}
	
	private void comandoGet(DataOutputStream DOS)
	{
		FileInputStream file = null;
		boolean existeFile = true;
		try {
			file = new FileInputStream(this.pedido);
		}
		catch(FileNotFoundException e) {
			existeFile = false;
		}
		
		String status = null;
		String content = null;
		String corpo = null;
		if(existeFile) {
			status = "HTTP 1.1 200 OK!" + CRLF;
			content = "Content-type: " + contentType(pedido) + CRLF;
		}
		else {
			status = "HTTP 1.1 404 Not Found!" + CRLF;
			content = "Content-type: text/html" + CRLF;
			corpo = "<HTML><HEAD><TITLE>404 Not Found</TITLE></HEAD><BODY>404 Not Found</BODY></HTML>";
		}
		
		try {
			DOS.writeBytes(status);
			DOS.writeBytes(content);
			DOS.writeBytes(CRLF);
			
			if(existeFile) {
				sendBytes(file, DOS);
			}
			else {
				DOS.writeBytes(corpo);
			}
			file.close();
		}
		catch(Exception e) {
			System.err.println(e);
		}	
	}
	
	private void comandoBad(DataOutputStream DOS) {
		try {
			DOS.writeBytes("HTTP 1.1 400 Bad Request!" + CRLF);
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	/*public String analisaRequisicao(String str) {
		StringTokenizer token = new StringTokenizer(str);
		String saida;
		
		if(token.nextToken().equals("GET")) {
			saida = new String("GET");
		}
		else { 
			if(token.nextToken().equals("HEAD")) {
				saida = new String("HEAD");
			}
			else {
				saida = new String("POST");
			}
		}
		// Para pular o comando que já foi identificado
		token.nextToken();
		str = token.nextToken();
		return saida;
	}*/
	
	private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception
	{
		// Construir um buffer de 1K para comportar os bytes no caminho para o socket.
		byte[] buffer = new byte[1024];
		int bytes = 0;
		// Copiar o arquivo requisitado dentro da cadeia de saída do socket.
		while((bytes = fis.read(buffer)) != -1 ) {
			os.write(buffer, 0, bytes);
		}
	}
	
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
