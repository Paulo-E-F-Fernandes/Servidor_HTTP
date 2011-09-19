import java.io.*;

public class Entrada {
	private BufferedReader teclado;
	private String str;
	
	Entrada() {}
	
	public String gets()
	{
		this.teclado = new BufferedReader(new InputStreamReader(System.in));
		try {
			this.str = teclado.readLine();
		}
		catch(IOException e)
		{
			System.err.println("Houve um erro ao recuperar o conteúdo do teclado!");
			return null;
		}
		return str;
	}
}
