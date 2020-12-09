package server;

import java.util.ArrayList;
import server.infra.AceitadoraDeConexao;

public class Aplicacao {
	private static final String DEFAULT_DOOR = "3333";

	public static void main(String[] args) {
		if (args.length > 1) {
			System.err.println("Uso esperado: java Servidor [PORTA]\n");
			return;
		}

		String porta = DEFAULT_DOOR;

		if (args.length == 1)
			porta = args[0];
		
		String comando = null;
		AceitadoraDeConexao aceitadora;
		ArrayList<Parceiro> usuarios;
		
		usuarios = new ArrayList<>();
		
		try {
			aceitadora = new AceitadoraDeConexao(porta, usuarios);
			aceitadora.start();
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
            
            System.out.println(comando);
            if (comando.toLowerCase().equals("desativar")) {
            	//TODO lançar a notificação para os clientes            	
            } 
            
            else 
            	System.err.println("Comando invállido");
		}
	}
}
