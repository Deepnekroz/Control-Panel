package com.sergeev.controlpanel.ssh;

import com.sergeev.controlpanel.model.Node;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;

import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class SshService {
	private static final String KEY_ALIAS    = "controlpanel";
	private static final char[] KEY_PASSWORD = "121212".toCharArray();

	private SSHClient          ssh;
	private ArrayList<Session> sessions;

	// TODO: Handle exceptions;
	public SshService() throws Exception {
		ssh = new SSHClient();
		ssh.loadKeys(SshService.loadKeyPair());
	}


	public static String test()
			throws IOException {
		String          res = "";
		final SSHClient ssh = new SSHClient();
		ssh.addHostKeyVerifier("a6:90:99:9c:c5:15:d8:07:b5:fa:c5:79:77:93:9b:b6");

		ssh.connect("192.168.1.169");
		try {
			ssh.authPassword("root", "nQXeRxl2eogCVPYEQsUD");
			final Session session = ssh.startSession();
			try {
				final Command cmd = session.exec("ping -c 1 google.com");
				res += IOUtils.readFully(cmd.getInputStream()).toString();
				cmd.join(5, TimeUnit.SECONDS);
				res += "\n** exit status: " + cmd.getExitStatus();
			} finally {
				session.close();
			}
		} finally {
			ssh.disconnect();
		}

		return res;
	}


	// TODO: Handle exceptions
	// TODO: Should be private?
	private static KeyPair loadKeyPair() throws Exception {
		KeyStore keystore = KeyStore.getInstance("jks");
		keystore.load(SshService.class.getResourceAsStream("/resources/controlpanel.jks"), KEY_PASSWORD);
		Key key = keystore.getKey(KEY_ALIAS, KEY_PASSWORD);

		if (key instanceof PrivateKey) {
			Certificate cert = keystore.getCertificate(KEY_ALIAS);
			PublicKey publicKey = cert.getPublicKey();
			return new KeyPair(publicKey, (PrivateKey) key);
		} else {
			// TODO: Throw exception
			throw new Exception();
		}
	}

	public int connectToNode(Node node) throws Exception {
		//ssh.addHostKeyVerifier(node.getSshId());
		ssh.connect(node.getInetAddress());
		ssh.authPublickey("root");

		Session session = ssh.startSession();
		sessions.add(session);

		// It's possible to just get size of the array, but that might result race conditions
		// Giving away Session is a bad idea, as that brakes OOP principles
		return sessions.lastIndexOf(session);
	}


}