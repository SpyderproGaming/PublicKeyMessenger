package ResourcePack;
import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.swing.JOptionPane;
public class ResourcePack
{
	private static final int HASHITERATIONS=1000;//iterations of password hashing
	private static final String LOGINFILENAME = "loginInfo.txt";
	private static final String SALTING = "Salt";
	private static final GraphicsDevice GD = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	private static final int WIDTH = GD.getDisplayMode().getWidth();
	private static final int HEIGHT = GD.getDisplayMode().getHeight();
	private static final Color TEXTCOLOR = Color.BLUE;//color of all the text in the windows
	private static final Color BACKGROUNDCOLOR = new Color(0, 204, 255);//color of the background
	private static final String VERSIONNUMBER = "1.0";
	private static final String STRINGENCODING = "UTF-8";
	private static final String HASHINGALGO = "SHA-512";
	private static  Key loggedInPrivKey;
	private static  Key loggedInPubKey;
	private static String loginDetailsFileLoc = "";
	private static String loggedInUser = "No Login!";
	private static String passString = "Default Key";
	public static void calcDirs()
	{
		File cwdFile=new File (".");
		String location=cwdFile.getAbsolutePath();
		location=location.substring(0,location.length()-1);
		char[] loc=location.toCharArray();
		ArrayList<Character> locList=new ArrayList<Character>();
		for(char a:loc)
			locList.add(a);
		for(int i=0;i<locList.size();i++)
		{
			if(locList.get(i)=='\\')
			{
				locList.add(i,'\\');
				i++;
			}
		}
		location="";
		for(char a:locList)
			location+=a;
		location+=LOGINFILENAME;
		loginDetailsFileLoc=location;
	}
	public static ArrayList<String> readStrings() throws IOException
	{
		  ArrayList <String> gatherings=new ArrayList <String>();
		  Path path = Paths.get(loginDetailsFileLoc);
		    try (Scanner scanner =  new Scanner(path,STRINGENCODING))
		    {
		      while (scanner.hasNextLine())
		      {
		        gatherings.add(scanner.nextLine());
		      }      
		    }
		    return gatherings;
	}
	public static void writeStrings(ArrayList<String> thingsToWrite) throws IOException
	{
		  Path path = Paths.get(loginDetailsFileLoc);
		    try (BufferedWriter writer = Files.newBufferedWriter(path,StandardCharsets.UTF_8))
		    {
		      for(String line : thingsToWrite)
		      {
		        writer.write(line);
		        writer.newLine();
		      }
		    }
	}
	public static boolean logIn(ArrayList<String> loginData,String userName) throws 
Exception
	{
		
		String testHash="1";
		int tracker=0;
		for(int i=0;i<loginData.size();i+=4)//search the contents of loginData to find username and then use their key
		{
			if(loginData.get(i).equals(userName))
			{
				testHash=loginData.get(i+1);
				tracker=i;
				break;
			}
		}
		if(auth(SALTING,testHash))
		{
			loggedInUser=userName;
			String privExp=decryptBlowfish(loginData.get(tracker+2),passString);
			String pubMod=decryptBlowfish(loginData.get(tracker+3),passString);
			loggedInPrivKey=privKeyFromData(new BigInteger(pubMod),new BigInteger(privExp));
			loggedInPubKey=pubKeyFromData(new BigInteger(pubMod));
			return true;
		}
		else
			return false;
	}
	private static boolean auth(String salt,String encryptedPass) throws UnsupportedEncodingException, NoSuchAlgorithmException
	{
		String testHash = null;
		char pepper='!';
		String key;
		for(int i=33;i<=126;i++)//go through all characters used as pepper
		{
			pepper=(char)i;
			key=(passString+salt+pepper);
			testHash=hasher((key));
			if(encryptedPass.equals(testHash))
				return true;
		}
		return false;
	}
	private static String hasher(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		MessageDigest sha = MessageDigest.getInstance(HASHINGALGO);// algorithm can be "MD5", "SHA-1", "SHA-256", "SHA-512"
		for(int i=0;i<HASHITERATIONS;i++)
		{
			sha.update(password.getBytes(STRINGENCODING));
			password=Base64.getEncoder().encodeToString(sha.digest());
		}
		return password;
	}
	private static double RandomGen(int min,int max)
	 {
			return (Math.random()*(max-min+1)+min);
	 }
	public static String hashGenerator(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException
	 {	
			int randomVal=(int)RandomGen(33,126);
			char pepper=(char)randomVal;
			password+=(SALTING+pepper);
			MessageDigest sha = MessageDigest.getInstance(HASHINGALGO);
			for(int i=0;i<HASHITERATIONS;i++)
			{
				sha.update(password.getBytes(STRINGENCODING));
				password=Base64.getEncoder().encodeToString(sha.digest());
			}
			return password;
	 }
	public static KeyPair generatePair() throws NoSuchAlgorithmException
	{
		SecureRandom random = new SecureRandom();//random object
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");//keygen object
	    generator.initialize(4096, random);//tested to 8192     get ready to make me some keys
	    KeyPair pair = generator.generateKeyPair();//make 'em
	    return pair;
	}
	public static BigInteger privKeyExp(Key privKey) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPrivateKeySpec rsaPrivKeySpec = keyFactory.getKeySpec(privKey, RSAPrivateKeySpec.class);
		return rsaPrivKeySpec.getPrivateExponent();
	}
	public static BigInteger pubKeyMod(Key pubKey) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPublicKeySpec rsaPubKeySpec = keyFactory.getKeySpec(pubKey, RSAPublicKeySpec.class);
		return rsaPubKeySpec.getModulus();
	}
	public static String encryptBlowfish(String toEncrypt, String key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException
	{
	      Cipher cipher = Cipher.getInstance("Blowfish");
	      cipher.init(Cipher.ENCRYPT_MODE, genBlowKey(key));
	      byte[] encrypted = cipher.doFinal(toEncrypt.getBytes());
	      return Base64.getEncoder().encodeToString(encrypted);
	}
	public static String decryptBlowfish(String toDecrypt, String key) throws Exception
	   {
	      byte[] toDecBytes=Base64.getDecoder().decode(toDecrypt);
	      Cipher cipher = Cipher.getInstance("Blowfish");
	      cipher.init(Cipher.DECRYPT_MODE, genBlowKey(key));
	      byte[] decrypted = cipher.doFinal(toDecBytes);
	      return new String(decrypted);
	   }
	private static SecretKey genBlowKey(String Seed) throws NoSuchAlgorithmException
	   {
		   SecureRandom sr = new SecureRandom(Seed.getBytes());
		   KeyGenerator kg = KeyGenerator.getInstance("Blowfish");
		   kg.init(128,sr);
		   SecretKey sk = kg.generateKey();
		   return sk;
	   }
	public static String genBlowSeed() throws NoSuchAlgorithmException
	   {
		   SecureRandom sr = new SecureRandom();
		   KeyGenerator kg = KeyGenerator.getInstance("Blowfish");
		   kg.init(128,sr);
		   SecretKey sk = kg.generateKey();
		   return Base64.getEncoder().encodeToString(sk.getEncoded());
	   }
	private static Key privKeyFromData(BigInteger privMod,BigInteger privExp) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(privMod, privExp);
		KeyFactory fact = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = fact.generatePrivate(rsaPrivateKeySpec);
		return privateKey;
	}
	private static Key pubKeyFromData(BigInteger pubMod) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(pubMod, BigInteger.valueOf(65537));
		KeyFactory fact = KeyFactory.getInstance("RSA");
		PublicKey publicKey = fact.generatePublic(rsaPublicKeySpec);
		return publicKey;
	}
	public static void setPass(String pass)
	 {
		 passString=pass;
	 }
	public static String getFileName()
	 {
		 return LOGINFILENAME;
	 }
	public static int getWidth()
	 {
		 return WIDTH;
	 }
	public static int getHeight()
	 {
		 return HEIGHT;
	 }
	public static String getLoggedInUser()
	 {
		 return loggedInUser;
	 }
	public static Color getTextColor()
	 {
		 return TEXTCOLOR;
	 }
	public static Color getBackgroundColor()
	 {
		 return BACKGROUNDCOLOR;
	 }
	public static String getVersionNum()
	 {
		return VERSIONNUMBER;
	 }
}