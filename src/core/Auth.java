package core;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.util.Scanner;

public final class Auth {

	/**
	 * Authenticates with bitcoind
	 * 
	 * @return a BitcoinJSONRPCClient
	 * @throws MalformedURLException
	 * @throws FileNotFoundException
	 */
	public static final void initAuth() throws FileNotFoundException{

		Scanner cin = new Scanner(new File("Auth.priv"));
		
		/* Configured in bitcoin.conf */
		final String rpcuser = cin.nextLine();
		final String rpcpassword = cin.nextLine();
		
		cin.close();

		Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(rpcuser, rpcpassword
						.toCharArray());
			}
		});



	}

}