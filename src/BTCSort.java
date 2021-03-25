import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Utils;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.Wallet;



public class BTCSort {

	private static final char[] ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz"
			.toCharArray();
	private static final int BASE_58 = ALPHABET.length;
	private static final int BASE_256 = 256;
	public static MainNetParams params = MainNetParams.get(); 

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InterruptedException {
		File file = new File("PrivateV2.txt");
		File file1 = new File("PrivateWIF.txt");
		File file2 = new File("Public.txt");
		//File file3 = new File("Winners.txt");
		Scanner scan = new Scanner(file);
		
		//PrintWriter writer2 = new PrintWriter(new FileWriter(file3, true));

		while(scan.hasNextLine() == true) {
			PrintWriter writer = new PrintWriter(new FileWriter(file1, true));
			PrintWriter writer1 = new PrintWriter(new FileWriter(file2, true));
			String prvkey = scan.nextLine();
			//String prvkey = "EDCC6224FEE390A57C76C13A9BECC9502A6F3B1BF6F72B6ED11B83A0F0E3E9FC";
			String wifprv = wifConverter(prvkey);
			System.out.println("Private Key (WIF): " + wifprv);
			String btcaddress = publicGenerator(prvkey);
			System.out.println("Public Address: " + btcaddress);
			if(btcaddress.startsWith("1")) {
			writer.append(wifprv + "\n");
			writer.close();
			writer1.append(btcaddress + "\n");
			writer1.close();
			}
			/*
			if(btcaddress.substring(0, 1).contains("1")) {
				//btcaddress = "19iSW3gwxGmLmF13SHcprqNiwvG8dmCyAw";
				//https://api.blocktrail.com/v1/btc/address/1NcXPMRaanz43b1kokpPuYDdk6GGDvxT2T?api_key=MY_APIKEY
				String balance = balanceGrab(btcaddress);
				double balance1 = Integer.parseInt(balance);
				if(Integer.parseInt(balance) != 0) {
					balance1 = balance1/100000000;
					writer2.append("Private Key (WIF): " + wifprv + "\n" + "Public Address: " + btcaddress + "\n" + "Balance: " + balance1 + " BTC" + "\n\n");
					writer2.close();
				}
				System.out.println("Balance: " + balance1 + " BTC");
			}
			//TimeUnit.SECONDS.sleep(1);
			*/
		}
		
	}


	public static String wifConverter(String prvkey) throws NoSuchAlgorithmException {
		prvkey = "80" + prvkey;
		String pubkey = prvkey;
		//System.out.println(prvkey);
		MessageDigest digest = MessageDigest.getInstance("SHA-256");

		byte[] bytearray = hexStringToByteArray(prvkey);

		byte[] encoded = digest.digest(digest.digest(bytearray));
		String hex = bytesToHex(encoded);
		//System.out.println(hex);
		pubkey = pubkey + hex.substring(0, 8);
		//System.out.println(pubkey);
		BigInteger encoding = new BigInteger(pubkey, 16);
		String finish = encode(encoding.toByteArray());
		finish = finish.substring(1, finish.length());
		//System.out.println(finish);
		return finish;
	}

	public static String publicGenerator(String prvkey) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		BigInteger privKey = new BigInteger(prvkey, 16);
		byte[] key = ECKey.publicKeyFromPrivate(privKey, false);
		//for(int i=0; i<key.length; i++) {
		//System.out.print(key[i]);
		//}
		//System.out.println();
		ECKey key1 = ECKey.fromPublicOnly(key);
		String public1 = key1.toString();
		//System.out.println(public1);
		public1 = public1.substring(14, 144);
		//System.out.println(public1);

		byte[] bytearray = hexStringToByteArray(public1);
		byte[] encoded = Utils.sha256hash160(bytearray); //error exists here
		//byte[] encoded = 
		//encoded = digest.digest(encoded);
		String hex = bytesToHex(encoded);
		hex = "00" + hex;

		//bytearray = hexStringToByteArray(hex);
		BigInteger big1 = new BigInteger(hex, 16);
		bytearray = big1.toByteArray();
		byte[] encoded1 = digest.digest(digest.digest(bytearray));
		String done = bytesToHex(encoded1);
		//System.out.println(done);
		hex = hex + done.substring(0, 8);
		//System.out.println(hex);


		BigInteger big = new BigInteger(hex, 16);
		String finished = encode(big.toByteArray());
		//System.out.println(finished);
		return finished;
	}

	public static String balanceGrab(String btcaddress) throws IOException {
		URL url = new URL("https://chain.api.btc.com/v3/address/" + btcaddress); //https://chain.api.btc.com/v3/address/15urYnyeJe3gwbGJ74wcX89Tz7ZtsFDVew/unspent
		//https://api.blockcypher.com/v1/btc/main/addrs/" + btcaddress + "/balance
		URLConnection con = url.openConnection();
		String balance = "";
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;

			while ((line = in.readLine()) != null) {
				//System.out.println(line);
				
				if(line.contains("\"balance\":") ) { //&& !line.contains("  \"unconfirmed_balance\": ") && !line.contains("  \"balance\": ")
					int start = line.indexOf("\"balance\":") + 10;
					int end = line.indexOf(",", start);
					line = line.substring(start, end);
					//line = line.replace("\"balance\":", "");
					//line = line.replace(",", "");
					balance = line;
				}
				
				
			}
		in.close();
		return balance;
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

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
					+ Character.digit(s.charAt(i+1), 16));
		}
		return data;
	}


	public static String encode(byte[] input) {
		if (input.length == 0) {
			// paying with the same coin
			return "";
		}

		//
		// Make a copy of the input since we are going to modify it.
		//
		input = copyOfRange(input, 0, input.length);

		//
		// Count leading zeroes
		//
		int zeroCount = 0;
		while (zeroCount < input.length && input[zeroCount] == 0) {
			++zeroCount;
		}

		//
		// The actual encoding
		//
		byte[] temp = new byte[input.length * 2];
		int j = temp.length;

		int startAt = zeroCount;
		while (startAt < input.length) {
			byte mod = divmod58(input, startAt);
			if (input[startAt] == 0) {
				++startAt;
			}

			temp[--j] = (byte) ALPHABET[mod];
		}

		//
		// Strip extra '1' if any
		//
		while (j < temp.length && temp[j] == ALPHABET[0]) {
			++j;
		}

		//
		// Add as many leading '1' as there were leading zeros.
		//
		while (--zeroCount >= 0) {
			temp[--j] = (byte) ALPHABET[0];
		}

		byte[] output = copyOfRange(temp, j, temp.length);
		return new String(output);
	}

	private static byte divmod58(byte[] number, int startAt) {
		int remainder = 0;
		for (int i = startAt; i < number.length; i++) {
			int digit256 = (int) number[i] & 0xFF;
			int temp = remainder * BASE_256 + digit256;

			number[i] = (byte) (temp / BASE_58);

			remainder = temp % BASE_58;
		}

		return (byte) remainder;
	}

	private static byte[] copyOfRange(byte[] source, int from, int to) {
		byte[] range = new byte[to - from];
		System.arraycopy(source, from, range, 0, range.length);

		return range;
	}

}
