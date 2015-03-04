/* Stefan Taylor 1006260 */

//import java.io.File;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class Receiver1 {
	public static void main(String[] args) throws Exception {

		// Get port and filename from user input
		int port = Integer.parseInt(args[0]);
		String filename = args[1];
		
		//Create array of bytes to hold payload
		byte[] payload = new byte[1024];
				
		//Create output stream to write file
		FileOutputStream fileoutput = new FileOutputStream(filename);
		
		//Variable for the offset of the filedata in the payload
		int payloadoffset = 3;
		
		//Create packet
		DatagramPacket packet = new DatagramPacket(payload, payload.length);
		
		//Create Socket
		DatagramSocket socket = new DatagramSocket(port);
		
		int packetcount = 1;
		
		//LOOP COLLECTS PACKETS WHILE EOF IS NOT FLAGGED
		while (true){
			//Receive packet
			socket.receive(packet);
			System.out.println("Received Packet " + packetcount);
			//Retrieve Packet Data
			payload = packet.getData();
			//If EOF FLAGGED THEN BREAK
			if (payload[2] == 1) {break;}
			//Write the file data
			fileoutput.write(payload, payloadoffset, 1021);
			packetcount++;
		}
		
		//Get Packet Length
		int packetlength = packet.getLength();
		//Write out to file
		fileoutput.write(payload, payloadoffset, packetlength-payloadoffset);
		
		//Close stream
		fileoutput.close();
		
		//Close socket
		socket.close();
		
		//Done
		System.out.println("Done");

	}
}