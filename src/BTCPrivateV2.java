import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;

public class BTCPrivateV2 extends Thread{
	public static String hex = "0000000000000000000000000000000000000000000000008000000004a91804"; //
	public void run() {
		BigInteger value = new BigInteger(hex, 16);
		//int value = Integer.parseInt(hex, 16);
		//value++;
		value = value.add(BigInteger.ONE);
		hex = bytesToHex(value.toByteArray());
		//hex = Integer.toHexString(value);
		//hex = hex.substring(2, hex.length());
		
		
		while(hex.length() < 64) {
			hex = "0" + hex;
		}
		String hex1 = hex;
		
		File file = new File("PrivateV2.txt");
		PrintWriter writer;
		try {
			writer = new PrintWriter(new FileWriter(file, true));
			writer.append(hex1 + "\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/*
		try {
			writeToFile(hex);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
		System.out.println(hex1);
	}

	public static void main(String[] args) throws IOException {
		//The plan for this program is to generate all possible 64 bit hex values until ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff and save all to a text file. Then, using the python script to convert them to private keys, import them all. 
		
		while(hex != "000000000000000000000000000000000000000000000000ffffffffffffffff") {
			//BTCPrivateV2 mult = new BTCPrivateV2();
			//mult.start();
			BigInteger value = new BigInteger(hex, 16);
			//int value = Integer.parseInt(hex, 16);
			//value++;
			value = value.add(BigInteger.ONE);
			hex = bytesToHex(value.toByteArray());
			//hex = Integer.toHexString(value);
			//hex = hex.substring(2, hex.length());
			
			
			while(hex.length() < 64) {
				hex = "0" + hex;
			}
			String hex1 = hex;
			
			/*
			File file = new File("PrivateV2.txt");
			PrintWriter writer;
			try {
				writer = new PrintWriter(new FileWriter(file, true));
				writer.append(hex1 + "\n");
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			writeToFile(hex1);
			
			/*
			try {
				writeToFile(hex);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	*/
			System.out.println(hex1);
		}

	}
	
	public static void writeToFile(String hex) throws IOException {
		File file = new File("PrivateV2.txt");
		PrintWriter writer = new PrintWriter(new FileWriter(file, true));
		writer.append(hex + "\n");
		writer.close();
	}

	private static String bytesToHex(byte[] hash) {
		StringBuilder hexString = new StringBuilder(2 * hash.length);
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if(hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}
	
}
