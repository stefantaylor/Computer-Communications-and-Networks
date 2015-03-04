/* Stefan Taylor 1006260 */

//Sender1 - sends 1024 byte payloads via UDP

// 1024 bytes, 2byte sequence number, 1 end of file byte, 1021 data bytes

import java.io.*;
import java.net.*;

public class Sender1 {

	public static void main(String args[]) throws Exception {
		// Get the address, port and name of file from user input
		InetAddress hostname = InetAddress.getByName(args[0]);
		final int port = Integer.parseInt(args[1]);
		final String fileName = args[2];

		// Create the socket
		DatagramSocket socket = new DatagramSocket();

		// Instantiate the file
		File file = new File(fileName);

		// Create Stream to read file
		InputStream inFromFile = new FileInputStream(file);

		// Create a byte array to hold file data in bytes and populate it
		byte[] fileinbytes = new byte[(int) file.length()];
		inFromFile.read(fileinbytes);

		// Declare Sequence Number Variable
		int seqno = 0;

		// Loop for each packet we need to send
		for (int i = 0; i < fileinbytes.length; i = i + 1021) {

			// Create new payload.
			byte[] payload = new byte[1024];

			// Check if it is the last packet, set eof byte accordingly, and
			// populate payload
			if ((i + 1021) >= fileinbytes.length) {
				for (int j = 0; j < (fileinbytes.length - i); j++) {
					payload = new byte[fileinbytes.length - i + 3]; //3 is the offset due to the header
					System.out.println(fileinbytes.length - i);
					payload[j + 3] = fileinbytes[i + j];
				}
				payload[2] = (byte) (1);
			} else {
				for (int j = 0; j <= 1020; j++) {
					payload[j + 3] = fileinbytes[i + j];
				}
				payload[2] = (byte) (0);
			}

			// Add sequence number to payload
			payload[0] = (byte) (seqno >> 8);
			payload[1] = (byte) (seqno);

			// Send the payload
			DatagramPacket sendPacket = new DatagramPacket(payload,
					payload.length, hostname, port);
			socket.send(sendPacket);
			System.out.println("Sent Packet " + (seqno + 1) + " of "
					+ (int) (Math.ceil((file.length()) / 1021.0)));

			// Sleep so we don't send too fast
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Increment sequence number
			seqno += 1;

		}

		// Close stuff to prevent resource leaks
		socket.close();
		inFromFile.close();
		
		//Done
		System.out.println("Done");
	}
}