package controller;

import java.util.Properties;

import model.syncManager.UserSyncManagerImpl;
import network.api.Peer;
import network.factories.PeerFactory;
import rest.api.Authentifier;
import rest.factories.AuthentifierFactory;
import rest.factories.RestServerFactory;

import crypt.impl.certificate.X509V3Generator;

/**
 * Main class
 * {@link Application} is a singleton
 * @author Julien Prudhomme
 *
 */
public class Application {
	private static Application instance = null;
	private Peer peer;
	private Authentifier auth;
	
	public Application() {
		if(instance != null) {
			throw new RuntimeException("Application can be instanciate only once !");
		}
		instance = this;
	}
	
	public static Application getInstance()	{
		return instance;
	}
	
	public void run() {
		setPeer(PeerFactory.createDefaultAndStartPeer());
		setAuth(AuthentifierFactory.createDefaultAuthentifier());
		RestServerFactory.createAndStartDefaultRestServer(8080); //start the rest api
	}
	
	public void runForTests(int restPort) {
		try
		{
			X509V3Generator test = new X509V3Generator();
			test.setConfigFile("./test.txt");
			test.initDatas();
		}		
		catch( Exception e )
		{
			System.out.println(e.toString());
		}

		Properties p = System.getProperties();
		p.put("derby.system.home", "./.db-" + restPort + "/");
		new UserSyncManagerImpl(); //just init the db
		setPeer(PeerFactory.createDefaultAndStartPeerForTest());
		setAuth(AuthentifierFactory.createDefaultAuthentifier());
		RestServerFactory.createAndStartDefaultRestServer(restPort);
	}
	
	public static void main(String[] args) {
		new Application();
		Application.getInstance().runForTests(8081);
		
	}

	public Peer getPeer() {
		return peer;
	}

	public void setPeer(Peer peer) {
		this.peer = peer;
	}

	public Authentifier getAuth() {
		return auth;
	}

	public void setAuth(Authentifier auth) {
		this.auth = auth;
	}
}
