import java.nio.*;
import java.net.*;

public class SocketClient{
	Socket client;
	
	SocketClient()
	{
		try {
			client = new Socket("localhost", 1313);
		} 
		catch (Exception e) {
			System.err.println("Não foi possível estabelecer uma conexão com o servidor!");
		}
	}
	
	public static void main(String[] args){
		SocketClient frame = new SocketClient();
		
	}
}