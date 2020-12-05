package server;

import java.util.ArrayList;
import server.Teclado;

public class Aplicacao {
	private static final String DEFAULT_DOOR = "3333";

	public static void main(String[] args) {
		String comando = null;
		
		if (args.length > 1) {
			System.err.println("Uso esperado: java Servidor [PORTA]\n");
			return;
		}

		String door = DEFAULT_DOOR;

		if (args.length == 1)
			door = args[0];
		
		//String só por hora
		ArrayList<String> parceiros = new ArrayList<>();
		
		try {
			
		}catch(Exception e) {
			System.err.print(e.getMessage());			
		}
		
		while (true) {
			System.out.println ("O servidor esta ativo! Para desativa-lo,");
            System.out.println ("use o comando \"desativar\"\n");
            System.out.print   ("> ");
            
            try {
            	comando = Teclado.getUmString();
            }catch(Exception e) {
            	System.err.println(e.getMessage());
            }
            
            if (comando.toLowerCase() == "desativar") {
            	//TODO lançar a notificação para os clientes            	
            } 
            
            else 
            	System.err.println("Comando invállido");
		}
	}
}
