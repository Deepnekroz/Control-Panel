package com.sergeev.controlpanel.ssh;

import com.sergeev.controlpanel.model.Node;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.userauth.UserAuthException;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class SshService {
	private static final String KEY_PATH;

	static {
		KEY_PATH = (new File("src/main/resources/cp.pem")).getAbsolutePath();
	}

	private final SSHClient ssh;
	private       Node      node;
	private       Session   session;

	public SshService(Node node) {
		ssh = new SSHClient();
		this.node = node;
	}

	public void updatePublicKey() {
		ssh.addHostKeyVerifier(node.getPublicKey());
	}

	private void connect() throws IOException {
		ssh.connect(node.getInetAddress());
	}

	private void disconnect() throws IOException {
		ssh.disconnect();
	}

	private void logIn() throws UserAuthException, TransportException {
		ssh.authPublickey("root", KEY_PATH);
	}

	private void logIn(String password) throws UserAuthException, TransportException {
		ssh.authPassword("root", password);
	}

	private void startSession() throws ConnectionException, TransportException {
		session = ssh.startSession();
	}

	private void stopSession() throws ConnectionException, TransportException {
		session.close();
	}

	/*
	String["Result", "Exit status"]
	 */
	private String[] executeCommand(String stringCommand) throws IOException {
		String[]      response = new String[2];
		final Command cmd      = session.exec(stringCommand);
		response[0] = IOUtils.readFully(cmd.getInputStream()).toString();
		cmd.join(5, TimeUnit.SECONDS);
		response[1] = cmd.getExitStatus().toString();

		return response;
	}

	public String[] sendCommand(String cmd, String password) throws IOException {
		this.connect();
		try {
			if (password.isEmpty()) {
				this.logIn();
			} else {
				this.logIn(password);
			}
			this.startSession();
			try {
				return this.executeCommand(cmd);
			} finally {
				this.stopSession();
			}
		} finally {
			this.disconnect();
		}
	}

	public void initializeNode(String password) throws IOException {
		// TODO: Read from resources/cp.id_rsa.pub
		this.sendCommand(
				"echo \"ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDYCj4uihLqTI2c+A++p0tXwWCnIh3F8m2OD7h4OtJdoF8Q4Lz3/Ziq0X+BuZ2QRmVXeJkBQMT/z8E1iOktSRGYgZrVqGDdOsAiu8g6PTbnE3BSsqa5pUJ4mdZ6xc5l0xRrGb05TN8qw/OtYbcnC0E7ya+sM1JXJBG5Xosz+QrRanTJZrtBXjjWD82yJAuvypX4g3tbl156ZZKN8PIYRVFEMvzxwz36kKLfCagfgFFfDnG+38rLPqDbKTvx5NspSupdmis2I8tg5FfKTxX8RygrTEzjPoxRVLGSoYiYEDw6V9a3COE9sxjXyGT38fqFQUqIPGNmbBFYY7IYg82IFWhf ControlPanel\" >> ~/.ssh/authorized_keys",
				password);
	}

	/*
	public static void main(String[] args)
			throws IOException, Exception {
		String          res = "";
		final SSHClient ssh = new SSHClient();
		ssh.addHostKeyVerifier("a6:90:99:9c:c5:15:d8:07:b5:fa:c5:79:77:93:9b:b6");

		ssh.connect("192.168.1.169");
		try {
			//ssh.authPassword("root", "");
			File file = new File("src/main/resources/cp.pem");
			ssh.authPublickey("root", file.getAbsolutePath());
			final Session session = ssh.startSession();
			try {
				final Command cmd = session.exec("uname -a");
				res += IOUtils.readFully(cmd.getInputStream()).toString();
				cmd.join(5, TimeUnit.SECONDS);
				res += "\n** exit status: " + cmd.getExitStatus();
			} finally {
				session.close();
			}
		} finally {
			ssh.disconnect();
		}


		System.out.println(res);
	}*/

	/*
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
*/

}